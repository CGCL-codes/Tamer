package org.restfaces.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.restfaces.util.Util;

/**
 *
 * @author agori
 */
public class View extends BaseElement {

    private String id;

    private Pattern pattern;

    private List<ViewListener> listenerList = new ArrayList<ViewListener>();

    public View() {
    }

    public int match(String s) {
        Matcher matcher = pattern.matcher(s);
        int count = -1;
        if (matcher.matches()) {
            count = 0;
            for (int i = 1; i <= matcher.groupCount(); ++i) {
                String g = matcher.group(i);
                count += g.length();
            }
        }
        return count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.pattern = Pattern.compile(Util.convertToRegex(id));
    }

    public List<ViewListener> getListenerList() {
        return listenerList;
    }

    public void setListenerList(List<ViewListener> listenerList) {
        this.listenerList = listenerList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(MessageFormat.format("id={0},pattern={1}, [", new Object[] { new Integer(id), pattern }));
        for (ViewListener listener : listenerList) {
            sb.append(listener.toString());
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final View other = (View) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }
}
