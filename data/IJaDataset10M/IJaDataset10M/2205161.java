package net.sf.appfw.security;

/**
 * Domain that provides security objects in one aspect or another.
 * 
 * @author Zhengmao HU
 *
 */
abstract interface AbstractDomain {

    /**
	 * Returns code name of the domain
	 * @return
	 */
    public String getCodeName();

    /**
	 * Returns name of the domain.
	 * @return
	 */
    public String getName();

    /**
	 * Returns description of the domain.
	 * @return
	 */
    public String getDescription();
}
