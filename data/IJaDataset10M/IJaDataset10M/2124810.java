package uk.gov.dti.og.fox.command;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.sql.BLOB;
import oracle.sql.CLOB;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import uk.gov.dti.og.fox.ConAgent;
import uk.gov.dti.og.fox.ContextUCon;
import uk.gov.dti.og.fox.ContextUElem;
import uk.gov.dti.og.fox.FileStorageLocation;
import uk.gov.dti.og.fox.Mod;
import uk.gov.dti.og.fox.State;
import uk.gov.dti.og.fox.StringUtil;
import uk.gov.dti.og.fox.UCon;
import uk.gov.dti.og.fox.WorkDoc;
import uk.gov.dti.og.fox.WorkingFileStoreLocation;
import uk.gov.dti.og.fox.WorkingStoreLocation;
import uk.gov.dti.og.fox.XFUtil;
import uk.gov.dti.og.fox.XThread;
import uk.gov.dti.og.fox.dom.DOM;
import uk.gov.dti.og.fox.dom.DOMList;
import uk.gov.dti.og.fox.dom.xpath.XPathResult;
import uk.gov.dti.og.fox.ex.ExActionFailed;
import uk.gov.dti.og.fox.ex.ExApp;
import uk.gov.dti.og.fox.ex.ExBadPath;
import uk.gov.dti.og.fox.ex.ExDB;
import uk.gov.dti.og.fox.ex.ExInternal;
import uk.gov.dti.og.fox.ex.ExModule;
import uk.gov.dti.og.fox.ex.ExRuntimeRoot;
import uk.gov.dti.og.fox.ex.ExServiceUnavailable;
import uk.gov.dti.og.fox.ex.ExTooFew;
import uk.gov.dti.og.fox.ex.ExTooMany;
import uk.gov.dti.og.fox.io.IOUtil;
import uk.gov.dti.og.fox.io.StreamParcel;
import uk.gov.dti.og.fox.io.StreamParcelInput;
import uk.gov.dti.og.fox.io.StreamParcelInputTempResource;
import uk.gov.dti.og.fox.renderer.Renderer;
import uk.gov.dti.og.fox.renderer.RendererEMF;
import uk.gov.dti.og.fox.spatial.SpatialEngine;
import uk.gov.dti.og.fox.spatial.SpatialRenderer;
import uk.gov.dti.og.fox.track.Track;
import uk.gov.dti.og.fox.xhtml.HtmlGenerator;
import uk.gov.dti.og.fox.xhtml.NodeInfo;
import uk.gov.dti.og.net.fileuploads.FileUploadInfo;

/** 
 * Simple command that sends alerts to the users browser.
 *
 * @author Philip Simpson
 */
public class GenerateCommand extends Track implements Command {

    /** The buffer to generate */
    private String mBuffer;

    private String mState;

    private String mMethod;

    private String mCache;

    private String mExpires;

    private String mScope;

    private String mContentType;

    private String mOutputType;

    private String mFileName;

    private String mUrlPath;

    private String mCopyToPath;

    private String mStoreLocationName;

    private String mSpatialDefinition;

    private String mSpatialInterface;

    private String mSpatialCanvasCacheKey;

    private Sheet sheets[];

    private static int uniqueWindowId = 1;

    private static final Set schemaIntegerTypeNamesSet = new HashSet(Arrays.asList(StringUtil.commaDelimitedListToStringArray("int,integer,long,nonPositiveInteger,nonNegativeInteger,negativeInteger," + "int,short,byte,unsignedLong,positiveInteger,unsignedInt,unsignedShort," + "unsignedByte")));

    private static final Set schemaRealTypeNamesSet = new HashSet(Arrays.asList(StringUtil.commaDelimitedListToStringArray("decimal,float,double")));

    private static final Set schemaDateTimeTypeNamesSet = new HashSet(Arrays.asList(StringUtil.commaDelimitedListToStringArray("date,time,dateTime")));

