package er.directtoweb.pages;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.ConfirmPageInterface;
import com.webobjects.directtoweb.NextPageDelegate;

public class ERDQuestionPage extends ERD2WMessagePage implements ConfirmPageInterface {

    public ERDQuestionPage(WOContext context) {
        super(context);
    }

    public void setOkDelegate(NextPageDelegate okDelegate) {
        setConfirmDelegate(okDelegate);
    }

    public void setOkNextPage(WOComponent page) {
        setNextPage(page);
    }

    public void setCancelNextPage(WOComponent page) {
        setCancelPage(page);
    }

    /** @deprecated use confirmAction() */
    public WOComponent okClicked() {
        return confirmAction();
    }

    /** @deprecated use cancelAction() */
    public WOComponent cancelClicked() {
        return cancelAction();
    }
}
