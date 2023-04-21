package sales.wicket.cheesestore.cart;

import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;
import sales.cheesestore.cart.Carts;

/**
 * Entity display slide page.
 * 
 * @author Dzenan Ridjanovic
 * @version 2009-01-05
 */
public class EntityDisplaySlidePage extends org.modelibra.wicket.concept.EntityDisplaySlidePage {

    private static final long serialVersionUID = 1231169447925L;

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
        Carts carts = (Carts) viewModel.getEntities();
        newViewModel.setEntities(carts);
        return newViewModel;
    }
}
