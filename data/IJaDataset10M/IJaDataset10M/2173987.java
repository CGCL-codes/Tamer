package net.tourbook.ui.action;

import net.tourbook.data.TourData;
import net.tourbook.data.TourType;
import net.tourbook.tour.TourManager;
import net.tourbook.tour.TourTypeMenuManager;
import net.tourbook.ui.ITourProvider;
import net.tourbook.ui.UI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

class ActionSetTourType extends Action {

    private TourType _tourType;

    private ITourProvider _tourProvider;

    private boolean _isSaveTour;

    /**
	 * @param tourType
	 * @param tourProvider
	 * @param isSaveTour
	 *            when <code>true</code> the tour will be saved and a
	 *            {@link TourManager#TOUR_CHANGED} event is fired, otherwise the {@link TourData}
	 *            from the tour provider is only updated
	 * @param isChecked
	 */
    public ActionSetTourType(final TourType tourType, final ITourProvider tourProvider, final boolean isSaveTour, final boolean isChecked) {
        super(tourType.getName(), AS_CHECK_BOX);
        if (isChecked == false) {
            final Image tourTypeImage = UI.getInstance().getTourTypeImage(tourType.getTypeId());
            setImageDescriptor(ImageDescriptor.createFromImage(tourTypeImage));
        }
        setChecked(isChecked);
        setEnabled(isChecked == false);
        _tourType = tourType;
        _tourProvider = tourProvider;
        _isSaveTour = isSaveTour;
    }

    @Override
    public void run() {
        TourTypeMenuManager.setTourTypeIntoTour(_tourType, _tourProvider, _isSaveTour);
    }
}
