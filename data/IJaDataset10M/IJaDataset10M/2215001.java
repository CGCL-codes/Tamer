package lebah.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lebah.util.Unzip;
import lebah.util.FileManagerModule.Resource;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import com.jspsmart.upload.Files;

public class FilesRepositoryModule extends lebah.portal.velocity.VTemplate {

    public FilesRepositoryModule() {
    }

    public FilesRepositoryModule(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
        super(engine, context, req, res);
    }

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/files_repository/file_manager.vm";
        return doTask(session, template_name);
    }

    protected Template doTask(HttpSession session, String template_name) throws Exception, ResourceNotFoundException, ParseErrorException {
        String submit = getParam("command");
        if ("createfolder".equals(submit)) {
            createFolder(session);
        } else if ("uploadfile".equals(submit)) {
            uploadFile(session);
        } else if ("uploadpif".equals(submit)) {
            uploadPIF(session);
        } else if ("delete".equals(submit)) {
            deleteFile(session);
        } else if ("deletefiles".equals(submit)) {
            deleteMultipleFiles(session);
        } else if ("newfile".equals(submit)) {
            String dirName = session.getAttribute("dirName") != null ? (String) session.getAttribute("dirName") : "";
            String fileName = getParam("fileName");
            String ext = fileName.lastIndexOf('.') > 0 ? fileName.substring(fileName.lastIndexOf('.')) : "";
            if (!".htm".equals(ext) && !".html".equals(ext)) {
                fileName = fileName + ".htm";
            }
            createFile("", dirName + "/" + fileName);
            String dir = request.getParameter("dir") != null ? request.getParameter("dir") : "";
            listFiles(session, dir);
        } else {
            String dir = request.getParameter("dir") != null ? request.getParameter("dir") : "";
            listFiles(session, dir);
        }
        context.put("util", new Util());
        Template template = engine.getTemplate(template_name);
        return template;
    }

    protected void deleteMultipleFiles(HttpSession session) throws Exception {
        String current_dir = (String) session.getAttribute("current_dir");
        String[] files = request.getParameterValues("files");
        String[] folders = request.getParameterValues("folders");
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                deleteDir(new File(current_dir + "/" + files[i]));
            }
        }
        if (folders != null) {
            for (int i = 0; i < folders.length; i++) {
                deleteDir(new File(current_dir + "/" + folders[i]));
            }
        }
        String dir = getParam("dir");
        listFiles(session, dir);
    }

    protected void deleteFile(HttpSession session) throws Exception {
        String current_dir = (String) session.getAttribute("current_dir");
        String name = getParam("file");
        deleteDir(new File(current_dir + "/" + name));
        String dir = getParam("dir");
        listFiles(session, dir);
    }

    protected void listFiles(HttpSession session, String dir) throws Exception {
        String current_dir = Resource.getPATH() + dir;
        listFilesInDirectory(session, dir, current_dir);
    }

    protected void listFilesInDirectory(HttpSession session, String dir, String current_dir) throws Exception {
        File file = new File(current_dir);
        if (!file.exists()) {
            new File(current_dir).mkdir();
        }
        session.setAttribute("current_dir", current_dir);
        context.put("dir", dir);
        String upDir = "";
        int last = dir.lastIndexOf('/');
        if (last > -1) upDir = dir.substring(0, last);
        context.put("upDir", upDir);
        Vector dirs = new Vector();
        for (StringTokenizer st = new StringTokenizer(dir, "/"); st.hasMoreTokens(); ) {
            dirs.addElement(st.nextToken());
        }
        context.put("dirs", dirs);
        Hashtable goDirTbl = new Hashtable();
        for (int i = 0; i < dirs.size(); i++) {
            String goDir = "";
            for (int k = 0; k < i; k++) {
                goDir += "/" + (String) dirs.elementAt(k);
            }
            goDir += "/" + (String) dirs.elementAt(i);
            goDirTbl.put(dirs.elementAt(i), goDir);
        }
        context.put("goDirTbl", goDirTbl);
        Vector names = new Vector();
        Vector folders = new Vector();
        File files[] = new File(current_dir).listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                folders.addElement(files[i].getName());
            } else {
                Hashtable fileProp = new Hashtable();
                fileProp.put("fileName", files[i].getName());
                String fileSize = new java.text.DecimalFormat("#,###,###").format((float) files[i].length() / 1024);
                fileProp.put("size", fileSize + " KB");
                long lastModified = files[i].lastModified();
                GregorianCalendar gregCal = new GregorianCalendar();
                gregCal.setTimeInMillis(lastModified);
                Date date = gregCal.getTime();
                fileProp.put("lastModified", date);
                names.addElement(fileProp);
            }
        }
        context.put("folders", folders);
        Collections.sort(names, new DatetimeComparator());
        context.put("names", names);
    }

    protected void createFile(String content, String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
                out.write(content);
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void createFolder(HttpSession session) throws Exception {
        String current_dir = (String) session.getAttribute("current_dir");
        String dir = request.getParameter("dir");
        String foldername = request.getParameter("foldername");
        if (!foldername.equals("")) {
            new File(current_dir + "/" + foldername).mkdir();
        }
        listFiles(session, dir);
    }

    protected void uploadFile(HttpSession session) throws Exception {
        String current_dir = (String) session.getAttribute("current_dir");
        lebah.upload.SmartUpload2 myUpload = new lebah.upload.SmartUpload2();
        myUpload.initialize2(session.getServletContext(), request, response);
        myUpload.upload();
        Files files = myUpload.getFiles();
        com.jspsmart.upload.File file = files.getFile(0);
        String ext = file.getFileExt();
        if (!"exe".equalsIgnoreCase(ext) && !"jsp".equalsIgnoreCase(ext) && !"cgi".equalsIgnoreCase(ext)) {
            int count2 = myUpload.save(current_dir, myUpload.SAVE_PHYSICAL);
        } else {
            System.out.println("File type disallowed - " + ext);
        }
        String dir = getParam("dir");
        listFiles(session, dir);
    }

    protected void uploadPIF(HttpSession session) throws Exception {
        String current_dir = (String) session.getAttribute("current_dir");
        lebah.upload.SmartUpload2 myUpload = new lebah.upload.SmartUpload2();
        myUpload.initialize2(session.getServletContext(), request, response);
        myUpload.upload();
        int count2 = myUpload.save(current_dir, myUpload.SAVE_PHYSICAL);
        String dir = getParam("dir");
        Files files = myUpload.getFiles();
        if (files.getCount() > 0) {
            com.jspsmart.upload.File file = files.getFile(0);
            Unzip.unzipFiles(current_dir + "/" + file.getFileName(), current_dir);
            deleteDir(new java.io.File(current_dir + "/" + file.getFileName()));
        }
        listFiles(session, dir);
    }

    protected boolean deleteDir(java.io.File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new java.io.File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static class Resource {

        private static ResourceBundle rb;

        private static String ROOT;

        private static String PATH;

        private static String IMAGE, IMAGEURL;

        private static String FLASH, FLASHURL;

        static {
            try {
                rb = ResourceBundle.getBundle("files");
                read();
            } catch (MissingResourceException e) {
                System.out.println(e.getMessage());
            }
        }

        public static String getPATH() {
            return PATH;
        }

        public static String getROOT() {
            return ROOT;
        }

        public static String getImagePath() {
            return IMAGE;
        }

        public static String getFlashPath() {
            return FLASH;
        }

        public static String getImageUrl() {
            return IMAGEURL;
        }

        public static String getFlashUrl() {
            return FLASHURL;
        }

        public static void read() {
            readROOT();
            readPATH();
            readIMAGE();
            readFLASH();
            readIMAGEURL();
            readFLASHURL();
        }

        private static void readPATH() {
            try {
                PATH = rb.getString("dir");
            } catch (MissingResourceException e) {
                System.out.println("Recource - " + e.getMessage());
            }
        }

        private static void readROOT() {
            try {
                ROOT = rb.getString("root");
            } catch (MissingResourceException e) {
                System.out.println("Recource - " + e.getMessage());
            }
        }

        private static void readIMAGE() {
            try {
                IMAGE = rb.getString("imageDir");
            } catch (MissingResourceException e) {
                System.out.println("Recource - " + e.getMessage());
            }
        }

        private static void readFLASH() {
            try {
                FLASH = rb.getString("flashDir");
            } catch (MissingResourceException e) {
                System.out.println("Recource - " + e.getMessage());
            }
        }

        private static void readIMAGEURL() {
            try {
                IMAGEURL = rb.getString("imageUrl");
            } catch (MissingResourceException e) {
                System.out.println("Recource - " + e.getMessage());
            }
        }

        private static void readFLASHURL() {
            try {
                FLASHURL = rb.getString("flashUrl");
            } catch (MissingResourceException e) {
                System.out.println("Recource - " + e.getMessage());
            }
        }
    }

    public static class DatetimeComparator implements java.util.Comparator {

        public int compare(Object o1, Object o2) {
            if (o1 == null || o2 == null) return 0;
            Hashtable h1 = (Hashtable) o1;
            Hashtable h2 = (Hashtable) o2;
            if (h1.get("lastModified") != null && h2.get("lastModified") != null) {
                ;
                Date date1 = ((Date) h1.get("lastModified"));
                Date date2 = ((Date) h2.get("lastModified"));
                if (date1.after(date2)) return -1; else return 1;
            } else return 0;
        }
    }
}
