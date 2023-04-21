package nl.alterra.openmi.sdk.backbone;

import nl.alterra.openmi.sdk.extensions.IInputExchangeItemEx;
import org.openmi.standard.ILinkableComponent;
import org.openmi.standard.IOutputExchangeItem;

/**
 * The input exchange item is an exchange item used for inputs in the receiving
 * component.
 */
public class InputExchangeItem extends ExchangeItem implements IInputExchangeItemEx {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an instance with the specified values. If the owner
     * ILinkableComponent also implements the ILinkableComponentEx interface
     * the created input exchange item will automatically be added to this
     * owner linkable component. The caption will be set to the ID and the
     * description will be left empty.
     *
     * @param owner ILinkableComponent that owns the exchange item
     * @param id    String ID of the exchange item
     */
    public InputExchangeItem(ILinkableComponent owner, String id) {
        this(owner, id, id, "");
    }

    /**
     * Creates an instance with the specified values. If the owner
     * ILinkableComponent also implements the ILinkableComponentEx interface
     * the created input exchange item will automatically be added to this
     * owner linkable component.
     *
     * @param owner       ILinkableComponent that owns the exchange item
     * @param id          String ID of the exchange item
     * @param caption     String caption of the exchange item
     * @param description String description of the exchange item
     */
    public InputExchangeItem(ILinkableComponent owner, String id, String caption, String description) {
        super(owner, id, caption, description);
    }

    /**
     * Checks if this item and a given item can be connected.
     *
     * @param itm The exchange item to compare with
     * @return True if the items are considered connectable
     */
    public boolean isConnectableWith(IOutputExchangeItem itm) {
        return super.isConnectableWith(itm);
    }
}
