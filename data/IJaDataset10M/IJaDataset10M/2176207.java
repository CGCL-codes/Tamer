package idlcompiler.compilers.plugins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import parsing.IDLCompiler;
import util.FileHelper;

/**
 *
 * @author angr
 */
public class PluginsLoader {

    private PluginsSettingsLoader settingsLoader;

    private URLClassLoader classLoader = null;

    public PluginsLoader() throws PluginsLoaderException {
        try {
            settingsLoader = new PluginsSettingsLoader();
            settingsLoader.parse("plugins.xml");
            classLoader = new URLClassLoader(settingsLoader.jarURLS.toArray(new URL[0]));
        } catch (SAXException ex) {
            throw new PluginsLoaderException("XML-Error in plugins.xml");
        } catch (IOException iOException) {
            throw new PluginsLoaderException("IOException when reading plugins.xml" + iOException.getMessage());
        }
    }

    public Vector<IDLCompiler> getCompilerPlugins() throws PluginsLoaderException {
        Vector<IDLCompiler> compilers = new Vector<IDLCompiler>();
        for (String name : settingsLoader.idlCompilers) {
            try {
                Class c = classLoader.loadClass(name);
                IDLCompiler comp = (IDLCompiler) c.getConstructor(null).newInstance(null);
                compilers.add(comp);
            } catch (Exception ex) {
                throw new PluginsLoaderException("Unable to load class for " + name);
            }
        }
        return compilers;
    }

    public Vector<String> listCompilers() {
        return settingsLoader.idlCompilers;
    }
}

class PluginsSettingsLoader extends DefaultHandler {

    public Vector<URL> jarURLS = new Vector<URL>();

    public Vector<String> idlCompilers = new Vector<String>();

    public void parse(String fileName) throws SAXException, IOException {
        XMLReader xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(this);
        xr.setErrorHandler(this);
        File f = new File(fileName);
        String s = "file:///" + FileHelper.unixSlashed(f.getAbsolutePath());
        xr.parse(new InputSource(s));
    }

    public void startElement(String uri, String name, String qName, Attributes atts) {
        try {
            if (name.equals("IDLCompiler")) {
                File tFile = new File(atts.getValue("jarfile"));
                URL jarURL = new URL("file:///" + FileHelper.unixSlashed(tFile.getAbsolutePath()));
                jarURLS.add(jarURL);
                String compilerName = atts.getValue("name");
                idlCompilers.add(compilerName);
            }
        } catch (MalformedURLException malformedURLException) {
            System.err.println("Invalid setting ignored...");
        }
    }

    public void startDocument() {
    }

    public void endDocument() {
    }

    public void endElement(String uri, String name, String qName) {
    }
}
