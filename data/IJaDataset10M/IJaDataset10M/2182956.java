package net.sourceforge.blogentis.plugins.editor.impl;

import net.sourceforge.blogentis.om.Blog;
import net.sourceforge.blogentis.plugins.editor.IFCKConfigurationExtension;
import net.sourceforge.blogentis.prefs.AbstractPrefsExtension;
import net.sourceforge.blogentis.prefs.IGroup;
import net.sourceforge.blogentis.prefs.IGroupFactory;
import net.sourceforge.blogentis.prefs.IPrefsExtensionPoint;
import net.sourceforge.blogentis.prefs.ITemplateGroup;
import net.sourceforge.blogentis.prefs.PrefsService;
import net.sourceforge.blogentis.prefs.impl.UserPrefsImpl;
import net.sourceforge.blogentis.prefs.settings.BooleanInput;
import net.sourceforge.blogentis.prefs.settings.StringInput;

public class FCKPrefsExtension extends AbstractPrefsExtension {

    public static final String FCK_USER_ENABLED = "fck.enable";

    public static final String FCK_BLOG_ENABLED = "fck.enable";

    public static final String FCK_BLOG_SKIP_TAGS = "fck.skip";

    public static final String FCK_DEFAULT_LANGUAGE = "fck.defalutLanguage";

    private static final IGroupFactory EditorPrefsFactory = new IGroupFactory() {

        public IGroup build(Blog blog, String groupName) {
            IGroup g = PrefsService.createGroup(blog, ITemplateGroup.class);
            g.setTitle("FCK Editor");
            g.setDescription("Settings regarding the FCK Editor plugin.");
            g.setName("FCKEditor");
            BooleanInput b = new BooleanInput(FCK_BLOG_ENABLED, "Use the FCK Editor", " If enabled, use the FCK WYSIWYG editor" + " instead of plain textareas. Needs" + " Javascript to work. Will fall back automatically" + " to textarea for browsers with javascript disabled.");
            b.setDefault(true);
            g.addSetting(b);
            StringInput s = new StringInput(FCK_BLOG_SKIP_TAGS, "Don't enable for:", " a space-separated list of the text areas " + "(by tag ID) that should remain" + " plain textareas." + " If you don't want the edditor to appear for" + " the comments area, enter <em>commentArea</em>");
            s.setDefault("");
            g.addSetting(s);
            s = new StringInput(FCK_DEFAULT_LANGUAGE, "Default language", " the default language of the rich text editor." + " Leave blank to use the default language of the blog.");
            s.setDefault("");
            g.addSetting(s);
            return g;
        }
    };

    public void adaptGroup(Blog blog, IGroup group, String groupName) {
        if (groupName.equals(UserPrefsImpl.USER_SETTINGS_EDIT)) {
            BooleanInput b = new BooleanInput(FCK_USER_ENABLED, "Use the FCK Editor", " If enabled, use the FCK WYSIWYG editor" + " instead of plain textareas. Needs" + " Javascript to work.");
            b.setEnabledDisabledStyle();
            b.setDefault(true);
            group.addSetting(b);
        }
    }

    public String getName() {
        return "FCK Editor preferences";
    }

    public void registerGroups(IPrefsExtensionPoint service) {
        service.registerGroupFactory(EditorPrefsFactory, IFCKConfigurationExtension.EDITOR_PREFS);
    }
}
