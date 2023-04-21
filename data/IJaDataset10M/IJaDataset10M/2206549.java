package net.sourceforge.blogentis.plugins.topposts;

import net.sourceforge.blogentis.om.Blog;
import net.sourceforge.blogentis.plugins.AbstractPlugin;
import net.sourceforge.blogentis.plugins.BlogPluginService;
import net.sourceforge.blogentis.plugins.base.INavigationExtensionPoint;
import net.sourceforge.blogentis.plugins.topposts.impl.TopPostsNavExtension;
import net.sourceforge.blogentis.plugins.topposts.impl.TopPostsPrefsExtension;
import net.sourceforge.blogentis.prefs.IPrefsExtensionPoint;

public class TopPostsPlugin extends AbstractPlugin {

    public String getName() {
        return "Top Posts Sidebox Plugin";
    }

    public String getDescription() {
        return "Provides a sidebox that shows the top posts by comment count.";
    }

    public void registerInBlog(Blog blog) {
        BlogPluginService.registerExtension(blog, IPrefsExtensionPoint.class, new TopPostsPrefsExtension());
        BlogPluginService.registerExtension(blog, INavigationExtensionPoint.class, new TopPostsNavExtension());
    }
}
