package com.c2b2.ipoint.processing;

import com.c2b2.ipoint.business.MediaServices;
import com.c2b2.ipoint.management.ManagementMBeans;
import com.c2b2.ipoint.model.Image;
import com.c2b2.ipoint.model.Media;
import com.c2b2.ipoint.model.MediaRepository;
import com.c2b2.ipoint.model.PersistentModelException;
import com.c2b2.ipoint.model.TaxonomyCategory;
import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Collection;

public class MediaServlet extends HttpServlet {

    public static final String MEDIA_ID_PARAM = "MediaID";

    public static final String MEDIA_KEY_PARAM = "MediaKey";

    public static final String FILE_PARAM = "File";

    public static final String REPO_PARAM = "Repository";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        myLogger = Logger.getLogger("MediaServlet");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "public");
        doImage(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(response.SC_FORBIDDEN, "This server does not support POST");
    }

    private void doImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String mediaIDStr = request.getParameter(MEDIA_ID_PARAM);
        String mediaKeyStr = request.getParameter(MEDIA_KEY_PARAM);
        try {
            long mediaID = Long.parseLong(mediaIDStr);
            MediaRepository mr = null;
            Media m = null;
            File file = null;
            String mimeType = null;
            String statsString = "";
            try {
                mr = MediaRepository.find(mediaKeyStr);
                m = Media.find(mediaIDStr);
                if (m.getMediaRepository().equals(mr)) {
                    MediaServices ms = new MediaServices(mr);
                    file = ms.getFile(m);
                    mimeType = m.getMimeType().getType();
                    statsString = "Media-" + m.getName() + "-" + m.getID();
                    if (!m.isVisibleTo(PortalRequest.getCurrentRequest().getCurrentUser())) {
                        response.sendError(response.SC_NOT_FOUND, "The specified Media Resource File was not found");
                        return;
                    }
                }
            } catch (PersistentModelException e) {
            }
            if (file == null) {
                Image image = Image.findImage(mediaID);
                if (image.getRandom().trim().equals(mediaKeyStr.trim())) {
                    file = new File(image.getSrc());
                    TaxonomyCategory tc = TaxonomyCategory.getGlobalImages();
                    Collection<Media> items = tc.getMedia().findMediaWithFile(file.getName());
                    if (items.size() > 0) {
                        m = (Media) items.toArray()[0];
                        MediaServices ms = new MediaServices(tc.getMedia());
                        file = ms.getFile(m);
                        mimeType = m.getMimeType().getType();
                        statsString = "Image-" + image.getName() + "-" + image.getID();
                    }
                }
            }
            if (file != null && file.exists() && file.canRead()) {
                boolean forDownload = false;
                if (request.getParameter("Download") != null) {
                    forDownload = true;
                }
                writeFile(file, mimeType, response, forDownload);
                PortalRequest.getCurrentRequest().getMBeans().incrementStatistics(ManagementMBeans.StatisticsType.MediaDownload, statsString, System.currentTimeMillis() - startTime, false);
            } else {
                response.sendError(response.SC_NOT_FOUND, "The specified Media Resource File was not found");
            }
        } catch (PersistentModelException e) {
            response.sendError(response.SC_NOT_FOUND, "The specified Media Resource was Not Found as: " + e.getMessage());
        } catch (NumberFormatException nfe) {
            response.sendError(response.SC_NOT_FOUND, "The specified Media Resource was Not Found as: " + nfe.getMessage());
        } catch (IOException ioe) {
            response.sendError(response.SC_NOT_FOUND, "The specified Media Resource was Not Found as: " + ioe.getMessage());
        }
    }

    private void writeFile(File file, String mimeType, HttpServletResponse response, boolean forDownload) throws IOException {
        if (forDownload) {
            response.setHeader("Content-disposition", "attachment; filename=\"" + file.getName() + "\"");
        } else {
            response.setHeader("Content-disposition", "inline; filename=\"" + file.getName() + "\"");
        }
        response.setContentType(mimeType);
        response.setContentLength((int) file.length());
        FileInputStream fis = new FileInputStream(file);
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
        fis.close();
    }

    private Logger myLogger;
}
