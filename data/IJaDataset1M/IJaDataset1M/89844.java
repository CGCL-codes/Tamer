package net.sourceforge.pyrus.hal;

public interface MObject {

    /**
	 * Get {@link MObject} id
	 * @return {@link MObject} id
	 */
    public String getId();

    /**
	 * Set {@link MObject} id. Called by the kernel when the object is 
	 * installed so that it can store its id.
	 */
    public void setId(String id);

    /**
	 * Get human readable description of {@link MObject}
	 * @return human readable description of {@link MObject}
	 */
    public String getDescription();

    /**
	 * Start {@link MObject}
	 */
    public void start();

    /**
	 * Stop {@link MObject}
	 */
    public void stop();
}
