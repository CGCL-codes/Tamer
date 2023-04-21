package org.form4j.form.field.data;

import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.form4j.form.event.FieldChangeEvent;
import org.form4j.form.event.FieldChangeListener;
import org.form4j.form.exceptions.InvalidRelativeIndexException;
import org.form4j.form.field.AbstractField;
import org.form4j.form.field.Field;
import org.form4j.form.field.util.ModelTrigger;
import org.form4j.form.main.Form;
import org.form4j.form.mapper.Mapper;
import org.form4j.form.mapper.MappingEnumeration;
import org.form4j.form.model.ModelConditionEvent;
import org.form4j.form.model.ModelConditionListener;
import org.form4j.form.model.ModelItemChangeEvent;
import org.form4j.form.model.ModelItemChangeListener;
import org.form4j.form.util.PropertyUtil;
import org.form4j.form.util.bsf.BSFManagerFactory;
import org.form4j.form.util.xml.XMLPrintHelper;
import org.w3c.dom.Element;

/**
 * Common functionality for data form fields. <br/>
 * FEATURE: visualisation of field states unsatifsfactory: empty/nonempty,
 * focused/unfocused, error, mandatory/optional
 * 
 * @author $Author: cjuon $
 * @version 0.2 $Revision: 1.50 $ $Date: 2010/04/26 04:13:25 $
 **/
public abstract class AbstractDataField extends AbstractField implements DataField, ModelItemChangeListener, KeyListener {

    static final RE debugFieldsRegexp = null;

    /**
	 * Constructs abstract data field control.
	 * 
	 * @param form
	 *            the parent form4j
	 * @param desc
	 *            the XML field descriptor
	 * @throws Exception
	 *             Exception
	 */
    public AbstractDataField(final Form form, final Element desc) throws Exception {
        super(form, desc);
        hasScripts = desc.getElementsByTagName("script").getLength() > 0;
    }

    /**
	 * set the data for this field.
	 * 
	 * @param data
	 *            the data for this field
	 */
    public abstract void setData(final Object data);

    /**
	 * get the data for this field.
	 * 
	 * @return the data for this field
	 */
    public abstract Object getData();

    /**
	 * flag to signal that this very data field is under ongoing changes. and
	 * should not recursively trigger more model changes.
	 * 
	 * @return true if this very data field is under ongoing changes
	 */
    public synchronized boolean isItemChanging() {
        return itemChanging;
    }

    /**
	 * true if the model should trigger only on focus lost.
	 * 
	 * @return true if the model should trigger only on focus lost
	 **/
    public boolean isTriggerOnFocus() {
        return triggerMode == ModelTrigger.ON_FOCUS;
    }

    /**
	 * true if the model should trigger on item change.
	 * 
	 * @return true, if triggering is fired on each item change
	 **/
    public boolean isTriggerOnItem() {
        return triggerMode == ModelTrigger.ON_ITEM;
    }

    /**
	 * Empty Key Listener method.
	 * 
	 * @param arg0
	 *            the key event
	 */
    public void keyPressed(final KeyEvent arg0) {
    }

    /**
	 * Fire dirty when the key was released.
	 * 
	 * @param arg0
	 *            the key event
	 */
    public void keyReleased(final KeyEvent arg0) {
        getForm().fireDirty(arg0.getSource(), true);
        if (isEnterIsTab() && arg0.getKeyCode() == KeyEvent.VK_ENTER) {
            getFocusComponent().transferFocus();
        }
    }

    /**
	 * Empty Key Listener method.
	 * 
	 * @param arg0
	 *            the key event
	 */
    public void keyTyped(final KeyEvent arg0) {
    }