    /**
   * Contructs the command from the XML element specified.
   *
   * @param commandElement the element from which the command will
   *        be constructed.
   */
    public GenerateCommand(Mod pMod, DOM commandElement) throws ExInternal {
        mSpatialDefinition = commandElement.getAttrOrNull("spatial-definition");
        mSpatialInterface = commandElement.getAttrOrNull("spatial-interface");
        mSpatialCanvasCacheKey = commandElement.getAttrOrNull("canvas-cache-key");
        mBuffer = commandElement.getAttrOrNull("buffer");
        mState = commandElement.getAttrOrNull("state");
        mStoreLocationName = commandElement.getAttrOrNull("storage-location");
        mUrlPath = XFUtil.nvl(commandElement.getAttrOrNull("url-xpath"), commandElement.getAttrOrNull("target"));
        mCopyToPath = commandElement.getAttrOrNull("copy-to-xpath");
        mExpires = calcMins(XFUtil.nvl(commandElement.getAttrOrNull("expires"), "00:09"));
        mContentType = XFUtil.nvl(commandElement.getAttrOrNull("content-type"), "text/html; charset=UTF-8");
        mOutputType = XFUtil.nvl(commandElement.getAttrOrNull("output-type"), "XHTML");
        mFileName = commandElement.getAttrOrNull("file-name");
        mCache = calcMins(XFUtil.nvl(commandElement.getAttrOrNull("client-cache"), "0"));
        mScope = XFUtil.nvl(commandElement.getAttrOrNull("scope"), "SESSION");
        if (!mScope.equals("SESSION")) {
            throw new ExInternal("fm:generate: the only valid scope at this time is SESSION - reserved for future use");
        }
        mMethod = XFUtil.nvl(commandElement.getAttrOrNull("method"), "preview");
        if (mMethod.equals("url") || mMethod.equals("internal")) {
            mMethod = "url";
            if (!XFUtil.exists(mUrlPath)) {
                throw new ExInternal("fm:generate: Method='url' requires url-xpath attr");
            }
        } else if (mMethod.equals("storage-location")) {
            if (!XFUtil.exists(mStoreLocationName)) {
                throw new ExInternal("fm:generate: Method='storage-location' requires storage-location attr");
            }
            if (XFUtil.exists(mUrlPath)) {
                throw new ExInternal("fm:generate: Attr url-path not supported with method='storage-location'");
            }
        } else if (mMethod.equals("copy-to")) {
            if (mOutputType.equalsIgnoreCase("XLS") || mOutputType.equalsIgnoreCase("CSV")) {
                throw new ExInternal("fm:generate: Method='copy-to' is not supported for MS Excel output methods of \"XLS\" or \"CSV\".");
            }
            if (!XFUtil.exists(mCopyToPath)) {
                throw new ExInternal("fm:generate: Method='copy-to' requires copy-to-xpath attr");
            }
            if (XFUtil.exists(mUrlPath)) {
                throw new ExInternal("fm:generate: Attr url-path not supported with method='copy-to'");
            }
        } else if (!mMethod.equals("preview")) {
            throw new ExInternal("fm:generate: Method unknown: " + mMethod);
        }
        try {
            DOMList sheetListElems = commandElement.xpathUL("fm:generate-sheet", null);
            List sheetList = new ArrayList(sheetListElems.getLength());
            for (int s = 0; s < sheetListElems.getLength(); s++) {
                DOM sheetElem = sheetListElems.item(s);
                Sheet sheet = new Sheet();
                sheetList.add(sheet);
                String sheetName = XFUtil.nvl(sheetElem.getAttrOrNull("name"), "Sheet-" + s);
                String rowExpr = sheetElem.getAttrOrNull("row-expr");
                String showHeadersExpr = sheetElem.getAttrOrNull("show-headers");
                if (rowExpr == null) {
                    throw new ExInternal("fm:generate: A sheet (number " + (s + 1) + ") has no row expression attribute - please specify one in order to " + "supply data for the worksheet.");
                }
                sheet.setName(sheetName);
                sheet.setRowExpression(rowExpr);
                sheet.setShowHeadersExpr(showHeadersExpr);
                DOMList columnListElems = sheetElem.xpathUL("fm:generate-column", null);
                List columnList = new ArrayList(columnListElems.getLength());
                for (int c = 0; c < columnListElems.getLength(); c++) {
                    DOM columnElem = columnListElems.item(c);
                    Sheet.Column column = new Sheet.Column();
                    columnList.add(column);
                    String columnName = XFUtil.nvl(columnElem.getAttrOrNull("name"), "Column-" + s);
                    String columExpr = columnElem.getAttrOrNull("column-expr");
                    String type = columnElem.getAttrOrNull("datatype");
                    String inputFormat = columnElem.getAttrOrNull("input-format");
                    String outputFormat = columnElem.getAttrOrNull("output-format");
                    String visibleExpr = columnElem.getAttrOrNull("visible-expr");
                    if (columExpr == null) {
                        throw new ExInternal("fm:generate: A sheet column (sheet=" + (s + 1) + ", column=" + (c + 1) + ") has no column expression attribute - please specify one in order to " + "supply data for the worksheet column.");
                    }
                    column.setName(columnName);
                    column.setColumnExpression(columExpr);
                    column.setType(type);
                    column.setInputFormatSpecification(inputFormat);
                    column.setOutputFormatSpecification(outputFormat);
                    column.setVisibleExpression(visibleExpr);
                }
                sheet.setColumns(columnList);
            }
            sheets = (Sheet[]) sheetList.toArray(new Sheet[sheetList.size()]);
        } catch (ExBadPath badPathEx) {
            throw badPathEx.toUnexpected();
        }
    }

    public static String calcMins(String pTime) {
        int mins = 0;
        int pos;
        String str = pTime;
        if ((pos = str.indexOf(':')) != -1) {
            str = str.substring(0, pos);
            if (XFUtil.exists(str)) {
                mins = 60 * Integer.valueOf(str).intValue();
            }
        } else {
            pos = -1;
        }
        str = pTime.substring(pos + 1);
        if (XFUtil.exists(str)) {
            mins += Integer.valueOf(str).intValue();
        }
        return String.valueOf(mins);
    }

