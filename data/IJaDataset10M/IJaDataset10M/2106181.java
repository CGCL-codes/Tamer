package org.kalypso.nofdpidss.inundation.frequency.utils;

import org.eclipse.swt.graphics.Image;
import org.kalypso.nofdpidss.core.base.gml.model.common.IAnnuality;
import org.kalypso.nofdpidss.core.ui.GeoDataTreeLabelProvider;

/**
 * @author Dirk Kuch
 */
public class IDFFrequencyWaterDepthLabelProvider extends GeoDataTreeLabelProvider {

    private static Image IMG_ANNUALITY = new Image(null, IDFFrequencyWaterDepthLabelProvider.class.getResourceAsStream("icons/annuality.gif"));

    /**
   * @see org.kalypso.nofdpidss.core.ui.GeoDataTreeLabelProvider#getImage(java.lang.Object)
   */
    @Override
    public Image getImage(final Object element) {
        if (element instanceof IAnnuality) return IMG_ANNUALITY;
        return super.getImage(element);
    }

    /**
   * @see org.kalypso.nofdpidss.core.ui.GeoDataTreeLabelProvider#getText(java.lang.Object)
   */
    @Override
    public String getText(final Object element) {
        if (element instanceof IAnnuality) {
            final IAnnuality a = (IAnnuality) element;
            return String.format("Annuality: %d", a.getAnuality());
        }
        return super.getText(element);
    }
}
