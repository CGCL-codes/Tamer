package net.sf.wicketdemo.app.wickettags;

import net.sf.wicketdemo.tech.GAEUtils;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.session.ISessionStore;

/**
 * 
 * @author Bram Bogaert
 */
public class WicketTagsApplication extends WebApplication {

    @Override
    public Class<WicketTagsHomePage> getHomePage() {
        return WicketTagsHomePage.class;
    }

    @Override
    protected void init() {
        super.init();
        GAEUtils.init(this);
    }

    @Override
    protected ISessionStore newSessionStore() {
        ISessionStore result = GAEUtils.newSessionStore(this);
        if (result == null) {
            result = super.newSessionStore();
        }
        return result;
    }
}
