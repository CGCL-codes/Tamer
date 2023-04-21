package net.wotonomy.ui.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import net.wotonomy.foundation.NSArray;
import net.wotonomy.foundation.NSSelector;
import net.wotonomy.foundation.internal.ValueConverter;
import net.wotonomy.foundation.internal.WotonomyException;
import net.wotonomy.ui.EOAssociation;
import net.wotonomy.ui.EODisplayGroup;

/**
* DateAssociation binds any component that has a set and get Date methods and
* fire actions events when the date has been changed.  Bindings are:
* <ul>
* <li>value: a property convertable to/from a date</li>
* <li>editable: a boolean property that determines whether
* the user can edit the date in the component</li>
* <li>enabled: a boolean property that determines whether
* the component is enabled or disabled</li>
* </ul>
*
* @author rob@yahoo.com
* @version $Revision: 904 $
*/
public class DateAssociation extends EOAssociation implements ActionListener, FocusListener {

    static final NSArray aspects = new NSArray(new Object[] { ValueAspect, EnabledAspect, EditableAspect });

    static final NSArray aspectSignatures = new NSArray(new Object[] { AttributeToOneAspectSignature, AttributeToOneAspectSignature, AttributeToOneAspectSignature });

    static final NSArray objectKeysTaken = new NSArray(new Object[] { "date", "enabled", "editable" });

    private static final NSSelector getDate = new NSSelector("getDate");

    private static final NSSelector setDate = new NSSelector("setDate", new Class[] { Date.class });

    private static final NSSelector addActionListener = new NSSelector("addActionListener", new Class[] { ActionListener.class });

    private static final NSSelector removeActionListener = new NSSelector("removeActionListener", new Class[] { ActionListener.class });

    private static final NSSelector addFocusListener = new NSSelector("addFocusListener", new Class[] { FocusListener.class });

    private static final NSSelector removeFocusListener = new NSSelector("removeFocusListener", new Class[] { FocusListener.class });

    private static final NSSelector setEditable = new NSSelector("setEditable", new Class[] { boolean.class });

    private boolean needsUpdate;

    private Date nullValue;

    /**
    * Constructor specifying the object to be controlled by this
    * association.  Does not establish connection.
    */
    public DateAssociation(Object anObject) {
        super(anObject);
        needsUpdate = false;
        nullValue = null;
    }

    /**
    * Returns a List of aspect signatures whose contents
    * correspond with the aspects list.  Each element is
    * a string whose characters represent a capability of
    * the corresponding aspect. <ul>
    * <li>"A" attribute: the aspect can be bound to
    * an attribute.</li>
    * <li>"1" to-one: the aspect can be bound to a
    * property that returns a single object.</li>
    * <li>"M" to-one: the aspect can be bound to a
    * property that returns multiple objects.</li>
    * </ul>
    * An empty signature "" means that the aspect can
    * bind without needing a key.
    * This implementation returns "A1M" for each
    * element in the aspects array.
    */
    public static NSArray aspectSignatures() {
        return aspectSignatures;
    }

    /**
    * Returns a List that describes the aspects supported
    * by this class.  Each element in the list is the string
    * name of the aspect.  This implementation returns an
    * empty list.
    */
    public static NSArray aspects() {
        return aspects;
    }

    /**
    * Returns a List of EOAssociation subclasses that,
    * for the objects that are usable for this association,
    * are less suitable than this association.
    */
    public static NSArray associationClassesSuperseded() {
        return new NSArray();
    }

    /**
    * Returns whether this class can control the specified
    * object.
    */
    public static boolean isUsableWithObject(Object anObject) {
        return setDate.implementedByObject(anObject);
    }

    /**
    * Returns a List of properties of the controlled object
    * that are controlled by this class.  For example,
    * "stringValue", or "selected".
    */
    public static NSArray objectKeysTaken() {
        return objectKeysTaken;
    }

    /**
    * Returns the aspect that is considered primary
    * or default.  This is typically "value" or somesuch.
    */
    public static String primaryAspect() {
        return ValueAspect;
    }

    /**
    * Returns whether this association can bind to the
    * specified display group on the specified key for
    * the specified aspect.
    */
    public boolean canBindAspect(String anAspect, EODisplayGroup aDisplayGroup, String aKey) {
        return (aspects.containsObject(anAspect));
    }

    /**
    * Establishes a connection between this association
    * and the controlled object.  This implementation
	* attempts to add this class as an ActionListener
	* and Focus Listener to the specified object.
    */
    public void establishConnection() {
        Object component = object();
        try {
            if (addActionListener.implementedByObject(component)) {
                addActionListener.invoke(component, this);
            }
            if (addFocusListener.implementedByObject(component)) {
                addFocusListener.invoke(component, this);
            }
        } catch (Exception exc) {
            throw new WotonomyException("Error while establishing connection", exc);
        }
        super.establishConnection();
        subjectChanged();
    }

    /**
    * Breaks the connection between this association and
    * its object.  Override to stop listening for events
    * from the object.
    */
    public void breakConnection() {
        Object component = object();
        try {
            if (removeActionListener.implementedByObject(component)) {
                removeActionListener.invoke(component, this);
            }
            if (removeFocusListener.implementedByObject(component)) {
                removeFocusListener.invoke(component, this);
            }
        } catch (Exception exc) {
            throw new WotonomyException("Error while breaking connection", exc);
        }
        super.breakConnection();
    }

