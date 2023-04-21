package ejp.presenter.xml;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import ejp.presenter.api.util.CustomLogger;
import ejp.presenter.api.util.HashIndex;

/**
 * Repository of defined filters.
 * 
 * @author Sebastien Vauclair
 * @version <code>$Revision: 1.9 $<br>$Date: 2005/02/14 12:06:21 $</code>
 */
public class FiltersRepository {

    /**
   * Single instance This is <code>null</code> until
   * <code>createInstance()</code> has been called
   */
    protected static FiltersRepository instance = null;

    public static final ExtensionFileFilter XML_EXTENSION_FILE_FILTER = new ExtensionFileFilter("xml");

    public static final String DIRECTORY_TAG_NAME = "directory";

    protected final Hashtable filtersTable = new Hashtable();

    protected String[] defaultFilters = new String[0];

    protected FiltersRepository(File aDefaultFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new ErrorHandler() {

            public void warning(SAXParseException aException) {
                CustomLogger.INSTANCE.warning("Parse exception - " + aException.toString());
            }

            public void error(SAXParseException aException) throws SAXException {
                throw aException;
            }

            public void fatalError(SAXParseException aException) throws SAXException {
                throw aException;
            }
        });
        registerFiltersForFile(aDefaultFile, builder);
        File directory = aDefaultFile.getParentFile();
        if (directory != null) {
            File[] allFiles = directory.listFiles(XML_EXTENSION_FILE_FILTER);
            for (int i = 0; i < allFiles.length; i++) if (!aDefaultFile.equals(allFiles[i])) registerFiltersForFile(allFiles[i], builder);
        }
        Enumeration enumeration = filtersTable.elements();
        while (enumeration.hasMoreElements()) ((Filter) enumeration.nextElement()).setReferences(this);
        enumeration = filtersTable.elements();
        while (enumeration.hasMoreElements()) ((Filter) enumeration.nextElement()).sortReferences();
    }

    public static void createInstance(File aDefaultFile) throws IllegalStateException, ParserConfigurationException, SAXException, IOException {
        if (instance != null) throw new IllegalStateException("Single instance already exists");
        instance = new FiltersRepository(aDefaultFile);
    }

    public static FiltersRepository getInstance() {
        return instance;
    }

    /**
   * Registers all filters defined in specified XML file.
   */
    protected void registerFiltersForFile(File aFile, DocumentBuilder aBuilder) throws SAXException, IOException {
        CustomLogger.INSTANCE.finest("Processing filters definitions in file " + aFile);
        Document document = aBuilder.parse(aFile);
        processDirectory(new String[] {}, document.getDocumentElement());
    }

    protected void processDirectory(String[] aPath, Node aNode) {
        NodeList children = aNode.getChildNodes();
        int nb = children.getLength();
        for (int i = 0; i < nb; i++) {
            Node child = children.item(i);
            if (DIRECTORY_TAG_NAME.equalsIgnoreCase(child.getNodeName())) {
                String[] newPath = new String[aPath.length + 1];
                for (int j = 0; j < aPath.length; j++) newPath[j] = aPath[j];
                newPath[aPath.length] = Filter.getAttribute(child.getAttributes(), "name");
                processDirectory(newPath, child);
            } else {
                try {
                    Filter filter = new Filter(aPath, child);
                    register(filter);
                } catch (Exception e) {
                }
            }
        }
    }

    /**
   * @return <code>true</code> iff the filter full name was already in table
   */
    protected boolean register(Filter aFilter) {
        String fullName = aFilter.getFullName();
        return (filtersTable.put(fullName, aFilter) != null);
    }

    /**
   * @return <code>null</code> iff no matching filter was found
   */
    protected Filter getFilterNamed(String aName) throws ClassCastException {
        return (Filter) filtersTable.get(aName);
    }

    public Vector createFiltersList() {
        Vector result = new Vector();
        Enumeration enumeration = filtersTable.elements();
        while (enumeration.hasMoreElements()) {
            Filter f = (Filter) enumeration.nextElement();
            f.clearImpl();
            result.add(f);
        }
        return result;
    }

    public void setDefaultFilters(String[] aNames) {
        defaultFilters = aNames;
    }

    public ArrayList createDefaultFiltersList() {
        ArrayList result = new ArrayList();
        for (int i = 0; i < defaultFilters.length; i++) {
            Filter f = getFilterNamed(defaultFilters[i]);
            f.clearImpl();
            result.add(f);
        }
        return result;
    }

    /**
   * @return a list of <code>Filter</code> values
   */
    public ArrayList getFilters(String[] aNames) {
        int nb = aNames.length;
        ArrayList result = new ArrayList(nb);
        for (int i = 0; i < nb; i++) {
            Filter f = getFilterNamed(aNames[i]);
            if (f == null) CustomLogger.INSTANCE.warning("Ignoring unsatisfied reference to filter named \"" + aNames[i] + '"'); else result.add(f);
        }
        return result;
    }

    public static String[] getWords(String aList, String aDelimiter) {
        if (aList == null) return new String[0];
        StringTokenizer st = new StringTokenizer(aList, aDelimiter, false);
        int nb = st.countTokens();
        String[] result = new String[nb];
        for (int i = 0; i < nb; i++) result[i] = st.nextToken();
        return result;
    }

    public void writeDotGraph(File aFile) throws IOException {
        PrintStream out = new PrintStream(new FileOutputStream(aFile));
        out.println("digraph G {");
        out.println();
        out.println("  fontname = \"Helvetica-bold\";");
        out.println("  fontsize = 11;");
        out.println("  nodesep = 0.2;");
        out.println("  ranksep = 0.2;");
        out.println("  rankdir = LR; // TB");
        out.println("  node [");
        out.println("    fontname = Helvetica");
        out.println("    fontsize = 10");
        out.println("    shape = box");
        out.println("    width = 0");
        out.println("    height = 0");
        out.println("  ]");
        out.println("  edge [");
        out.println("    fontname = Helvetica");
        out.println("    fontsize = 10");
        out.println("  ]");
        out.println();
        out.println("  subgraph cluster_Legend {");
        out.println("    label = Legend;");
        out.println("    A1 [ label = A ];");
        out.println("    A2 [ label = A ];");
        out.println("    B1 [ label = B ];");
        out.println("    B2 [ label = B ];");
        out.println("    A1 -> B1 [ label = \"A must\\" + "nprecede B\", color = blue ];");
        out.println("    A2 -> B2 [ label = \"A requires B\", color = red, " + " style = bold ];");
        out.println("  };");
        out.println();
        HashIndex dirs = new HashIndex();
        Enumeration filters = filtersTable.elements();
        while (filters.hasMoreElements()) {
            Filter f = (Filter) filters.nextElement();
            dirs.add(f.pathName, f);
        }
        Enumeration k = dirs.keys();
        while (k.hasMoreElements()) {
            String s = (String) k.nextElement();
            out.println("  subgraph \"cluster_" + s + "\" {");
            out.println("    label = \"" + s + " directory\";");
            out.println("    style = filled;");
            out.println("    color = gray85;");
            out.println();
            ArrayList l = dirs.get(s);
            int nb = l.size();
            for (int i = 0; i < nb; i++) {
                Filter f = (Filter) l.get(i);
                out.println("    \"" + f.getFullName() + "\" [ label = \"" + f.name + "\" ];");
            }
            out.println("  };");
            out.println();
        }
        filters = filtersTable.elements();
        while (filters.hasMoreElements()) {
            Filter f = (Filter) filters.nextElement();
            ArrayList l = f.getMustPrecedeFilters();
            int nb = l.size();
            if (nb != 0) {
                out.println("  \"" + f.getFullName() + "\" -> {");
                for (int i = 0; i < nb; i++) out.println("    \"" + ((Filter) (l.get(i))).getFullName() + "\";");
                out.println("  } [ color = blue ];");
                out.println();
            }
            l = f.getRequiresFilters();
            nb = l.size();
            if (nb != 0) {
                out.println("  \"" + f.getFullName() + "\" -> {");
                for (int i = 0; i < nb; i++) out.println("    \"" + ((Filter) (l.get(i))).getFullName() + "\";");
                out.println("  } [ color = red, style = bold ];");
                out.println();
            }
        }
        out.println("}");
        out.close();
    }

    /**
   * File filter based on extension.
   */
    protected static class ExtensionFileFilter implements FileFilter {

        /**
     * 
     * Extension is stored in lowercase, with leading dot.
     */
        public final String extension;

        public ExtensionFileFilter(String aExtension) {
            extension = "." + aExtension.toLowerCase();
        }

        public boolean accept(File aPath) {
            return aPath.isFile() && aPath.getName().endsWith(extension);
        }
    }
}