    /**
   * Runs the command with the specified user thread and session.
   *
   * @param userThread the user thread context of the command
   * @return userSession the user's session context
   */
    public void run(XThread pXThread, ContextUElem pContextUElem, ContextUCon pContextUCon) {
        try {
            String outputType = pContextUElem.extendedStringOrXPathString(pContextUElem.attachDOM(), mOutputType);
            ContextUCon lLocalContextUCon = null;
            try {
                if (mMethod.equals("storage-location") && ("CSV".equalsIgnoreCase(outputType) || "XLS".equalsIgnoreCase(outputType))) {
                    try {
                        lLocalContextUCon = new ContextUCon();
                        lLocalContextUCon.define(ContextUCon.MAIN_TRANSACTION, ConAgent.getUCon(pXThread.getTopApp().mConnectKey, "Generate Excel"));
                        pContextUCon = lLocalContextUCon;
                    } catch (ExServiceUnavailable e) {
                        throw new ExInternal("Could not get UCon for storage-location generate", e);
                    }
                } else if (mMethod.equals("preview") || mMethod.equals("url")) {
                    pContextUCon = pContextUCon.duplicate().push(ContextUCon.THREAD_TRANSACION);
                }
                if ("XHTML".equalsIgnoreCase(outputType)) {
                    runXHTMLGenerate(pXThread, pContextUElem, pContextUCon);
                } else if ("CSV".equalsIgnoreCase(outputType) || "XLS".equalsIgnoreCase(outputType)) {
                    runExcelGenerate(outputType, pXThread, pContextUElem, pContextUCon);
                } else if ("SPATIAL-EMF".equalsIgnoreCase(outputType)) {
                    runSpatialEMFGenerate(pXThread, pContextUElem, pContextUCon);
                } else {
                    throw new ExInternal("Unrecognised generate method, \"" + mOutputType + "\", used - please check your module and use one of the enumerated types!");
                }
            } finally {
                if (lLocalContextUCon != null) {
                    lLocalContextUCon.getUCon().closeForRecycle();
                }
            }
        } catch (ExActionFailed ex) {
            ex.toUnexpected();
        }
    }

    public void runSpatialEMFGenerate(XThread pXThread, ContextUElem pContextUElem, ContextUCon pContextUCon) {
        UCon lUCon = null;
        BLOB lBlob = null;
        String lTempResourceId = pXThread.createTempWorkDocId();
        String lContentType = "application/emf";
        String lFileExtension = ".emf";
        String lFileName = "generated-" + XFUtil.unique() + lFileExtension;
        if (!XFUtil.isNull(mFileName)) {
            try {
                lFileName = pContextUElem.extendedStringOrXPathString(pContextUElem.attachDOM(), mFileName) + lFileExtension;
            } catch (ExActionFailed e) {
                throw new ExInternal("Filename XPath is invalid: " + mFileName, e);
            }
        }
        try {
            lUCon = pContextUCon.getUCon();
            SpatialEngine lSpatialEngine = pXThread.getTopApp().mSpatialEngine;
            if (lSpatialEngine == null) {
                throw new ExInternal("Cartographic widget could not be instantiated", new ExApp("Spatial engine not defined for application " + pXThread.getTopApp().getMnemonicName()));
            }
            String lDefMnem = mSpatialDefinition;
            String lIntfMnem = mSpatialInterface;
            String lCacheKey = mSpatialCanvasCacheKey;
            if (XFUtil.isNull(lDefMnem)) {
                throw new ExInternal("Null value for spatial-definition found on schema element fm:generate");
            } else if (XFUtil.isNull(lIntfMnem)) {
                throw new ExInternal("Null value for spatial-interface found on schema element fm:generate");
            } else if (XFUtil.isNull(lCacheKey)) {
                throw new ExInternal("Null value for canvas-cache-key found on schema element fm:generate");
            }
            try {
                lDefMnem = XFUtil.nvl(pContextUElem.extendedXPathString(pContextUElem.attachDOM(), lDefMnem), lDefMnem);
                lIntfMnem = XFUtil.nvl(pContextUElem.extendedXPathString(pContextUElem.attachDOM(), lIntfMnem), lIntfMnem);
                lCacheKey = XFUtil.nvl(pContextUElem.extendedXPathString(pContextUElem.attachDOM(), lCacheKey), lCacheKey);
            } catch (ExActionFailed ex) {
                throw new ExInternal("Could not process XPath", ex);
            }
            DOM lSpatialBootstrapDOM = DOM.createDocument("spatial-canvas-bootstrap");
            if (!XFUtil.isNull(lDefMnem) && !XFUtil.isNull(lIntfMnem)) {
                lSpatialBootstrapDOM.addElem("spatial-definition-mnem", lDefMnem);
                lSpatialBootstrapDOM.addElem("spatial-interface-mnem", lIntfMnem);
                lSpatialBootstrapDOM.addElem("canvas-cache-key", lCacheKey);
                lSpatialBootstrapDOM.addElem("data").addElem("PROJECT_CACHE_KEY", lCacheKey);
            } else {
                throw new ExInternal("Evaluated values of ns:spatial-definition and ns:spatial-interface may not be null");
            }
            lSpatialBootstrapDOM = lSpatialEngine.bootstrapSpatialCanvas(lSpatialBootstrapDOM, pXThread.getCurrentCallId(), XFUtil.nvl(pXThread.getFoxUser().getUserDOM().get1SNoEx("/*/WUA_ID"), "0"), lUCon);
            String lXmlRequestBody;
            String lCanvasId;
            Integer lWidth;
            Integer lHeight;
            String lStatement = "DECLARE\n" + "  l_render_xml XMLTYPE;\n" + "BEGIN\n" + "  l_render_xml := spatialmgr.spm_fox.generate_render_xml(\n" + "    p_sc_id      => :1\n" + "  , p_width_px   => :2\n" + "  , p_height_px  => :3\n" + "  , p_datasource => :4\n" + "  );\n" + "  :5 := l_render_xml.getClobVal();\n" + "END;";
            try {
                lCanvasId = lSpatialBootstrapDOM.get1S("/*/canvas-id");
                lWidth = new Integer(lSpatialBootstrapDOM.get1S("/*/canvas-width").replaceAll("[^0-9]", ""));
                lHeight = new Integer(lSpatialBootstrapDOM.get1S("/*/canvas-height").replaceAll("[^0-9]", ""));
                Object lParams[] = { lCanvasId, lWidth, lHeight, pXThread.getTopApp().mConnectKey.toUpperCase().intern(), CLOB.class };
                lUCon.executeCall(lStatement, lParams, new char[] { 'I', 'I', 'I', 'I', 'O' });
                StringBuffer sb = lUCon.clob2StringBuffer((CLOB) lParams[4], 0, null);
                lXmlRequestBody = sb.toString();
            } catch (ExDB ex) {
                throw new ExInternal("Query failed in Spatial Renderer", ex);
            } catch (ExTooMany ex) {
                throw new ExInternal("Cardinality error resulting from spatial rendering query", ex);
            } catch (ExTooFew ex) {
                throw new ExInternal("Cardinality error resulting from spatial rendering query", ex);
            } catch (ExServiceUnavailable ex) {
                throw new ExInternal("Service unavailable in spatial rendering query", ex);
            }
            DOM lRequestDOM = DOM.createDocumentFromXMLString(lXmlRequestBody);
            int lEMFHeight, lEMFWidth;
            lEMFHeight = Integer.parseInt(lRequestDOM.get1SNoEx("/INTERNAL_MAP_REQUEST/HEIGHT"));
            lEMFWidth = Integer.parseInt(lRequestDOM.get1SNoEx("/INTERNAL_MAP_REQUEST/WIDTH"));
            Renderer lEMF = new RendererEMF(lEMFWidth, lEMFHeight, 72, null);
            SpatialRenderer.internalRender(lRequestDOM, lUCon, lEMF);
            OutputStream blobOS = null;
            try {
                lBlob = lUCon.getTemporaryBlob();
                blobOS = lBlob.getBinaryOutputStream();
                blobOS.write(lEMF.generate());
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                IOUtil.close(blobOS);
            }
            if (mMethod.equals("url") || mMethod.equals("preview")) {
                pXThread.getTopApp().createTemporaryResource(lTempResourceId, "4", "4", lBlob, lContentType, lUCon);
                StreamParcel lStreamParcel;
                try {
                    lStreamParcel = new StreamParcel(StreamParcel.TYPE_STANDARD, lFileName, pXThread.getTopApp(), pXThread.getFoxRequest().getCookieValue("streamsession"));
                } catch (ExServiceUnavailable e) {
                    throw new ExInternal("Unable to get App for creating Stream Parcel: ", e);
                }
                StreamParcelInput lStreamParcelInput = new StreamParcelInputTempResource(lFileName, null, lContentType, lTempResourceId);
                lStreamParcel.addStreamParcelInput(lStreamParcelInput, pXThread);
                pXThread.addURIFilePopup(lStreamParcel.getStreamURL(pXThread.getFoxRequest()), lFileName);
            } else if (mMethod.equalsIgnoreCase("storage-location")) {
                FileStorageLocation sl = pXThread.getTopModule().getFileStorageLocation(mStoreLocationName);
                if (sl == null) {
                    throw new ExInternal("Unable to locate file storage location, \"" + mStoreLocationName + "\", in module \"" + pXThread.getTopModule().getName() + "\" - " + "please update the module specification and ensure one exists.");
                }
                WorkingFileStoreLocation workingSL = new WorkingFileStoreLocation(sl, pContextUElem);
                workingSL.updateLOB(lBlob, pContextUCon, new FileUploadInfo());
            }
        } catch (ExServiceUnavailable e) {
            throw new ExInternal("No connection available for fm:generate", e);
        } catch (ExModule e) {
            throw new ExInternal("An unexpected error occurred during Spatial EMF generation: ", e);
        } finally {
            try {
                lBlob.close();
            } catch (Throwable ignore) {
            }
        }
    }

