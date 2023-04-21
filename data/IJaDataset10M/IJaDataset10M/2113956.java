package nl.alterra.openmi.sdk.backbone;

import org.openmi.standard.*;
import java.io.Serializable;
import nl.alterra.openmi.sdk.configuration.LinkManager;
import nl.alterra.openmi.sdk.extensions.ILinkEx;

/**
 * The Link is used to describe the data transfer between linkable components.
 * you cannot modify a link. Create new instance instead.
 */
public class Link extends BackboneObject implements ILinkEx, Serializable {

    private static final long serialVersionUID = 1L;

    private LinkManager owner = null;

    private DataOperations dataOperations;

    private ILinkableComponent sourceComponent;

    private IOutputExchangeItem sourceExchangeItem;

    private ILinkableComponent targetComponent;

    private IInputExchangeItem targetExchangeItem;

    /**
     * Enumeration of Link Validation Status.
     */
    public enum ValidationStatus {

        Ok("Ok"), NullReferences("Missing component(s) and/or exchange item(s)."), SourceItemNotPartOfSourceComponent("Output echange item not part of source linkable component."), TargetItemNotPartOfTargetComponent("Input exchange item not part of target linkable component."), MismatchingDataTypes("Source and target data types do not match.");

        private String message;

        /**
         * Creates an instance with the specified text. This text typically is
         * a message to be passed in exceptions.
         *
         * @param message The text to associate with the enum value
         */
        private ValidationStatus(String message) {
            this.message = message;
        }

        /**
         * Gets the message for the enum value.
         *
         * @return String The message
         */
        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    /**
     * Verifies wether a link between a given source and target can be constructed.
     *
     * @param source     ILinkableComponent
     * @param sourceItem IOutputExchangeItem
     * @param target     ILinkableComponent
     * @param targetItem IInputExchangeItem
     * @return True when the link is possible, false otherwise
     */
    public static ValidationStatus canConnect(ILinkableComponent source, IOutputExchangeItem sourceItem, ILinkableComponent target, IInputExchangeItem targetItem) {
        if ((sourceItem == null) || (source == null) || (targetItem == null) || (target == null)) {
            return ValidationStatus.NullReferences;
        }
        boolean ok = false;
        for (int i = 0; i < source.getOutputExchangeItemCount(); i++) {
            if (source.getOutputExchangeItem(i) == sourceItem) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            return ValidationStatus.SourceItemNotPartOfSourceComponent;
        }
        ok = false;
        for (int i = 0; i < target.getInputExchangeItemCount(); i++) {
            if (target.getInputExchangeItem(i) == targetItem) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            return ValidationStatus.TargetItemNotPartOfTargetComponent;
        }
        IDataType sourceDataType = sourceItem.getDataType();
        IDataType targetDataType = targetItem.getDataType();
        if ((sourceDataType == null) || (targetDataType == null)) {
            if (sourceDataType != targetDataType) return ValidationStatus.MismatchingDataTypes;
        } else {
            if (!sourceDataType.equals(targetDataType)) return ValidationStatus.MismatchingDataTypes;
        }
        return ValidationStatus.Ok;
    }

    /**
     * Creates an instance, with the given ID and controlled by the specified
     * LinkManager. The LinkManager assures the consistency and validate state
     * of all the links under its control. When null is passed for the manager
     * checking has to be done elsewhere.
     *
     * @param manager LinkManager the created link belongs to
     * @param ID      String with ID for the new link
     */
    public Link(LinkManager manager, String ID) {
        super(ID);
        dataOperations = new DataOperations();
        owner = manager;
        if (owner != null) {
            owner.addLink(this);
        }
    }

    /**
     * Connects the link to the specified source and target, only if such a
     * connection is possible (checked by canConnect()).
     *
     * @param source     ILinkableComponent
     * @param sourceItem IOutputExchangeItem
     * @param target     ILinkableComponent
     * @param targetItem IInputExchangeItem
     * @return True if the connection was made with the link
     */
    public boolean connect(ILinkableComponent source, IOutputExchangeItem sourceItem, ILinkableComponent target, IInputExchangeItem targetItem) {
        if (canConnect(source, sourceItem, target, targetItem) != ValidationStatus.Ok) {
            return false;
        }
        reset();
        if (owner != null) {
            owner.removeLinkToTarget(targetItem);
        }
        sourceComponent = source;
        sourceExchangeItem = sourceItem;
        targetComponent = target;
        targetExchangeItem = targetItem;
        sourceComponent.addLink(this);
        targetComponent.addLink(this);
        return true;
    }

