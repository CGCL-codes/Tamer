package net.sf.coredd.versioncontrol.domain;

/**
 * @author roberto.reinert
 * 
 * Interface for versions.
 */
public interface IVersion extends Cloneable {

    /**
	 * @return
	 */
    public String getCode();

    /**
	 * @param code
	 */
    public void setCode(String code);

    /**
	 * @return
	 */
    public String getDescription();

    /**
	 * @param description
	 */
    public void setDescription(String description);
}