    /**
   * Runs the command with the specified user thread and session for XHTML
   * generation.
   *
   * @param userThread the user thread context of the command
   * @return userSession the user's session context
   */
    public void runXHTMLGenerate(XThread pXThread, ContextUElem pContextUElem, ContextUCon pContextUCon) {
        UCon lUCon = pContextUCon.getUCon();
        String lResourceId = null;
        WorkDoc lWorkDoc = null;
        DOM lHtmlDOM;
        try {
            if (mMethod.equals("url") || mMethod.equals("preview")) {
                lResourceId = pXThread.createTempWorkDocId();
                lWorkDoc = pXThread.getOpenTempWorkDoc(mExpires, mCache, lResourceId, lUCon);
                lWorkDoc.setContentType("text/html; charset=UTF-8");
                lHtmlDOM = lWorkDoc.getXML();
            } else if (mMethod.equals("storage-location")) {
                WorkingStoreLocation lWorkingStoreLocation;
                try {
                    lWorkingStoreLocation = new WorkingStoreLocation(pXThread.getTopModule().getStoreLocation(mStoreLocationName), pContextUElem, pXThread.getTopApp(), pXThread.getCurrentCallId());
                } catch (ExModule e) {
                    throw e.toUnexpected("fm:generate: Storage-location error");
                } catch (ExServiceUnavailable e) {
                    throw e.toUnexpected("fm:generate: Storage-location error");
                }
                lWorkDoc = WorkDoc.getWorkDoc(lWorkingStoreLocation, pXThread.getThreadRef());
                try {
                    lWorkDoc.open(lUCon, pXThread, pContextUElem, pContextUCon);
                } catch (ExModule e) {
                    throw e.toUnexpected("fm:generate: Unable to open storage-location");
                } catch (ExServiceUnavailable e) {
                    throw e.toUnexpected("fm:generate: Unable to open storage-location");
                }
                lWorkDoc.setContentType("text/html; charset=UTF-8");
                lHtmlDOM = lWorkDoc.getXML();
            } else {
                lHtmlDOM = DOM.createDocument("html");
            }
            State lState;
            if (XFUtil.exists(mState)) {
                lState = pXThread.getTopModule().getState(mState);
                if (lState == null) {
                    throw new ExInternal("Generate command is trying to use an invalid state called: " + mState);
                }
            } else {
                lState = pXThread.getActiveState();
            }
            try {
                HtmlGenerator hgen = new HtmlGenerator(pXThread, pXThread.getFoxRequest(), lHtmlDOM, lState, true);
                hgen.generate(mBuffer);
            } catch (ExModule e) {
                throw new ExInternal("Mute HtmlGenerator has created an error", e);
            }
            if (XFUtil.exists(mCopyToPath)) {
                try {
                    lHtmlDOM.copyToParent(pContextUElem.getCreateXPath1E(mCopyToPath));
                } catch (ExActionFailed e) {
                    throw e.toUnexpected("fm:generate: Error assigning value to url-xpath");
                }
            }
            if (lUCon != null && lWorkDoc != null) {
                try {
                    lWorkDoc.close();
                    lWorkDoc = null;
                } catch (ExModule e) {
                    throw e.toUnexpected("fm:generate: error closing storage-location");
                } catch (ExServiceUnavailable e) {
                    throw e.toUnexpected("fm:generate: error closing storage-location");
                }
            }
            if (mMethod.equals("preview") || XFUtil.exists(mUrlPath)) {
                String lFileName = "generated.html";
                StreamParcel lStreamParcel;
                try {
                    lStreamParcel = new StreamParcel(StreamParcel.TYPE_STANDARD, lFileName, pXThread.getTopApp(), pXThread.getFoxRequest().getCookieValue("streamsession"));
                } catch (ExServiceUnavailable e) {
                    throw new ExInternal("Unable to get App for creating Stream Parcel: ", e);
                }
                lStreamParcel.addStreamParcelInput(new StreamParcelInputTempResource(lFileName, null, mContentType, lResourceId), pXThread);
                if (mMethod.equals("preview")) {
                    pXThread.addURIFilePopup(lStreamParcel.getStreamURL(pXThread.getFoxRequest()), lFileName);
                } else if (XFUtil.exists(mUrlPath)) {
                    if (XFUtil.exists(mUrlPath)) {
                        String lUrlString = lStreamParcel.getStreamURL(pXThread.getFoxRequest());
                        try {
                            pContextUElem.getCreateXPath1E(mUrlPath).setText(lUrlString);
                        } catch (ExActionFailed e) {
                            throw e.toUnexpected("fm:generate: Error assigning value to url-xpath");
                        }
                    }
                }
            }
        } finally {
            if (lWorkDoc != null) {
                try {
                    lWorkDoc.abort();
                } catch (ExRuntimeRoot ignore) {
                }
            }
        }
    }

