package de.dgrid.bisgrid.secure.proxy.wsdl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLWriter;
import org.apache.log4j.Logger;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Element;

/**
 * This file extends com.ibm.wsdl.xml.WSDLReaderImpl so that 
 * the wsdl file to be read  and all import wsdl/xsd-files are written
 * into a local cache directory. 
 * Furthermore all namespaces of imported WSDL documents are saved.
 * 
 * @author Guido Scherp 
 * 
 */
public class WSDLReader extends com.ibm.wsdl.xml.WSDLReaderImpl {

    private static final Logger log = Logger.getLogger(WSDLReader.class);

    private String cacheDir = null;

    private boolean caching = false;

    private Hashtable<String, String> importNamespaces = null;

    public WSDLReader() {
        super();
        this.importNamespaces = new Hashtable<String, String>();
        this.disableCaching();
    }

    public WSDLReader(String cacheDir) {
        super();
        this.enableCaching(cacheDir);
        this.importNamespaces = new Hashtable<String, String>();
    }

    @SuppressWarnings("unchecked")
    protected Import parseImport(org.w3c.dom.Element importEl, Definition def, java.util.Map importedDefs) throws WSDLException {
        Import returnImport = super.parseImport(importEl, def, importedDefs);
        if (this.caching) try {
            String filename = WSDLReader.URLtoFilename(importEl.getAttribute("location"));
            WSDLReader.writeWSDLFileToCache(this.cacheDir, filename, returnImport.getDefinition());
        } catch (IOException e) {
            throw new WSDLException("-1", e.getMessage());
        }
        Iterator<?> namespaces = returnImport.getDefinition().getNamespaces().keySet().iterator();
        while (namespaces.hasNext()) {
            String prefix = (String) namespaces.next();
            if (prefix != "") {
                String namespace = returnImport.getDefinition().getNamespace(prefix);
                if (!this.importNamespaces.contains(prefix)) {
                    ;
                    this.importNamespaces.put(prefix, namespace);
                }
            }
        }
        return returnImport;
    }

    @SuppressWarnings("unchecked")
    protected ExtensibilityElement parseSchema(Class classArg, Element importEl, Definition def, ExtensionRegistry erArg) throws WSDLException {
        ExtensibilityElement returnElement = super.parseSchema(classArg, importEl, def, erArg);
        Schema xsd = (javax.wsdl.extensions.schema.Schema) returnElement;
        if (this.caching) try {
            String filename = WSDLReader.URLtoFilename(importEl.getNamespaceURI());
            WSDLReader.writeXSDFileToCache(this.cacheDir, filename, xsd.getElement());
        } catch (IOException e) {
            throw new WSDLException("-1", e.getMessage());
        }
        return returnElement;
    }

    public Map<String, String> getImportNamespaces() {
        return this.importNamespaces;
    }

    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    public String getCacheDir(String cacheDir) {
        return this.cacheDir;
    }

    public void enableCaching() {
        this.enableCaching(this.cacheDir);
    }

    public void enableCaching(String cacheDir) {
        this.caching = true;
        this.cacheDir = cacheDir;
        log.info("Using cacheDir : " + cacheDir);
    }

    public void disableCaching() {
        this.caching = false;
        log.info("Caching is disabled");
    }

    public boolean isCachingEnabled() {
        return this.caching;
    }

    public static void writeWSDLFileToCache(String cacheDir, String file, Definition definition) throws IOException, WSDLException {
        String filename = cacheDir + File.separator + file;
        log.info("Writing WSDL-file to cache: " + filename);
        String dirname = "";
        if (filename.contains("/")) {
            dirname = filename.substring(0, filename.lastIndexOf("/"));
        }
        File dir = new File(dirname);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (!result) {
                log.error("Could not create directory : " + dir);
                throw new IOException("Could not create directory");
            }
        }
        WSDLWriter writer = WSDLFactory.newInstance().newWSDLWriter();
        FileOutputStream ou = new FileOutputStream(new File(filename));
        writer.writeWSDL(definition, ou);
        ou.close();
    }

    public static void writeXSDFileToCache(String cacheDir, String file, Element xsd) throws IOException {
        String filename = cacheDir + File.separator + file;
        log.info("Writing XSD-file to cache: " + filename);
        String dirname = "";
        if (filename.contains("/")) {
            dirname = filename.substring(0, filename.lastIndexOf("/"));
        }
        File dir = new File(dirname);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (!result) {
                log.error("Could not create directory : " + dir);
                throw new IOException("Could not create directory");
            }
        }
        FileWriter fw = new FileWriter(new File(filename));
        XMLSerializer serializer = new XMLSerializer();
        serializer.setOutputCharStream(fw);
        serializer.serialize(xsd);
    }

    public static String URLtoFilename(String url) {
        url = url.replaceFirst("^http[s]?://.*:[0-9]*", "");
        url = url.replaceFirst("\\?[wW][sS][dD][lL]$", "");
        url = url.replaceAll("/", File.separator);
        return url;
    }
}