    /**
    * Called when either the selection or the contents
    * of an associated display group have changed.
    */
    public void subjectChanged() {
        Object component = object();
        EODisplayGroup displayGroup;
        String key;
        Object value;
        displayGroup = displayGroupForAspect(ValueAspect);
        if (displayGroup != null) {
            key = displayGroupKeyForAspect(ValueAspect);
            if (component instanceof Component) {
                ((Component) component).setEnabled(displayGroup.enabledToSetSelectedObjectValueForKey(key));
            }
            if (displayGroup.selectedObjects().size() > 1) {
                Object previousValue;
                Iterator indexIterator = displayGroup.selectionIndexes().iterator();
                int initialIndex = ((Integer) indexIterator.next()).intValue();
                previousValue = displayGroup.valueForObjectAtIndex(initialIndex, key);
                value = null;
                while (indexIterator.hasNext()) {
                    int index = ((Integer) indexIterator.next()).intValue();
                    Object currentValue = displayGroup.valueForObjectAtIndex(index, key);
                    if (currentValue != null && !currentValue.equals(previousValue)) {
                        value = null;
                        break;
                    } else {
                        value = currentValue;
                    }
                }
            } else {
                value = displayGroup.selectedObjectValueForKey(key);
            }
            try {
                Date dateValue = null;
                if (value instanceof Date) {
                    dateValue = (Date) value;
                }
                if ((dateValue == null) && (value instanceof Calendar)) {
                    dateValue = ((Calendar) value).getTime();
                }
                if (dateValue == null) {
                    nullValue = new Date();
                    dateValue = nullValue;
                } else {
                    nullValue = null;
                }
                if (!dateValue.equals(getDate.invoke(component))) {
                    setDate.invoke(component, dateValue);
                    needsUpdate = false;
                }
            } catch (Exception exc) {
                throw new WotonomyException("Error while updating component connection", exc);
            }
        }
        displayGroup = displayGroupForAspect(EnabledAspect);
        key = displayGroupKeyForAspect(EnabledAspect);
        if (((displayGroup != null) || (key != null)) && (component instanceof Component)) {
            if (displayGroup != null) {
                value = displayGroup.selectedObjectValueForKey(key);
            } else {
                value = key;
            }
            Boolean converted = null;
            if (value != null) {
                converted = (Boolean) ValueConverter.convertObjectToClass(value, Boolean.class);
            }
            if (converted == null) converted = Boolean.FALSE;
            if (converted.booleanValue() != ((Component) component).isEnabled()) {
                ((Component) component).setEnabled(converted.booleanValue());
            }
        }
        displayGroup = displayGroupForAspect(EditableAspect);
        key = displayGroupKeyForAspect(EditableAspect);
        if (((displayGroup != null) || (key != null)) && (setEditable.implementedByObject(component))) {
            try {
                if (displayGroup != null) {
                    value = displayGroup.selectedObjectValueForKey(key);
                } else {
                    value = key;
                }
                Boolean converted = (Boolean) ValueConverter.convertObjectToClass(value, Boolean.class);
                if (converted != null) {
                    setEditable.invoke(component, converted);
                }
            } catch (Exception exc) {
                throw new WotonomyException("Error while updating component connection (editable aspect)", exc);
            }
        }
    }

    /**
    * Forces this association to cause the object to
    * stop editing and validate the user's input.
    * @return false if there were problems validating,
    * or true to continue.
    */
    public boolean endEditing() {
        return writeValueToDisplayGroup();
    }

    /**
	* Writes the value currently in the component
	* to the selected object in the display group
	* bound to the value aspect.
        * @return false if there were problems validating,
        * or true to continue.
	*/
    protected boolean writeValueToDisplayGroup() {
        if (!needsUpdate) return true;
        EODisplayGroup displayGroup = displayGroupForAspect(ValueAspect);
        if (displayGroup != null) {
            String key = displayGroupKeyForAspect(ValueAspect);
            Object component = object();
            Object value = null;
            try {
                if (getDate.implementedByObject(component)) {
                    value = getDate.invoke(component);
                }
                if (nullValue != null) {
                    if (nullValue.equals(value)) {
                        value = null;
                    }
                }
            } catch (Exception exc) {
                throw new WotonomyException("Error updating display group", exc);
            }
            needsUpdate = false;
            boolean returnValue = true;
            Iterator selectedIterator = displayGroup.selectionIndexes().iterator();
            while (selectedIterator.hasNext()) {
                int index = ((Integer) selectedIterator.next()).intValue();
                if (!displayGroup.setValueForObjectAtIndex(value, index, key)) {
                    returnValue = false;
                }
            }
            return returnValue;
        }
        return false;
    }

    /**
	* Updates object on action performed.
	*/
    public void actionPerformed(ActionEvent evt) {
        needsUpdate = true;
        writeValueToDisplayGroup();
    }

    /**
	* Notifies of beginning of edit.
	*/
    public void focusGained(FocusEvent evt) {
        Object o;
        EODisplayGroup displayGroup;
        Enumeration e = aspects().objectEnumerator();
        while (e.hasMoreElements()) {
            displayGroup = displayGroupForAspect(e.nextElement().toString());
            if (displayGroup != null) {
                displayGroup.associationDidBeginEditing(this);
            }
        }
    }

    /**
	* Updates object on focus lost and notifies of end of edit.
	*/
    public void focusLost(FocusEvent evt) {
        if (endEditing()) {
            Object o;
            EODisplayGroup displayGroup;
            Enumeration e = aspects().objectEnumerator();
            while (e.hasMoreElements()) {
                displayGroup = displayGroupForAspect(e.nextElement().toString());
                if (displayGroup != null) {
                    displayGroup.associationDidEndEditing(this);
                }
            }
        } else {
        }
    }
}
