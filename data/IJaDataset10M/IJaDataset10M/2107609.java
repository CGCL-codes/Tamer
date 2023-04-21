package org.kalypso.nofdpidss.ui.view.common.parts;

import org.eclipse.swt.widgets.Composite;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.common.NofdpIDSSConstants;
import org.kalypso.nofdpidss.core.view.WindowManager.WINDOW_TYPE;
import org.kalypso.nofdpidss.evaluation.rating.RatingResultPage;
import org.kalypso.nofdpidss.ui.view.evaluation.navigation.rating.RMenuPart;

/**
 * @author Dirk Kuch
 */
public class ViewPartRatingResults extends AbstractViewPart {

    public static final String ID = NofdpIDSSConstants.NOFDP_VIEWPART_RATING_RESULTS;

    /**
   * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
   */
    @Override
    public void createPartControl(final Composite parent) {
        NofdpCorePlugin.getWindowManager().setAbstractViewPart(WINDOW_TYPE.eAdditional, this);
        if (parent.isDisposed()) return;
        draw(parent);
    }

    /**
   * @see org.kalypso.nofdpidss.core.view.IAbstractViewPart#draw(org.eclipse.swt.widgets.Composite)
   */
    public void draw(final Composite parent) {
        if (setParent(parent)) {
            final RMenuPart menuPart = (RMenuPart) NofdpCorePlugin.getWindowManager().getMenuPart();
            final RatingResultPage result = new RatingResultPage(menuPart, getBody());
            result.generateBody();
        }
    }
}
