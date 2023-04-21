package travel.wicket.reference.countrylanguage;

import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;
import travel.reference.countrylanguage.CountryLanguages;

/**
 * Entity display list page.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-12-04
 */
public class EntityDisplayListPage extends org.modelibra.wicket.concept.EntityDisplayListPage {

    private static final long serialVersionUID = 1176319588573L;

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
        CountryLanguages countryLanguages = (CountryLanguages) viewModel.getEntities();
        newViewModel.setEntities(countryLanguages);
        return newViewModel;
    }
}