    /**
	 * Respond to change items for specific (xpath referenced) nodes in the
	 * document.
	 * 
	 * @param evt
	 *            the event that has occured
	 */
    public void itemChange(final ModelItemChangeEvent evt) {
        debug(String.valueOf(evt));
        if (evt.getSource() != getComponent()) {
            debug("x.0 key=" + getKey() + " name=" + getName());
            synchronized (this) {
                debug("x.1");
                itemChanging = true;
                try {
                    if (LOG.isDebugEnabled()) LOG.debug("xpath=" + getFieldDescriptor().getAttribute("key") + " getData '" + getData() + "', newValue '" + evt.getNewValue() + "' DIFFERENT=" + differentValue(getData(), evt.getNewValue()));
                    if (differentValue(getData(), evt.getNewValue())) {
                        debug("HANDLE ITEM CHANGE(" + getFieldDescriptor().getAttribute("constraints") + ")   " + evt);
                        setData((String) evt.getNewValue());
                    } else LOG.debug("xpath=" + getFieldDescriptor().getAttribute("key") + " Identity Change IGNORED");
                } catch (Exception e) {
                    LOG.error(e, e);
                }
                itemChanging = false;
            }
        }
        debug("DONE");
    }

    private boolean differentValue(Object oldValue, Object newValue) {
        if (oldValue == null && newValue != null) return true;
        if (newValue == null && oldValue != null) return true;
        if (oldValue == null && newValue == null) return false;
        return !oldValue.equals(newValue);
    }

    /**
	 * get the xpath associated with this field.
	 * 
	 * @return the xpath associated with this field
	 */
    public String getXPath() {
        String relRoot = getFieldDescriptor().getAttribute("relativeRoot");
        if (relRoot.equals("")) return getFieldDescriptor().getAttribute("key"); else return relRoot + "[" + getFieldDescriptor().getAttribute("relativeIndex") + "]/" + getFieldDescriptor().getAttribute("key");
    }

    /**
	 * get the relative xpath associated with this item listener.
	 * 
	 * @return the relative xpath associated with this item listener
	 */
    public String getKey() {
        return getFieldDescriptor().getAttribute("key");
    }

    /**
	 * get the class of data associated with this item listener.
	 * 
	 * @return the class of data associated with this item listener
	 */
    public Class getDataClass() {
        return String.class;
    }

    /**
	 * controls behavior, when target (text) node is missing from the model.
	 * <ul>
	 * <li><b><code>whenEmpty='fail'</code></b> the field creation fails</li>
	 * <li><b><code>whenEmpty='useDefault'</code></b> the <code>default='...'</code> is used and the missing
	 * node is created</li>
	 * <li><b><code>whenEmpty='leaveEmpty'</code></b> empty is an acceptable state for this field.
	 * No missing node is created</li>
	 * </ul>
	 * 
	 * @param whenEmptyBehaviour
	 *            the desired empty behavior
	 */
    public void setWhenEmpty(final String whenEmptyBehaviour) {
        this.whenEmpty = whenEmptyBehaviour;
        if (whenEmpty.equals("fail")) this.whenEmptyCode = FAIL_WHEN_EMPTY; else if (whenEmpty.equals("useDefault")) this.whenEmptyCode = USE_DEFAULT_WHEN_EMPTY; else if (whenEmpty.equals("leaveEmpty")) this.whenEmptyCode = LEAVE_EMPTY_WHEN_EMPTY; else if (whenEmpty.equals("useFirst")) this.whenEmptyCode = USE_FIRST_WHEN_EMPTY; else this.whenEmptyCode = LEAVE_EMPTY_WHEN_EMPTY;
    }

    /**
	 * returns the code for the desired behavior for missing model nodes.
	 * 
	 * @return FAIL_WHEN_EMPTY,USE_DEFAULT_WHEN_EMPTY,LEAVE_EMPTY_WHEN_EMPTY
	 */
    public int getWhenEmpty() {
        return whenEmptyCode;
    }

    /**
	 * Set the visualsation of the component to empty state. Empty means, no
	 * data from the model is associated with this componentent. Entering data
	 * will probably make this component non-empty.
	 * 
	 * @param empty
	 *            the components desired empty state
	 */
    public void setEmpty(final boolean empty) {
    }

