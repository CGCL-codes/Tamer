package dm.wicket.meta.domain;

import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;
import dm.meta.domain.Domains;

public class EntityDisplayTablePage extends org.modelibra.wicket.concept.EntityDisplayTablePage {

    private static final long serialVersionUID = 110110117L;

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
	 * @return view model
	 */
    private static ViewModel getNewViewModel(final ViewModel viewModel) {
        ViewModel newViewModel = new ViewModel();
        newViewModel.copyPropertiesFrom(viewModel);
        Domains domains = (Domains) viewModel.getEntities();
        domains = (Domains) domains.getDomainsOrderedByCode();
        newViewModel.setEntities(domains);
        return newViewModel;
    }
}