    /**
   * Runs the command with the specified user thread and session for XHTML
   * generation.
   *
   * @param outputType the type of output required: CSV or XLS
   * @param userThread the user thread context of the command
   * @param pContextUElem the contextual DOM elements
   * @param pContextUCon the contextual connection parameters
   */
    public void runExcelGenerate(String outputType, XThread userThread, ContextUElem pContextUElem, ContextUCon pContextUCon) {
        String fileExtension = "." + outputType.toLowerCase();
        String contentType = ("CSV".equalsIgnoreCase(outputType) ? "text/csv" : "application/vnd.ms-excel");
        String popupWindowName = getUniqueWindowName();
        UCon lUCon = null;
        BLOB lBlob = null;
        CLOB lClob = null;
        try {
            lUCon = pContextUCon.getUCon();
            Writer writer = null;
            OutputStream blobOS = null;
            String lFileName = "generated-" + XFUtil.unique() + fileExtension;
            if (!XFUtil.isNull(mFileName)) {
                try {
                    lFileName = pContextUElem.extendedStringOrXPathString(pContextUElem.attachDOM(), mFileName) + fileExtension;
                } catch (ExActionFailed e) {
                    throw new ExInternal("Filename XPath is invalid: " + mFileName, e);
                }
            }
            String lTempResourceId = userThread.createTempWorkDocId();
            if ("CSV".equalsIgnoreCase(outputType)) {
                try {
                    lClob = lUCon.getTemporaryClob();
                    try {
                        writer = lClob.getCharacterOutputStream();
                    } catch (SQLException e) {
                        throw new ExInternal("ERROR", e);
                    }
                    for (int s = 0; s < sheets.length; s++) {
                        boolean visibleColumns[] = new boolean[sheets[s].getColumns().length];
                        boolean lShowHeaders;
                        if (XFUtil.isNull(sheets[s].getShowHeadersExpr())) {
                            lShowHeaders = true;
                        } else {
                            lShowHeaders = pContextUElem.extendedXPathBoolean(pContextUElem.attachDOM(), sheets[s].getShowHeadersExpr());
                        }
                        for (int c = 0; c < sheets[s].getColumns().length; c++) {
                            Sheet.Column column = sheets[s].getColumns()[c];
                            visibleColumns[c] = column.getVisibleExpression() == null || pContextUElem.extendedXPathBoolean(pContextUElem.attachDOM(), column.getVisibleExpression());
                        }
                        for (int c = 0; c < sheets[s].getColumns().length; c++) {
                            Sheet.Column column = sheets[s].getColumns()[c];
                            String cellValue = pContextUElem.extendedStringOrXPathString(pContextUElem.attachDOM(), column.getNameExpr());
                            if (lShowHeaders && visibleColumns[c]) {
                                writer.write(escapeCSVFieldValue(cellValue));
                                if (c < sheets[s].getColumns().length - 1 && visibleColumns[c + 1]) writer.write(",");
                            }
                        }
                        if (lShowHeaders) {
                            writer.write(System.getProperty("line.separator", "\r\n"));
                        }
                        DOMList rowElems = pContextUElem.extendedXPathUL(sheets[s].getRowExpression(), ContextUElem.ATTACH);
                        for (int r = 0; r < rowElems.getLength(); r++) {
                            DOM rowElem = rowElems.item(r);
                            for (int c = 0; c < sheets[s].getColumns().length; c++) {
                                if (!visibleColumns[c]) continue;
                                Sheet.Column column = sheets[s].getColumns()[c];
                                XPathResult cellValue = pContextUElem.extendedXPathResult(rowElem, column.getColumnExpression());
                                writer.write(getOrConvertCSVCellValue(userThread, pContextUElem, pContextUCon, cellValue, column));
                                if (c < sheets[s].getColumns().length - 1 && visibleColumns[c + 1]) writer.write(",");
                            }
                            writer.write(System.getProperty("line.separator", "\r\n"));
                        }
                    }
                } finally {
                    IOUtil.close(writer);
                }
            } else if ("XLS".equalsIgnoreCase(outputType)) {
                try {
                    lBlob = lUCon.getTemporaryBlob();
                    blobOS = lBlob.getBinaryOutputStream();
                    HSSFWorkbook wb = new HSSFWorkbook();
                    for (int s = 0; s < sheets.length; s++) {
                        HSSFSheet sheet = wb.createSheet(pContextUElem.extendedStringOrXPathString(pContextUElem.attachDOM(), sheets[s].getName()));
                        boolean visibleColumns[] = new boolean[sheets[s].getColumns().length];
                        boolean lShowHeaders;
                        if (XFUtil.isNull(sheets[s].getShowHeadersExpr())) {
                            lShowHeaders = false;
                        } else {
                            lShowHeaders = pContextUElem.extendedXPathBoolean(pContextUElem.attachDOM(), sheets[s].getShowHeadersExpr());
                        }
                        for (int c = 0; c < sheets[s].getColumns().length; c++) {
                            Sheet.Column column = sheets[s].getColumns()[c];
                            visibleColumns[c] = column.getVisibleExpression() == null || pContextUElem.extendedXPathBoolean(pContextUElem.attachDOM(), column.getVisibleExpression());
                        }
                        int rowOffset = 0;
                        HSSFRow headerRow = null;
                        if (lShowHeaders) {
                            headerRow = sheet.createRow(0);
                            rowOffset++;
                        }
                        for (int c = 0, visCols = 0; c < sheets[s].getColumns().length; c++) {
                            Sheet.Column column = sheets[s].getColumns()[c];
                            String cellValue = pContextUElem.extendedStringOrXPathString(pContextUElem.attachDOM(), column.getNameExpr());
                            if (lShowHeaders && visibleColumns[c]) {
                                HSSFCell cell = headerRow.createCell((short) visCols);
                                cell.setCellValue(cellValue);
                            }
                            visCols++;
                        }
                        DOMList rowElems = pContextUElem.extendedXPathUL(sheets[s].getRowExpression(), ContextUElem.ATTACH);
                        for (int r = 0; r < rowElems.getLength(); r++) {
                            DOM rowElem = rowElems.item(r);
                            HSSFRow row = sheet.createRow(r + rowOffset);
                            for (int c = 0, visCols = 0; c < sheets[s].getColumns().length; c++) {
                                Sheet.Column column = sheets[s].getColumns()[c];
                                XPathResult cellValue = pContextUElem.extendedXPathResult(rowElem, column.getColumnExpression());
                                if (!visibleColumns[c]) continue;
                                HSSFCell cell = row.createCell((short) visCols);
                                setXLSCellValue(userThread, pContextUElem, pContextUCon, wb, cell, cellValue, column);
                                visCols++;
                            }
                        }
                    }
                    wb.write(blobOS);
                } finally {
                    IOUtil.close(blobOS);
                }
            }
            if (mMethod.equals("url") || mMethod.equals("preview")) {
                if (lBlob != null) {
                    userThread.getTopApp().createTemporaryResource(lTempResourceId, "4", "4", lBlob, contentType, lUCon);
                } else if (lClob != null) {
                    userThread.getTopApp().createTemporaryResource(lTempResourceId, "4", "4", lClob, contentType, lUCon);
                }
                StreamParcel lStreamParcel;
                try {
                    lStreamParcel = new StreamParcel(StreamParcel.TYPE_STANDARD, lFileName, userThread.getTopApp(), userThread.getFoxRequest().getCookieValue("streamsession"));
                } catch (ExServiceUnavailable e) {
                    throw new ExInternal("Unable to get App for creating Stream Parcel: ", e);
                }
                StreamParcelInput lStreamParcelInput = new StreamParcelInputTempResource(lFileName, null, contentType, lTempResourceId);
                lStreamParcel.addStreamParcelInput(lStreamParcelInput, userThread);
                userThread.addURIFilePopup(lStreamParcel.getStreamURL(userThread.getFoxRequest()), lFileName);
            } else if (mMethod.equalsIgnoreCase("storage-location")) {
                FileStorageLocation sl = userThread.getTopModule().getFileStorageLocation(mStoreLocationName);
                if (sl == null) {
                    throw new ExInternal("Unable to locate file storage location, \"" + mStoreLocationName + "\", in module \"" + userThread.getTopModule().getName() + "\" - " + "please update the module specification and ensure one exists.");
                }
                WorkingFileStoreLocation workingSL = new WorkingFileStoreLocation(sl, pContextUElem);
                if (lBlob != null) {
                    workingSL.updateLOB(lBlob, pContextUCon, new FileUploadInfo());
                } else if (lClob != null) {
                    workingSL.updateLOB(lClob, pContextUCon, new FileUploadInfo());
                }
            }
        } catch (Exception ioEx) {
            throw new ExInternal("An unexpected error occurred during Excel generation: ", ioEx);
        } finally {
            try {
                lBlob.close();
            } catch (Throwable ignore) {
            }
            try {
                lClob.close();
            } catch (Throwable ignore) {
            }
            try {
                lUCon.freeTemporaryBlob(lBlob);
            } catch (Throwable ignore) {
            }
            try {
                lUCon.freeTemporaryClob(lClob);
            } catch (Throwable ignore) {
            }
        }
    }

