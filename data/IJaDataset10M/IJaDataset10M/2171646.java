package org.pengyou.client.web;

import org.apache.log4j.Logger;
import org.dom4j.io.SAXReader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import java.io.InputStream;

public class MimeTypeIcons {

    private static Logger log = Logger.getLogger(MimeTypeIcons.class);

    private static Document doc = null;

    private static Document parse(InputStream iStream) throws DocumentException {
        SAXReader reader = new SAXReader();
        return reader.read(iStream);
    }

    public MimeTypeIcons(InputStream xmlStream) throws Exception {
        try {
            doc = parse(xmlStream);
        } catch (DocumentException e) {
            throw new Exception("Can't parse xml resources", e);
        }
    }

    private static String getFileType(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos == -1) return fileName;
        if (pos == fileName.length()) return "";
        return fileName.substring(pos + 1).toLowerCase();
    }

    public static String getImgUrl(String fileName, String resType) {
        log.info(fileName);
        if (resType.equals("COLLECTION")) {
            return "./img/mimetypes/48x48/gnome-fs-directory.png";
        }
        String fileType = getFileType(fileName);
        try {
            Node node = doc.selectSingleNode("//fileTypes/extension[@type='" + fileType + "']");
            if (node == null) return "./img/mimetypes/48x48/gnome-mime-text-x-authors.png";
            return "./img/mimetypes/48x48/" + node.valueOf("img");
        } catch (Exception e) {
            log.error("can't select the right img", e);
            return "./img/mimetypes/48x48/gnome-mime-text-x-authors.png";
        }
    }

    public static String getImgUrlSmall(String fileName, String resType) {
        log.info(fileName);
        if (resType.equals("COLLECTION")) {
            return "./img/mimetypes/16x16/deb.png";
        }
        String fileType = getFileType(fileName);
        try {
            Node node = doc.selectSingleNode("//fileTypes/extension[@type='" + fileType + "']");
            if (node == null) return "./img/mimetypes/16x16/unknown.png";
            return "./img/mimetypes/16x16/" + node.valueOf("img");
        } catch (Exception e) {
            log.error("can't select the right img", e);
            return "./img/mimetypes/16x16/unknown.png";
        }
    }
}
