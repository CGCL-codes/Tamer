package dmeduc.wicket.weblink.question;

import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;
import dmeduc.weblink.question.Questions;

/**
 * Entity display slide page.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-11-09
 */
public class EntityDisplaySlidePage extends org.modelibra.wicket.concept.EntityDisplaySlidePage {

    private static final long serialVersionUID = 1171896744343L;

    /**
	 * Constructs an entity display slide page.
	 * 
	 * @param viewModel
	 *            model context
	 * @param view
	 *            view context
	 */
    public EntityDisplaySlidePage(final ViewModel viewModel, final View view) {
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
        Questions questions = (Questions) viewModel.getEntities();
        newViewModel.setEntities(questions);
        return newViewModel;
    }
}
