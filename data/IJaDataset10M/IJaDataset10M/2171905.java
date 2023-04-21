package cn.ourpk.bbs.robot.core.internal;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import cn.ourpk.bbs.robot.core.Activator;
import cn.ourpk.bbs.robot.service.RobotService;
import cn.ourpk.bbs.robot.service.Site;

public class RobotImpl implements RobotService {

    private static final String EXTENSION_BBSSITES = "sites";

    private List<Site> sites = null;

    public Site[] getSites() {
        if (sites == null) querySites();
        return (Site[]) sites.toArray(new Site[sites.size()]);
    }

    private void querySites() {
        if (sites == null) sites = new ArrayList<Site>();
        IConfigurationElement[] elements = Platform.getExtensionRegistry().getExtensionPoint(Activator.PLUGIN_ID, EXTENSION_BBSSITES).getConfigurationElements();
        for (int i = 0; i < elements.length; i++) {
            Site site = new SiteImpl(new SiteDescriptor(elements[i]));
            sites.add(site);
        }
    }
}
