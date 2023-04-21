package net.sf.josceleton.playground.motion.app2.framework.page.system;

import net.sf.josceleton.playground.motion.app2.framework.page.Page;

public class SystemLoginPage extends Page<SystemLoginView> {

    public static final String ID = SystemLoginPage.class.getName();

    public SystemLoginPage(String idOfNextPage) {
        super(ID, new SystemLoginView(idOfNextPage));
    }
}
