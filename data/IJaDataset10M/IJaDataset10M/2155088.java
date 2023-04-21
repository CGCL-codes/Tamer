package er.directtoweb.pages.templates._xhtml;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import er.ajax.AjaxUtils;
import er.directtoweb.pages.templates.ERD2WMessagePageTemplate;
import er.diva.ERDIVPageInterface;

public class ERD2WMessagePageTemplate2 extends ERD2WMessagePageTemplate implements ERDIVPageInterface {

    public ERD2WMessagePageTemplate2(WOContext context) {
        super(context);
    }

    @Override
    public String message() {
        return super.message().trim();
    }

    public String stylesheet() {
        return (String) d2wContext().valueForKey(ERDIVPageInterface.Keys.Stylesheet);
    }

    @Override
    public void appendToResponse(WOResponse response, WOContext context) {
        super.appendToResponse(response, context);
        if (stylesheet() != null) {
            AjaxUtils.addStylesheetResourceInHead(context, response, "app", stylesheet());
        }
    }
}
