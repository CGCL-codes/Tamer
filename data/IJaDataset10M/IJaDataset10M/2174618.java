package edu.berkeley.guir.quill;

import edu.berkeley.guir.quill.gesturelib.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import java.beans.*;

/** Various utility functions that didn't really go anywhere else. 
 * <P>
 * This software is distributed under the 
 * <A HREF="http://guir.cs.berkeley.edu/projects/COPYRIGHT.txt">
 * Berkeley Software License</A>.
 */
public class QuillUtil implements QuillConstants {

    public static Icon LOW_SEVERITY_ICON = new ImageIcon(SummaryLog.class.getResource("images/lightbulb-white.png"));

    public static Icon MEDIUM_SEVERITY_ICON = new ImageIcon(SummaryLog.class.getResource("images/lightbulb-yellow.png"));

    public static Icon HIGH_SEVERITY_ICON = new ImageIcon(SummaryLog.class.getResource("images/lightbulb-bright.png"));

    public static Icon INFORMATION_ICON = new ImageIcon(SummaryLog.class.getResource("images/Information16.png"));

    public static void setProperty(Collection gestureObjs, String name, Object value) {
        setProperty(gestureObjs, name, value, false);
    }

    public static void setProperty(Collection gestureObjs, String name, Object value, boolean recursive) {
        setProperty(gestureObjs.iterator(), name, value, recursive);
    }

    public static void setProperty(Iterator iter, String name, Object value, boolean recursive) {
        for (; iter.hasNext(); ) {
            Object obj = iter.next();
            if (obj instanceof GestureObject) {
                GestureObject gestureObj = (GestureObject) obj;
                gestureObj.setProperty(name, value);
                if (recursive && (gestureObj instanceof GestureContainer)) {
                    setProperty(((GestureContainer) gestureObj).iterator(), name, value, true);
                }
            }
        }
    }

    public static void unsetProperty(Iterator iter, String name, boolean recursive) {
        for (; iter.hasNext(); ) {
            Object obj = iter.next();
            if (obj instanceof GestureObject) {
                GestureObject gestureObj = (GestureObject) obj;
                gestureObj.unsetProperty(name);
                if (recursive && (gestureObj instanceof GestureContainer)) {
                    unsetProperty(((GestureContainer) gestureObj).iterator(), name, true);
                }
            }
        }
    }

    public static void setEnabledRecursively(GestureContainer container, boolean value) {
        container.setEnabled(value);
        setEnabledOnForest(container.iterator(), value);
    }

    /** forest should contain GestureObjects.  Other types will be ignored */
    public static void setEnabledOnForest(Collection forest, boolean value) {
        setEnabledOnForest(forest.iterator(), value);
    }

    public static void setEnabledOnForest(Iterator iter, boolean value) {
        for (; iter.hasNext(); ) {
            Object obj = iter.next();
            if (obj instanceof GestureObject) {
                GestureObject gestureObj = (GestureObject) obj;
                gestureObj.setEnabled(value);
                if (gestureObj instanceof GestureContainer) {
                    setEnabledOnForest(((GestureContainer) gestureObj).iterator(), value);
                }
            }
        }
    }

    /** simple exhaustive search, for arrays that are not sorted */
    public static boolean contains(int[] array, int n) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == n) {
                return true;
            }
        }
        return false;
    }

    /** search up the hierarchy until the MainFrame is enountered, then
      get its SummaryLog */
    public static SummaryLog getSummaryLog(Component component) {
        MainFrame mf = (MainFrame) SwingUtilities.getAncestorOfClass(MainFrame.class, component);
        return (mf == null) ? null : mf.getSummaryLog();
    }

    /** return a list of all descendants of container that are type
      gestureClass (or a subclass) */
    public static List collectGestureObjects(GestureContainer container, Class gestureClass) {
        return collectGestureObjects(container.iterator(), gestureClass);
    }

    public static List collectGestureObjects(Iterator iter, Class gestureClass) {
        List result = new ArrayList();
        while (iter.hasNext()) {
            GestureObject obj = (GestureObject) iter.next();
            if (gestureClass.isInstance(obj)) {
                result.add(obj);
            }
            if (obj instanceof GestureContainer) {
                result.addAll(collectGestureObjects((GestureContainer) obj, gestureClass));
            }
        }
        return result;
    }

    protected static final String DELETE_LISTENER_PROP = "DELETE_LISTENER_PROP";

    /** Add a listener to the display so that if the object it displays
      is removed from its parent, the internal frame containing the
      display is closed */
    public static void addDeleteListener(final GestureObjectDisplay display) {
        final JComponent jDisplay = (JComponent) display;
        PropertyChangeListener listener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName() == GestureObject.PARENT_PROP && e.getNewValue() == null) {
                    System.out.println("parent change for " + e.getSource() + ": " + e.getOldValue() + e.getNewValue());
                    JInternalFrame iFrame = (JInternalFrame) SwingUtilities.getAncestorOfClass(JInternalFrame.class, jDisplay);
                    if (iFrame != null) {
                        try {
                            iFrame.setClosed(true);
                        } catch (PropertyVetoException exception) {
                            System.err.println("WARNING: could not close display for " + display.getDisplayedObject());
                        }
                    }
                }
            }
        };
        display.getDisplayedObject().addPropertyChangeListener(GestureObject.PARENT_PROP, listener);
        jDisplay.putClientProperty(DELETE_LISTENER_PROP, listener);
    }

    public static void removeDeleteListener(GestureObjectDisplay display) {
        JComponent jDisplay = (JComponent) display;
        PropertyChangeListener listener = (PropertyChangeListener) jDisplay.getClientProperty(DELETE_LISTENER_PROP);
        if (listener != null) {
            display.getDisplayedObject().removePropertyChangeListener(GestureObject.PARENT_PROP, listener);
        }
    }

    /** return true iff all bits that are on in mask are on in set
      (other bits in set may be on as well) */
    public static boolean areBitsSet(int set, int mask) {
        return (set & mask) == mask;
    }

    /** return true iff gestureObj is a descendent of the training set of a
      GesturePackage */
    public static boolean isInTrainingSet(GestureObject gestureObj) {
        GestureSet ancestorSet = (GestureSet) AbstractGestureContainer.findAncestorOfClass(gestureObj, GestureSet.class);
        GestureContainer parent = (ancestorSet == null) ? null : ancestorSet.getParent();
        return (parent != null) && (parent instanceof GesturePackage);
    }
}
