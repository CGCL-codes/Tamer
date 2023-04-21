package org.xmlhammer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.apache.xml.resolver.CatalogManager;
import org.xmlhammer.gui.XMLHammer;
import org.xmlhammer.model.jaxp.Attributes;
import org.xmlhammer.model.jaxp.Feature;
import org.xmlhammer.model.jaxp.Features;
import org.xmlhammer.model.jaxp.JAXPDocumentBuilderFactory;
import org.xmlhammer.model.jaxp.JAXPSAXParserFactory;
import org.xmlhammer.model.jaxp.JAXPSchemaFactory;
import org.xmlhammer.model.jaxp.JAXPTransformerFactory;
import org.xmlhammer.model.jaxp.JAXPXPathFactory;
import org.xmlhammer.model.jaxp.Mapping;
import org.xmlhammer.model.jaxp.Mappings;
import org.xmlhammer.model.jaxp.Properties;
import org.xmlhammer.model.jaxp.SchemaFactoryProperties;
import org.xmlhammer.model.jaxp.SchemaFactoryProperty;
import org.xmlhammer.model.jaxp.Settings;
import org.xmlhammer.model.preferences.CdataSectionElements;
import org.xmlhammer.model.preferences.Charsets;
import org.xmlhammer.model.preferences.Directories;
import org.xmlhammer.model.preferences.DoctypePublics;
import org.xmlhammer.model.preferences.DoctypeSystems;
import org.xmlhammer.model.preferences.Expressions;
import org.xmlhammer.model.preferences.Extensions;
import org.xmlhammer.model.preferences.ExternalApplication;
import org.xmlhammer.model.preferences.Frame;
import org.xmlhammer.model.preferences.Medias;
import org.xmlhammer.model.preferences.Menu;
import org.xmlhammer.model.preferences.Patterns;
import org.xmlhammer.model.preferences.Preferences;
import org.xmlhammer.model.preferences.Projects;
import org.xmlhammer.model.preferences.Proxy;
import org.xmlhammer.model.preferences.Results;
import org.xmlhammer.model.preferences.Schemas;
import org.xmlhammer.model.preferences.Searches;
import org.xmlhammer.model.preferences.Sources;
import org.xmlhammer.model.preferences.Split;
import org.xmlhammer.model.preferences.Stylesheets;
import org.xmlhammer.model.preferences.Titles;
import org.xmlhammer.model.preferences.Menu.Menuitem;
import org.xmlhammer.model.preferences.Preferences.Catalogs;
import org.xmlhammer.model.preferences.Preferences.Helpmenu;
import org.xmlhammer.model.preferences.Preferences.History;
import org.xmlhammer.model.preferences.Preferences.Catalogs.Catalog;

public class PreferencesHandler {

    private static final String PREFERENCES_FILE = ".xmlhammer.xml";

    private static final String PREFERENCES_DIR = System.getProperty("user.home") + File.separator + ".xmlhammer" + File.separator;

    private static PreferencesHandler handler = null;

    private Preferences preferences = null;

    private boolean useDefault = false;

    private PreferencesHandler() {
        super();
    }

    public static PreferencesHandler getInstance() {
        if (handler == null) {
            handler = new PreferencesHandler();
        }
        return handler;
    }

    public void useDefaultPreferences() {
        useDefault = true;
        preferences = null;
    }

    /**
     * @return the preferences.
     */
    public Preferences getPreferences() {
        if (preferences == null) {
            File file = new File(PREFERENCES_DIR + PREFERENCES_FILE);
            if (file.exists() && !useDefault) {
                URI uri = file.toURI();
                try {
                    JAXBContext context = JAXBContext.newInstance("org.xmlhammer.model.preferences");
                    JAXBResult result = new JAXBResult(context);
                    TransformerFactory factory = TransformerFactory.newInstance();
                    Transformer transformer = factory.newTransformer(new StreamSource(getClass().getResourceAsStream("/org/xmlhammer/convert/convert-from-2006-01-to-2007.xsl")));
                    transformer.transform(new StreamSource(uri.toString()), result);
                    preferences = (Preferences) result.getResult();
                } catch (Exception e) {
                    File oldFile = getOldFile();
                    file.renameTo(oldFile);
                    Logger.getLogger(XMLHammer.class).error("JAXB Error", e);
                    Logger.getLogger(XMLHammer.class).error("Unable to load preferences. Created new preferences file, the old preferences file has been renamed to \"" + oldFile.getName() + "\".");
                }
            }
            if (preferences == null) {
                preferences = new Preferences();
            }
        }
        initPreferences(preferences);
        return preferences;
    }

