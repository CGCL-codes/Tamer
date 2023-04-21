package torrentjedi.core;

/**
 * @author Alper Kokmen
 *
 */
public class TorrentSource {

    private String url;

    private String name;

    private String icon;

    private Boolean favorite;

    private Integer updateInterval;

    private Boolean autoLoad;

    public TorrentSource() {
        super();
    }

    /**
	 * @return the url
	 */
    public String getUrl() {
        return url;
    }

    /**
	 * @param url the url to set
	 */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the icon
	 */
    public String getIcon() {
        return icon;
    }

    /**
	 * @param icon the icon to set
	 */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
	 * @return the favorite
	 */
    public Boolean getFavorite() {
        return favorite;
    }

    /**
	 * @param favorite the favorite to set
	 */
    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    /**
	 * @return the updateInterval
	 */
    public Integer getUpdateInterval() {
        return updateInterval;
    }

    /**
	 * @param updateInterval the updateInterval to set
	 */
    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
    }

    /**
	 * @return the autoLoad
	 */
    public Boolean getAutoLoad() {
        return autoLoad;
    }

    /**
	 * @param autoLoad the autoLoad to set
	 */
    public void setAutoLoad(Boolean autoLoad) {
        this.autoLoad = autoLoad;
    }

    @Override
    public String toString() {
        return getName() + " [" + getUrl().toString() + "]";
    }
}
