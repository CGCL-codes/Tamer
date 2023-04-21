package Cauldron3;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FilenameFilter;

/**
 * HiddenFileFilter
 */
public class HiddenFileFilter implements FilenameFilter {

    private String filter;

    private String hidePrefix;

    private final String WILDCARD;

    private boolean showHidden;

    private boolean onlyDirectories;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public HiddenFileFilter() {
        filter = "";
        hidePrefix = ".";
        WILDCARD = "*";
        showHidden = false;
        onlyDirectories = false;
    }

    @Override
    public boolean accept(File dir, String name) {
        if (onlyDirectories == false) {
            return isNotHiddenOrFiltered(name);
        } else if (onlyDirectories) {
            return (isNotDirectory(dir, name) && isNotHiddenOrFiltered(name));
        }
        return true;
    }

    private boolean isNotHiddenOrFiltered(String name) {
        if (filter.contains(WILDCARD)) {
            if (filter.startsWith(WILDCARD) && filter.endsWith(WILDCARD)) {
                filter = filter.replace(WILDCARD, "");
                if (name.contains(filter)) {
                    if ((showHidden == false) && (isHidden(name))) {
                        return false;
                    }
                    return true;
                }
            } else if (filter.startsWith(WILDCARD)) {
                filter = filter.replace(WILDCARD, "");
                if (name.endsWith(filter)) {
                    if ((showHidden == false) && (isHidden(name))) {
                        return false;
                    }
                    return true;
                }
            } else if (filter.endsWith(WILDCARD)) {
                filter = filter.replace(WILDCARD, "");
                if (name.startsWith(filter)) {
                    if ((showHidden == false) && (isHidden(name))) {
                        return false;
                    }
                    return true;
                }
            }
        } else {
            if (name.contains(filter)) {
                if ((showHidden == false) && (isHidden(name))) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private boolean isHidden(String name) {
        if (name.startsWith(hidePrefix)) {
            return true;
        }
        return false;
    }

    private boolean isNotDirectory(File dir, String name) {
        File[] contained = dir.listFiles();
        for (int i = 0; i < contained.length; i++) {
            if (contained[i].getName().equals(name)) {
                File curFile = contained[i];
                if (curFile.isDirectory()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public void setShowing(boolean value) {
        boolean old = showHidden;
        showHidden = value;
        pcs.firePropertyChange("showing", old, value);
    }

    public boolean getShowing() {
        return showHidden;
    }

    public void setShowDirectories(boolean value) {
        boolean old = onlyDirectories;
        onlyDirectories = value;
        pcs.firePropertyChange("showDirectories", old, value);
    }

    public boolean getShowDirectories() {
        return onlyDirectories;
    }

    public void setFilter(String value) {
        if (value != null) {
            String old = filter;
            filter = value;
            pcs.firePropertyChange("filter", old, value);
        } else {
            String old = filter;
            filter = "";
            pcs.firePropertyChange("filter", old, "");
        }
    }

    public String getFilter() {
        return filter;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
}
