package ingenias.editor.entities;

import ingenias.exception.WrongConversion;
import java.util.*;
import java.io.*;

public class Entity implements java.io.Serializable, Comparable {

    public String id;

    public String type;

    public String helprec = "";

    public String helpdesc = "";

    public ViewPreferences prefs = new ViewPreferences();

    public Entity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return id;
    }

    public void toMap(Map ht) {
        ht.put("_view_type", this.prefs.view.toString());
    }

    public void fromMap(Map m) {
        try {
            if (m.get("_view_type") != null) prefs.view = prefs.view.fromString(m.get("_view_type").toString());
        } catch (WrongConversion e) {
            e.printStackTrace();
        }
    }

    public String getType() {
        String className = this.getClass().getName();
        int index = className.lastIndexOf(".");
        return className.substring(index + 1, className.length());
    }

    public boolean equals(Object object) {
        if (object instanceof Entity) return ((Entity) object).getId().equalsIgnoreCase(this.getId());
        return super.equals(object);
    }

    public int hashCode() {
        return this.getId().hashCode();
    }

    public static String decodeSpecialSymbols(String text) {
        try {
            String s = text;
            s = ingenias.generator.util.Conversor.restoreInvalidChar(text);
            return s;
        } catch (Exception uee) {
            uee.printStackTrace();
        }
        return "";
    }

    public static String encodeutf8Text(String text) {
        try {
            java.io.ByteArrayOutputStream ba = new java.io.ByteArrayOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(ba, "UTF-8");
            osw.write(text);
            osw.close();
            String s = new String(ba.toByteArray(), "UTF-8");
            s = text;
            s = ingenias.generator.util.Conversor.replaceInvalidChar(s);
            StringBuffer sb = new StringBuffer(s);
            return s;
        } catch (Exception uee) {
            uee.printStackTrace();
        }
        return "";
    }

    public static String decodeutf8Text(String text) {
        return text;
    }

    public void setHelpDesc(String desc) {
        this.helpdesc = desc;
    }

    public String getHelpDesc() {
        return this.helpdesc;
    }

    public void setHelpRecom(String rec) {
        this.helprec = rec;
    }

    public String getHelpRecom() {
        return this.helprec;
    }

    public int compareTo(Object o) {
        if (o instanceof Entity) {
            Entity e = (Entity) o;
            return this.getId().compareTo(e.getId());
        }
        return 0;
    }

    public ViewPreferences getPrefs() {
        return prefs;
    }
}
