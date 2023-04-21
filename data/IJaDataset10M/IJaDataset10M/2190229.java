package org.scopemvc.view.swing;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Control;
import org.scopemvc.core.PropertyView;
import org.scopemvc.core.Selector;
import org.scopemvc.view.util.ModelBindable;

/**
 * <P>
 *
 * An SAction that is bound to a property and performs a test on the value of
 * the property to determine its active state. </P> <P>
 *
 * A comparable object is used to perform the test on the view value. This
 * action is active when the comparable object returns a value greater than 0
 * when passed the view value in its {@link Comparable#compareTo compareTo()}
 * method.</P> <P>
 *
 * Note: it is convenient to use the Comparable interface to perform tests
 * because it is already implemented in many places (natural ordering). For
 * example, to have this SModelAction enabled when the view value is an Integer
 * less than 1, then do: <br>
 * <code>setValueTest(new Integer(1))</code> <br>
 * because <code>new Integer(1).compareTo(value)</code> will return 1 if value
 * is an Integer less than 1</P> <P>
 *
 * If the comparison fails because of an exception coming from the Comparable
 * test, then this action is disabled. </P>
 *
 * @author <a href="mailto:ludovicc@users.sourceforge.net>Ludovic Claude</a>
 * @version $Revision: 1.7 $ $Date: 2003/01/17 16:34:00 $
 * @created 18 June 2002
 */
public class SModelAction extends SAction implements ModelBindable, PropertyView, Refreshable {

    private static final Log LOG = LogFactory.getLog(SModelAction.class);

    /**
     * Helper to manage model to view binding.
     */
    private SwingBoundModel boundModel = new SwingBoundModel(this);

    /**
     * The model object that this component presents, which may be a property of
     * the bound model if a Selector is specified.
     */
    private Object shownModel;

    private Comparable valueTest;

    /**
     * Constructor for the ModelAction object. <BR>
     * It defines a test that will activate the action if the view value is not
     * null, and if the view value is boolean then the action is activated only
     * if the view value is true.
     *
     * @param inControlID The control ID to be issued by this Action
     */
    public SModelAction(String inControlID) {
        super(inControlID);
        setValueTest(new NotNullComparable());
    }

    /**
     * Constructor for the SModelAction object. <BR>
     * It defines a test that will activate the action if the view value is not
     * null, and if the view value is boolean then the action is activated only
     * if the view value is true.
     *
     * @param inControlID The control ID to be issued by this Action
     * @param inView The view owning this Action; its bound controller will
     *      receive the Controls issued by this Action.
     * @param inSelector The selector for the property
     */
    public SModelAction(String inControlID, SwingView inView, Selector inSelector) {
        this(inControlID, inView, inSelector, new NotNullComparable());
    }

    /**
     * Constructor for the SModelAction object.
     *
     * @param inControlID The control ID to be issued by this Action
     * @param inView The view owning this Action; its bound controller will
     *      receive the Controls issued by this Action.
     * @param inSelector The selector for the property
     * @param inValueTest The test for the enabled state. If the compareTo()
     *      method returns a value greater than 0, then this action is active.
     */
    public SModelAction(String inControlID, SwingView inView, Selector inSelector, Comparable inValueTest) {
        super(inControlID, inView);
        setSelector(inSelector);
        setValueTest(inValueTest);
    }

    /**
     * Gets the bound model
     *
     * @return The boundModel value
     */
    public final Object getBoundModel() {
        return boundModel.getBoundModel();
    }

    /**
     * Gets the Selector used to identify the property that this component will
     * be bound to. <br>
     * The component will use this property to determine the enabled state of
     * the action.
     *
     * @return A selector.
     */
    public final Selector getSelector() {
        return boundModel.getSelector();
    }

    /**
     * Get the current value (what would be set as a property of the bound model
     * object) being presented on the View.
     *
     * @return property's value from the UI.
     */
    public final Object getViewValue() {
        return shownModel;
    }

    /**
     * Returns the Comparable used to test the view value.
     *
     * @return The valueTest value
     */
    public Comparable getValueTest() {
        return valueTest;
    }

    /**
     * Sets the Selector used to identify the property that this component will
     * be bound to. <br>
     * This component will present this property to the user.
     *
     * @param inSelector The new selector to use
     */
    public final void setSelector(Selector inSelector) {
        boundModel.setSelector(inSelector);
    }

