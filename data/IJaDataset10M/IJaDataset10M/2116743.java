package com.c2b2.ipoint.presentation.portlets.services;

import com.c2b2.ipoint.business.DocumentRepositoryServices;
import com.c2b2.ipoint.management.ManagementMBeans;
import com.c2b2.ipoint.model.DocumentRepository;
import com.c2b2.ipoint.model.PersistentModelException;
import com.c2b2.ipoint.model.Property;
import com.c2b2.ipoint.processing.PortalRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

/**
  * This class implements the service for downloading a file from a Document Repository
  * It takes two input parameters DocumentRepositoryID which is the ID of the
  * Document Repository and File which is the Repository relative file path
  * used to retrieve the file. If a User can not see the file a 404 is sent back
  * to the browser.
  * <p>
  * $Date: 2007/04/16 09:32:55 $
  * 
  * $Id: DownloadRepositoryFile.java,v 1.4 2007/04/16 09:32:55 steve Exp $<br>
  * 
  * Copyright 2005 C2B2 Consulting Limited. All rights reserved.
  * </p>
  * @author $Author: steve $
  * @version $Revision: 1.4 $
  */
public class DownloadRepositoryFile implements SupplementaryService {

    public static final String REPO_PARAM = "DocumentRepositoryID";

    public static final String OLD_REPO_PARAM = "Repository";

    public static final String FILE_PARAM = "File";

    private Logger myLogger;

    public DownloadRepositoryFile() {
        myLogger = Logger.getLogger(this.getClass().getName());
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        long start = System.currentTimeMillis();
        try {
            response.reset();
            String repository = request.getParameter(REPO_PARAM);
            String filePath = request.getParameter(FILE_PARAM);
            if (repository == null) {
                String oldRepo = request.getParameter(OLD_REPO_PARAM);
                if (oldRepo != null && oldRepo.startsWith("Portlet_")) {
                    repository = Property.getPropertyValue("PortalDocumentRepository");
                    if (filePath != null && !filePath.startsWith("/")) {
                        filePath = "/" + filePath;
                    }
                }
            }
            if (repository == null && filePath == null) {
                response.sendError(response.SC_NOT_FOUND, "The specified Media Resource was Not Found ");
            } else {
                try {
                    DocumentRepository dr = DocumentRepository.find(repository);
                    if (!dr.isVisibleTo(PortalRequest.getCurrentRequest().getCurrentUser())) {
                        myLogger.log(Level.INFO, "An illegal attempt was made to access the file " + repository + filePath + " which the user " + PortalRequest.getCurrentRequest().getCurrentUser().getUserName() + " does not have permission to access ");
                        response.sendError(response.SC_NOT_FOUND, "The Specified File was Not Found");
                    }
                    DocumentRepositoryServices drs = new DocumentRepositoryServices(dr);
                    File file = drs.convertToFile(filePath);
                    if (file.exists() && file.canRead()) {
                        FileNameMap fileMap = URLConnection.getFileNameMap();
                        String mimeType = fileMap.getContentTypeFor(file.getAbsolutePath());
                        response.setHeader("Content-disposition", "attachment; filename=\"" + file.getName() + "\"");
                        writeFile(file, mimeType, response);
                        long end = System.currentTimeMillis();
                        PortalRequest.getCurrentRequest().getMBeans().incrementStatistics(ManagementMBeans.StatisticsType.DocumentDownload, repository + "-" + filePath, end - start, false);
                    } else {
                        response.sendError(response.SC_NOT_FOUND, "The specified File was Not Found ");
                    }
                } catch (PersistentModelException e) {
                    response.sendError(response.SC_NOT_FOUND, "The specified File was Not Found ");
                }
            }
        } catch (IOException e) {
            throw new ServletException("There was a problem sending the file", e);
        }
    }

    private void writeFile(File file, String mimeType, HttpServletResponse response) throws IOException {
        response.setContentType(mimeType);
        response.setContentLength((int) file.length());
        FileInputStream fis = new FileInputStream(file);
        try {
            ServletOutputStream sos = response.getOutputStream();
            byte data[] = new byte[1024];
            int bytesRead = -1;
            do {
                bytesRead = fis.read(data);
                if (bytesRead >= 0) {
                    sos.write(data, 0, bytesRead);
                }
            } while (bytesRead != -1);
            sos.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
}