    /**
	 * map incoming data according to empty-behavior. Either
	 * FAIL_WHEN_EMPTY,USE_DEFAULT_WHEN_EMPTY,LEAVE_EMPTY_WHEN_EMPTY set by
	 * setWhenEmpty resp. the attribute <code>whenEmpty</code>
	 * 
	 * @param data
	 *            the data to check on empty condition
	 * @return the mapped data
	 */
    public String handleEmpty(final Object data) {
        setEmpty(false);
        String value = "";
        try {
            value = data.toString();
        } catch (Exception e) {
            LOG.debug(e);
        }
        if (value.replace('\n', ' ').trim().equals("")) switch(getWhenEmpty()) {
            case FAIL_WHEN_EMPTY:
                break;
            case USE_DEFAULT_WHEN_EMPTY:
                value = getFieldDescriptor().getAttribute("default");
                try {
                    getForm().getModel().setData(getComponent(), this.getXPath(), value);
                } catch (Exception e) {
                    LOG.error(e);
                }
                break;
            case LEAVE_EMPTY_WHEN_EMPTY:
                setEmpty(true);
                break;
            case USE_FIRST_WHEN_EMPTY:
                setEmpty(true);
                break;
            default:
                LOG.error("cannot handle empty condition '" + getWhenEmpty() + "'");
                break;
        }
        return value;
    }

    /**
	 * Associate a focus listener with the field.
	 * 
	 * @param listener
	 *            the listener to associate with the field
	 */
    public void addFocusListener(final FocusListener listener) {
        getComponent().addFocusListener(listener);
    }

    /**
	 * Remove some associated focus listener.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
    public void removeFocusListener(final FocusListener listener) {
        getComponent().removeFocusListener(listener);
    }

    /**
	 * complete standard initialisation of actions.
	 * 
	 * @throws Exception
	 *             Exception
	 */
    public void init() throws Exception {
        PropertyUtil.setInheritedAttributes(this, getFieldDescriptor(), new String[] { "whenEmpty" }, new Class[] { String.class }, new String[] { "leaveEmpty" });
        try {
            boolean leaveEmpty = (getWhenEmpty() == LEAVE_EMPTY_WHEN_EMPTY || getWhenEmpty() == FAIL_WHEN_EMPTY);
            Object data = getForm().getModel().getData(getXPath(), leaveEmpty);
            if (data == null && (getWhenEmpty() == FAIL_WHEN_EMPTY)) throw new IllegalArgumentException();
            setData(data);
        } catch (Exception e) {
            LOG.error("cannot get data from model with key '" + getXPath() + "'");
            throw e;
        }
        getForm().getModel().addItemChangeListener(this);
        if (hasEnabledWhenCondition(getFieldDescriptor())) {
            setupEnabledWhenCondition(getFieldDescriptor());
        }
        if (getFieldDescriptor().getAttribute("changeListener").trim().length() > 0) try {
            LOG.debug("assigning FieldChangeListener " + getFieldDescriptor().getAttribute("changeListener"));
            FieldChangeListener fieldChangeListener = (FieldChangeListener) Class.forName(getFieldDescriptor().getAttribute("changeListener")).newInstance();
            fieldChangeListeners.addElement(fieldChangeListener);
        } catch (Exception e) {
            LOG.error("Cannot assign FieldChangeListener '" + getFieldDescriptor().getAttribute("changeListener") + "': " + e.toString());
        }
        triggerMode = ModelTrigger.getTriggerMode(getFieldDescriptor());
        super.init();
    }

    /**
	 * set field to default content (e.g. content of attribute 'default' in
	 * field descriptor).
	 */
    public void setToDefault() {
        if (getComponent().isEnabled()) setData(getFieldDescriptor().getAttribute("default"));
    }

    /**
	 * apply the 'before' mappings/scripts on the model.
	 */
    public void processBeforeOps() {
        applyMappings(Mapper.BEFORE);
        if (hasScripts) BSFManagerFactory.runScripts(BSFManagerFactory.BEFORE, getFieldDescriptor(), getForm(), this);
    }

    /**
	 * apply the 'after' mappings/scripts on the model.
	 */
    public void processAfterOps() {
        applyMappings(Mapper.AFTER);
        if (hasScripts) BSFManagerFactory.runScripts(BSFManagerFactory.AFTER, getFieldDescriptor(), getForm(), this);
    }