    private String getOrConvertCSVCellValue(XThread userThread, ContextUElem pContextUElem, ContextUCon pContextUCon, XPathResult cellValue, Sheet.Column column) throws ExBadPath {
        String cellStrValue = cellValue.asString();
        DOM resultDOM = cellValue.asResultDOMOrNull();
        NodeInfo nodeInfo = resultDOM != null ? userThread.getTopModule().getNodeInfo(resultDOM) : null;
        String columnDataType = column.getType();
        if (cellStrValue == null) cellStrValue = "";
        if (columnDataType == null && nodeInfo != null) {
            String moduleDataType = nodeInfo.getDataTypeIntern();
            int colonPos = moduleDataType.indexOf(":");
            if (colonPos >= 0) moduleDataType = moduleDataType.substring(colonPos + 1);
            if (schemaIntegerTypeNamesSet.contains(moduleDataType)) columnDataType = "integer"; else if (schemaRealTypeNamesSet.contains(moduleDataType)) columnDataType = "real"; else if (schemaDateTimeTypeNamesSet.contains(moduleDataType)) columnDataType = moduleDataType.toLowerCase();
        }
        if ("integer".equalsIgnoreCase(columnDataType) || "real".equalsIgnoreCase(columnDataType)) {
            Number number = null;
            try {
                number = cellValue.asNumber();
                String excelFormatPattern = XFUtil.nvl(column.getOutputFormatSpecification(), ("real".equalsIgnoreCase(columnDataType) ? "0.00" : "0"));
                DecimalFormat df = new DecimalFormat(excelFormatPattern);
                cellStrValue = df.format(number.doubleValue());
            } catch (Throwable ignoreTh) {
            }
        } else if ("boolean".equalsIgnoreCase(columnDataType)) {
            Number number = null;
            try {
                cellStrValue = cellValue.asBoolean() ? "true" : "false";
            } catch (Throwable ignoreTh) {
            }
        } else if ("date".equalsIgnoreCase(columnDataType) || "time".equalsIgnoreCase(columnDataType) || "datetime".equalsIgnoreCase(columnDataType)) {
            try {
                String inputDateFormat = column.getInputFormatSpecification();
                if (inputDateFormat == null) {
                    if ("date".equalsIgnoreCase(columnDataType)) inputDateFormat = "yyyy-MM-dd"; else if ("time".equalsIgnoreCase(columnDataType)) inputDateFormat = "HH:mm:ss"; else if ("datetime".equalsIgnoreCase(columnDataType)) inputDateFormat = "yyyy-MM-dd'T'HH:mm:ss"; else inputDateFormat = "yyyy-MM-dd";
                }
                SimpleDateFormat df = new SimpleDateFormat(inputDateFormat);
                Date inputDate = df.parse(cellValue.asString());
                String excelFormatPattern = column.getOutputFormatSpecification();
                if (excelFormatPattern == null) {
                    if ("date".equalsIgnoreCase(columnDataType)) excelFormatPattern = "dd-MMM-YYYY"; else if ("time".equalsIgnoreCase(columnDataType)) excelFormatPattern = "hh:mm:ss a"; else if ("datetime".equalsIgnoreCase(columnDataType)) excelFormatPattern = "dd-MMM-YYYY hh:mm:ss a"; else excelFormatPattern = "dd-MMM-YYYY";
                }
                df.applyPattern(excelFormatPattern);
                cellStrValue = df.format(inputDate);
            } catch (Throwable ignoreTh) {
            }
        }
        return escapeCSVFieldValue(cellStrValue);
    }

