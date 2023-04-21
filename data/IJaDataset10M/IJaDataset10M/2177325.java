package iwork.patchpanel.manager;

import java.lang.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import iwork.eheap2.*;

public class EventInspectorModel extends AbstractTableModel {

    private EventNode n = null;

    private String[] fieldNames = null;

    private ArrayList listeners = new ArrayList();

    private static Event defaultEvent = null;

    public EventInspectorModel() {
        super();
        try {
            this.defaultEvent = new Event("DefaultEvent");
        } catch (EventHeapException ex) {
            ex.printStackTrace();
        }
    }

    public void sortByColumn(int col, boolean asc) {
        switch(col) {
            case 0:
                Arrays.sort(fieldNames, String.CASE_INSENSITIVE_ORDER);
                break;
            case 1:
                Arrays.sort(fieldNames, new EventComparator(1));
                break;
            case 2:
                Arrays.sort(fieldNames, new EventComparator(2));
                break;
            case 3:
                Arrays.sort(fieldNames, new EventComparator(3));
                break;
        }
        if (!asc) Collections.reverse(Arrays.asList(fieldNames));
    }

    public EventNode getNode() {
        return n;
    }

    public void setNode(EventNode n) {
        if (n != null) {
            this.n = n;
            Tuple event = n.getNewEvent();
            if (event != null) {
                this.fieldNames = n.getFieldNames();
                Arrays.sort(this.fieldNames, new FieldNameComparator());
            } else {
                this.fieldNames = null;
            }
            fireTableDataChanged();
            fireTableStructureChanged();
        } else {
            this.fieldNames = null;
            fireTableDataChanged();
            fireTableStructureChanged();
        }
    }

    public static final Class[] EVENT_CLASS = { String.class, String.class, Object.class, Object.class };

    public Class getColumnClass(int colIndex) {
        return EVENT_CLASS[colIndex];
    }

    public static final String[] EVENT_COLUMNS = { "Field Name", "Type", "Post Value", "Template Value" };

    public int getColumnCount() {
        return EVENT_COLUMNS.length;
    }

    public String getColumnName(int colIndex) {
        return EVENT_COLUMNS[colIndex];
    }

    public int getRowCount() {
        if (n != null) {
            if (fieldNames != null) {
                return fieldNames.length;
            }
        }
        return 0;
    }

