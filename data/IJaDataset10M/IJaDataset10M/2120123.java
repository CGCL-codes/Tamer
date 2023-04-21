package com.google.code.sagetvaddons.sre3.server;

/**
 * <p>Contains details about an airing's override</p>
 * 
 * <p>An AiringOverride allows users to tell SRE to use a different title and subtitle than the ones in
 * the EPG for a given recording when monitoring it.  Typical use is to set a title/subtile for timed
 * recordings or to change the details of a scheduled recording when the user knows the EPG data is
 * incorrect.</p>
 * 
 * @author dbattams
 *
 */
public final class AiringOverride {

    private int id;

    private String title;

    private String subtitle;

    private boolean enabled;

    /**
	 * Constructor
	 * @param id The airing id this override is associated with
	 * @param title The override title
	 * @param subtitle The override subtitle (episode)
	 * @param isEnabled When true this airing will be monitored; when false monitoring is disabled for this specific airing
	 */
    public AiringOverride(int id, String title, String subtitle, boolean isEnabled) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        enabled = isEnabled;
    }

    /**
	 * @return the id
	 */
    public int getId() {
        return id;
    }

    /**
	 * @return the title
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * @return the subtitle
	 */
    public String getSubtitle() {
        return subtitle;
    }

    /**
	 * @return the enabled
	 */
    public boolean isEnabled() {
        return enabled;
    }
}
