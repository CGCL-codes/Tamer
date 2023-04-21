package org.targol.warfocdamanager.ui.views.gametypes;

import org.eclipse.swt.graphics.Image;
import org.targol.warfocdamanager.core.model.GameType;
import org.targol.warfocdamanager.ui.utils.Images;
import org.targol.warfocdamanager.ui.views.AbstractTreeViewerLabelProvider;

/**
 * Label Provider for {@link CurrenciesView}.
 * 
 * @author mhardy
 */
public class GameTypesViewLabelProvider extends AbstractTreeViewerLabelProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public final Image getImageImpl(final Object obj) {
        final GameType proj = (GameType) obj;
        return Images.getImageForGameType(proj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final String getTextImpl(final Object obj) {
        final GameType proj = (GameType) obj;
        return proj.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Image getToolTipImage(final Object object) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getToolTipText(final Object element) {
        final GameType proj = (GameType) element;
        return proj.getDescription();
    }
}
