package simpleorm.simpleweb.eg.database;

import simpleorm.simpleweb.dbute.WGenericCrudPagelet;
import simpleorm.simpleweb.eg.dbute.WUser;

public class WUserAutoCrudPage extends WTestPage {

    public final WUserAutoCrudPagelet pagelet = new WUserAutoCrudPagelet(this);

    public static class WUserAutoCrudPagelet extends WGenericCrudPagelet {

        public WUserAutoCrudPagelet(WUserAutoCrudPage page) {
            super(page, WUser.meta);
            generateCrudFields();
        }
    }
}
