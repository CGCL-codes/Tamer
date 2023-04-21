package org.zxframework.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import org.zxframework.ZXBO;
import org.zxframework.ZXException;
import org.zxframework.zXType;
import org.zxframework.util.StringUtil;
import org.zxframework.logging.Log;
import org.zxframework.logging.LogFactory;

/**
 * Document business object.
 * 
 * <pre>
 * Change    : BD2APR03
 * Why       : Added saveAs function
 * 
 * Change    : BD9JUN03
 * Why       : Added fllFleNme attribute
 * 
 * Change    : DGS31JUL2003
 * Why       : For embedded gif/jpg images (including tifs exported to gif), added dummy querystring
 *              value to make the src URL unique (as already done for those viewed with viewer and
 *              file types other than gif/jpg/tif).
 * 
 * Change    : BD6MAY04
 * Why       : Fixed a bug in getAttr where it failed to associate the
 *                fllFleNme with the attribute definition (as usual I may add...)
 * </pre>
 * 
 * @author Michael Brewer
 * @author Bertus Dispa
 * @author David Swann
 * 
 * @since 0.01
 * @version 0.0.1
 *  
 **/
public class ZXDoc extends ZXBO {

    private static Log log = LogFactory.getLog(ZXDoc.class);

    private DocTpe docTpe;

    /**
     * Default contructor.
     */
    public ZXDoc() {
        super();
    }

    /**
     * View the document.
     *
     * @param pstrLogDir  
     * @param pstrPhysDir  
     * @return Returns 
     * @throws ZXException Thrown if view fails. 
     */
    public StringBuffer view(String pstrLogDir, String pstrPhysDir) throws ZXException {
        return view(pstrLogDir, pstrPhysDir, false, true);
    }

    /**
     * View the document.
     *
     * @param pstrLogDir  
     * @param pstrPhysDir  
     * @param pblnUseViewer Optional, default is false 
     * @return Returns 
     * @throws ZXException Thrown if view fails. 
     */
    public StringBuffer view(String pstrLogDir, String pstrPhysDir, boolean pblnUseViewer) throws ZXException {
        return view(pstrLogDir, pstrPhysDir, pblnUseViewer, true);
    }

