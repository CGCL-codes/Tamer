package com.pricefeeder.bandsintown;

import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves concert events from BandsInTown API using the Recommended query type.
 * <br/><br/>
 * See 
 * {@link <a href="http://www.bandsintown.com/api/requests#events-recommended">http://www.bandsintown.com/api/requests#events-recommended</a>} 
 * for more information about this service call.
 * 
 * @author Eli Colner
 *
 */
public class RecommendedRequest extends BandsInTown {

    /**
	 * The list of artists. Max 50. (required)
	 */
    private List<String> artists = new ArrayList<String>();

    /**
	 * The location of your end user.  (required)
	 */
    private String location;

    /**
	 * The radius from your end user's location.  Max 150.
	 */
    private int radius = 25;

    /**
	 * The date or date range.
	 */
    private String date;

    /**
	 * ????
	 */
    private boolean onlyRecs = false;

    /**
	 * The start page of results.
	 */
    private int page = 1;

    /**
	 * The end page of results.  Max 100.
	 */
    private int perPage = 50;

    /**
	 * Builds the service call endpoint URL just prior to the time when the
	 * service call occurs according to expected Recommended query type parameters.
	 */
    @Override
    protected void preService() {
        url = new StringBuffer();
        url.append(SERVICE_ENDPOINT).append("/events/recommended").append("?").append("app_id=").append(applicationId).append("&format=").append(RESPONSE_FORMAT);
        for (String artist : artists) {
            url.append("&artists[]=");
            url.append(artist);
        }
        url.append("&location=").append(getLocation());
        url.append("&radius=").append(radius);
        url.append("&date=").append(getDate());
        url.append("&page=").append(page);
        url.append("&per_page=").append(perPage);
        url.append("&only_recs=").append(onlyRecs);
    }

    /**
	 * Occurs just after the service call completes.
	 */
    @Override
    protected void postService() {
    }

    /**
	 * Gets the artists.
	 * @return the artists to get recommended concerts for
	 */
    public List<String> getArtists() {
        return artists;
    }

    /**
	 * Sets the artists.  Max 50.<br/><br/>Individual artist names can be formatted:<br/>
	 * artist name <b>or</b> MusicBrainz ID (mbid_<id>)<br/>
	 * @param artists the artists
	 */
    public void setArtists(List<String> artists) {
        this.artists = artists;
    }

    /**
	 * Gets the location.
	 * @return the location of end user
	 */
    public String getLocation() {
        if (location == null) {
            return "";
        }
        return location;
    }

    /**
	 * Sets the user location.<br/><br/>Location can be set to one of the following:<br/>
	 * city,state<br/>
	 * city,country<br/>
	 * lat,lon<br/>
	 * ip address<br/>
	 * use_geoip (will use the ip the request came from)
	 * @param location the location of end user
	 */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
	 * Gets the radius in miles.
	 * @return the radius around the location
	 */
    public int getRadius() {
        return radius;
    }

    /**
	 * Sets the radius in miles.  Max 150.
	 * @param radius the radius around the location.
	 */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
	 * Gets the date or date range.
	 * @return the date or date range
	 */
    public String getDate() {
        if (date == null) {
            return "upcoming";
        }
        return date;
    }

    /**
	 * Sets the date or date range of concerts.<br/><br/>Date can be set to one of the following:<br/>
	 * yyyy-mm-dd<br/>
	 * yyyy-mm-dd,yyyy-mm-dd (inclusive range)<br/>
	 * upcoming
	 * @param date the date or date range
	 */
    public void setDate(String date) {
        this.date = date;
    }

    /**
	 * ???
	 * @return onlyRecs
	 */
    public boolean isOnlyRecs() {
        return onlyRecs;
    }

    /**
	 * ???
	 * @param onlyRecs onlyRecs
	 */
    public void setOnlyRecs(boolean onlyRecs) {
        this.onlyRecs = onlyRecs;
    }

    /**
	 * Gets the start page.
	 * @return the page number
	 */
    public int getPage() {
        return page;
    }

    /**
	 * Sets the start page.
	 * @param page the page number
	 */
    public void setPage(int page) {
        this.page = page;
    }

    /**
	 * Gets number of concerts per page.
	 * @return the per page number
	 */
    public int getPerPage() {
        return perPage;
    }

    /**
	 * Sets the number of concerts per page.  Max 100.
	 * @param perPage the per page number
	 */
    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }
}
