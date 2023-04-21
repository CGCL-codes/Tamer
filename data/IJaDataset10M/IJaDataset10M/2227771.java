package com.volantis.mcs.layouts;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.ObjectHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * A format that contains a Form.
 *
 * @mock.generate base="Format"
 */
public class Form extends Format {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(Form.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory.createExceptionLocalizer(Form.class);

    private static String[] userAttributes = new String[] { FormatConstants.NAME_ATTRIBUTE };

    private static String[] defaultAttributes = new String[] {};

    private static String[] persistentAttributes = new String[] { FormatConstants.NAME_ATTRIBUTE };

    protected Pane defaultPane;

    protected List formFragments;

    public Form(CanvasLayout canvasLayout) {
        super(1, canvasLayout);
        formFragments = new ArrayList();
    }

    public FormatType getFormatType() {
        return FormatType.FORM;
    }

    public String[] getUserAttributes() {
        return userAttributes;
    }

    public String[] getDefaultAttributes() {
        return defaultAttributes;
    }

    public String[] getPersistentAttributes() {
        return persistentAttributes;
    }

    public boolean visit(FormatVisitor visitor, Object object) throws FormatVisitorException {
        return visitor.visit(this, object);
    }

    /**
   * Add a fragment to the form
   * @param fragment The fragment to add
   * @todo later it should not be possible to add a FormFragment that is not
   * a decendent of this Form. This method should be fixed along with the
   * related child operations.
   */
    public void addFormFragment(FormFragment fragment) {
        if (logger.isDebugEnabled()) {
            logger.debug("Added form fragment " + fragment.getName());
        }
        formFragments.add(fragment);
    }

    /**
   * Get the name of the default form fragment for this form
   * @return The name of the default form fragment
   */
    public FormFragment getDefaultFormFragment() {
        if (isFragmented()) {
            FormFragment def = (FormFragment) formFragments.get(0);
            if (logger.isDebugEnabled()) {
                logger.debug("Default form fragment is " + def.getName());
            }
            return def;
        } else {
            return null;
        }
    }

    /**
   * Get the previous fragment to the current one
   * @param current The current form fragment
   * @return the name of the form fragment preceding the current one
   */
    public FormFragment getPreviousFormFragment(FormFragment current) {
        if (logger.isDebugEnabled()) {
            logger.debug("Getting previous form fragment to " + current.getName());
        }
        if (isFragmented() == false) return null;
        int idx = findFragment(current);
        if (idx == -1) return null;
        if (idx == 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("There is no previous form fragment.");
            }
            return null;
        }
        FormFragment frag = (FormFragment) formFragments.get(idx - 1);
        if (logger.isDebugEnabled()) {
            logger.debug("Previous form fragment is " + frag.getName());
        }
        return frag;
    }

    /**
   * Get the next fragment to the current one
   * @param current The current form fragment
   * @return the name of the form fragment following the current one
   */
    public FormFragment getNextFormFragment(FormFragment current) {
        if (logger.isDebugEnabled()) {
            logger.debug("Getting next form fragment from " + current.getName());
        }
        if (isFragmented() == false) return null;
        int idx = findFragment(current);
        if (idx == -1) return null;
        if (idx == formFragments.size() - 1) {
            if (logger.isDebugEnabled()) {
                logger.debug("There is no next form fragment.");
            }
            return null;
        }
        FormFragment frag = (FormFragment) formFragments.get(idx + 1);
        if (logger.isDebugEnabled()) {
            logger.debug("Next form fragment is " + frag.getName());
        }
        return frag;
    }

    /**
   * Search the list of fragments for a fragment and return its index.
   * @param fragment The fragment we are searching for
   * @return the index of the fragment or -1
   */
    private int findFragment(FormFragment fragment) {
        if (isFragmented() == false) return -1;
        String fragName = fragment.getName();
        for (int i = 0; i < formFragments.size(); i++) {
            FormFragment frag = (FormFragment) formFragments.get(i);
            if (frag.getName().equals(fragName)) {
                return i;
            }
        }
        return -1;
    }

    /**
   * Determine if a form is fragmented. If there is a default fragment name
   * then the form is fragmented
   */
    public boolean isFragmented() {
        if (formFragments.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
   * Set the name of the Form as an attribute
   * @param name The name of the Form
   */
    public void setName(String name) {
        setAttribute(FormatConstants.NAME_ATTRIBUTE, name);
    }

    /**
   * Retrieve the name of the Form
   * @return The name of the Form
   */
    public String getName() {
        return (String) getAttribute(FormatConstants.NAME_ATTRIBUTE);
    }

    /**
   * Override the SimpleAttributeContainer.setAttribute method to enable
   * special processing to be done.
   */
    public Object getAttribute(String name) {
        return super.getAttribute(name);
    }

    /**
   * Get the default pane for this form. This is the first pane that is
   * reached in a depth first traversal of the tree. This method is only
   * used at runtime.
   */
    public Pane getDefaultPane() {
        if (defaultPane == null) {
            FormatVisitor visitor = new FormatVisitorAdapter() {

                public boolean visit(ColumnIteratorPane pane, Object object) {
                    defaultPane = pane;
                    return true;
                }

                public boolean visit(DissectingPane pane, Object object) {
                    defaultPane = pane;
                    return true;
                }

                public boolean visit(Pane pane, Object object) {
                    defaultPane = pane;
                    return true;
                }

                public boolean visit(RowIteratorPane pane, Object object) {
                    defaultPane = pane;
                    return true;
                }
            };
            try {
                visitChildren(visitor, null);
            } catch (FormatVisitorException e) {
                throw new ExtendedRuntimeException(exceptionLocalizer.format("unexpected-exception"), e);
            }
        }
        return defaultPane;
    }

    public void validate(ValidationContext context) {
        validateRequiredName(context);
        if (isContainedBy(Form.class)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR, context.createMessage("form-must-not-be-in-form", this.getName()));
        }
        if (isContainedBy(SpatialFormatIterator.class)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR, context.createMessage("form-must-not-be-in-spatial", this.getName()));
        }
        if (isContainedBy(TemporalFormatIterator.class)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR, context.createMessage("form-must-not-be-in-temporal", this.getName()));
        }
        validateChildren(context);
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        if (!super.equals(other)) return false;
        final Form form = (Form) other;
        return ObjectHelper.equals(getDefaultPane(), form.getDefaultPane()) && ObjectHelper.equals(getDefaultFormFragment(), form.getDefaultFormFragment());
    }

    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode += ObjectHelper.hashCode(getDefaultPane());
        hashCode += ObjectHelper.hashCode(getDefaultFormFragment());
        return hashCode;
    }
}
