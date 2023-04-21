package net.sourceforge.blogentis.plugins.comments.impl;

import net.sourceforge.blogentis.om.Blog;
import net.sourceforge.blogentis.plugins.AbstractPlugin;
import net.sourceforge.blogentis.plugins.BlogPluginService;
import net.sourceforge.blogentis.plugins.base.INavigationExtensionPoint;
import net.sourceforge.blogentis.plugins.comments.ICommentExtensionPoint;
import net.sourceforge.blogentis.prefs.IPrefsExtensionPoint;

/**
 * A plugin that removes unsave tags from the comments.
 * 
 * @author abas
 */
public class CleanCommentPlugin extends AbstractPlugin {

    public String getName() {
        return "Clean Comment";
    }

    public String getDescription() {
        return "Limits tags used in comments to a specific set.";
    }

    public void registerInBlog(Blog blog) {
        BlogPluginService.registerExtension(blog, ICommentExtensionPoint.class, new CleanCommentExtension());
        BlogPluginService.registerExtension(blog, IPrefsExtensionPoint.class, new CleanCommentPrefsExtension());
        BlogPluginService.registerExtension(blog, INavigationExtensionPoint.class, new CleanCommentNavExtension());
    }
}
