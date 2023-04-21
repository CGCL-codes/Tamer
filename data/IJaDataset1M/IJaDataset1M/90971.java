package net.sourceforge.blogentis.plugins.sociality.impl;

import net.sourceforge.blogentis.om.Post;
import net.sourceforge.blogentis.plugins.BlogPluginService;
import net.sourceforge.blogentis.plugins.base.AbstractPostViewExtension;
import net.sourceforge.blogentis.plugins.base.PostViewCollector;
import net.sourceforge.blogentis.plugins.sociality.ISocialityLinkExtensionPoint;
import net.sourceforge.blogentis.turbine.BlogRunData;
import net.sourceforge.blogentis.utils.tools.FragmentTool;

public class SocialityPostViewExtension extends AbstractPostViewExtension {

    public String getName() {
        return "Adds social bookmarking links to a post.";
    }

    public void buildExtraHTML(BlogRunData data, Post post, PostViewCollector collector) {
        if (!blog.getConfiguration().getBoolean(SocialityPrefsExtension.SOCIALITY_ENABLED, true)) return;
        ISocialityLinkExtensionPoint slep = (ISocialityLinkExtensionPoint) BlogPluginService.getInstance().locateExtensionPoint(data.getBlog(), ISocialityLinkExtensionPoint.class);
        if (slep == null) return;
        String sup = FragmentTool.findAndInvoke(data, "SocialityPostAddition", post).trim();
        collector.postEnd.append(sup);
    }
}
