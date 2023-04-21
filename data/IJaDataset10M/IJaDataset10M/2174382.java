package com.bureauveritas.photolibrary;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;

/**
 * @version 1.0
 * @author
 */
public class ThumbnailServlet extends HttpServlet {

    protected String LIBRARY_PATH = null;

    /**
     * @see javax.servlet.http.HttpServlet#void
     *      (javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getParameter("path");
        String name = req.getParameter("name");
        if (!PathUtils.isValid(path + "/" + name)) {
            return;
        }
        File jpegFile = new File(LIBRARY_PATH + path + "/" + name);
        Metadata metadata;
        try {
            metadata = JpegMetadataReader.readMetadata(jpegFile);
            ExifDirectory exif = (ExifDirectory) metadata.getDirectory(ExifDirectory.class);
            byte[] result = exif.getThumbnailData();
            output(resp, result, "thumb_" + jpegFile.getName());
        } catch (JpegProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MetadataException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void output(HttpServletResponse resp, byte[] data, String fileName) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(data);
        resp.reset();
        String mimeType = "image/jpeg";
        resp.setContentType(mimeType);
        resp.setContentLength(baos.size());
        resp.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
        resp.setHeader("Cache-Control", "must-revalidate");
        ServletOutputStream sout = resp.getOutputStream();
        baos.writeTo(sout);
        sout.flush();
        resp.flushBuffer();
    }

    public void init() throws ServletException {
        InputStream is = ThumbnailServlet.class.getResourceAsStream("/path.properties");
        Properties p = new Properties();
        try {
            p.load(is);
            LIBRARY_PATH = p.getProperty("path");
        } catch (IOException e) {
            throw new ServletException();
        }
    }
}
