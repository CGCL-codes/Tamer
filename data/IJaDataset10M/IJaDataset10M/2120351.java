package net.myphpshop.admin.gui.mainform;

import java.util.Vector;

public class UrlList {

    Vector _urlList = new Vector();

    public void addUrl(String url) {
        for (int i = 0; i < _urlList.size(); i++) {
            if (url.equals((String) _urlList.get(i))) {
                _urlList.remove(i);
                break;
            }
        }
        _urlList.add(0, url);
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < _urlList.size(); i++) {
            result += (String) _urlList.get(i);
            if (i < _urlList.size() - 1) {
                result += "\t";
            }
        }
        return result;
    }

    public void fromString(String string) {
        String[] strings = string.split("\t");
        for (int i = 0; i < strings.length; i++) {
            _urlList.add(strings[i]);
        }
    }

    public String[] getStringArray() {
        String[] array = new String[_urlList.size()];
        for (int i = 0; i < _urlList.size(); i++) {
            array[i] = (String) _urlList.get(i);
        }
        return array;
    }
}
