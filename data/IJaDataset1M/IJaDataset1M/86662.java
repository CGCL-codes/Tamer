package net.sf.joafip.performance.entity;

@SuppressWarnings("PMD")
public class Item implements Cloneable {

    private final ItemTemplate itemTemplate;

    private final int identifier;

    private String value;

    private EnumItemState itemState;

    /**
	 * default constructor to make this persistable
	 * 
	 */
    public Item() {
        super();
        itemTemplate = null;
        identifier = -1;
    }

    public Item(final int identifier, final ItemTemplate itemTemplate) {
        super();
        this.identifier = identifier;
        this.itemTemplate = itemTemplate;
    }

    public Item(final int identifier, final ItemTemplate itemTemplate, final String value) {
        super();
        this.identifier = identifier;
        this.value = value;
        this.itemTemplate = itemTemplate;
    }

    /**
	 * copy constructor
	 * 
	 * @param item
	 */
    public Item(final Item item) {
        super();
        final ItemTemplate itemTemplateFromItem = item.getItemTemplate();
        if (itemTemplateFromItem == null) {
            itemTemplate = null;
        } else {
            itemTemplate = new ItemTemplate(itemTemplateFromItem);
        }
        identifier = item.getIdentifier();
        final String valueFromItem = item.getValue();
        if (valueFromItem == null) {
            value = null;
        } else {
            value = "" + valueFromItem;
        }
        itemState = item.itemState;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public int getIdentifier() {
        return identifier;
    }

    public ItemTemplate getItemTemplate() {
        return itemTemplate;
    }

    public EnumItemState getItemState() {
        return itemState;
    }

    public void setItemState(final EnumItemState itemState) {
        this.itemState = itemState;
    }

    @Override
    public String toString() {
        return "id=" + identifier + " value=" + value;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + identifier;
        result = PRIME * result + ((itemTemplate == null) ? 0 : itemTemplate.hashCode());
        result = PRIME * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj.getClass().isAssignableFrom(Item.class)) return false;
        final Item other = (Item) obj;
        if (identifier != other.getIdentifier()) return false;
        if (itemTemplate == null) {
            if (other.getItemTemplate() != null) return false;
        } else if (!itemTemplate.equals(other.getItemTemplate())) return false;
        if (value == null) {
            if (other.getValue() != null) return false;
        } else if (!value.equals(other.getValue())) return false;
        return true;
    }

    @Override
    public Item clone() {
        try {
            return (Item) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new InternalError();
        }
    }
}
