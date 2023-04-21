package net.wsnware.netbeans.templates.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author alex
 */
public class DemoUtils {

    public static void createProjectFrom(InputStream source, FileObject projectRoot, String groupId, String artifactId) throws IOException {
        try {
            ZipInputStream str = new ZipInputStream(source);
            ZipEntry entry;
            while ((entry = str.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    FileUtil.createFolder(projectRoot, entry.getName());
                } else {
                    FileObject fo = FileUtil.createData(projectRoot, entry.getName());
                    if ("pom.xml".equals(entry.getName())) {
                        updatePomProject(fo, str, groupId, artifactId);
                    } else {
                        Utilities.writeFile(str, fo);
                    }
                }
            }
        } finally {
            source.close();
        }
    }

    public static void updatePomProject(FileObject fo, ZipInputStream str, String groupId, String artifactId) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileUtil.copy(str, baos);
            Document doc = XMLUtil.parse(new InputSource(new ByteArrayInputStream(baos.toByteArray())), false, false, null, null);
            if (groupId != null) Utilities.replaceXmlField1(doc, "groupId", groupId);
            if (artifactId != null) Utilities.replaceXmlField1(doc, "artifactId", artifactId);
            OutputStream out = fo.getOutputStream();
            try {
                XMLUtil.write(doc, out, "UTF-8");
            } finally {
                out.close();
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            Utilities.writeFile(str, fo);
        }
    }
}
