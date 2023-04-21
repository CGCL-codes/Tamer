package it.csi.otre.services.ooo;

import javax.ejb.Local;

/**
 * Local interface of the Template Manager.
 * 
 * @author oscar ghersi (email: oscar.ghersi@csi.it)
 */
@Local
public interface OOoTemplateManagerLocal extends OOoTemplateManagerInterface {

    public static final String BEAN_NAME = "otre/services/OOoTemplateManagerBean/local";
}
