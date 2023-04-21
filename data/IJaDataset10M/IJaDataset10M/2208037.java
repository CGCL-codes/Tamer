package supersync.ui.filebrowser;

import java.io.IOException;
import java.util.regex.Matcher;
import supersync.file.AbstractFile;
import java.util.regex.Pattern;

/**
 *
 * @author Brandon Drake
 */
public class FileFilter {

    protected int applyTo = 0;

    protected Matcher filter = null;

    protected String filterText = null;

    protected String name = null;

    public static final int APPLY_TO_FILES = 1;

    public static final int APPLY_TO_FOLDERS = 2;

    public static final int APPLY_TO_HIDDEN_OBJECTS = 4;

    public static final int APPLY_TO_ALL = 0xFFFFFFFF;

    public boolean appliesTo(AbstractFile l_file) throws IOException {
        return false == ((l_file.isDirectory() && (applyTo & APPLY_TO_FOLDERS) == 0) || (false == l_file.isDirectory() && (applyTo & APPLY_TO_FILES) == 0) || (l_file.isHidden() && (applyTo & APPLY_TO_HIDDEN_OBJECTS) == 0));
    }

    public FileFilter() {
        this.name = "All Files";
        this.applyTo = APPLY_TO_FOLDERS | APPLY_TO_FILES;
        this.setFilter(".*");
    }

    public FileFilter(int l_applyTo, String l_filter, String l_name) {
        this.applyTo = l_applyTo;
        this.name = l_name;
        this.setFilter(l_filter);
    }

    public int getApplyTo() {
        return this.applyTo;
    }

    public String getFilterText() {
        return this.filterText;
    }

    public String getName() {
        return this.name;
    }

    /** Checks if the specified item matches this file.
     */
    public boolean matches(AbstractFile l_file) throws IOException {
        if (false == this.appliesTo(l_file)) {
            return false;
        }
        filter.reset(l_file.getName());
        return filter.matches();
    }

    public void setApplyTo(int l_value) {
        this.applyTo = l_value;
    }

    public void setFilter(String l_value) {
        this.filterText = l_value;
        this.filter = Pattern.compile(l_value).matcher("");
    }

    public void setName(String l_value) {
        this.name = l_value;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
