package net.sourceforge.blogentis.plugins.opml;

import net.sourceforge.blogentis.om.Blog;
import net.sourceforge.blogentis.plugins.AbstractPlugin;
import net.sourceforge.blogentis.plugins.BlogPluginService;
import net.sourceforge.blogentis.plugins.base.INavigationExtensionPoint;
import net.sourceforge.blogentis.plugins.editor.IFCKFileManagerExtensionPoint;
import net.sourceforge.blogentis.plugins.opml.impl.OPMLLinkSource;
import net.sourceforge.blogentis.plugins.opml.impl.OPMLNavExtension;
import net.sourceforge.blogentis.plugins.opml.impl.OPMLPrefsExtension;
import net.sourceforge.blogentis.prefs.IPrefsExtensionPoint;

public class OPMLPlugin extends AbstractPlugin {

    public String getName() {
        return "OPML Plugin";
    }

    public String getDescription() {
        return "Allows OPML outlines stored in the blog to" + " be used as a sidebox and as link source in the text editor.";
    }

    public void registerInBlog(Blog blog) {
        BlogPluginService.registerExtension(blog, IPrefsExtensionPoint.class, new OPMLPrefsExtension());
        BlogPluginService.registerExtension(blog, IFCKFileManagerExtensionPoint.class, new OPMLLinkSource());
        BlogPluginService.registerExtension(blog, INavigationExtensionPoint.class, new OPMLNavExtension());
    }
}
