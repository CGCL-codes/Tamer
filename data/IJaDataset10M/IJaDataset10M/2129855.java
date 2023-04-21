package org.cast.isi.page;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.cast.isi.ISIApplication;

public class ExceptionPage extends ISIBasePage {

    public ExceptionPage(final PageParameters param, RuntimeException e) {
        super(param);
        setPageTitle("Internal Error");
        add(new BookmarkablePageLink<ISIStandardPage>("home", ISIApplication.get().getHomePage()));
        add(new ISIApplication.LogoutLink("logout2"));
        String message = e.getMessage() + "<br /><br />\n";
        StackTraceElement[] trace;
        Throwable cause = e;
        while (cause.getCause() != null) cause = cause.getCause();
        message += "<b>" + cause.toString() + "</b>\n<ul>\n";
        trace = cause.getStackTrace();
        for (int i = 0; i < trace.length; i++) message += "<li>" + trace[i].toString() + "</li>\n";
        message += "</ul><br />";
        add(new Label("details", message).setEscapeModelStrings(false));
        if (cause instanceof PageExpiredException) throw new RestartResponseException(Login.class);
    }

    @Override
    public String getPageName() {
        return null;
    }

    @Override
    public String getPageType() {
        return "Error";
    }

    @Override
    public String getPageViewDetail() {
        return null;
    }
}
