package er.directtoweb.interfaces;

import com.webobjects.directtoweb.EditPageInterface;
import com.webobjects.eocontrol.EOEnterpriseObject;

/**
 * Small improvements to the EditPageInterface.<br />
 * 
 */
public interface ERDEditPageInterface extends EditPageInterface {

    public EOEnterpriseObject object();
}
