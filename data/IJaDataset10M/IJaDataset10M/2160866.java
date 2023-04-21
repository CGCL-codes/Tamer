package jezuch.utils.ini;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jezuch.utils.CollectionAdapter;
import jezuch.utils.PredicateAdapter;

/**
 * FIXME: javadoc
 * @author ksobolewski
 */
public class INIFile implements Set<INISection> {

    private static final Pattern REGEX_COMMENT = Pattern.compile("^\\s*[;#]|^\\s*$");

    private static final Pattern REGEX_SECTION = Pattern.compile("^\\[([^\\]]+)\\]$");

    private static final int REGEX_SECTION_NAME = 1;

    private static final Pattern REGEX_VALUE = Pattern.compile("^\\s*+(.+?)\\s*+=\\s*(.+)\\s*$");

    private static final int REGEX_VALUE_KEY = 1;

    private static final int REGEX_VALUE_VALUE = 2;

    private final Map<String, INISection> sections;

    public INIFile() {
        sections = new LinkedHashMap<String, INISection>();
    }

    public INIFile(INIFile original) {
        sections = new LinkedHashMap<String, INISection>(original.sections);
        for (Map.Entry<String, INISection> e : sections.entrySet()) e.setValue(new INISection(e.getValue()));
    }

    public INIFile(Reader in) throws IOException, ParseException {
        this(in instanceof LineNumberReader ? (LineNumberReader) in : new LineNumberReader(in));
    }

    private INIFile(LineNumberReader in) throws IOException, ParseException {
        this();
        INISection curSection = new INISection((String) null);
        String line = null;
        while ((line = in.readLine()) != null) {
            Matcher m;
            if ((m = REGEX_COMMENT.matcher(line)).find()) {
            } else if ((m = REGEX_SECTION.matcher(line)).find()) {
                if (curSection.getName() != null || curSection.size() != 0) sections.put(curSection.getName(), curSection);
                String secName = m.group(REGEX_SECTION_NAME);
                if (sections.containsKey(secName)) throw new ParseException("Duplicate section: " + secName + " (line " + in.getLineNumber() + ")", in.getLineNumber());
                curSection = new INISection(secName);
            } else if ((m = REGEX_VALUE.matcher(line)).find()) {
                String key = m.group(REGEX_VALUE_KEY);
                String value = m.group(REGEX_VALUE_VALUE);
                curSection.put(key, value);
            } else {
                throw new ParseException("Malformed INI line (" + in.getLineNumber() + "): " + line, in.getLineNumber());
            }
        }
        if (curSection.getName() != null || curSection.size() != 0) sections.put(curSection.getName(), curSection);
    }

    public void writeTo(Writer out) throws IOException {
        writeTo(out instanceof PrintWriter ? (PrintWriter) out : new PrintWriter(out));
    }

    private void writeTo(PrintWriter out) throws IOException {
        for (INISection sec : sections.values()) {
            if (sec.getName() != null) {
                out.println();
                out.println("[" + sec.getName() + "]");
                if (out.checkError()) throw new IOException();
            }
            for (Map.Entry<String, String> entry : sec.entrySet()) {
                out.print(entry.getKey());
                out.print("=");
                out.println(entry.getValue());
                if (out.checkError()) throw new IOException();
            }
        }
        out.flush();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof INIFile) return sections.equals(((INIFile) o).sections);
        return false;
    }

    @Override
    public int hashCode() {
        return sections.hashCode();
    }

    /**
	 * @return unmodifiable Map of section names to sections
	 */
    public Map<String, INISection> getSectionNamesMap() {
        return Collections.unmodifiableMap(sections);
    }

    public int size() {
        return sections.size();
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean contains(Object o) {
        return o instanceof INISection && o.equals(sections.get(((INISection) o).getName()));
    }

    public Iterator<INISection> iterator() {
        return sections.values().iterator();
    }

    public Object[] toArray() {
        return sections.values().toArray();
    }

    public <T> T[] toArray(T[] a) {
        return sections.values().toArray(a);
    }

    public boolean add(INISection o) {
        return sections.put(o.getName(), o) == null;
    }

    public boolean remove(Object o) {
        return o instanceof INISection && sections.remove(((INISection) o).getName()) != null;
    }

    public boolean containsAll(Collection<?> c) {
        return sections.values().containsAll(c);
    }

    public boolean addAll(Collection<? extends INISection> c) {
        boolean ret = false;
        for (INISection p : c) ret |= add(p);
        return ret;
    }

    public boolean retainAll(Collection<?> c) {
        return sections.keySet().retainAll(new CollectionAdapter<INISection, String>(INISection.class, c, new INIExtractor()));
    }

    public boolean removeAll(Collection<?> c) {
        return sections.keySet().removeAll(new CollectionAdapter<INISection, String>(INISection.class, c, new INIExtractor()));
    }

    public void clear() {
        sections.clear();
    }

    private static class INIExtractor implements PredicateAdapter<INISection, String> {

        INIExtractor() {
        }

        public boolean accept(INISection source) {
            return true;
        }

        public String adapt(INISection source) {
            return source.getName();
        }
    }
}
