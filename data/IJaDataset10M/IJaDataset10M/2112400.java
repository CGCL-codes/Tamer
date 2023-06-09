package info.jtrac.domain;

import java.io.Serializable;

/**
 * Class that exists purely to hold a single item related to another item
 * along with a integer "type" indicating the nature of the relationship
 * between Item --> Item (one directional relationship)
 *
 * This is used in the following cases
 * - item is a duplicate of another
 * - item depends on another
 *
 * and can be used for other kinds of relationships in the future
 */
public class ItemItem implements Serializable {

    private long id;

    private Item item;

    private Item relatedItem;

    private int type;

    public static final int RELATED = 0;

    public static final int DUPLICATE_OF = 1;

    public static final int DEPENDS_ON = 2;

    public static String getRelationText(int type) {
        if (type == RELATED) {
            return "relatedTo";
        } else if (type == DUPLICATE_OF) {
            return "duplicateOf";
        } else if (type == DEPENDS_ON) {
            return "dependsOn";
        } else {
            throw new RuntimeException("unknown type: " + type);
        }
    }

    public ItemItem() {
    }

    public ItemItem(Item item, Item relatedItem, int type) {
        this.item = item;
        this.relatedItem = relatedItem;
        this.type = type;
    }

    public String getRelationText() {
        return getRelationText(type);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getRelatedItem() {
        return relatedItem;
    }

    public void setRelatedItem(Item relatedItem) {
        this.relatedItem = relatedItem;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id [").append(id);
        sb.append("]; item [").append(item);
        sb.append("]; type [").append(type);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemItem)) {
            return false;
        }
        final ItemItem ii = (ItemItem) o;
        return (id == ii.getId());
    }

    @Override
    public int hashCode() {
        return (int) id;
    }
}
