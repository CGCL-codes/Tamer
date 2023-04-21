package course.wicket.lecture.slide;

import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;
import course.lecture.slide.Slides;

/**
 * Entity display table page.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-12-03
 */
public class EntityDisplayTablePage extends org.modelibra.wicket.concept.EntityDisplayTablePage {

    private static final long serialVersionUID = 1176413490739L;

    /**
	 * Constructs an entity display table page.
	 * 
	 * @param viewModel
	 *            view model
	 * @param view
	 *            view
	 */
    public EntityDisplayTablePage(final ViewModel viewModel, final View view) {
        super(getNewViewModel(viewModel), view);
    }

    /**
	 * Gets a new view model.
	 * 
	 * @param viewModel
	 *            view model
	 * @return new view model
	 */
    private static ViewModel getNewViewModel(final ViewModel viewModel) {
        ViewModel newViewModel = new ViewModel();
        newViewModel.copyPropertiesFrom(viewModel);
        Slides slides = (Slides) viewModel.getEntities();
        newViewModel.setEntities(slides);
        return newViewModel;
    }
}