    private String escapeCSVFieldValue(String cellStrValue) {
        boolean containsCommas = cellStrValue.indexOf(',') >= 0;
        boolean containsDoubleQuotes = cellStrValue.indexOf('\"') >= 0;
        boolean containsLeadingOrTrailingSpaces = cellStrValue.startsWith(" ") || cellStrValue.endsWith(" ");
        boolean containsLineBreaks = cellStrValue.indexOf("\r") > 0 || cellStrValue.indexOf("\n") > 0;
        boolean requiresDoubleQuoteDelimiters = containsCommas || containsDoubleQuotes || containsLeadingOrTrailingSpaces || containsLineBreaks;
        if (containsDoubleQuotes) {
            cellStrValue = StringUtil.replace(cellStrValue, "\"", "\"\"");
        }
        if (requiresDoubleQuoteDelimiters) cellStrValue = "\"" + cellStrValue + "\"";
        return cellStrValue;
    }

    private void setXLSCellValue(XThread userThread, ContextUElem pContextUElem, ContextUCon pContextUCon, HSSFWorkbook workBook, HSSFCell cell, XPathResult cellValue, Sheet.Column column) throws ExBadPath {
        DOM resultDOM = cellValue.asResultDOMOrNull();
        NodeInfo nodeInfo = resultDOM != null ? userThread.getTopModule().getNodeInfo(resultDOM) : null;
        String columnDataType = column.getType();
        if (columnDataType == null && nodeInfo != null) {
            String moduleDataType = nodeInfo.getDataTypeIntern();
            int colonPos = moduleDataType.indexOf(":");
            if (colonPos >= 0) moduleDataType = moduleDataType.substring(colonPos + 1);
            if (schemaIntegerTypeNamesSet.contains(moduleDataType)) columnDataType = "integer"; else if (schemaRealTypeNamesSet.contains(moduleDataType)) columnDataType = "real"; else if (schemaDateTimeTypeNamesSet.contains(moduleDataType)) columnDataType = moduleDataType.toLowerCase();
        }
        if ("integer".equalsIgnoreCase(columnDataType) || "real".equalsIgnoreCase(columnDataType)) {
            Number number = null;
            try {
                number = cellValue.asNumber();
                cell.setCellValue(number.doubleValue());
                String excelFormatPattern = XFUtil.nvl(column.getOutputFormatSpecification(), ("real".equalsIgnoreCase(columnDataType) ? "0.00" : "0"));
                HSSFCellStyle cellStyle = workBook.createCellStyle();
                cellStyle.setDataFormat(workBook.createDataFormat().getFormat(excelFormatPattern));
                cell.setCellStyle(cellStyle);
            } catch (Throwable ignoreTh) {
                cell.setCellValue(cellValue.asString());
            }
        } else if ("boolean".equalsIgnoreCase(columnDataType)) {
            try {
                cell.setCellValue(cellValue.asBoolean() ? "true" : "false");
            } catch (Throwable ignoreTh) {
            }
        } else if ("date".equalsIgnoreCase(columnDataType) || "time".equalsIgnoreCase(columnDataType) || "datetime".equalsIgnoreCase(columnDataType)) {
            try {
                String inputDateFormat = column.getInputFormatSpecification();
                if (inputDateFormat == null) {
                    if ("date".equalsIgnoreCase(columnDataType)) inputDateFormat = "yyyy-MM-dd"; else if ("time".equalsIgnoreCase(columnDataType)) inputDateFormat = "HH:mm:ss"; else if ("datetime".equalsIgnoreCase(columnDataType)) inputDateFormat = "yyyy-MM-dd'T'HH:mm:ss"; else inputDateFormat = "yyyy-MM-dd";
                }
                SimpleDateFormat df = new SimpleDateFormat(inputDateFormat);
                cell.setCellValue(df.parse(cellValue.asString()));
                String excelFormatPattern = column.getOutputFormatSpecification();
                if (excelFormatPattern == null) {
                    if ("date".equalsIgnoreCase(columnDataType)) excelFormatPattern = "d-mmm-yyyy"; else if ("time".equalsIgnoreCase(columnDataType)) excelFormatPattern = "h:mm:ss AM/PM"; else if ("datetime".equalsIgnoreCase(columnDataType)) excelFormatPattern = "d-mmm-yyyy h:mm:ss AM/PM"; else excelFormatPattern = "d-mmm-yyyy";
                }
                HSSFCellStyle cellStyle = workBook.createCellStyle();
                cellStyle.setDataFormat(workBook.createDataFormat().getFormat(excelFormatPattern));
                cell.setCellStyle(cellStyle);
            } catch (Throwable ignoreTh) {
                cell.setCellValue(cellValue.asString());
            }
        } else {
            cell.setCellValue(cellValue.asString());
        }
    }

