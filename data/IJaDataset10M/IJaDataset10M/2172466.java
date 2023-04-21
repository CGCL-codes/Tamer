package org.atlantal.api.cms.definition;

/**
 * @author <a href="mailto:masurel@mably.com">Francois MASUREL</a>
 */
public class ListItemSortInfos {

    private String currentSortway;

    private String nextSortway;

    private String sortTitle;

    private String image;

    /**
     * @return string
     */
    public String getCurrentSortway() {
        return currentSortway;
    }

    /**
     * @return string
     */
    public String getImage() {
        return image;
    }

    /**
     * @return string
     */
    public String getNextSortway() {
        return nextSortway;
    }

    /**
     * @return string
     */
    public String getSortTitle() {
        return sortTitle;
    }

    /**
     * @param string string
     */
    public void setCurrentSortway(String string) {
        currentSortway = string;
    }

    /**
     * @param string string
     */
    public void setImage(String string) {
        image = string;
    }

    /**
     * @param string string
     */
    public void setNextSortway(String string) {
        nextSortway = string;
    }

    /**
     * @param string string
     */
    public void setSortTitle(String string) {
        sortTitle = string;
    }
}
