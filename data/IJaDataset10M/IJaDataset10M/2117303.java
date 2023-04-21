package er.directtoweb.embed;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WSelect;

/**
 * Embedded component that can be used for nesting a pick inside another page configuration.<br />
 * 
 * @binding action
 * @binding branchDelegate
 * @binding dataSource
 * @binding entityName
 * @binding pageConfiguration
 * @binding selectedObject
 * @binding nextPage
 */
public class ERXD2WSelect extends D2WSelect {

    public ERXD2WSelect(WOContext context) {
        super(context);
    }
}
