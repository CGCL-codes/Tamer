package de.fzi.injectj.util.doc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Comparator;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

/**
 * @author <a href="mailto:mies@fzi.de">Sebastian Mies</a>
 */
public class InjectRootDoc {

    private static Comparator comp = new Comparator() {

        public int compare(Object o1, Object o2) {
            if (o1 == null || !(o1 instanceof String)) {
                return -1;
            }
            if (o2 == null || !(o1 instanceof String)) {
                return 1;
            }
            return ((String) o1).compareTo((String) o2);
        }
    };

    private TreeMap libraries = new TreeMap(comp);

    private TreeMap packages = new TreeMap(comp);

    private HashMap javamap = new HashMap();

    public InjectRootDoc(RootDoc root) {
        ClassDoc[] cls = root.classes();
        for (int i = 0; i < cls.length; i++) addLibrary(cls[i]);
        fillPackages();
        javamap.put("Object", "Type");
        Iterator i = libraries.values().iterator();
        while (i.hasNext()) {
            LibraryDoc l = (LibraryDoc) i.next();
            String[] javatypes = l.javaType().split(",");
            for (int j = 0; j < javatypes.length; j++) javamap.put(javatypes[j].trim(), l.name());
        }
        i = libraries.values().iterator();
        while (i.hasNext()) {
            LibraryDoc l = (LibraryDoc) i.next();
            l.map(this);
        }
    }

    private void fillPackages() {
        Iterator i = libraries.values().iterator();
        while (i.hasNext()) {
            LibraryDoc libDoc = (LibraryDoc) i.next();
            Vector v = (Vector) packages.get(libDoc.packageName());
            if (v == null) {
                v = new Vector();
                packages.put(libDoc.packageName(), v);
            }
            v.add(libDoc);
        }
    }

    private void addLibrary(ClassDoc c) {
        TagInfo ti = new TagInfo(c.tags(InjectTags.TAG_INJECT));
        if (ti.isEmpty() || !ti.hasParam(InjectTags.TAG_EXPORT)) return;
        LibraryDoc libDoc = new LibraryDoc(c);
        libraries.put(libDoc.name(), libDoc);
    }

    public LibraryDoc getLibrary(String name) {
        return (LibraryDoc) libraries.get(name);
    }

    public String getLibraryType(String javatype) {
        String v = (String) javamap.get(javatype);
        if (v == null) v = javatype;
        return v;
    }

    public void output(String path) {
        Iterator i = libraries.values().iterator();
        while (i.hasNext()) {
            LibraryDoc doc = (LibraryDoc) i.next();
            String filename = path + File.separatorChar + doc.packageName().replace('.', File.separatorChar) + File.separatorChar + doc.name() + ".ijl";
            File file = new File(filename);
            file.getParentFile().mkdirs();
            PrintWriter writer;
            try {
                writer = new PrintWriter(new FileWriter(file));
                writer.println(doc.toString() + "\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeHTML(String path) {
        File dir = new File(path);
        dir.mkdirs();
        Iterator i = libraries.values().iterator();
        while (i.hasNext()) {
            LibraryDoc doc = (LibraryDoc) i.next();
            File file = new File(path + doc.linkName() + ".html");
            HTMLWriter writer;
            try {
                writer = new HTMLWriter(new FileWriter(file));
                writer.start(doc.name());
                doc.writeHTML(writer);
                writer.end();
                writer.flush();
                writer.close();
            } catch (IOException e) {
                System.err.println("Could not write file " + file.getAbsolutePath());
            }
        }
        writeHTMLIndex(path + "pkg_index_all.html", "All Libraries", libraries.values().iterator());
        HTMLWriter writer = null;
        try {
            writer = new HTMLWriter(new FileWriter(path + "packages.html"));
            writer.start("Package List");
            writer.println("<TABLE BORDER=\"0\" WIDTH=\"100%\"><TR><TD NOWRAP>");
            writer.println("<a href=\"pkg_index_all.html\" target=\"libraryFrame\">All Libraries</a><br><br>");
            writer.println("<a href=\"pkg_index_.html\" target=\"libraryFrame\">Default</a><br>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        i = packages.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            String packageName = ((String) entry.getKey());
            Iterator values = ((Vector) entry.getValue()).iterator();
            String filename = "pkg_index_" + packageName.replace('.', '_') + ".html";
            writeHTMLIndex(path + filename, "Package " + packageName, values);
            writer.println("<a href=\"" + filename + "\" target=\"libraryFrame\">" + packageName + "</a><br>");
        }
        writer.flush();
        writer.close();
    }

    private void writeHTMLIndex(String filename, String title, Iterator i) {
        try {
            HTMLWriter writer = new HTMLWriter(new FileWriter(filename));
            writer.start(title);
            writer.println("<TABLE BORDER=\"0\" WIDTH=\"100%\"><TR><TD NOWRAP>");
            while (i.hasNext()) {
                LibraryDoc doc = (LibraryDoc) i.next();
                writer.link(doc.linkName(), doc.name(), "rightFrame");
                writer.println("<br>");
            }
            writer.println("</TD></TR></TABLE>");
            writer.end();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("Libraries : \n");
        Iterator i = libraries.values().iterator();
        while (i.hasNext()) {
            b.append(i.next().toString() + "\n");
        }
        return b.toString();
    }
}