    public void updateCatalogProperties() {
        Catalogs catalogs = getPreferences().getCatalogs();
        List<Catalog> srcs = getPreferences().getCatalogs().getCatalog();
        List<Catalog> temp = new ArrayList<Catalog>(srcs);
        StringBuffer buffer = new StringBuffer();
        for (Catalog catalog : temp) {
            if (catalog.getSrc() != null) {
                if (catalog.isActive()) {
                    buffer.append(catalog.getSrc());
                    buffer.append(";");
                }
            } else {
                srcs.remove(catalog);
            }
        }
        CatalogManager.getStaticManager().setIgnoreMissingProperties(true);
        CatalogManager.getStaticManager().setUseStaticCatalog(false);
        CatalogManager.getStaticManager().setPreferPublic(catalogs.isPreferPublicIdentifiers());
        CatalogManager.getStaticManager().setCatalogFiles(buffer.toString());
    }

    public void updateProxy() {
        Proxy proxy = getPreferences().getProxy();
        if (proxy.isEnabled()) {
            System.setProperty("http.proxyHost", proxy.getAddress());
            System.setProperty("http.proxyPort", proxy.getPort());
        } else {
            System.getProperties().remove("http.proxyHost");
            System.getProperties().remove("http.proxyPort");
        }
    }

    public void storePreferences(JFrame frame, JSplitPane split, JSplitPane horizontalSplit) throws JAXBException, FileNotFoundException {
        if (!useDefault) {
            Preferences preferences = getPreferences();
            preferences.getFrame().setHeight(frame.getSize().height);
            preferences.getFrame().setWidth(frame.getSize().width);
            preferences.getFrame().setXPos(frame.getLocation().x);
            preferences.getFrame().setYPos(frame.getLocation().y);
            preferences.getFrame().getSplit().setVertical(split.getDividerLocation());
            preferences.getFrame().getSplit().setHorizontal(horizontalSplit.getDividerLocation());
            JAXBContext context = JAXBContext.newInstance("org.xmlhammer.model.preferences");
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            File dir = new File(PREFERENCES_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, PREFERENCES_FILE);
            marshaller.marshal(preferences, new FileOutputStream(file));
        }
    }

