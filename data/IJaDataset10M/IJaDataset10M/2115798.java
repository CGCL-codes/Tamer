package sun.tools.jconsole.inspector;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.net.*;

/**
 * This provides a wrapper to the Object class to allow it to be
 displayed/manipulated as a GUI object.
*/
public class XObject extends JLabel {

    private Object object;

    private static boolean useHashCodeRepresentation = true;

    public static final XObject NULL_OBJECT = new XObject("null");

    public XObject(Object object, Icon icon) {
        this(object);
        setIcon(icon);
    }

    public XObject(Object object) {
        setObject(object);
        setHorizontalAlignment(SwingConstants.LEFT);
    }

    public boolean equals(Object o) {
        try {
            if (o instanceof XObject) {
                return object.equals(((XObject) o).getObject());
            }
        } catch (Throwable t) {
            System.out.println("Error comparing XObjects" + t.getMessage());
        }
        return false;
    }

    public Object getObject() {
        return object;
    }

    public static void useHashCodeRepresentation(boolean useHashCodeRepresentation) {
        XObject.useHashCodeRepresentation = useHashCodeRepresentation;
    }

    public static boolean hashCodeRepresentation() {
        return useHashCodeRepresentation;
    }

    public void setObject(Object object) {
        this.object = object;
        try {
            String text = null;
            if (object instanceof JLabel) {
                setIcon(((JLabel) object).getIcon());
                if (getText() != null) {
                    text = ((JLabel) object).getText();
                }
            } else if (object instanceof JButton) {
                setIcon(((JButton) object).getIcon());
                if (getText() != null) {
                    text = ((JButton) object).getText();
                }
            } else if (getText() != null) {
                text = object.toString();
                setIcon(IconManager.DEFAULT_XOBJECT);
            }
            if (text != null) {
                if (useHashCodeRepresentation && (this != NULL_OBJECT)) {
                    text = text + "     (" + object.hashCode() + ")";
                }
                setText(text);
            }
        } catch (Exception e) {
            System.out.println("Error setting XObject object :" + e.getMessage());
        }
    }
}
