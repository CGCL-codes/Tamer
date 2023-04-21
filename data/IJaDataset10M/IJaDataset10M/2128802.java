package prove;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.jdom.Document;
import finder.SearchingSettingsModel;
import finder.SearchingStrategyPlugin;
import services.W3CSerivcesJDOM;

public class ProvaPlugInLoading {

    private final String PATH = "plugin/searchingStrategy/SearchingStrategyPlugInSettings.xml";

    public ProvaPlugInLoading() throws Exception {
        multipleResources();
    }

    private void multipleResources() throws Exception {
        ClassLoader cl = this.getClass().getClassLoader();
        Enumeration<URL> urls = cl.getResources(PATH);
        while (urls.hasMoreElements()) {
            URL url = (URL) urls.nextElement();
            String stringUrl = url.getPath();
            int index1 = stringUrl.lastIndexOf(":");
            int index2 = stringUrl.lastIndexOf("!");
            String jarName = stringUrl.substring(index1 - 1, index2);
            JarFile jarFile = new JarFile(new File(jarName));
            ZipEntry entry = jarFile.getEntry(PATH);
            if (entry != null) {
                InputStream in = jarFile.getInputStream(entry);
                Document doc = W3CSerivcesJDOM.parseXML(in);
                String n = doc.getRootElement().getAttributeValue("NAME");
                System.out.println(n);
            }
        }
    }

    private void singleResource() throws Exception {
        ClassLoader cl = this.getClass().getClassLoader();
        URL url = cl.getResource(PATH);
        String stringUrl = url.getPath();
        int index1 = stringUrl.lastIndexOf(":");
        int index2 = stringUrl.lastIndexOf("!");
        String filePath = stringUrl.substring(index1 - 1, index2);
        System.out.println("index1: " + index1);
        System.out.println("index2: " + index2);
        System.out.println("url: " + stringUrl);
        System.out.println("file: " + filePath);
        Document doc = extractFileFromJar(filePath, "plugin/searchingStrategy/SearchingStrategyPlugInSettings.xml");
        String n = doc.getRootElement().getAttributeValue("NAME");
        System.out.println(n);
    }

    public static void main(String[] args) throws Exception {
        new ProvaPlugInLoading();
    }

    public static Document extractFileFromJar(String jarName, String fileName) throws IOException {
        JarFile jarFile = new JarFile(new File(jarName));
        ZipEntry entry = jarFile.getEntry(fileName);
        if (entry != null) {
            InputStream in = jarFile.getInputStream(entry);
            return W3CSerivcesJDOM.parseXML(in);
        } else {
            return null;
        }
    }
}
