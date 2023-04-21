package orgii_micro_uff;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.filechooser.FileFilter;

public class FiltroArquivo extends FileFilter {

    public FiltroArquivo() {
        filters = null;
        description = null;
        fullDescription = null;
        useExtensionsInDescription = true;
        filters = new Hashtable();
    }

    public FiltroArquivo(String s) {
        this(s, null);
    }

    public FiltroArquivo(String s, String s1) {
        this();
        if (s != null) addExtension(s);
        if (s1 != null) setDescription(s1);
    }

    public FiltroArquivo(String as[]) {
        this(as, null);
    }

    public FiltroArquivo(String as[], String s) {
        this();
        for (int i = 0; i < as.length; i++) addExtension(as[i]);
        if (s != null) setDescription(s);
    }

    public boolean accept(File file) {
        if (file != null) {
            if (file.isDirectory()) return true;
            String s = getExtension(file);
            if (s != null && filters.get(getExtension(file)) != null) return true;
        }
        return false;
    }

    public String getExtension(File file) {
        if (file != null) {
            String s = file.getName();
            int i = s.lastIndexOf('.');
            if (i > 0 && i < s.length() - 1) return s.substring(i + 1).toLowerCase();
        }
        return null;
    }

    public void addExtension(String s) {
        if (filters == null) filters = new Hashtable(5);
        filters.put(s.toLowerCase(), this);
        fullDescription = null;
    }

    public String getDescription() {
        if (fullDescription == null) if (description == null || isExtensionListInDescription()) {
            fullDescription = description != null ? description + " (" : "(";
            Enumeration enumeration = filters.keys();
            if (enumeration != null) for (fullDescription += "." + (String) enumeration.nextElement(); enumeration.hasMoreElements(); fullDescription += ", ." + (String) enumeration.nextElement()) ;
            fullDescription += ")";
        } else {
            fullDescription = description;
        }
        return fullDescription;
    }

    public void setDescription(String s) {
        description = s;
        fullDescription = null;
    }

    public void setExtensionListInDescription(boolean flag) {
        useExtensionsInDescription = flag;
        fullDescription = null;
    }

    public boolean isExtensionListInDescription() {
        return useExtensionsInDescription;
    }

    private static String TYPE_UNKNOWN = "Type Unknown";

    private static String HIDDEN_FILE = "Hidden File";

    private Hashtable filters;

    private String description;

    private String fullDescription;

    private boolean useExtensionsInDescription;
}
