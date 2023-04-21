package com.cosylab.capfast.vdctmodel;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import com.cosylab.capfast.model.Wire;

/**
 * holds field data
 *
 * @author ilist
 */
public class Field {

    /** DOCUMENT ME! */
    public static final int DEFAULT_VISIBLE = -1;

    /** DOCUMENT ME! */
    public static final int NON_DEFAULT_VISIBLE = 0;

    /** DOCUMENT ME! */
    public static final int ALWAYS_VISIBLE = 1;

    /** DOCUMENT ME! */
    public static final int NEVER_VISIBLE = 2;

    public static final int LT_NOTALINK = -1;

    public static final int LT_NORMAL = 0;

    public static final int LT_INVISIBLE = 1;

    public static final int LT_OUTPUT = 2;

    public static final int LT_INPUT = 3;

    String name;

    String value;

    Vector path = null;

    int visibility = -1;

    int linktype = -1;

    /**
	 * Creates a new Field object.
	 *
	 * @param name DOCUMENT ME!
	 */
    public Field(String name) {
        this.name = name;
    }

    /**
	 * Creates a new Field object.
	 *
	 * @param name DOCUMENT ME!
	 * @param value DOCUMENT ME!
	 */
    public Field(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
	 * Creates a new Field object
	 */
    public Field(String name, String value, Vector path, int linktype) {
        this.name = name;
        this.value = value;
        this.path = path;
        this.linktype = linktype;
    }

    /**
	 * Creates a new Field object.
	 *
	 * @param name DOCUMENT ME!
	 * @param value DOCUMENT ME!
	 * @param visibility DOCUMENT ME!
	 */
    public Field(String name, String value, int visibility) {
        this.name = name;
        this.value = value;
        this.visibility = visibility;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param r DOCUMENT ME!
	 * @param sc
	 *
	 * @throws IOException DOCUMENT ME!
	 */
    public void write(Record r, SerializationContext sc) throws IOException {
        final String[] fieldstring = { "field", "port", "macro" };
        String type = fieldstring[r.getInternalType()];
        sc.writeLine("  " + type + "(" + name + ",\"" + value + "\")");
        if (r.getInternalType() == Record.TYPE_NORMAL) {
            if (visibility != DEFAULT_VISIBLE) {
                sc.writeEofComment("Visibility(\"" + r.getName() + "." + name + "\"," + visibility + ")");
            }
            if (path != null) {
                int k = 1;
                sc.writeEofComment("Field(\"" + r.getName() + "." + name + "\",16777215,0,\"" + r.getName() + "." + name + "\")");
                sc.writeEofComment("Link(\"" + r.getName() + "." + name + "\",\"" + r.getName() + "/" + name + 1 + "\")");
                for (Iterator i = path.iterator(); i.hasNext(); k++) {
                    Wire.ConnectorPoint cp = (Wire.ConnectorPoint) i.next();
                    cp.write(sc, r.getName() + "/" + name + k, i.hasNext() ? r.getName() + "/" + name + (k + 1) : value, i.hasNext() ? 0 : linktype);
                }
            }
        } else if (r.getInternalType() == Record.TYPE_EXPANDRECORD) {
            int visibility = this.visibility;
            if (visibility == -1) {
                visibility = 1;
            }
            sc.writeEofComment("TemplateField(\"" + r.getName() + "\",\"" + name + "\",16777215,0," + visibility + ")");
        }
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return
	 */
    public String getValue() {
        return value;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param string
	 */
    public void setValue(String string) {
        value = string;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return
	 */
    public int getVisibility() {
        return visibility;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param i
	 */
    public void setVisibility(int i) {
        visibility = i;
    }

    /**
	 * @return
	 */
    public int getLinktype() {
        return linktype;
    }
}
