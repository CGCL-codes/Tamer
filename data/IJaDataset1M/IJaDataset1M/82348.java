package twoadw.wicket.website.status;

import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;
import twoadw.website.status.Statuss;

/**
 * Entity display slide page.
 * 
 * @author TeamFcp
 * @version 2009-03-16
 */
public class EntityDisplaySlidePage extends org.modelibra.wicket.concept.EntityDisplaySlidePage {

    private static final long serialVersionUID = 1235990711429L;

    /**
	 * Constructs an entity display slide page.
	 * 
	 * @param viewModel
	 *            view model
	 * @param view
	 *            view
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
        Statuss statuss = (Statuss) viewModel.getEntities();
        newViewModel.setEntities(statuss);
        return newViewModel;
    }
}