    private void applyMappings(final int beforeAfterMode) {
        try {
            {
                MappingEnumeration mappings = new MappingEnumeration(getFieldDescriptor());
                for (; mappings.hasMoreElements(); ) {
                    Element mapping = (Element) mappings.nextElement();
                    if (LOG.isDebugEnabled()) LOG.debug("MAPPING " + XMLPrintHelper.xmlDocumentToString(mapping));
                    applyMappings(mapping, beforeAfterMode);
                }
            }
        } catch (InvalidRelativeIndexException e) {
            LOG.warn(e);
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }

    private void applyMappings(Element mappings, final int beforeAfterMode) throws Exception {
        if (mappings != null) {
            LOG.debug("APPL MAPPINGS " + beforeAfterMode);
            new Mapper(mappings, getForm()).map(getForm().getModel(), beforeAfterMode);
            if (mappings.getAttribute("nextFocus").trim().length() > 0) {
                String focusField = mappings.getAttribute("nextFocus");
                Field fld = getForm().getFieldByName(focusField);
                if (fld != null) fld.getFocusComponent().requestFocus(); else LOG.warn("Cannot focus on field nextFocus='" + focusField + "' in Mappings");
            }
        }
    }

    /**
	 * fire all associated field change listeners.
	 * 
	 * @param field
	 *            the field
	 * @param key
	 *            the xpath key for this field
	 * @param data
	 *            the new data
	 */
    public void fireFieldChangeListeners(final Field field, final String key, final Object data) {
        if (fieldChangeListeners.size() > 0) try {
            FieldChangeEvent evt = new FieldChangeEvent(field.getComponent(), "data", getForm().getModel().getData(key), data);
            LOG.debug(evt);
            for (int i = 0; i < fieldChangeListeners.size(); i++) ((FieldChangeListener) fieldChangeListeners.elementAt(i)).itemChange(evt);
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    protected boolean hasEnabledWhenCondition(final Element desc) throws Exception {
        return PropertyUtil.hasEnabledWhenCondition(desc);
    }

    private void setupEnabledWhenCondition(final Element desc) {
        try {
            enabledWhenConditionName = PropertyUtil.inheritAttribute(desc, "enabledWhen");
            enabledWhenConditionXPathPredicate = PropertyUtil.createConditionXPathPredicate(getXPathAPI(), desc, enabledWhenConditionName);
            getForm().getModel().addModelConditionListener(new ModelConditionListener() {

                public void conditionChange(final ModelConditionEvent evt) {
                    Boolean value = (Boolean) evt.getNewValue();
                    getComponent().setEnabled(value.booleanValue());
                }

                public String getConditionName() {
                    return enabledWhenConditionName;
                }

                public String getConditionXPathPredicate() {
                    return enabledWhenConditionXPathPredicate;
                }
            });
        } catch (Exception e) {
            LOG.error("Cannot setup 'visibleWhen' condition: " + desc + "\n" + e.toString());
        }
        LOG.debug("name '" + enabledWhenConditionName + "' xpath '" + enabledWhenConditionXPathPredicate + "'");
    }

    private void debug(String msg) {
        debug(msg, null);
    }

    private void debug(String msg, Throwable e) {
        if (debugFieldsRegexp != null) {
            if (debugFieldsRegexp.match(getName())) {
                if (e != null) LOG.fatal(getName() + ": " + msg, e); else LOG.fatal(getName() + ": " + msg);
            }
        } else {
            if (e != null) LOG.debug(getName() + ": " + msg, e); else LOG.debug(getName() + ": " + msg);
        }
    }

    /** Constant for getWhenEmpty(): FAIL field creation on empty condition. **/
    public static final int FAIL_WHEN_EMPTY = 0;

    /** Constant for getWhenEmpty(): use default value on empty condition. **/
    public static final int USE_DEFAULT_WHEN_EMPTY = 1;

    /** Constant for getWhenEmpty(): leave field data empty on empty condition. **/
    public static final int LEAVE_EMPTY_WHEN_EMPTY = 2;

    /**
	 * Constant for getWhenEmpty(): use first enum item (ChoiceField and
	 * Spinner).
	 **/
    public static final int USE_FIRST_WHEN_EMPTY = 3;

    private String enabledWhenConditionName = null;

    private String enabledWhenConditionXPathPredicate = null;

    private String whenEmpty = "leaveEmpty";

    private int whenEmptyCode = FAIL_WHEN_EMPTY;

    private Vector fieldChangeListeners = new Vector();

    private int triggerMode = ModelTrigger.ON_FOCUS;

    private boolean hasScripts = false;

    private boolean itemChanging = false;

    private static final Logger LOG = Logger.getLogger(AbstractDataField.class.getName());
}