    /**
    * Returns a unique name for a popup window.
    * 
    * @return a unique window name
    */
    private static String getUniqueWindowName() {
        return "gen" + (uniqueWindowId++);
    }

    public void validate(Mod module) {
    }

    private static class Sheet {

        protected String name;

        protected String rowExpr;

        protected Column columns[];

        protected String showHeadersExpr;

        public static class Column {

            protected String name;

            protected String columnExpression;

            protected String type;

            protected String inputFormatSpecification;

            protected String outputFormatSpecification;

            protected String visibleExpression;

            public Column() {
            }

            public String getNameExpr() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getColumnExpression() {
                return columnExpression;
            }

            public void setColumnExpression(String columnExpression) {
                this.columnExpression = columnExpression;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getInputFormatSpecification() {
                return inputFormatSpecification;
            }

            public void setInputFormatSpecification(String inputFormatSpecification) {
                this.inputFormatSpecification = inputFormatSpecification;
            }

            public String getOutputFormatSpecification() {
                return outputFormatSpecification;
            }

            public void setOutputFormatSpecification(String outputFormatSpecification) {
                this.outputFormatSpecification = outputFormatSpecification;
            }

            public String getVisibleExpression() {
                return visibleExpression;
            }

            public void setVisibleExpression(String visibleExpression) {
                this.visibleExpression = visibleExpression;
            }
        }

        public Sheet() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRowExpression() {
            return rowExpr;
        }

        public void setRowExpression(String rowExpr) {
            this.rowExpr = rowExpr;
        }

        public Column[] getColumns() {
            return columns;
        }

        public void setColumns(Column columns[]) {
            this.columns = columns;
        }

        public void setColumns(List columns) {
            this.columns = (Column[]) columns.toArray(new Column[columns.size()]);
        }

        public void setShowHeadersExpr(String showHeadersExpr) {
            this.showHeadersExpr = showHeadersExpr;
        }

        public String getShowHeadersExpr() {
            return showHeadersExpr;
        }
    }

    public boolean isCallTransition() {
        return false;
    }
}
