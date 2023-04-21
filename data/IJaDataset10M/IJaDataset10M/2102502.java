package net.sf.log2web.web.beans;

/**
 * @author Guillermo Manzato (manzato@gmail.com)
 * @date Jan 22, 2008
 */
public class ClientConfig implements Cloneable {

    private int screenHeight;

    private LoggingProfile[] profiles;

    public static final ClientConfig DEFAULT_CONFIG;

    static {
        DEFAULT_CONFIG = new ClientConfig();
        DEFAULT_CONFIG.setScreenHeight(1024);
    }

    /**
	 * @return the profiles
	 */
    public LoggingProfile[] getProfiles() {
        return profiles;
    }

    /**
	 * @param profiles the profiles to set
	 */
    public void setProfiles(LoggingProfile[] profiles) {
        this.profiles = profiles;
    }

    /**
	 * @return the screenHeight
	 */
    public int getScreenHeight() {
        return screenHeight;
    }

    /**
	 * @param screenHeight the screenHeight to set
	 */
    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String toString() {
        String str = this.getClass().getName() + "{screenHeight:" + screenHeight + ",profiles=>{";
        for (int i = 0; i < profiles.length; i++) {
            str = str.concat("[" + profiles[i] + "]");
        }
        return str.concat("}}");
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
