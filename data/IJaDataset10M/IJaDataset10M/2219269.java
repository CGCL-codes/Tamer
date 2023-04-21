package org.seqtagutils.util.acegi;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

@SuppressWarnings("unchecked")
public class CAcegiLoginListener implements ApplicationListener {

    protected ILoginListener loginListener;

    protected IAcegiService acegiService = new CAcegiHelper();

    @Required
    public void setLoginListener(final ILoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void onApplicationEvent(ApplicationEvent evt) {
        acegiService.logLogin(loginListener, evt);
    }
}
