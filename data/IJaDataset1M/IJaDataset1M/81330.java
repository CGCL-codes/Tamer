package org.makagiga.plugins;

import static org.makagiga.commons.UI._;
import java.awt.Color;
import java.awt.Dimension;
import org.makagiga.commons.MColor;
import org.makagiga.commons.MIcon;
import org.makagiga.ghns.StuffRenderer;

/**
 * @since 2.0
 */
public class PluginRenderer extends StuffRenderer<PluginInfo> {

    private final boolean showVersion;

    boolean showType;

    public PluginRenderer(final boolean showVersion) {
        super(new Dimension(MIcon.getUISize(), MIcon.getUISize()));
        this.showVersion = showVersion;
    }

    /**
	 * @since 3.0
	 */
    protected String getDisplayName(final PluginInfo info) {
        return info.toString();
    }

    protected void onRenderStuff(final PluginInfo info) {
        setImage(info.getIcon());
        setName(getDisplayName(info));
        String summary = info.shortDescription.get();
        if (summary == null) summary = "";
        if (showType) {
            PluginType type = info.type.get();
            if (type != null) summary = "[" + type.getText() + "] " + summary;
        }
        setSummary(summary);
        if (showVersion && !info.isInternal()) setVersion(info.version.toString()); else setVersion(null);
        String style = "font-weight: bold";
        if (info.test.get()) setExtra(_("Test"), Color.BLUE, style); else if (info.enabled.get()) setExtra(null, null, null); else setExtra(info.isCompatible() ? _("Disabled") : _("Incompatible"), MColor.DARK_RED, style);
    }
}
