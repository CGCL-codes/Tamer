package bibtex.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 */
public class FieldType {

    public static final FieldType ADDRESS = new FieldType("address");

    public static final FieldType MONTH = new FieldType("month");

    public static final FieldType AUTHOR = new FieldType("author");

    public static final FieldType EDITOR = new FieldType("editor");

    public static final FieldType BOOKTITLE = new FieldType("booktitle");

    public static final FieldType SERIES = new FieldType("series");

    public static final FieldType JOURNAL = new FieldType("journal");

    public static final FieldType CROSSREF = new FieldType("crossref");

    public static final FieldType YEAR = new FieldType("year");

    public static final FieldType PAGES = new FieldType("pages");

    public static final FieldType PUBLISHER = new FieldType("publisher");

    public static final FieldType INSTITUTION = new FieldType("institution");

    public static final FieldType ORGANIZATION = new FieldType("organization");

    public static final FieldType SCHOOL = new FieldType("school");

    public static final FieldType LOCATION = new FieldType("location");

    public static final FieldType SHORT_TITLE = new FieldType("shorttitle");

    public static final FieldType ANNOTE = new FieldType("annote");

    public static final FieldType CHAPTER = new FieldType("chapter");

    public static final FieldType EDITION = new FieldType("edition");

    public static final FieldType HOWPUBLISHED = new FieldType("howpublished");

    public static final FieldType KEY = new FieldType("key");

    public static final FieldType NOTE = new FieldType("note");

    public static final FieldType NUMBER = new FieldType("number");

    public static final FieldType TITLE = new FieldType("title");

    public static final FieldType TYPE = new FieldType("type");

    public static final FieldType URL = new FieldType("url");

    public static final FieldType VOLUME = new FieldType("volume");

    public static final FieldType ALL = new FieldType("all");

    public static final FieldType SOURCE_FILE = new FieldType("sourceFile");

    public static final FieldType LABEL = new FieldType("label");

    private String name;

    private static Map<String, FieldType> types = new HashMap<String, FieldType>();

    private static Map<String, FieldType> getTypes() {
        if (types == null) {
            types = new HashMap<String, FieldType>();
        }
        return types;
    }

    public static FieldType getType(String name) {
        String canonicalName = name.toLowerCase();
        FieldType result = getTypes().get(canonicalName);
        if (result == null) {
            result = new FieldType(canonicalName);
        }
        return result;
    }

    private FieldType(String name) {
        assert name != null && name != "";
        this.name = name;
        getTypes().put(name, this);
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof FieldType) {
            result = this.getName().equals(((FieldType) obj).getName());
        }
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Set<FieldType> getTypes(Set<String> fieldNames) {
        Set<FieldType> result = new HashSet<FieldType>();
        for (String fieldName : fieldNames) {
            result.add(getType(fieldName));
        }
        return result;
    }
}
