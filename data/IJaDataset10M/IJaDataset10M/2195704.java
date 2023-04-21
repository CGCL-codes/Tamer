package com.volantis.mcs.eclipse.ab.actions.layout;

import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;
import java.util.List;
import org.jdom.Element;

/**
 * This is the action used for each New format action within the New menu that
 * creates a non-grid format, based on a given prototypical element. It is
 * appropriate to the Layout Outline page and the Layout Graphical Editor page.
 * It is enabled when the creation of the given format type is appropriate to
 * the currently selected element.
 */
public class NewNonGridFormatActionCommand extends FormatTypeBasedActionCommand {

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param formatType       identifies the type of grid that this action
     *                         command will create. Must not be null and must
     *                         have a non-grid structure
     * @param selectionManager selection manager that allows the
     *                         action to set the new format as the current
     *                         selection. Can not be null.
     */
    public NewNonGridFormatActionCommand(FormatType formatType, ODOMSelectionManager selectionManager) {
        super(formatType, selectionManager);
    }

    protected void checkFormatType(FormatType formatType) {
        if (formatType.getStructure() == FormatType.Structure.GRID) {
            throw new IllegalArgumentException("The format type must be a non-grid structure " + "(was given " + formatType.getTypeName() + ")");
        }
    }

    /**
     * This action command can only be performed when a single empty format is
     * selected and the format type can reasonably be created at the selected
     * point in the document.
     */
    public boolean enable(ODOMActionDetails details) {
        return ((details.getNumberOfElements() == 1) && selectionIsAppropriate(details.getElement(0)) && canReplace(details.getElement(0)));
    }

    /**
     * Supporting method that is used to make sure that the selection is
     * appropriate for this action command.
     *
     * @param selection the single selected element
     * @return true if the element is of an appropriate type for this command
     */
    protected boolean selectionIsAppropriate(Element selection) {
        return selection.getName().equals(FormatType.EMPTY.getElementName());
    }

    /**
     * The selected empty format is replaced by a prototype for the format
     * type.
     */
    public void run(ODOMActionDetails details) {
        if (details.getNumberOfElements() != 1) {
            throw new IllegalStateException("Only a single selection should be available " + "when the action is run (" + details.getNumberOfElements() + " elements selected)");
        } else {
            Element e = replace(details.getElement(0));
            setSelection(e);
        }
    }

    /**
     * Determines whether the given element can be replaced by the required
     * type of format without causing layout constraints to be violated.
     *
     * @param element the empty format element that the action wants to replace
     * @return true if the given element can be replaced without causing
     *         constraints to be violated
     */
    protected boolean canReplace(ODOMElement element) {
        Element e = ActionSupport.cloneContainingDeviceLayout(element);
        e = replace(e);
        return !LayoutConstraintsProvider.constraints.violated(e, null);
    }

    /**
     * The specified format is replaced by a prototypical instance of the
     * required type of format.
     *
     * @param element the format to be replaced
     * @return the new (top-level) element representing the required format
     *         type
     */
    protected Element replace(Element element) {
        Element replacement = null;
        if (!selectionIsAppropriate(element)) {
            throw new IllegalArgumentException("The action can not replace a " + element.getName());
        } else {
            replacement = FormatPrototype.get(formatType);
            this.replaceElement(element, replacement);
        }
        return replacement;
    }
}
