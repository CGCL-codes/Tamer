package net.sourceforge.x360mediaserve.util.database.items.containers;

import net.sourceforge.x360mediaserve.api.database.items.container.TVShowItem;

public class TVShowItemImpl extends ContainerItemImpl implements TVShowItem {

    private int numberOfSeasons;

    private int numberOfEpisodes;

    /**
	 * 
	 */
    public TVShowItemImpl() {
        super();
    }

    /**
	 * @param item
	 */
    public TVShowItemImpl(TVShowItem item) {
        super(item);
        numberOfSeasons = item.getNumberOfSeasons();
        numberOfEpisodes = item.getNumberOfEpisodes();
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }
}
