package de.ios.framework.report.data;

import de.ios.framework.basic.*;
import de.ios.framework.report.control.*;
import java.util.*;
import java.io.*;
import java.awt.*;

/**
 * Matrix for storing ObjectData with automatic convertions to 
 * support StringMatrix-operations.
 */
public class ObjectDataMatrix extends StringMatrix {

    /**
   * Constructors
   */
    public ObjectDataMatrix() {
        super();
    }

    /**
   * Copy-constructor.
   */
    public ObjectDataMatrix(ObjectMatrix src) {
        super(src);
    }

    /**
   * Constructor.
   * @param _vecCapInc Capacity increment for internaly used vectors.
   */
    public ObjectDataMatrix(int _vecCapInc) {
        super(_vecCapInc);
    }

    /**
   * Constructor.
   * Creates matrix with given size. The resulting matrix can be resized like any other.
   * @param _rows Number of initial rows.
   * @param _cols Number of initial columns.
   */
    public ObjectDataMatrix(int _rows, int _cols) {
        super(_rows, _cols);
    }

    /**
   * Constructor.
   * Creates matrix with given size and vector increment. 
   * The resulting matrix can be resized like any other.
   * @param _rows Number of initial rows.
   * @param _cols Number of initial columns.
   * @param _vecCapInc Capacity increment for internaly vectors.
   */
    public ObjectDataMatrix(int _rows, int _cols, int _vecCapInc) {
        super(_rows, _cols, _vecCapInc);
    }

    /**
   * Gets the number of subelements that are not null (recursiv).
   */
    public int countElement() {
        Enumeration e = elements();
        Vector emptyP = new Vector();
        ObjectData obj;
        int c = 0;
        while (e.hasMoreElements()) {
            obj = (ObjectData) e.nextElement();
            if (obj != null) {
                try {
                    c += ((DoubleData) obj.execute("size", emptyP)).value;
                } catch (Exception e1) {
                }
            }
        }
        return c;
    }

    /**
   * create another Matrix of the same Type
   */
    public ObjectMatrix createAnother() {
        return new ObjectDataMatrix(vecCapInc);
    }

    /**
   * get an Element as String.
   */
    public ObjectData getData(int row, int col) {
        return (ObjectData) getElement(row, col);
    }

    /**
   * get a single Element
   */
    public Object getElement(int row, int col) {
        ObjectData obj = (ObjectData) accessRow(row).elementAt(col);
        if (obj == null) obj = new DoubleData("", 0.0);
        return obj;
    }

    /**
   * Cast an Element to a String
   */
    public String getString(Object o) {
        if (o instanceof StringMatrix) {
            return Double.toString(((StringMatrix) o).sum(null));
        } else if (o instanceof ObjectData) {
            try {
                return ((ObjectData) o).execute("sum", new Vector()).toString();
            } catch (Exception e) {
            }
        } else return super.getString(o);
        return "";
    }

    /**
   * parse an Object to Double
   */
    public double getDouble(Object o, Object xparam) {
        String s;
        int i;
        char c;
        s = getString(o).trim();
        if (s.length() == 0) return 0; else {
            try {
                return Double.valueOf(s).doubleValue();
            } catch (NumberFormatException e) {
                for (i = s.length() - 1; i >= 0; i--) {
                    c = s.charAt(i);
                    if (c >= '0' && c <= '9') {
                        try {
                            return Double.valueOf(s.substring(0, c + 1)).doubleValue();
                        } catch (NumberFormatException e2) {
                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
   * store a Double as a DoubleData-Object
   */
    public Object doubleObject(double d, Object xparam) {
        return new DoubleData("", d);
    }

    /**
   * new Row: casting the new Objects to internal Objects
   */
    public Vector newRow(Vector v) {
        int i;
        for (i = 0; i < v.size(); i++) v.setElementAt(newElement(v.elementAt(i)), i);
        return v;
    }

    /**
   * new Element:  casting the new Objects to internal Objects
   */
    public Object newElement(Object o) {
        try {
            return ReportCtrl.makeDataObject(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new TextData("", "#ERROR");
    }
}