    private void initPreferences(Preferences preferences) {
        if (preferences.getFrame() == null) {
            preferences.setFrame(new Frame());
            preferences.getFrame().setSplit(new Split());
        }
        if (preferences.getExternalApplication() == null) {
            preferences.setExternalApplication(new ExternalApplication());
            preferences.getExternalApplication().setBrowserExtensions("htm,html,xhtm,xhtml");
            String os = System.getProperty("os.name").toLowerCase();
            if (os.indexOf("windows") > -1) {
                preferences.getExternalApplication().setEditorCommand("notepad.exe");
                if ((os.indexOf("nt") > -1) || (os.indexOf("2000") > -1) || (os.indexOf("xp") > -1)) {
                    preferences.getExternalApplication().setDefaultCommand("cmd.exe /c");
                } else {
                    preferences.getExternalApplication().setDefaultCommand("command.com /c");
                }
            }
        }
        if (preferences.getProxy() == null) {
            preferences.setProxy(new Proxy());
        }
        Settings settings = preferences.getJAXPSettings();
        if (settings == null) {
            settings = new Settings();
            preferences.setJAXPSettings(settings);
        }
        if (settings.getJAXPDocumentBuilderFactory() == null) {
            JAXPDocumentBuilderFactory domFactory = new JAXPDocumentBuilderFactory();
            domFactory.setSettings(new JAXPDocumentBuilderFactory.Settings());
            domFactory.setFeatures(new Features());
            domFactory.setAttributes(new Attributes());
            settings.setJAXPDocumentBuilderFactory(domFactory);
        }
        if (settings.getJAXPSAXParserFactory() == null) {
            JAXPSAXParserFactory saxFactory = new JAXPSAXParserFactory();
            saxFactory.setSettings(new JAXPSAXParserFactory.Settings());
            Features features = new Features();
            features.getFeature().add(createFeature("http://xml.org/sax/features/external-general-entities", true));
            features.getFeature().add(createFeature("http://xml.org/sax/features/external-parameter-entities", true));
            features.getFeature().add(createFeature("http://xml.org/sax/features/namespace-prefixes", false));
            features.getFeature().add(createFeature("http://xml.org/sax/features/string-interning", true));
            features.getFeature().add(createFeature("http://xml.org/sax/features/lexical-handler/parameter-entities", true));
            features.getFeature().add(createFeature("http://xml.org/sax/features/unicode-normalization-checking", false));
            features.getFeature().add(createFeature("http://xml.org/sax/features/allow-dtd-events-after-endDTD", false));
            saxFactory.setFeatures(features);
            saxFactory.setProperties(new Properties());
            settings.setJAXPSAXParserFactory(saxFactory);
        }
        if (settings.getJAXPTransformerFactory() == null) {
            JAXPTransformerFactory transformerFactory = new JAXPTransformerFactory();
            transformerFactory.setSettings(new JAXPTransformerFactory.Settings());
            transformerFactory.setFeatures(new Features());
            transformerFactory.setAttributes(new Attributes());
            settings.setJAXPTransformerFactory(transformerFactory);
        }
        if (settings.getJAXPXPathFactory() == null) {
            JAXPXPathFactory xpathFactory = new JAXPXPathFactory();
            xpathFactory.setSettings(new JAXPXPathFactory.Settings());
            xpathFactory.setFeatures(new Features());
            Mappings mappings = new Mappings();
            addMapping(mappings, "fo", "http://www.w3.org/1999/XSL/Format");
            addMapping(mappings, "html", "http://www.w3.org/1999/xhtml");
            addMapping(mappings, "math", "http://www.w3.org/1998/Math/MathML");
            addMapping(mappings, "nrl", "http://www.thaiopensource.com/validate/nrl");
            addMapping(mappings, "rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
            addMapping(mappings, "rng", "http://relaxng.org/ns/structure/1.0");
            addMapping(mappings, "soap-env", "http://schemas.xmlsoap.org/soap/envelope/");
            addMapping(mappings, "soap-enc", "http://schemas.xmlsoap.org/soap/encoding/");
            addMapping(mappings, "svg", "http://www.w3.org/2000/svg");
            addMapping(mappings, "wsdl", "http://schemas.xmlsoap.org/wsdl/");
            addMapping(mappings, "xinclude", "http://www.w3.org/2001/XInclude");
            addMapping(mappings, "xlink", "http://www.w3.org/1999/xlink");
            addMapping(mappings, "xsd", "http://www.w3.org/2001/XMLSchema");
            addMapping(mappings, "xsi", "http://www.w3.org/1999/XMLSchema-instance");
            addMapping(mappings, "xsl", "http://www.w3.org/1999/XSL/Transform");
            xpathFactory.setMappings(mappings);
            settings.setJAXPXPathFactory(xpathFactory);
        }
        if (settings.getJAXPSchemaFactory() == null) {
            JAXPSchemaFactory schemaFactory = new JAXPSchemaFactory();
            SchemaFactoryProperties schemaProperties = new SchemaFactoryProperties();
            SchemaFactoryProperty property = new SchemaFactoryProperty();
            property.setLanguage(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            schemaProperties.getSchemaFactoryProperty().add(property);
            property = new SchemaFactoryProperty();
            property.setLanguage(XMLConstants.RELAXNG_NS_URI);
            schemaProperties.getSchemaFactoryProperty().add(property);
            schemaFactory.setSchemaFactoryProperties(schemaProperties);
            schemaFactory.setSettings(new JAXPSchemaFactory.Settings());
            schemaFactory.setFeatures(new Features());
            schemaFactory.setProperties(new Properties());
            settings.setJAXPSchemaFactory(schemaFactory);
        }
        if (preferences.getCatalogs() == null) {
            preferences.setCatalogs(new Preferences.Catalogs());
        }
        if (preferences.getClasspath() == null) {
            preferences.setClasspath(new Preferences.Classpath());
        }
        if (preferences.getViews() == null) {
            preferences.setViews(new Preferences.Views());
        }
        if (preferences.getSocket() == null) {
            Preferences.Socket socket = new Preferences.Socket();
            socket.setServer(true);
            socket.setPort(10097);
            preferences.setSocket(socket);
        }
        if (preferences.getHelpmenu() == null) {
            Helpmenu helpMenu = new Helpmenu();
            Menu menu = new Menu();
            menu.setTitle("XML Hammer Online");
            menu.getMenuitem().add(newMenu("Homepage", "http://www.xmlhammer.org/"));
            menu.getMenuitem().add(newMenu("Report Bugs", "http://sourceforge.net/tracker/?group_id=145971&atid=763631"));
            menu.getMenuitem().add(newMenu("Request Features", "http://sourceforge.net/tracker/?group_id=145971&atid=763634"));
            menu.getMenuitem().add(newMenu("User Mailing List", "http://lists.sourceforge.net/mailman/listinfo/xmlhammer-user"));
            menu.getMenuitem().add(newMenu("Developer Mailing List", "http://lists.sourceforge.net/mailman/listinfo/xmlhammer-development"));
            menu.getMenuitem().add(newMenu("Source Code (CVS)", "http://sourceforge.net/cvs/?group_id=145971"));
            helpMenu.getMenu().add(menu);
            menu = new Menu();
            menu.setTitle("JAXP Resources");
            menu.getMenuitem().add(newMenu("JAXP Implementations", "http://www.edankert.com/jaxpimplementations.html"));
            menu.getMenuitem().add(newMenu("JAXP 1.3", "http://java.sun.com/webservices/jaxp/"));
            helpMenu.getMenu().add(menu);
            menu = new Menu();
            menu.setTitle("XML Specifications");
            menu.getMenuitem().add(newMenu("XML 1.0", "http://www.w3.org/TR/REC-xml/"));
            menu.getMenuitem().add(newMenu("XPath 1.0", "http://www.w3.org/TR/xpath"));
            menu.getMenuitem().add(newMenu("XSLT 1.0", "http://www.w3.org/TR/xslt"));
            menu.getMenuitem().add(newMenu("XInclude 1.0", "http://www.w3.org/TR/xinclude/"));
            menu.getMenuitem().add(newMenu("Namespaces in XML", "http://www.w3.org/TR/REC-xml-names/"));
            menu.getMenuitem().add(newMenu("XML Schema Primer", "http://www.w3.org/TR/xmlschema-0/"));
            menu.getMenuitem().add(newMenu("XML Schema Structures", "http://www.w3.org/TR/xmlschema-1/"));
            menu.getMenuitem().add(newMenu("XML Schema Datatypes", "http://www.w3.org/TR/xmlschema-2/"));
            helpMenu.getMenu().add(menu);
            preferences.setHelpmenu(helpMenu);
        }
        History history = preferences.getHistory();
        if (history == null) {
            history = new History();
            preferences.setHistory(history);
        }
        if (history.getProjects() == null) {
            Projects projects = new Projects();
            projects.setSize(10);
            history.setProjects(projects);
        }
        if (history.getSources() == null) {
            Sources sources = new Sources();
            sources.setSize(25);
            history.setSources(sources);
        }
        if (history.getResults() == null) {
            Results results = new Results();
            results.setSize(25);
            history.setResults(results);
        }
        if (history.getSchemas() == null) {
            Schemas schemas = new Schemas();
            schemas.setSize(25);
            history.setSchemas(schemas);
        }
        if (history.getExpressions() == null) {
            Expressions expressions = new Expressions();
            expressions.setSize(25);
            history.setExpressions(expressions);
        }
        if (history.getPatterns() == null) {
            Patterns patterns = new Patterns();
            patterns.setSize(25);
            history.setPatterns(patterns);
        }
        if (history.getDirectories() == null) {
            Directories directories = new Directories();
            directories.setSize(25);
            history.setDirectories(directories);
        }
        if (history.getExtensions() == null) {
            Extensions extensions = new Extensions();
            extensions.setSize(25);
            history.setExtensions(extensions);
        }
        if (history.getStylesheets() == null) {
            Stylesheets stylesheets = new Stylesheets();
            stylesheets.setSize(25);
            history.setStylesheets(stylesheets);
        }
        if (history.getDoctypePublics() == null) {
            DoctypePublics publics = new DoctypePublics();
            publics.setSize(25);
            history.setDoctypePublics(publics);
        }
        if (history.getDoctypeSystems() == null) {
            DoctypeSystems systems = new DoctypeSystems();
            systems.setSize(25);
            history.setDoctypeSystems(systems);
        }
        if (history.getSearches() == null) {
            Searches searches = new Searches();
            searches.setSize(25);
            history.setSearches(searches);
        }
        if (history.getCdataSectionElements() == null) {
            CdataSectionElements elements = new CdataSectionElements();
            elements.setSize(25);
            history.setCdataSectionElements(elements);
        }
        if (history.getMedias() == null) {
            Medias medias = new Medias();
            medias.setSize(25);
            history.setMedias(medias);
        }
        if (history.getTitles() == null) {
            Titles titles = new Titles();
            titles.setSize(25);
            history.setTitles(titles);
        }
        if (history.getCharsets() == null) {
            Charsets charsets = new Charsets();
            charsets.setSize(25);
            history.setCharsets(charsets);
        }
    }

    private static void addMapping(Mappings mappings, String prefix, String uri) {
        Mapping mapping = new Mapping();
        mapping.setPrefix(prefix);
        mapping.setUri(uri);
        mappings.getMapping().add(mapping);
    }

    private static Feature createFeature(String name, boolean value) {
        Feature feature = new Feature();
        feature.setName(name);
        feature.setEnabled(value);
        return feature;
    }

    private static Menuitem newMenu(String title, String src) {
        Menuitem item = new Menuitem();
        item.setSrc(src);
        item.setTitle(title);
        return item;
    }

    private static File getOldFile() {
        NumberFormat format = DecimalFormat.getNumberInstance();
        format.setMinimumIntegerDigits(3);
        int i = 1;
        while (true) {
            System.out.println(format.format(i));
            File file = new File(PREFERENCES_DIR + "xmlhammer." + format.format(i));
            if (!file.exists()) {
                return file;
            }
            i++;
        }
    }
}
