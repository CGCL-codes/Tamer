package calclipse.caldron.gui.operation;

import calclipse.caldron.gui.skinning.SkinningBag;
import calclipse.caldron.gui.skinning.StylablePanel;
import calclipse.caldron.gui.skinning.StylingBag;

/**
 * Skin related constants,
 * such as selectors, and skinning and styling bags.
 * The skinnable selector is the name of the package of this class.
 * The other selectors follow a common naming convention,
 * such that a constant named XXX_YYY_SELECTOR would contain the value xxxYyy.
 * @author T. Sommerland
 */
public final class SkinningConstants {

    public static final String SKINNABLE_SELECTOR = "calclipse.caldron.gui.operation";

    public static final String SUB_PANEL_SELECTOR = "subPanel";

    public static final String MAIN_PANEL_SELECTOR = "mainPanel";

    public static final String LABEL_SELECTOR = "label";

    public static final String BUTTON_SELECTOR = "button";

    public static final String PROGRESS_BAR_SELECTOR = "progressBar";

    public static final String SCROLL_PANE_SELECTOR = "scrollPane";

    public static final String LIST_SELECTOR = "list";

    /**
     * A bag to throw {@link calclipse.core.gui.skin.Stylable}s in.
     */
    public static final SkinningBag SKINNING_BAG = new SkinningBag(SKINNABLE_SELECTOR);

    public static final StylingBag<StylablePanel> SUB_PANEL_BAG = new StylingBag<StylablePanel>(SUB_PANEL_SELECTOR);

    static {
        SKINNING_BAG.add(SUB_PANEL_BAG);
    }

    private SkinningConstants() {
    }
}
