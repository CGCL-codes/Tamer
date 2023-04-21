package com.goodcodeisbeautiful.archtea.search.rss20;

import com.goodcodeisbeautiful.archtea.search.FoundItem;
import com.goodcodeisbeautiful.syndic8.rss20.Rss20Item;

/**
 * An item to be shown in result for rss 2.0.
 */
public class Rss20FoundItem implements FoundItem {

    /** item */
    private Rss20Item m_item;

    /** index */
    private int m_index;

    /**
     * Constructor.
     * @param index is a number for this item.
     * @param item is a Rss20Item instance for this class.
     */
    public Rss20FoundItem(int index, Rss20Item item) {
        m_index = index;
        m_item = item;
    }

    /**
     * Get the index number for this instance.
     * @return index number.
     */
    public String getIndexNumber() {
        return "" + m_index;
    }

    /**
     * Get title.
     * @return title
     */
    public String getTitle() {
        return m_item != null ? m_item.getTitle() : "";
    }

    /**
     * Get summary.
     * @return summary text.
     */
    public String getSummary() {
        return m_item != null ? m_item.getDescription() : "";
    }

    /**
     * Get an url.
     * @return url.
     */
    public String getUrl() {
        return m_item != null ? m_item.getLink() : "";
    }

    /**
     * Get a summarized url.
     */
    public String getSummarizedUrl() {
        return m_item != null ? m_item.getLink() : "";
    }

    /**
     * There is not any information about size.
     * So, this method return "".
     * @return ""
     */
    public String getSize() {
        return "";
    }
}