    public boolean valueChanged(String fieldName, int colIndex) {
        try {
            Tuple oldEvent = n.getOriginalEvent();
            Tuple newEvent = n.getNewEvent();
            if (oldEvent == null) {
                boolean isDefault = isDefaultField(fieldName);
                if (isDefault) {
                    oldEvent = defaultEvent;
                } else {
                    return true;
                }
            } else if (!oldEvent.fieldExists(fieldName)) {
                return true;
            }
            switch(colIndex) {
                case 0:
                    return false;
                case 1:
                    String oldType = oldEvent.getFieldType(fieldName).toString();
                    String newType = newEvent.getFieldType(fieldName).toString();
                    return (!oldType.equals(newType));
                case 2:
                    String oldPost = oldEvent.getPostValueString(fieldName);
                    String newPost = newEvent.getPostValueString(fieldName);
                    return (!oldPost.equals(newPost));
                case 3:
                    String oldTemplate = oldEvent.getTemplateValueString(fieldName);
                    String newTemplate = newEvent.getTemplateValueString(fieldName);
                    return (!oldTemplate.equals(newTemplate));
            }
        } catch (EventHeapException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Object getValueAt(int rowIndex, int colIndex) {
        if (n != null) {
            try {
                String fieldName = fieldNames[rowIndex];
                Tuple event = n.getNewEvent();
                switch(colIndex) {
                    case 0:
                        return fieldName;
                    case 1:
                        return event.getFieldType(fieldName);
                    case 2:
                        return event.getPostValueString(fieldName);
                    case 3:
                        return event.getTemplateValueString(fieldName);
                    default:
                        return null;
                }
            } catch (EventHeapException ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static final boolean[] EDITABLE = { true, true, true, true };

    public static final boolean[] EVENT_TYPE_EDITABLE = { false, false, true, true };

    public boolean isCellEditable(int rowIndex, int colIndex) {
        String fieldName = fieldNames[rowIndex];
        if (fieldName.equals("EventType")) {
            return EVENT_TYPE_EDITABLE[colIndex];
        } else {
            return EDITABLE[colIndex];
        }
    }

    public void setValueAt(Object aValue, int rowIndex, int colIndex) {
        String fieldName = fieldNames[rowIndex];
        String newValue = (String) aValue;
        Tuple event = n.getNewEvent();
        try {
            switch(colIndex) {
                case 0:
                    String newFieldName = newValue;
                    if (!event.fieldExists(newFieldName) && !fieldName.equals("EventType")) {
                        String oldType = event.getFieldType(fieldName);
                        String oldPostValue = event.getPostValueString(fieldName);
                        String oldTemplateValue = event.getTemplateValueString(fieldName);
                        n.removeField(fieldName);
                        n.addField(newFieldName, oldType, oldPostValue, oldTemplateValue);
                        notifyListeners();
                    }
                    break;
                case 1:
                    String newType = newValue;
                    String oldPostValue = event.getPostValueString(fieldName);
                    String oldTemplateValue = event.getTemplateValueString(fieldName);
                    n.replaceField(fieldName, newType, oldPostValue, oldTemplateValue);
                    notifyListeners();
                    break;
                case 2:
                    String oldPost = event.getPostValueString(fieldName);
                    if (oldPost != newValue) {
                        n.setPostValueString(fieldName, newValue);
                        notifyListeners();
                    }
                    break;
                case 3:
                    String oldTemplate = event.getTemplateValueString(fieldName);
                    if (oldTemplate != newValue) {
                        n.setTemplateValueString(fieldName, newValue);
                        notifyListeners();
                    }
                    break;
            }
        } catch (EventHeapException ex) {
            String msg = "Error: " + ex.getMessage();
            JOptionPane.showMessageDialog(null, msg, "Error Changing Field", JOptionPane.ERROR_MESSAGE);
            notifyListeners();
        }
        fireTableDataChanged();
    }

    public void addListener(PPListener listener) {
        listeners.add(listener);
    }

    public void removeListener(PPListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        Iterator it = listeners.iterator();
        while (it.hasNext()) {
            PPListener ppl = (PPListener) it.next();
            ppl.dataChanged();
        }
    }

    public class EventComparator implements Comparator {

        int field;

        public EventComparator(int field) {
            this.field = field;
        }

        public int compare(Object o1, Object o2) {
            try {
                if (o1 instanceof String && o2 instanceof String) {
                    String field1 = (String) o1;
                    String field2 = (String) o2;
                    String value1;
                    String value2;
                    Tuple event = n.getNewEvent();
                    switch(field) {
                        case 1:
                            value1 = event.getFieldType(field1).toString();
                            value2 = event.getFieldType(field2).toString();
                            break;
                        case 2:
                            value1 = event.getPostValueString(field1);
                            value2 = event.getPostValueString(field2);
                            break;
                        case 3:
                        default:
                            value1 = event.getTemplateValueString(field1);
                            value2 = event.getTemplateValueString(field2);
                            break;
                    }
                    return value1.compareTo(value2);
                } else {
                    return 0;
                }
            } catch (EventHeapException ex) {
                ex.printStackTrace();
                return 0;
            }
        }
    }

    public static boolean isDefaultField(String s) {
        if (s != null && s.equals("EventType")) {
            return false;
        } else {
            boolean isDefault = defaultEvent.fieldExists(s);
            return isDefault;
        }
    }

    public class FieldNameComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            String s1 = o1.toString();
            String s2 = o2.toString();
            boolean s1Default = isDefaultField(s1);
            boolean s2Default = isDefaultField(s2);
            if (s1Default && !s2Default) {
                return 1;
            } else if (!s1Default && s2Default) {
                return -1;
            } else {
                return s1.compareTo(s2);
            }
        }
    }
}
