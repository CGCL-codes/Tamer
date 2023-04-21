package net.sf.opentranquera.ui.branding;

/**
 * @author Guillermo Meyer
 * Brand para renderizar.
 */
public class Brand {

    private String name = null;

    private BrandXkin xkin = null;

    /**
	 * Returns the name.
	 * @return String
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets the name.
	 * @param name The name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Returns the xkin.
	 * @return BrandXkin
	 */
    public BrandXkin getXkin() {
        return xkin;
    }

    /**
	 * Sets the xkin.
	 * @param xkin The xkin to set
	 */
    public void setXkin(BrandXkin xkin) {
        this.xkin = xkin;
    }
}
