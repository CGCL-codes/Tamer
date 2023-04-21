package com.yubuild.coreman.web.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.yubuild.coreman.business.DocumentManager;
import com.yubuild.coreman.business.SubDocumentManager;
import com.yubuild.coreman.business.exception.DocumentAccessException;
import com.yubuild.coreman.data.Document;
import com.yubuild.coreman.data.SubDocument;
import com.yubuild.coreman.data.User;

public class FileDisplayController implements Controller {

    private final Log log = LogFactory.getLog(getClass());

    DocumentManager documentManager;

    SubDocumentManager subDocumentManager;

    protected String repositoryPrefix;

    public FileDisplayController() {
    }

    public String getRepositoryPrefix() {
        return repositoryPrefix;
    }

    public void setRepositoryPrefix(String repositoryPrefix) {
        this.repositoryPrefix = repositoryPrefix;
    }

    public DocumentManager getDocumentManager() {
        return documentManager;
    }

    public void setDocumentManager(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        java.io.OutputStream os;
        String fileName2;
        String str = request.getParameter("documentId");
        User user = (User) request.getSession().getAttribute("currentUserForm");
        os = response.getOutputStream();
        if (str != null) {
            String type = request.getParameter("type");
            String docPath;
            if (StringUtils.isEmpty(request.getParameter("subdocument"))) {
                Document doc = getDocumentManager().getDocument(new Long(str), user.getId());
                if ("doc".equals(type)) docPath = repositoryPrefix + doc.getDocFilePath(); else docPath = repositoryPrefix + doc.getPdfFilePath();
            } else {
                SubDocument doc = getSubDocumentManager().getSubDocument(new Long(str), user.getId());
                if ("doc".equals(type)) docPath = repositoryPrefix + doc.getDocFilePath(); else docPath = repositoryPrefix + doc.getPdfFilePath();
            }
            String docName = docPath.substring(docPath.lastIndexOf(File.separator) + 1, docPath.length());
            setResponseContentType(response, docName.substring(docName.lastIndexOf('.') + 1, docName.length()));
            if (request.getParameter("compare") == null) response.setHeader("Content-Disposition", "attachment;filename=" + docName); else response.setHeader("Content-Disposition", "inline;filename=" + docName);
            byte f[] = readFile(docPath);
            response.addHeader("Content-Length", "" + f.length);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            if (log.isDebugEnabled()) log.debug("---------=====   response.getBufferSize()  " + response.getBufferSize());
            bos.write(f);
            bos.flush();
            bos.close();
            if (log.isDebugEnabled()) log.debug("---------===== zavrsio citanje fajla ....");
        }
        String ids[] = request.getParameterValues("downloadDocumentId");
        String subdocIds[] = request.getParameterValues("downlaodSubdocumentIds");
        if (ids == null && subdocIds == null) {
            if (log.isDebugEnabled()) log.debug("There are no files selected for download....");
            return null;
        }
        ZipOutputStream zipOutputStream = null;
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        String zipName = sdf.format(new Date());
        fileName2 = "coreman" + zipName + ".zip";
        String fileType = "zip";
        File tmpZip = null;
        FileOutputStream fos = null;
        String fileName = "";
        try {
            if (log.isDebugEnabled()) log.debug("---------===== pocinje file name =  " + repositoryPrefix + fileName2);
            tmpZip = new File(repositoryPrefix + fileName2);
            tmpZip.createNewFile();
            fos = new FileOutputStream(tmpZip);
            zipOutputStream = new ZipOutputStream(fos);
            zipOutputStream.setMethod(ZipOutputStream.STORED);
            setResponseContentType(response, fileType);
            CRC32 crc = new CRC32();
            if (log.isDebugEnabled()) log.debug("---------===== pocinje download zipa ....");
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    if (log.isDebugEnabled()) log.debug("citam document id=" + ids[i]);
                    Document doc = getDocumentManager().getDocument(new Long(ids[i]), user.getId());
                    fileName = "_p" + doc.getId() + doc.getPdfFilePath().substring(doc.getPdfFilePath().lastIndexOf(File.separator) + 1, doc.getPdfFilePath().length());
                    ZipEntry entry = new ZipEntry(fileName);
                    byte tmp[] = readFile(repositoryPrefix + doc.getPdfFilePath());
                    entry.setSize(tmp.length);
                    crc = new CRC32();
                    crc.update(tmp);
                    entry.setCrc(crc.getValue());
                    zipOutputStream.putNextEntry(entry);
                    if (log.isDebugEnabled()) log.debug("---------===== pre internog write ....");
                    zipOutputStream.write(tmp);
                    if (log.isDebugEnabled()) log.debug("---------===== posle internog write ....");
                    zipOutputStream.closeEntry();
                    if (log.isDebugEnabled()) log.debug("ubacen fajl u jar: " + fileName);
                    if (!StringUtils.isEmpty(doc.getDocFilePath())) {
                        fileName = "_d" + doc.getId() + doc.getDocFilePath().substring(doc.getDocFilePath().lastIndexOf(File.separator) + 1, doc.getDocFilePath().length());
                        entry = new ZipEntry(fileName);
                        tmp = readFile(repositoryPrefix + doc.getPdfFilePath());
                        entry.setSize(tmp.length);
                        crc = new CRC32();
                        crc.update(tmp);
                        entry.setCrc(crc.getValue());
                        zipOutputStream.putNextEntry(entry);
                        if (log.isDebugEnabled()) log.debug("---------===== pre internog write ....");
                        zipOutputStream.write(tmp);
                        zipOutputStream.closeEntry();
                        if (log.isDebugEnabled()) log.debug("---------===== posle internog write ....");
                    }
                }
            }
            if (subdocIds != null) {
                for (int i = 0; i < subdocIds.length; i++) {
                    if (log.isDebugEnabled()) log.debug("citam subdocument id=" + subdocIds[i]);
                    SubDocument doc = getSubDocumentManager().getSubDocument(new Long(subdocIds[i]), user.getId());
                    fileName = "_sdp" + doc.getId() + doc.getPdfFilePath().substring(doc.getPdfFilePath().lastIndexOf(File.separator) + 1, doc.getPdfFilePath().length());
                    ZipEntry entry = new ZipEntry(fileName);
                    byte tmp[] = readFile(repositoryPrefix + doc.getPdfFilePath());
                    entry.setSize(tmp.length);
                    crc = new CRC32();
                    crc.update(tmp);
                    entry.setCrc(crc.getValue());
                    zipOutputStream.putNextEntry(entry);
                    zipOutputStream.write(tmp);
                    zipOutputStream.closeEntry();
                    if (log.isDebugEnabled()) log.debug("ubacen fajl u jar: " + fileName);
                    if (!StringUtils.isEmpty(doc.getDocFilePath())) {
                        fileName = "_sdd" + doc.getId() + doc.getDocFilePath().substring(doc.getDocFilePath().lastIndexOf(File.separator) + 1, doc.getDocFilePath().length());
                        entry = new ZipEntry(fileName);
                        tmp = readFile(repositoryPrefix + doc.getPdfFilePath());
                        entry = new ZipEntry(fileName);
                        tmp = readFile(repositoryPrefix + doc.getPdfFilePath());
                        entry.setSize(tmp.length);
                        crc = new CRC32();
                        crc.update(tmp);
                        entry.setCrc(crc.getValue());
                        zipOutputStream.putNextEntry(entry);
                        if (log.isDebugEnabled()) log.debug("---------===== pre internog write ....");
                        zipOutputStream.write(tmp);
                        zipOutputStream.closeEntry();
                        if (log.isDebugEnabled()) log.debug("---------===== posle internog write ....");
                    }
                }
            }
            if (log.isDebugEnabled()) log.debug("---------===== zavrsio kreiranje fajla ....");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new Exception(t);
        } finally {
            zipOutputStream.flush();
            zipOutputStream.close();
        }
        byte f[] = readFile(repositoryPrefix + fileName2);
        response.addHeader("Content-Length", "" + f.length);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName2);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        if (log.isDebugEnabled()) log.debug("---------=====   response.getBufferSize()  " + response.getBufferSize());
        bos.write(f);
        bos.flush();
        bos.close();
        if (log.isDebugEnabled()) log.debug("---------===== zavrsio citanje fajla ....");
        return null;
    }

    private void setResponseContentType(HttpServletResponse response, String fileType) {
        if (fileType.equalsIgnoreCase("DOC")) response.setContentType("application/msword"); else if (fileType.equalsIgnoreCase("PDF")) response.setContentType("application/pdf"); else response.setContentType("application/x-download");
    }

    private byte[] readFile(String path) {
        if (log.isDebugEnabled()) log.debug("-------------============= usao u read= ");
        File file = new File(path);
        byte bytes[] = (byte[]) null;
        try {
            if (log.isDebugEnabled()) log.debug("-------------============= pravi input stream");
            InputStream is = new FileInputStream(file);
            if (log.isDebugEnabled()) log.debug("-------------============= uzima duzinu");
            long length = file.length();
            bytes = new byte[(int) length];
            int offset = 0;
            for (int numRead = 0; offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0; ) {
                offset += numRead;
                if (log.isDebugEnabled()) log.debug("-------------============= offset= " + offset);
            }
            if (offset < bytes.length) throw new DocumentAccessException("Could not completely read file " + file.getName());
            if (log.isDebugEnabled()) log.debug("-------------============= pre close");
            is.close();
        } catch (Exception e) {
            throw new DocumentAccessException(e.getMessage());
        }
        return bytes;
    }

    public SubDocumentManager getSubDocumentManager() {
        return subDocumentManager;
    }

    public void setSubDocumentManager(SubDocumentManager subDocumentManager) {
        this.subDocumentManager = subDocumentManager;
    }
}
