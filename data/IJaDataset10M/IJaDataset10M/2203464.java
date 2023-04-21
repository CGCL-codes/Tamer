package druid.core;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/** The following methods may affect the dataChanged bit:
  * - setInt, setBool, setString and setData
  *
  * The following methods don't affect the dataChanged bit:
  * - addAttrib, removeAttrib, duplicate, load, save
  */
public class AttribSet {

    private Hashtable htAttribs = new Hashtable(30, 0.75f);

    protected AttribSet getNewInstance() {
        return new AttribSet();
    }

    public AttribSet() {
    }

    public void addAttrib(String name, String data) {
        addAttrib(name, Attrib.STRING, data);
    }

    public void addAttrib(String name, int data) {
        addAttrib(name, Attrib.INT, new Integer(data));
    }

    public void addAttrib(String name, boolean data) {
        addAttrib(name, Attrib.BOOL, Boolean.valueOf(data));
    }

    public void removeAttrib(String name) {
        if (!htAttribs.containsKey(name)) throw new DruidException(DruidException.ILL_ARG, "Attrib not found", name);
        htAttribs.remove(name);
    }

    public boolean contains(String name) {
        return htAttribs.containsKey(name);
    }

    public boolean isEmpty() {
        return htAttribs.isEmpty();
    }

    public Enumeration attribs() {
        return htAttribs.keys();
    }

    public String getString(String name) {
        return (String) getData(name);
    }

    public int getInt(String name) {
        return ((Integer) getData(name)).intValue();
    }

    public boolean getBool(String name) {
        return ((Boolean) getData(name)).booleanValue();
    }

    public Boolean getBoolean(String name) {
        return (Boolean) getData(name);
    }

    public Object getData(String name) {
        Attrib a = (Attrib) htAttribs.get(name);
        if (a == null) throw new DruidException(DruidException.ILL_ARG, "Attrib not found", name);
        return a.oData;
    }

    public boolean isAttribAString(String name) {
        Attrib a = (Attrib) htAttribs.get(name);
        if (a == null) throw new DruidException(DruidException.ILL_ARG, "Attrib not found", name);
        return a.iType == Attrib.STRING;
    }

    public boolean isAttribAnInt(String name) {
        Attrib a = (Attrib) htAttribs.get(name);
        if (a == null) throw new DruidException(DruidException.ILL_ARG, "Attrib not found", name);
        return a.iType == Attrib.INT;
    }

    public boolean isAttribABoolean(String name) {
        Attrib a = (Attrib) htAttribs.get(name);
        if (a == null) throw new DruidException(DruidException.ILL_ARG, "Attrib not found", name);
        return a.iType == Attrib.BOOL;
    }

    public void setString(String name, String value) {
        setData(name, value);
    }

    public void setInt(String name, int value) {
        setData(name, new Integer(value));
    }

    public void setBool(String name, boolean value) {
        setData(name, Boolean.valueOf(value));
    }

    public void setData(String name, Object value) {
        Attrib a = (Attrib) htAttribs.get(name);
        if (a == null) throw new DruidException(DruidException.ILL_ARG, "Attrib not found", name);
        a.setData(value);
    }

    public AttribSet duplicate() {
        AttribSet as = getNewInstance();
        for (Enumeration e = htAttribs.keys(); e.hasMoreElements(); ) {
            String name = (String) e.nextElement();
            Attrib value = (Attrib) htAttribs.get(name);
            as.htAttribs.put(name, value.duplicate());
        }
        return as;
    }

    public int remapId() {
        if (contains("id")) {
            setInt("id", Serials.get());
            return getInt("id");
        }
        return 0;
    }

    public String toString() {
        StringBuffer s = new StringBuffer("[ATTRSET:\n");
        for (Enumeration e = htAttribs.keys(); e.hasMoreElements(); ) {
            String name = (String) e.nextElement();
            Object value = htAttribs.get(name);
            s.append("  ").append(name).append(" = ").append(value).append("\n");
        }
        return s.append("]").toString();
    }

    public void sync(AttribSet as) {
        Vector v = new Vector();
        for (Enumeration e = htAttribs.keys(); e.hasMoreElements(); ) {
            String attrib = (String) e.nextElement();
            if (!as.contains(attrib)) v.add(attrib);
        }
        for (int i = 0; i < v.size(); i++) htAttribs.remove(v.get(i));
        for (Enumeration e = as.attribs(); e.hasMoreElements(); ) {
            String attrib = (String) e.nextElement();
            if (!htAttribs.containsKey(attrib)) htAttribs.put(attrib, as.getData(attrib));
        }
    }

    private void addAttrib(String name, int type, Object data) {
        if (htAttribs.containsKey(name)) throw new DruidException(DruidException.ILL_ARG, "Attrib already in use", name);
        htAttribs.put(name, new Attrib(type, data));
    }
}

class Attrib {

    static final int STRING = 0;

    static final int INT = 1;

    static final int BOOL = 2;

    int iType;

    Object oData;

    public Attrib(int type, Object data) {
        iType = type;
        oData = data;
    }

    public void setAndConvert(String value) {
        switch(iType) {
            case INT:
                oData = new Integer(value);
                break;
            case BOOL:
                oData = Boolean.valueOf(value);
                break;
            default:
                oData = value;
        }
    }

    public Attrib duplicate() {
        Attrib a = new Attrib(iType, null);
        switch(iType) {
            case INT:
                int v = ((Integer) oData).intValue();
                a.oData = new Integer(v);
                break;
            case BOOL:
                boolean b = ((Boolean) oData).booleanValue();
                a.oData = Boolean.valueOf(b);
                break;
            default:
                String s = (String) oData;
                a.oData = s;
        }
        return a;
    }

    public void setData(Object data) {
        boolean bStr = (iType == STRING && !(data instanceof String));
        boolean bInt = (iType == INT && !(data instanceof Integer));
        boolean bBool = (iType == BOOL && !(data instanceof Boolean));
        if (bStr || bInt || bBool) throw new DruidException(DruidException.ILL_ARG, "Wrong attrib class", data);
        if (!oData.toString().equals(data.toString())) DataTracker.setDataChanged();
        oData = data;
    }

    public String toString() {
        return oData.toString();
    }
}
