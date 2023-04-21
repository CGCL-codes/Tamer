package libomv.inventory;

import libomv.primitives.Primitive.AttachmentPoint;
import libomv.types.UUID;

/** InventoryObject Class contains details on a primitive or coalesced set of primitives */
@SuppressWarnings("serial")
public class InventoryObject extends InventoryItem {

    /**
	 * Construct an InventoryObject object
	 * 
	 * @param itemID
	 *            A {@link OpenMetaverse.UUID} which becomes the
	 *            {@link OpenMetaverse.InventoryItem} objects AssetUUID
	 */
    public InventoryObject(UUID itemID) {
        super(itemID);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.Object;
    }

    /** Gets or sets the upper byte of the Flags value */
    public final int getItemFlags() {
        return ItemFlags & ~0xFF;
    }

    public final void setItemFlags(int value) {
        ItemFlags = value | (ItemFlags & 0xFF);
    }

    /**
	 * Gets or sets the object attachment point, the lower byte of the Flags
	 * value
	 */
    public final AttachmentPoint getAttachPoint() {
        return AttachmentPoint.setValue(ItemFlags & 0xFF);
    }

    public final void setAttachPoint(AttachmentPoint value) {
        ItemFlags = value.getValue() | (ItemFlags & ~0xFF);
    }
}
