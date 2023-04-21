package er.directtoweb;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import com.webobjects.directtoweb.*;
import er.extensions.*;

public class ERDTrashcan extends ERDCustomEditComponent {

    public ERDTrashcan(WOContext context) {
        super(context);
    }

    public boolean isStateless() {
        return true;
    }

    public boolean synchronizesVariablesWithBindings() {
        return false;
    }

    public boolean canDelete() {
        return object() != null && object() instanceof ERXGuardedObjectInterface ? ((ERXGuardedObjectInterface) object()).canDelete() : true;
    }

    public EODataSource dataSource() {
        return (EODataSource) valueForBinding("dataSource");
    }

    public D2WContext d2wContext() {
        return (D2WContext) valueForBinding("d2wContext");
    }

    public WOComponent deleteObjectAction() {
        ConfirmPageInterface nextPage = (ConfirmPageInterface) D2W.factory().pageForConfigurationNamed((String) d2wContext().valueForKey("confirmConfigurationNameForEntity"), session());
        nextPage.setConfirmDelegate(new ERDDeletionDelegate(object(), dataSource(), context().page()));
        nextPage.setCancelDelegate(new ERDPageDelegate(context().page()));
        nextPage.setMessage("<B>Are you sure you want to delete the following " + d2wContext().valueForKey("displayNameForEntity") + "</B>:<br> " + object().userPresentableDescription());
        ((D2WPage) nextPage).setObject(object());
        return (WOComponent) nextPage;
    }

    public String onMouseOverTrashcan() {
        return hasBinding("trashcanExplanation") ? "self.status='" + valueForBinding("trashcanExplanation") + "'; return true" : "";
    }

    public String onMouseOverNoTrashcan() {
        return hasBinding("noTrashcanExplanation") ? "self.status='" + valueForBinding("noTrashcanExplanation") + "'; return true" : "";
    }
}