    /**
     * View the document.
     *
     * @param pstrLogDir Logical directory 
     * @param pstrPhysDir Physical directory 
     * @param pblnUseViewer Optional, default is false 
     * @param pblnDownload Optional, default is true 
     * @return Returns the html for the view button.
     * @throws ZXException Thrown if view fails. 
     */
    public StringBuffer view(String pstrLogDir, String pstrPhysDir, boolean pblnUseViewer, boolean pblnDownload) throws ZXException {
        StringBuffer view = new StringBuffer(1024);
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
            getZx().trace.traceParam("pstrLogDir", pstrLogDir);
            getZx().trace.traceParam("pstrPhysDir", pstrPhysDir);
            getZx().trace.traceParam("pblnUseViewer", pblnUseViewer);
            getZx().trace.traceParam("pblnDownload", pblnDownload);
        }
        try {
            if (loadBO("*").pos != zXType.rc.rcOK.pos) {
                getZx().trace.addError("Unable to load data for zxDoc");
                throw new ZXException();
            }
            docTpe = (DocTpe) getZx().getBos().quickLoad("zxDocTpe", getValue("docTpe"), "", "bseDir");
            if (docTpe == null) {
                getZx().trace.addError("Unable to retrieve zxDocTpe");
                throw new ZXException();
            }
            if (!pstrPhysDir.endsWith(File.separator)) {
                pstrPhysDir = pstrPhysDir + File.separator;
            }
            if (!pstrLogDir.endsWith("/")) {
                pstrPhysDir = pstrPhysDir + '/';
            }
            String strPath = docTpe.getValue("bseDir").getStringValue() + File.separatorChar + getValue("fleNme").getStringValue();
            String strFileName = "Attachment" + getZx().getUserProfile().getValue("id").getStringValue();
            strFileName = strFileName + "-" + getValue("id").getStringValue();
            String strExtension = extension().toLowerCase();
            if (StringUtil.len(strExtension) > 0) {
                strFileName = strFileName + "." + strExtension;
            }
            String strVirtualTempPath = pstrLogDir + strFileName;
            String strPhysicalTempPath = pstrPhysDir + strFileName;
            if (!new File(strPath).exists()) {
                getZx().trace.addError("Uploaded file does not exist", strPath);
            }
            copy(strPath, strPhysicalTempPath);
            if (pblnDownload) {
                view.append("<script language=\"Javascript\" type=\"text/javascript\">\n");
                view.append("	elementByName(top.fraFooter, 'anchorDownLoad').href='").append(strVirtualTempPath).append("';\n");
                view.append("</script>\n");
            }
            if (pblnUseViewer) {
                view.append("<script language=\"Javascript\" type=\"text/javascript\">\n");
                view.append(" this.document.location = '").append(strVirtualTempPath).append("?dummy=").append(getValue("id").getStringValue()).append("';\n");
                view.append("</script>\n");
            } else {
                if (strExtension.equalsIgnoreCase("gif") || strExtension.equalsIgnoreCase("png") || strExtension.equalsIgnoreCase("jpg")) {
                    view.append("<img src='").append(strVirtualTempPath).append("?dummy=").append(getValue("id").getStringValue()).append("' title='").append(strPath).append("'").append(">\n");
                } else if (strExtension.equalsIgnoreCase("tif")) {
                } else {
                    view.append("<script language=\"Javascript\" type=\"text/javascript\">\n");
                    view.append(" this.document.location = '").append(strVirtualTempPath).append("?dummy=").append(getValue("id").getStringValue()).append("';\n");
                    view.append("</script>\n");
                }
            }
            return view;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : View the document.", e);
            if (log.isErrorEnabled()) {
                log.error("Parameter : pstrLogDir = " + pstrLogDir);
                log.error("Parameter : pstrPhysDir = " + pstrPhysDir);
                log.error("Parameter : pblnUseViewer = " + pblnUseViewer);
                log.error("Parameter : pblnDownload = " + pblnDownload);
            }
            if (getZx().throwException) throw new ZXException(e);
            return view;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(view);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Gets the extension from the filename of the document.
     * 
     * @return Returns the extension of the file.
     * @throws ZXException Thrown if extension fails
     */
    public String extension() throws ZXException {
        String extension = "";
        String strFleNme = getValue("fleNme").getStringValue();
        int intLen = StringUtil.len(strFleNme);
        if (intLen > 0) {
            int intDot = StringUtil.reverse(strFleNme).indexOf('.');
            if (intDot != -1) {
                extension = strFleNme.substring(intLen - intDot, intLen);
            }
        }
        return extension;
    }

    /**
     * Gets the mime type from the extension of the filename of the document.
     * 
     * @return Returns the mime type from the extension.
     */
    public String mimeType() {
        return "";
    }

    /**
     * Save file associated with this doc to another place.
     * 
     * @param pstrFileName The file to copy.
     * @throws ZXException Thrown if saveAs fails.
     */
    public void saveAs(String pstrFileName) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
        }
        try {
            copy(getFullFileName(), pstrFileName);
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Save file associated with this doc to another place.", e);
            if (getZx().throwException) throw new ZXException(e);
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Replace 'my' file with the named file.
     * 
     * @param pstrFileName The file to copy
     * @throws ZXException Thrown if replaceFile fails
     */
    public void replaceFile(String pstrFileName) throws ZXException {
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
        }
        try {
            copy(pstrFileName, getFullFileName());
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Replace 'my' file with the named file.", e);
            if (getZx().throwException) throw new ZXException(e);
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * Gets the full filename for the document, including the base path from the related zxDocType
     *
     * @return Returns the full filename for the document.
     * @throws ZXException Thrown if getFullFileName fails. 
     */
    public String getFullFileName() throws ZXException {
        String getFullFileName = "";
        if (getZx().trace.isFrameworkCoreTraceEnabled()) {
            getZx().trace.enterMethod();
        }
        try {
            boolean blnGetDocTpe = false;
            if (docTpe == null) {
                blnGetDocTpe = true;
                docTpe = (DocTpe) getZx().createBO("zxDocTpe");
                if (docTpe == null) {
                    getZx().trace.addError("Unable to create instance of zxDocTpe");
                    throw new ZXException();
                }
            } else {
                if (!docTpe.getValue("id").getStringValue().equals(getValue("docTpe").getStringValue())) {
                    blnGetDocTpe = true;
                }
            }
            if (blnGetDocTpe) {
                docTpe.setValue("id", getValue("docTpe"));
                if (docTpe.loadBO().pos != zXType.rc.rcOK.pos) {
                    getZx().trace.addError("Unable to load zXDocTpe");
                    throw new ZXException();
                }
            }
            String strBaseDir = docTpe.getValue("bseDir").getStringValue();
            if (strBaseDir.endsWith(File.separator)) {
                getFullFileName = strBaseDir + getValue("fleNme").getStringValue();
            } else {
                getFullFileName = strBaseDir + File.separatorChar + getValue("fleNme").getStringValue();
            }
            return getFullFileName;
        } catch (Exception e) {
            getZx().trace.addError("Failed to : Gets the full filename for the document, including the base path from the related zxDocType", e);
            if (getZx().throwException) throw new ZXException(e);
            return getFullFileName;
        } finally {
            if (getZx().trace.isFrameworkCoreTraceEnabled()) {
                getZx().trace.returnValue(getFullFileName);
                getZx().trace.exitMethod();
            }
        }
    }

    /**
     * A util method used to copy files.
     * 
     * @param pstrFileFrom The full filename to copy from.
     * @param pstrFileTo The full filename to copy to.
     */
    public static void copy(String pstrFileFrom, String pstrFileTo) {
        try {
            FileChannel srcChannel = new FileInputStream(pstrFileFrom).getChannel();
            FileChannel dstChannel = new FileOutputStream(pstrFileTo).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            dstChannel.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
