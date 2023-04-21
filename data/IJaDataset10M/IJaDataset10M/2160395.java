package er.divalite.pages;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import er.directtoweb.pages.ERD2WTabInspectPage;

public class ERLITEditPage extends ERD2WTabInspectPage {

    public ERLITEditPage(WOContext aContext) {
        super(aContext);
    }

    @Override
    public boolean switchTabAction() {
        return true;
    }

    @Override
    public WOComponent cancelAction() {
        String subTask = (String) d2wContext().valueForKey("subTask");
        if (subTask != null && subTask.equals("wizard")) {
            clearValidationFailed();
        }
        return super.cancelAction();
    }
}
