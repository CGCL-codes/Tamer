package er.directtoweb.embed;

import com.webobjects.appserver.WOContext;

/**
 * Embedded component that can be used for nesting a pick inside another page configuration.<br />
 * 
 * @binding action
 * @binding branchDelegate
 * @binding dataSource
 * @binding entityName
 * @binding pageConfiguration
 * @binding selectedObjects
 * @binding nextPage
 */
public class ERXD2WPick extends D2WPick {

    public ERXD2WPick(WOContext context) {
        super(context);
    }
}
