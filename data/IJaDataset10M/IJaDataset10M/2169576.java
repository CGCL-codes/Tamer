package ircam.jmax.editors.sequence.track;

import ircam.jmax.fts.*;
import ircam.jmax.toolkit.*;
import ircam.jmax.*;
import ircam.jmax.editors.sequence.renderers.*;
import ircam.jmax.editors.sequence.*;
import java.io.*;
import javax.swing.*;
import java.util.*;
import java.awt.datatransfer.*;

/**
* The EventValue object that represents a Integer event. Is used during score-recognition */
public class FloatValue extends AbstractEventValue {

    public FloatValue() {
        super();
        setProperty("value", new Float(0.0));
        setProperty("duration", new Double(64.0));
    }

    Object floatValue, duration;

    public void setProperty(String name, Object value) {
        if (name.equals("value")) if (value instanceof Double) floatValue = new Float(((Double) value).floatValue()); else floatValue = value; else if (name.equals("duration")) duration = value; else super.setProperty(name, value);
    }

    public Object getProperty(String name) {
        if (name.equals("value")) return floatValue;
        if (name.equals("duration")) return duration; else return super.getProperty(name);
    }

    public ValueInfo getValueInfo() {
        return info;
    }

    static class FloatValueInfo extends AbstractValueInfo {

        /**
  * Returns the name of this value object */
        public String getName() {
            return FLOAT_NAME;
        }

        public String getPublicName() {
            return FLOAT_PUBLIC_NAME;
        }

        public ImageIcon getIcon() {
            return FLOAT_ICON;
        }

        public Object newInstance() {
            return new FloatValue();
        }

        public DataFlavor getDataFlavor() {
            return FloatValueDataFlavor.getInstance();
        }
    }

    /**
* Returns its specialized renderer (an AmbitusEventRenderer) */
    public SeqObjectRenderer getRenderer() {
        return IntegerEventRenderer.getRenderer();
    }

    public void setPropertyValues(int nArgs, Object args[]) {
        if (nArgs == 1) setProperty("value", args[0]); else super.setPropertyValues(nArgs, args);
    }

    public static final String fs = File.separator;

    public static final String FLOAT_NAME = "float";

    public static ImageIcon FLOAT_ICON;

    static String path;

    public static final String FLOAT_PUBLIC_NAME = "float";

    public static FloatValueInfo info = new FloatValueInfo();

    public static final int DEFAULT_MAX_VALUE = 1;

    public static final int DEFAULT_MIN_VALUE = 0;

    static {
        if (JMaxApplication.getProperty("ftm") == null) {
            path = JMaxApplication.getProperty("jmaxRoot") + fs + "packages" + fs + "sequence" + fs + "images" + fs;
            FLOAT_ICON = new ImageIcon(path + "seq_float.gif");
        } else FLOAT_ICON = JMaxUtilities.loadIconFromResource("images/seq_float.gif");
    }
}
