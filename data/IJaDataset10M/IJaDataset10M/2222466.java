package com.bol.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bol.log.BLogger;
import com.bol.log.BolLogger;

@SuppressWarnings("serial")
public class FileUpload extends HttpServlet {

    private static BLogger log = BolLogger.getLogger(FileUpload.class);

    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("INSIDE THE FILEUPLOAD SERVLET ----------------------->>>>>", "doGet");
        PrintWriter outp = resp.getWriter();
        String regExp = "";
        String buff = "";
        File file1 = (File) req.getAttribute("fileToUpload");
        if (file1 != null && isValidImageName(req.getParameter("fileToUpload"))) {
            log.debug("File1 getAbsolutePath " + file1.getAbsolutePath(), "fileToUpload -- Line 46");
            log.debug("File1 name " + file1.getName(), "fileToUpload -- Line 47");
            if (file1 == null) {
                buff = "Error: File is NULL ";
                log.debug(" file1 == null  :::", "doGet");
            } else if (!file1.exists()) {
                buff = "Error: File Not Exists ";
                log.debug(" file1 == null  :::", "doGet");
            } else if (file1.isDirectory()) {
                buff = "Error: File is a directory";
                log.debug("file1.isDirectory() --------File Name :::" + file1.getName(), "doGet");
            } else {
                log.debug("File Name :::" + file1.getName(), "doGet");
                try {
                    Map env = System.getProperties();
                    String path = (String) env.get("jetty.home") + "/webapps" + req.getContextPath();
                    String imgname = path + "/images/complaintimg/";
                    log.debug("$$$$$$$$$$$$$$$$$$$$$$$$$$$IMAGE PATH ->" + imgname, "doGet");
                    String filename = req.getParameter("fileToUpload");
                    filename = getOnlyFilename(filename);
                    if (filename != null && isValidImageName(filename)) {
                        try {
                            byte[] buf = getBytesFromFileNIO(file1);
                            log.debug("Got bytes", "getBytesFromFile");
                            buff = writeRawBytes(buf, imgname, filename.trim());
                            log.debug("Wrote file-->" + buff, "writeRawBytes");
                        } catch (Exception e) {
                            if (e != null) {
                                buff = "Error : IO Error ::" + e.getMessage();
                            } else {
                                buff = "Error : IO Error";
                            }
                        }
                    } else {
                        buff = "Error: Invalid File (supports .jpg,.gif,.png,.jpeg)";
                        log.error("Error: Invalid File - null or not an .jpg,.gif,.png,.jpeg extension", "Line 86");
                    }
                } catch (Exception e) {
                    if (e != null) {
                        buff = "Error: " + e.getMessage();
                        log.error(e.getMessage(), "Exception File Upload");
                    }
                }
                log.debug("buff:::" + buff, "doGet");
            }
        } else {
            buff = "Error: Invalid File (supports .jpg,.gif,.png,.jpeg)";
            log.debug("File is invalid:::", "firstIfCheck");
        }
        outp.write(buff);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private String writeRawBytes(byte[] file, String dir, String filename) throws IOException {
        Date now = new Date();
        long time = now.getTime();
        final FileOutputStream fos = new FileOutputStream(dir + time + "_" + filename);
        FileChannel fc = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.wrap(file);
        fc.write(buffer);
        fc.close();
        return time + "_" + filename;
    }

    private byte[] getBytesFromFileNIO(File file) throws IOException {
        log.debug("Reading file NIO::NAME::" + file.getName(), "getBytesFromFileNIO");
        FileInputStream is = new FileInputStream(file);
        FileChannel fc = is.getChannel();
        long length = fc.size();
        log.debug("Reading file NIO::SIZE::" + length, "getBytesFromFileNIO");
        if (length > Integer.MAX_VALUE) {
            throw new IOException("File is too large");
        }
        byte[] bytes = new byte[(int) length];
        ByteBuffer buffer = ByteBuffer.allocate((int) length);
        fc.read(buffer);
        log.debug("Reading file into BUFFER::", "getBytesFromFileNIO");
        bytes = buffer.array();
        fc.close();
        log.debug("Closing File channel and returning::", "getBytesFromFileNIO");
        return bytes;
    }

    private boolean isValidImageName(String fname) {
        String patterstring = "\\.(jpg|jpeg|png|gif)$";
        Pattern pat = Pattern.compile(patterstring);
        Matcher matcher = pat.matcher(fname);
        return matcher.find();
    }

    private String getOnlyFilename(String file) {
        File f = new File(file);
        log.debug("File name only " + f.getName(), "getOnlyFilename");
        log.debug("File getParent only " + f.getParent(), "getOnlyFilename");
        log.debug("File getPath only " + f.getPath(), "getOnlyFilename");
        return f.getName();
    }
}
