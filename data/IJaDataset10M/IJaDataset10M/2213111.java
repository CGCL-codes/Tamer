package net.sf.buildbox.plugin.bootstrap;

import net.sf.buildbox.plugin.api.BuildboxPlugin;
import net.sf.buildbox.plugin.api.PluginFeedback;

/**
 * @author Petr Kozelka
 */
public class SvnFetch implements BuildboxPlugin {

    private String svnUrl;

    private String revision;

    public void setFeedback(PluginFeedback feedback) {
    }

    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Boolean call() throws Exception {
        return null;
    }
}
