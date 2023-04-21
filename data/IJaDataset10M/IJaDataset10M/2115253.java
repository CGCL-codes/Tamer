package calclipse.caldron.gui.theme.themes.corona.skins;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import calclipse.caldron.gui.theme.themes.corona.CoronaImageBundle;
import calclipse.caldron.gui.theme.themes.corona.UISkin;
import calclipse.caldron.gui.theme.themes.corona.ui.CoronaTabbedPaneUI;
import calclipse.core.gui.skin.Selection;
import calclipse.core.gui.skin.style.UIStyle;

/**
 * The skin used for tabbed panes.
 * @author T. Sommerland
 */
public class TabbedPaneSkin extends UISkin {

    public TabbedPaneSkin(final CoronaImageBundle images, final Selection skinSelection, final Selection styleSelection) {
        super(images, skinSelection, styleSelection);
    }

    public TabbedPaneSkin(final CoronaImageBundle images) {
        this(images, new Selection(calclipse.caldron.gui.SkinningConstants.SKINNABLE_SELECTOR), new Selection("tabbedPane"));
    }

    @Override
    protected UIStyle createUI(final CoronaImageBundle images) {
        return new UIStyle() {

            @Override
            public ComponentUI getUI(final JComponent c) {
                return new CoronaTabbedPaneUI(images);
            }
        };
    }
}
