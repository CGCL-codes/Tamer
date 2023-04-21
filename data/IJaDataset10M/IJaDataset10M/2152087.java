package com.liferay.portal.tools;

import java.util.Iterator;
import java.util.List;
import com.dotmarketing.util.Logger;
import com.liferay.util.ant.WarTask;

/**
 * <a href="WebSiteWar.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.2 $
 *
 */
public class WebSiteWar {

    public static void main(String[] args) {
        new WebSiteWar();
    }

    public WebSiteWar() {
        try {
            List webSites = WebSiteBuilder.getWebSites();
            _war(webSites);
        } catch (Exception e) {
            Logger.error(this, e.getMessage(), e);
        }
    }

    private void _war(List webSites) throws Exception {
        Iterator itr = webSites.iterator();
        while (itr.hasNext()) {
            WebSite webSite = (WebSite) itr.next();
            WarTask.war("../web-sites/" + webSite.getId() + "-web/docroot", "../web-sites/" + webSite.getId() + "-web.war", "WEB-INF/web.xml", "../web-sites/" + webSite.getId() + "-web/docroot/WEB-INF/web.xml");
        }
    }

    private String _portalExtProperties = null;

    private String _orionConfigDir = null;

    private int _nfcListenPort = 7777;
}