    /**
     * Sets the Selector used to identify the property that this component will
     * be bound to. <br>
     * This component will present this property to the user.
     *
     * @param inSelectorString The string representation of the selector
     * @see Selector#fromString
     */
    public void setSelector(String inSelectorString) {
        boundModel.setSelector(inSelectorString);
    }

    /**
     * Sets the Selector used to identify the property that this component will
     * be bound to. <br>
     * This component will present this property to the user.
     *
     * @param inSelectorString The string representation of the selector
     * @deprecated Use setSelector(String) instead
     */
    public void setSelectorString(String inSelectorString) {
        setSelector(inSelectorString);
    }

    /**
     * Sets the Comparable used to test the view value. <BR>
     * This action is enabled when the compareTo() method of the test returns a
     * value greater than 0 when the passed value is the model value bound to
     * this SModelAction.
     *
     * @param inValueTest The new valueTest value
     */
    public void setValueTest(Comparable inValueTest) {
        valueTest = inValueTest;
        updateEnabledState();
    }

    /**
     * Sets the bound model
     *
     * @param inModel The new boundModel value
     */
    public void setBoundModel(Object inModel) {
        boundModel.setBoundModel(inModel);
    }

    /**
     * Use the passed property value and read-only state to update the View.
     * <BR>
     * Ignores inReadOnly.
     *
     * @param inValue The new value of the property in the bound model
     * @param inReadOnly The new read-only state of the property
     */
    public void updateFromProperty(Object inValue, boolean inReadOnly) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateFromProperty: " + inValue + ", " + inReadOnly);
        }
        setShownModel(inValue);
    }

    /**
     * Updates the component with the current state of the bound model.
     */
    public void refresh() {
        Object propertyValue = boundModel.getPropertyValue();
        boolean propertyReadOnly = boundModel.getPropertyReadOnly();
        updateFromProperty(propertyValue, propertyReadOnly);
    }

    /**
     * Return a string representation of this object
     *
     * @return a string representation of this object
     */
    public String toString() {
        return new ToStringBuilder(this).append("selector", Selector.asString(getSelector())).append("controlID", getControlID()).append("view", getOwner()).toString();
    }

    /**
     * Now overwrite the firing of the control to include the additional
     * information of the model
     *
     * @return The Control to fire
     */
    protected Control createControl() {
        Control returnValue = super.createControl();
        if (LOG.isDebugEnabled()) {
            LOG.debug("createControl: Creating the control " + returnValue + "with value " + this.shownModel);
        }
        returnValue.setParameter(this.shownModel);
        return returnValue;
    }

    /**
     * Called internally from updateFromProperty(). Issues a
     * CHANGE_MODEL_CONTROL_ID Control to notify parent Controller of the
     * change.
     *
     * @param inModel The new shownModel value
     */
    private void setShownModel(Object inModel) {
        if (shownModel == inModel) {
            return;
        }
        shownModel = inModel;
        updateEnabledState();
    }

    private void updateEnabledState() {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("updateEnabledState: Testing " + shownModel + " with test " + valueTest + ", result: " + valueTest.compareTo(shownModel));
            }
            this.setEnabled(valueTest.compareTo(shownModel) > 0);
        } catch (NullPointerException ex) {
            LOG.info("NPE when testing the view value with the valueTest. Assuming that this action: " + this.getName() + " is disabled");
            setEnabled(false);
        } catch (Exception ex) {
            LOG.warn("Could not test the view value with the valueTest", ex);
            setEnabled(false);
        }
    }

    /**
     * Test that returns 1 if the value is not null.<br>
     * If the value is a Boolean, it returns 1 if the vaue is Boolean.TRUE
     *
     * @author lclaude
     * @version $Revision: 1.7 $
     * @created 12 September 2002
     */
    public static class NotNullComparable implements Comparable {

        /**
         * Test if the object is not null
         *
         * @param inOther The object to test
         * @return 1 if the object is not null, 0 otherwise
         */
        public int compareTo(Object inOther) {
            if (inOther == null) {
                return 0;
            }
            if (inOther instanceof Boolean) {
                return ((Boolean) inOther).booleanValue() ? 1 : 0;
            }
            return 1;
        }

        /**
         * Returns a string representation
         *
         * @return a string representation
         */
        public String toString() {
            return "NotNullComparable";
        }
    }
}
