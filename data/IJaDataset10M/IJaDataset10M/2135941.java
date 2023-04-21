package net.sourceforge.javautil.developer.ide.eclipse.project.descriptor;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.javautil.common.xml.annotation.XmlCollection;
import net.sourceforge.javautil.common.xml.annotation.XmlTag;
import net.sourceforge.javautil.common.xml.annotation.XmlTag.ElementType;
import net.sourceforge.javautil.developer.ide.eclipse.project.EclipseProject;

/**
 * The structure of an Eclipse {@link EclipseProject#CLASS_PATH_FILE}, until complete
 * this should only be used for reading, as generated files will erase unsupported tags.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: EclipseClassPath.java 312 2009-08-03 03:00:15Z ponderator $
 */
@XmlTag(name = "classpath")
public class EclipseClassPath {

    public static final String OUTPUT_ENTRY = "output";

    public static final String SOURCE_ENTRY = "src";

    public static final String CONTAINER_ENTRY = "con";

    public static final String LIBRARY_ENTRY = "lib";

    /**
	 * The entries in this class path
	 */
    private List<ClassPathEntry> entries = new ArrayList<ClassPathEntry>();

    /**
	 * @return The {@link #entries}
	 */
    @XmlTag(collection = @XmlCollection(ClassPathEntry.class))
    public List<ClassPathEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<ClassPathEntry> entries) {
        this.entries = entries;
    }

    /**
	 * @param entryType The type of entry
	 * @return A list, possibly empty, of all the entries of the specified type
	 */
    public List<ClassPathEntry> getEntries(String entryType) {
        List<ClassPathEntry> entries = new ArrayList<ClassPathEntry>();
        for (ClassPathEntry entry : this.entries) {
            if (entryType.equalsIgnoreCase(entry.getKind())) entries.add(entry);
        }
        return entries;
    }

    /**
	 * @return The default output entry, or null if its not defined
	 */
    @XmlTag(ignore = true)
    public ClassPathEntry getDefaultOutputEntry() {
        for (ClassPathEntry entry : this.entries) {
            if (OUTPUT_ENTRY.equalsIgnoreCase(entry.getKind())) return entry;
        }
        return null;
    }

    /**
	 * An entry specifying the type of entry and the path to it.
	 *
	 * @author elponderador
	 * @author $Author: ponderator $
	 * @version $Id: EclipseClassPath.java 312 2009-08-03 03:00:15Z ponderator $
	 */
    @XmlTag(name = "classpathentry")
    public static class ClassPathEntry {

        private String kind;

        private String path;

        private String output;

        @XmlTag(elementType = ElementType.Attribute)
        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }

        @XmlTag(elementType = ElementType.Attribute)
        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        @XmlTag(elementType = ElementType.Attribute)
        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
