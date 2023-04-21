package uima;

import de.nava.informa.core.ItemIF;

/**
 * @author pon3
 * RSS Feed item
 */
public class RSSFeedItem {

    private ItemIF item;

    private String category;

    private String parentCategory;

    public RSSFeedItem(ItemIF item, String category, String parentCategory) {
        this.item = item;
        this.category = category;
        this.parentCategory = parentCategory;
    }

    /**
	 * @return
	 */
    public String getCategory() {
        return category;
    }

    /**
	 * @param category
	 */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
	 * @return
	 */
    public String getParentCategory() {
        return parentCategory;
    }

    /**
	 * @param parentCategory
	 */
    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    /**
	 * @return
	 */
    public ItemIF getItem() {
        return item;
    }

    /**
	 * @param item
	 */
    public void setItem(ItemIF item) {
        this.item = item;
    }
}
