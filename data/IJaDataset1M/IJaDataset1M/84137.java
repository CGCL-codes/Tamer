package blue.automation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import electric.xml.Element;
import electric.xml.Elements;

public class ParameterIdList implements Serializable {

    private static final Comparator comparator = new Comparator() {

        public int compare(Object o1, Object o2) {
            String s1 = (String) o1;
            String s2 = (String) o2;
            return s1.compareToIgnoreCase(s2);
        }
    };

    private ArrayList parameters = new ArrayList();

    private int selectedIndex = -1;

    transient Vector listListeners = null;

    transient Vector listSelectionListeners = null;

    public String getParameterId(int index) {
        return (String) parameters.get(index);
    }

    public void addParameterId(String parameterId) {
        parameters.add(parameterId);
        String current = null;
        if (getSelectedIndex() > 0) {
            current = getParameterId(getSelectedIndex());
        }
        Collections.sort(parameters, comparator);
        int index = parameters.indexOf(parameterId);
        ListDataEvent lde = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index);
        fireAddDataEvent(lde);
        if (size() == 1) {
            setSelectedIndex(0);
        } else if (current != null) {
            setSelectedIndex(parameters.indexOf(current));
        }
    }

    public void removeParameterId(String parameterId) {
        if (parameters.contains(parameterId)) {
            int index = parameters.indexOf(parameterId);
            parameters.remove(parameterId);
            ListDataEvent lde = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index);
            fireRemoveDataEvent(lde);
            if (size() == 0) {
                setSelectedIndex(-1);
            } else if (index == selectedIndex) {
                if (selectedIndex >= size()) {
                    setSelectedIndex(size() - 1);
                }
            } else if (index < selectedIndex) {
                setSelectedIndex(selectedIndex - 1);
            }
        }
    }

    public void removeParameterId(int index) {
        parameters.remove(index);
        ListDataEvent lde = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index);
        fireRemoveDataEvent(lde);
    }

    public ArrayList getParameters() {
        return parameters;
    }

    public Iterator iterator() {
        return parameters.iterator();
    }

    public int size() {
        return parameters.size();
    }

    public Element saveAsXML() {
        Element retVal = new Element("parameterIdList");
        retVal.setAttribute("selectedIndex", Integer.toString(selectedIndex));
        for (int i = 0; i < size(); i++) {
            retVal.addElement("parameterId").setText((String) parameters.get(i));
        }
        return retVal;
    }

    public static ParameterIdList loadFromXML(Element data) {
        ParameterIdList retVal = new ParameterIdList();
        String indexStr = data.getAttributeValue("selectedIndex");
        if (indexStr != null && indexStr.length() > 0) {
            retVal.selectedIndex = Integer.parseInt(indexStr);
        }
        Elements nodes = data.getElements();
        while (nodes.hasMoreElements()) {
            Element node = nodes.next();
            String nodeName = node.getName();
            if (nodeName.equals("parameterId")) {
                retVal.parameters.add(node.getTextString());
            }
        }
        if (retVal.size() > 0 && retVal.getSelectedIndex() < 0) {
            retVal.setSelectedIndex(0);
        }
        return retVal;
    }

    public void addListDataListener(ListDataListener l) {
        if (listListeners == null) {
            listListeners = new Vector();
        }
        listListeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        if (listListeners != null) {
            listListeners.remove(l);
        }
    }

    private void fireAddDataEvent(ListDataEvent lde) {
        if (listListeners == null) {
            return;
        }
        Iterator iter = new Vector(listListeners).iterator();
        while (iter.hasNext()) {
            ListDataListener listener = (ListDataListener) iter.next();
            listener.intervalAdded(lde);
        }
    }

    private void fireRemoveDataEvent(ListDataEvent lde) {
        if (listListeners == null) {
            return;
        }
        Iterator iter = new Vector(listListeners).iterator();
        while (iter.hasNext()) {
            ListDataListener listener = (ListDataListener) iter.next();
            listener.intervalRemoved(lde);
        }
    }

    public void addListSelectionListener(ListSelectionListener listener) {
        if (listSelectionListeners == null) {
            listSelectionListeners = new Vector();
        }
        if (listSelectionListeners.contains(listener)) {
            return;
        }
        listSelectionListeners.add(listener);
    }

    public void removeListSelectionListener(ListSelectionListener l) {
        if (listSelectionListeners != null) {
            listSelectionListeners.remove(l);
        }
    }

    private void fireListSelectionEvent(int index) {
        if (listSelectionListeners != null && listSelectionListeners.size() > 0) {
            Iterator iter = new Vector(listSelectionListeners).iterator();
            ListSelectionEvent lse = new ListSelectionEvent(this, index, index, false);
            while (iter.hasNext()) {
                ListSelectionListener listener = (ListSelectionListener) iter.next();
                listener.valueChanged(lse);
            }
        }
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public boolean contains(String parameterId) {
        return parameters.contains(parameterId);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        fireListSelectionEvent(selectedIndex);
    }

    void setSelectedParameter(String paramId) {
        if (paramId == null) {
            return;
        }
        int index = parameters.indexOf(paramId);
        if (index >= 0) {
            setSelectedIndex(index);
        }
    }
}
