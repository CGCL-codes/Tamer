package org.stanwood.media.store.memory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.stanwood.media.model.ISeason;
import org.stanwood.media.model.IShow;
import org.stanwood.media.model.Season;
import org.stanwood.media.model.Show;

/**
 * Used to create a cache of the TV show data
 */
public class CacheShow extends Show {

    private List<CacheSeason> seasons = new ArrayList<CacheSeason>();

    private IShow show;

    /**
	 * Used to create a instance of the cache show
	 * @param show The show that is been cached
	 */
    public CacheShow(IShow show) {
        super(show.getShowId());
        this.show = show;
    }

    /**
	 * Used to get a season from the show with the given season number.
	 * If the season can't be found, then it will return null.
	 * @param seasonNum The number of the season too fetch.
	 * @return The season, or null if it can't be found
	 */
    public ISeason getSeason(int seasonNum) {
        for (Season season : seasons) {
            if (season.getSeasonNumber() == seasonNum) {
                return season;
            }
        }
        return null;
    }

    /**
	 * Used to remove a season with the given season number from the show.
	 * @param seasonNumber The season number of the season to remove
	 */
    public void removeSeason(int seasonNumber) {
        Iterator<CacheSeason> it = seasons.iterator();
        while (it.hasNext()) {
            CacheSeason foundSeason = it.next();
            if (foundSeason.getSeasonNumber() == seasonNumber) {
                it.remove();
            }
        }
    }

    /**
	 * Used to add a season to the show.
	 * @param season The season to add to the show.
	 */
    public void addSeason(CacheSeason season) {
        seasons.add(season);
    }

    /**
	 * Used to set the long summary of the show
	 *
	 * @param longSummary The long summary of the show
	 */
    @Override
    public void setLongSummary(String longSummary) {
        show.setLongSummary(longSummary);
    }

    /**
	 * Used to set the show summary of the show
	 *
	 * @param shortSummary The short summary iof the show
	 */
    @Override
    public void setShortSummary(String shortSummary) {
        show.setShortSummary(shortSummary);
    }

    /**
	 * Used to set the genres that the show belongs too
	 *
	 * @param genres The genres that the show belongs too
	 */
    @Override
    public void setGenres(List<String> genres) {
        show.setGenres(genres);
    }

    /**
	 * Used to set the name/title of the show
	 *
	 * @param name The name of the show
	 */
    @Override
    public void setName(String name) {
        show.setName(name);
    }

    /**
	 * Used too set the URL which points to a summary of the show
	 * @param showURL The URL which points to a summary of the show
	 */
    @Override
    public void setShowURL(URL showURL) {
        show.setShowURL(showURL);
    }

    /**
	 * Used to get a long summary of the show
	 * @return The long summary of the show
	 */
    @Override
    public String getLongSummary() {
        return show.getLongSummary();
    }

    /**
	 * Used to get a short summary of the show
	 * @return The short summary of the show
	 */
    @Override
    public String getShortSummary() {
        return show.getShortSummary();
    }

    /**
	 * Used to get the genres that the show belongs too
	 * @return The genres the show belongs too
	 */
    @Override
    public List<String> getGenres() {
        return show.getGenres();
    }

    /**
	 * Used to get the name/title of the show
	 * @return The name/title of the show
	 */
    @Override
    public String getName() {
        return show.getName();
    }

    /**
	 * Used to get the id of the show, which was defined by
	 * the source it was fetched from.
	 * @return The show id
	 */
    @Override
    public String getShowId() {
        return show.getShowId();
    }

    /**
	 * Used to get a URL which points too a image of the show
	 * @return A URL which points too a image of the show
	 */
    @Override
    public URL getImageURL() {
        return show.getImageURL();
    }

    /**
	 * Used to set a URL which points too a image of the show
	 * @param imageURL A URL which points too a image of the show
	 */
    @Override
    public void setImageURL(URL imageURL) {
        show.setImageURL(imageURL);
    }

    /**
	 * Used to get a URL which points to a summary of the show
	 * @return The URL which points to a summary of the show
	 */
    @Override
    public URL getShowURL() {
        return show.getShowURL();
    }

    /**
	 * Used to get the source id of the source that was used to retrieve the shows information.
	 * @return The source id
	 */
    @Override
    public String getSourceId() {
        return show.getSourceId();
    }

    /**
	 * Used to set the source id of the source that was used to retrieve the shows information.
	 * @param sourceId The source id
	 */
    @Override
    public void setSourceId(String sourceId) {
        show.setSourceId(sourceId);
    }

    /**
	 * Used to add a genre to the show
	 * @param genre The genre
	 */
    @Override
    public void addGenre(String genre) {
        show.addGenre(genre);
    }

    /**
	 * This is useful if the film belongs to more than one genres. It will returned the
	 * genre that is preferred.
	 * @return The preferred genre or null if not or flagged as preferred.
	 */
    @Override
    public String getPreferredGenre() {
        return show.getPreferredGenre();
    }

    /**
	 * Used to set the genre that is preferred in the list of genres.
	 * @param preferredGenre The preferred genre
	 */
    @Override
    public void setPreferredGenre(String preferredGenre) {
        show.setPreferredGenre(preferredGenre);
    }

    /**
	 * Used to get extra information to a show that their are no getters/setters for in the regular fields
	 * @return The extra information in a map of key value pairs
	 */
    @Override
    public Map<String, String> getExtraInfo() {
        return show.getExtraInfo();
    }

    /**
	 * Used to add extra information to a show that their are no getters/setters for in the regular fields
	 * @param params The extra information in a map of key value pairs
	 */
    @Override
    public void setExtraInfo(Map<String, String> params) {
        show.setExtraInfo(params);
    }

    /**
	 * Used to get a list of seasons in the show
	 * @return a list of seasons in the show
	 */
    public List<CacheSeason> getSeasons() {
        return seasons;
    }
}
