package net.sourceforge.blogentis.plugins.editor;

import net.sourceforge.blogentis.om.Blog;
import net.sourceforge.blogentis.plugins.AbstractPlugin;
import net.sourceforge.blogentis.plugins.BlogPluginService;
import net.sourceforge.blogentis.plugins.base.IEditorExtensionPoint;
import net.sourceforge.blogentis.plugins.base.INavigationExtensionPoint;
import net.sourceforge.blogentis.plugins.editor.impl.FCKEditorExtension;
import net.sourceforge.blogentis.plugins.editor.impl.FCKFileManagerExtensionPoint;
import net.sourceforge.blogentis.plugins.editor.impl.FCKNavExtension;
import net.sourceforge.blogentis.plugins.editor.impl.FCKPrefsExtension;
import net.sourceforge.blogentis.plugins.editor.impl.ImageExtension;
import net.sourceforge.blogentis.plugins.editor.impl.MediaExtension;
import net.sourceforge.blogentis.plugins.editor.impl.PostExtension;
import net.sourceforge.blogentis.prefs.IPrefsExtensionPoint;

/**
 * A plugin that replaces the default TextAreas with the FCK Editor.
 * 
 * @author abas
 */
public class FCKEditorPlugin extends AbstractPlugin {

    public String getName() {
        return "FCK Editor plugin";
    }

    public String getDescription() {
        return "Use the FCK Editor to edit textareas.";
    }

    public void registerInBlog(Blog blog) {
        BlogPluginService.registerExtensionPoint(blog, new FCKConfigExtensionPoint(this, blog));
        BlogPluginService.registerExtensionPoint(blog, new FCKFileManagerExtensionPoint(this, blog));
        BlogPluginService.registerExtension(blog, IEditorExtensionPoint.class, new FCKEditorExtension());
        BlogPluginService.registerExtension(blog, IPrefsExtensionPoint.class, new FCKPrefsExtension());
        BlogPluginService.registerExtension(blog, INavigationExtensionPoint.class, new FCKNavExtension());
        BlogPluginService.registerExtension(blog, IFCKFileManagerExtensionPoint.class, new MediaExtension());
        BlogPluginService.registerExtension(blog, IFCKFileManagerExtensionPoint.class, new ImageExtension());
        BlogPluginService.registerExtension(blog, IFCKFileManagerExtensionPoint.class, new PostExtension());
    }
}
