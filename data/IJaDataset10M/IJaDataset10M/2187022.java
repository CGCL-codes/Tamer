package client.ws.milanas.helpers.beans;

import java.util.ArrayList;

/**
 *
 * @author milana
 */
public class Music extends Item {

    private String studio;

    private String numCD;

    private String summary;

    private String salesRank;

    private String averageRating;

    /**
     * Creates a new instance of Music.
     * This class is used as an Amazon's musics wrapper and it only has get/set methods
     * in order to store the data sent in the Amazon's responses.
     */
    public Music() {
        studio = new String();
        numCD = new String();
        summary = new String();
        salesRank = new String();
        averageRating = new String();
    }

    public void setStudio(String studio) {
        if (studio != null) {
            this.studio = studio;
        }
    }

    public void setNumCD(String numCD) {
        if (numCD != null) {
            this.numCD = numCD;
        }
    }

    public void addArtist(String artist) {
        if (artist != null) {
            super.addCreator(artist);
        }
    }

    public void setSummary(String summary) {
        if (summary != null) {
            this.summary = summary;
        }
    }

    public void setSalesRank(String salesRank) {
        if (salesRank != null) {
            this.salesRank = salesRank;
        }
    }

    public void setAverageRating(String averageRating) {
        if (averageRating != null) {
            this.averageRating = averageRating;
        }
    }

    public String getStudio() {
        return studio;
    }

    public String getNumCD() {
        return numCD;
    }

    public String getFirstArtist() {
        return super.getFirstCreator();
    }

    public ArrayList<String> getArtists() {
        return super.getCreators();
    }

    public String getSummary() {
        return summary;
    }

    public String getSalesRank() {
        return salesRank;
    }

    public String getAverageRating() {
        return averageRating;
    }
}
