package org.fudaa.ebli.visuallibrary;

import javax.swing.JPopupMenu;
import org.fudaa.ebli.visuallibrary.actions.EbliWidgetActionUngroup;

/**
 * @author deniger
 */
public class EbliWidgetControllerForGroup extends EbliWidgetController {

    public boolean hasAlreadyFusion = false;

    public boolean isFusionCalque = false;

    public EbliWidgetControllerForGroup(final EbliWidget _widget, final boolean isFusionClaques) {
        super(_widget, true, true, false);
        isFusionCalque = isFusionClaques;
    }

    public EbliWidgetControllerForGroup(final EbliWidget _widget) {
        this(_widget, false);
    }

    @Override
    protected void buildPopupMenu(final JPopupMenu _menu) {
        super.buildPopupMenu(_menu);
        _menu.add(new EbliWidgetActionUngroup(getWidget().getEbliScene(), node_));
    }
}
