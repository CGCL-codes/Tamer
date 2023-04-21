package twoadw.wicket.reference.state;

import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;
import twoadw.reference.state.States;

/**
 * Entity display list page.
 * 
 * @author TeamFcp
 * @version 2009-03-16
 */
public class EntityDisplayListPage extends org.modelibra.wicket.concept.EntityDisplayListPage {

    private static final long serialVersionUID = 1236723018877L;

    /**
	 * Constructs an entity display list page.
	 * 
	 * @param viewModel
	 *            view model
	 * @param view
	 *            view
	 */
    public EntityDisplayListPage(final ViewModel viewModel, final View view) {
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
        States states = (States) viewModel.getEntities();
        newViewModel.setEntities(states);
        return newViewModel;
    }
}
