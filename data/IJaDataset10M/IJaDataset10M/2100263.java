package org.musicbrainz.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.musicbrainz.webservice.filter.ReleaseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>A release event, indicating where and when a release took place.</p>
 * 
 * <p>All country codes used must be valid ISO-3166 country codes (i.e. 'DE',
 * 'UK' or 'FR'). The dates are strings and must have the format 'YYYY',
 * 'YYYY-MM' or 'YYYY-MM-DD'.</p>
 * 
 * @author Patrick Ruhkopf
 */
public class ReleaseEvent {

    private Logger log = LoggerFactory.getLogger(ReleaseEvent.class);

    /**
	 * A string containing an ISO-3166 country code
	 */
    private String countryId;

    /**
	 * A string containing a date string
	 */
    private String dateStr;

    /**
	 * Default Constructor
	 */
    public ReleaseEvent() {
    }

    /**
	 * Minimal Constructor
	 * @param countryId A string containing an ISO-3166 country code
	 * @param dateStr A string containing a date string
	 */
    public ReleaseEvent(final String countryId, final String dateStr) {
        this.countryId = countryId;
        this.dateStr = dateStr;
    }

    /**
	 * Due to a server limitation, the web service does not
	 * return country IDs for release collection queries. This only
	 * affects the {@link org.musicbrainz.Query#getReleases(ReleaseFilter)} query.
	 * 
	 * @return the countryId
	 */
    public String getCountryId() {
        return this.countryId;
    }

    /**
	 * @param countryId the countryId to set
	 */
    public void setCountryId(final String countryId) {
        this.countryId = countryId;
    }

    /**
	 * @return the dateStr
	 */
    public String getDateStr() {
        return this.dateStr;
    }

    /**
	 * @param dateStr the dateStr to set
	 */
    public void setDateStr(final String dateStr) {
        this.dateStr = dateStr;
    }

    /**
	 * Parses the date string and returns a Date
	 * @return A Date object
	 */
    public Date getDate() {
        return this.dateForString();
    }

    private Date dateForString() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy");
        if (this.dateStr == null) {
            return null;
        }
        if (this.dateStr.length() == 10) {
            f = new SimpleDateFormat("yyyy-MM-dd");
        }
        if (this.dateStr.length() == 7) {
            f = new SimpleDateFormat("yyyy-MM");
        }
        try {
            return f.parse(this.dateStr);
        } catch (ParseException e) {
            this.log.warn("Could not parse date string - returning null", e);
            return null;
        }
    }

    @Override
    public String toString() {
        return "ReleaseEvent country=" + this.countryId + ", date=" + this.dateStr;
    }
}
