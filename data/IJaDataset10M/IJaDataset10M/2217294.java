package icm.unicore.plugins.dbbrowser.visualization;

import java.util.Vector;
import icm.unicore.plugins.dbbrowser.visualization.*;

/**
 * @author     Michal Wronski (wrona@mat.uni.torun.pl)
 * @created    09. March 2003
 */
public class DefaultFormatsVector implements FormatsVector {

    Vector items;

    public DefaultFormatsVector() {
        items = new Vector();
    }

    public void removeAllElements() {
        items.removeAllElements();
    }

    public void add(String url, String extension, String description) {
        items.add(new Entry(url, extension, description));
    }

    public String getElements() {
        String res = "";
        for (int i = 0; i < items.size(); i++) res = res + ((Entry) items.get(i)).getUrl() + "\n";
        return res;
    }

    public int size() {
        return items.size();
    }

    public String getExtension(int index) {
        return ((Entry) items.get(index)).getExtension();
    }

    public String getDescription(int index) {
        return ((Entry) items.get(index)).getDescription();
    }

    public String getUrl(int index) {
        return ((Entry) items.get(index)).getUrl();
    }

    public String getUrl(String description) {
        Entry t;
        for (int i = 0; i < items.size(); i++) {
            t = (Entry) items.get(i);
            if (description.compareTo(t.getDescription()) == 0) return getUrl(i);
        }
        return "";
    }

    private class Entry {

        String url;

        String extension;

        String description;

        public Entry(String url, String extension, String description) {
            this.url = url;
            this.extension = extension;
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public String getExtension() {
            return extension;
        }

        public String getDescription() {
            return description;
        }
    }
}
