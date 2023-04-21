package entities;

import java.util.List;

public class Venues {

    private long page;

    private long totalPages;

    private List<Venue> venue;

    /**
     * @return the page
     */
    public long getPage() {
        return page;
    }

    /**
     * @return the totalPages
     */
    public long getTotalPages() {
        return totalPages;
    }

    /**
     * @return the venue
     */
    public List<Venue> getVenues() {
        return venue;
    }
}
