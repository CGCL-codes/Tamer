package net.itsite;

import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.SkinUtils;

public class ItSitePageHandle extends DefaultPageHandle {

    public String deploy = "/simple/template/css/";

    @Override
    public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
        if ("importCSS".equals(beanProperty)) {
            return getImportCSS(pageParameter);
        }
        return super.getBeanProperty(pageParameter, beanProperty);
    }

    public static String[] csss = { "itsite.css", "search.css" };

    public String[] getImportCSS(final PageParameter pageParameter) {
        final String[] importCss = new String[csss.length + 1];
        final String skin = SkinUtils.getSkin(pageParameter, SkinUtils.DEFAULT_SKIN);
        int i = 0;
        importCss[i++] = "/default/simple.css";
        for (final String css : csss) {
            importCss[i++] = deploy + skin + "/" + css;
        }
        return importCss;
    }
}