    /**
     * Resets the link to an "unconnected" state. When it was connected before
     * the source and target linkable components will be requested to remove
     * their references to the link, so that it can be deleted properly. Or
     * used to connect other linkable components.
     */
    public void reset() {
        if (sourceComponent != null) {
            sourceComponent.removeLink(getID());
        }
        if (targetComponent != null) {
            targetComponent.removeLink(getID());
        }
        sourceComponent = null;
        sourceExchangeItem = null;
        targetComponent = null;
        targetExchangeItem = null;
    }

    /**
     * Returns true if the link is connected to a source and a target.
     *
     * @return True if the link is connected, false otherwise
     */
    public boolean isConnected() {
        return ((sourceExchangeItem != null) && (targetExchangeItem != null));
    }

    /**
     * Gets the source component.
     *
     * @return Returns the ILinkableComponent
     */
    public ILinkableComponent getSourceComponent() {
        return sourceComponent;
    }

    /**
     * Gets the source exchange item the link is connected to.
     * <p/>
     * Since OpenMI is a pull-based architecture the link's source is an
     * output exchange item (of a linkable component), and the target is
     * an input exchange item (of a linkable component).
     *
     * @return IOutputExchangeItem The link's source
     */
    public IOutputExchangeItem getSourceExchangeItem() {
        return sourceExchangeItem;
    }

    /**
     * Gets the source data type.
     *
     * @return Returns the IDataType
     */
    public IDataType getSourceDataType() {
        return sourceExchangeItem.getDataType();
    }

    /**
     * Gets the source element set.
     *
     * @return Returns the IElementSet
     */
    public IElementSet getSourceElementSet() {
        return sourceExchangeItem.getElementSet();
    }

    /**
     * Gets the target component.
     *
     * @return Returns the ILinkableComponent
     */
    public ILinkableComponent getTargetComponent() {
        return targetComponent;
    }

    /**
     * Gets the target exchange item the link is connected to.
     * <p/>
     * Since OpenMI is a pull-based architecture the link's source is an
     * output exchange item (of a linkable component), and the target is
     * an input exchange item (of a linkable component).
     *
     * @return IInputExchangeItem The link's target
     */
    public IInputExchangeItem getTargetExchangeItem() {
        return targetExchangeItem;
    }

    /**
     * Gets the target data type.
     *
     * @return Returns the IDataType
     */
    public IDataType getTargetDataType() {
        return targetExchangeItem.getDataType();
    }

    /**
     * Gets the target element set.
     *
     * @return Returns the IElementSet
     */
    public IElementSet getTargetElementSet() {
        return targetExchangeItem.getElementSet();
    }

    /**
     * Returns the LinkManager that is the owner of this link.
     *
      * @return LinkManager, can be null
     */
    public LinkManager getOwner() {
        return owner;
    }

    /**
     * Returns the number of data operations.
     *
     * @return The number of data operations
     */
    public int getDataOperationsCount() {
        return dataOperations.size();
    }

    /**
     * Gets a data operation.
     *
     * @param index The index of the selected data operation
     * @return The selected data operation
     */
    public IDataOperation getDataOperation(int index) {
        return dataOperations.get(index);
    }

    /**
     * Adds a given data operation.
     *
     * @param dataOperation The data operation to be added
     */
    public void addDataOperation(IDataOperation dataOperation) {
        dataOperations.add(dataOperation);
    }

    @Override
    public boolean describesSameAs(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!super.describesSameAs(obj)) {
            return false;
        }
        Link l = (Link) obj;
        return ((getSourceComponent() == l.getSourceComponent()) && (getSourceExchangeItem() == l.getSourceExchangeItem()) && (getTargetComponent() == l.getTargetComponent()) && (getTargetExchangeItem() == l.getTargetExchangeItem()));
    }

    @Override
    public String toString() {
        return getCaption();
    }
}
