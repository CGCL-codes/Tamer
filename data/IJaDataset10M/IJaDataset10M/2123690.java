package org.dcm4chex.wado.common;

/**
 * @author franz.willer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface WADOExtRequestObject extends WADORequestObject {

    /**
	 * @return Returns the serviceType.
	 */
    public String getServiceType();

    /**
	 * @param serviceType The serviceType to set.
	 */
    public void setServiceType(String serviceType);

    /**
	 * @return Returns the level.
	 */
    public String getLevel();

    /**
	 * @param level The level to set.
	 */
    public void setLevel(String level);
}
