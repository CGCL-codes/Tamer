package org.webical.web.component;

/**
 * Interface that serves as a basis for all components (panels, pages, etc) that need to 
 * switch between accessible and Ajax for example
 * @author ivo
 *
 */
public interface IAccessibilitySwitchingComponent {

    /**
	 * Override and setup your common components. Used in the accessible aswell as the non-accessible version of the component
	 * Add components with addOrReplace instead of add
	 */
    public void setupCommonComponents();

    /**
	 * Override and setup your accessible components.
	 * Add components with addOrReplace instead of add
	 */
    public void setupAccessibleComponents();

    /**
	 * Override and setup your accessible components.
	 * Add components with addOrReplace instead of add
	 */
    public void setupNonAccessibleComponents();
}
