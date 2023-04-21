package inc.che.jarinspector;

import inc.che.common.config.Config;
import inc.che.common.gui.ConfigDialog;
import inc.che.common.resource.ResourceManager;
import java.awt.Color;
import java.util.Locale;
import javax.swing.JFrame;
import org.apache.log4j.Logger;

/**
 *  <b>@todo</b>
 * @version $Id: JarInspectorConfigDialog.java,v 1.1 2005/03/06 12:56:57 stevemcmee Exp $
 * @author <address> Steve McMee &lt;stevemcmee@sourceforge.net&gt; </address>
 */
public class JarInspectorConfigDialog extends ConfigDialog {

    /** CVS ID of this file */
    public static final String CVS_ID = "$Id: JarInspectorConfigDialog.java,v 1.1 2005/03/06 12:56:57 stevemcmee Exp $";

    /** logger instance for this class */
    private static Logger log = Logger.getLogger(JarInspectorConfigDialog.class);

    /**
     * The ResourceManager
     */
    private static ResourceManager resourceManager = ResourceManager.getResourceManager(StringResources.TEXT_RESOURCES);

    private Color colorSearch = config.getColorParameter("color.highlight.search");

    private Color colorHit = config.getColorParameter("color.highlight.hit");

    public JarInspectorConfigDialog(JFrame owner, Config config) {
        super(owner, config, config.getLocaleParameter("locale", Locale.GERMAN));
        PropertyGroup propGroup = null;
        setAvailableLocales(new Locale[] { Locale.ENGLISH, Locale.GERMAN });
        propGroup = createPropertyGroup(resourceManager.getText("config.propgroup.sort"));
        propGroup.addPropertyItem("sort.jar", new ConfigDialog.PropertyItem("sort.jar", resourceManager.getText("config.sortjar.label"), ConfigDialog.BOOLEAN_PROPERTY, false, false));
        propGroup = createPropertyGroup(resourceManager.getText("config.propgroup.misc"));
        propGroup.addPropertyItem("sound.play", new ConfigDialog.PropertyItem("sound.play", resourceManager.getText("config.playsound.label"), ConfigDialog.BOOLEAN_PROPERTY, false, true));
        propGroup.addPropertyItem("dir.last", new ConfigDialog.PropertyItem("dir.last", resourceManager.getText("config.dirlast.label"), ConfigDialog.DIR_PROPERTY, false, false));
        propGroup = createPropertyGroup(resourceManager.getText("config.propgroup.locale"));
        propGroup.addPropertyItem("locale", new ConfigDialog.PropertyItem("locale", resourceManager.getText("config.locale.label"), ConfigDialog.LOCALE_PROPERTY, false, true));
        propGroup = createPropertyGroup(resourceManager.getText("config.propgroup.search"));
        propGroup.addPropertyItem("search.case", new ConfigDialog.PropertyItem("search.case", resourceManager.getText("config.searchcase.label"), ConfigDialog.BOOLEAN_PROPERTY, false, false));
        propGroup.addPropertyItem("color.highlight.hit", new ConfigDialog.PropertyItem("color.highlight.hit", resourceManager.getText("config.highlight.hit.label"), ConfigDialog.COLOR_PROPERTY, false, false));
        propGroup.addPropertyItem("color.highlight.search", new ConfigDialog.PropertyItem("color.highlight.search", resourceManager.getText("config.highlight.search.label"), ConfigDialog.COLOR_PROPERTY, false, false));
        propGroup.addPropertyItem("search.recursive", new ConfigDialog.PropertyItem("search.recursive", resourceManager.getText("config.rsearch.label"), ConfigDialog.BOOLEAN_PROPERTY, false, false));
    }

    protected void closeDialog() {
        if (changedColor()) {
            JarInspectorFrame.getJarInspectorFrame().setHighlighter();
        }
        super.closeDialog();
    }

    private boolean changedColor() {
        return (!this.colorSearch.equals(config.getColorParameter("color.highlight.search")) || !this.colorHit.equals(config.getColorParameter("color.highlight.hit")));
    }
}
