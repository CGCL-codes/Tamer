package entities;

import java.util.List;

public class Bands {

    private long page;

    private long totalPages;

    private List<Band> band;

    /**
     * @return the page
     */
    public long getCurrentPage() {
        return page;
    }

    /**
     * @return the totalPages
     */
    public long getTotalPages() {
        return totalPages;
    }

    /**
     * @return the band
     */
    public List<Band> getBands() {
        return band;
    }
}
