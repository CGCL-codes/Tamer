package net.kano.joscar.ssiitem;

import net.kano.joscar.DefensiveTools;
import net.kano.joscar.MiscTools;
import net.kano.joscar.snaccmd.ssi.SsiItem;
import net.kano.joscar.tlv.TlvChain;
import net.kano.joscar.tlv.TlvTools;

/**
 * A base class for the two item types that only contain a name and reside in
 * the root group (group <code>0x0000</code>). These are {@link PermitItem} and
 * {@link DenyItem}.
 */
public abstract class SimpleNamedItem extends AbstractItemObj implements SsiItemObjectWithId {

    /** The parent ID to use in simple named items. */
    private static final int PARENTID_DEFAULT = 0;

    /** The user's screenname. */
    private final String sn;

    /** The item ID. */
    private final int id;

    /**
     * Creates a new simple named item object generated from the data in the
     * given SSI item block.
     *
     * @param item a simple named SSI item
     */
    protected SimpleNamedItem(SsiItem item) {
        this(item.getName(), item.getId(), TlvTools.readChain(item.getData()));
    }

    /**
     * Creates a new simple named item object with the same properties as the
     * given item.
     *
     * @param other a simple named item object
     */
    protected SimpleNamedItem(SimpleNamedItem other) {
        this(other.sn, other.id, other.copyExtraTlvs());
    }

    /**
     * Creates a new simple named item object with the given screenname and the
     * given item ID.
     *
     * @param sn the screenname for this item
     * @param id this item's SSI item ID
     */
    protected SimpleNamedItem(String sn, int id) {
        this(sn, id, null);
    }

    /**
     * Creates a new simple named item object with the given screenname and the
     * given item ID.
     *
     * @param sn the screenname for this item
     * @param id this item's SSI item ID
     * @param extraTlvs a list of extra TLV's to store in this item
     */
    protected SimpleNamedItem(String sn, int id, TlvChain extraTlvs) {
        super(extraTlvs);
        DefensiveTools.checkNull(sn, "sn");
        DefensiveTools.checkRange(id, "id", 0);
        this.sn = sn;
        this.id = id;
    }

    /**
     * Returns this item's screenname.
     *
     * @return this item's screenname
     */
    public final String getScreenname() {
        return sn;
    }

    /**
     * Returns the SSI item ID of this simple named item object.
     *
     * @return this item's SSI item ID
     */
    public final int getId() {
        return id;
    }

    /**
     * Returns the SSI item type of this item. This should normally return one
     * of the {@linkplain SsiItem#TYPE_BUDDY <code>SsiItem.TYPE_<i>*</i></code>
     * constants}.
     *
     * @return the SSI item type of this item
     */
    protected abstract int getItemType();

    public SsiItem toSsiItem() {
        return generateItem(sn, PARENTID_DEFAULT, id, getItemType(), null);
    }

    public String toString() {
        return MiscTools.getClassName(this) + " for " + sn + " (id=0x" + Integer.toHexString(id) + ")";
    }
}
