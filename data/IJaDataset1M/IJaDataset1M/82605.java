package de.schwarzrot.ui.service;

import java.util.HashMap;
import java.util.Map;
import de.schwarzrot.data.Entity;

/**
 * is a factory that serves {@code SelectionPopupProvider}s based on given
 * {@code Entity} class. Main usage is for loadable applications, that need to
 * use a selection-popup-service coded in another application. With this
 * factory, an application may register its popup-provider and all other
 * applications may use it.
 * <p>
 * The {@code SelectionPopupFactory} is itself a registered application service,
 * which is accessible through the singleton {@code ApplicationServiceProvider}.
 * 
 * @see de.schwarzrot.app.support.ApplicationServiceProvider
 * @see de.schwarzrot.ui.service.SelectionPopupProvider
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 */
@SuppressWarnings("unchecked")
public class SelectionPopupFactory {

    public <E extends Entity> SelectionPopupProvider<E> getSelectionPopupProviderFor(Class<E> entity) {
        SelectionPopupProvider<E> rv = null;
        if (providers.containsKey(entity.getName())) rv = providers.get(entity.getName());
        return rv;
    }

    public <E extends Entity> void registerSelectionPopupProvider(Class<E> entity, SelectionPopupProvider<?> service) {
        providers.put(entity.getName(), service);
    }

    @SuppressWarnings("rawtypes")
    private Map<String, SelectionPopupProvider> providers = new HashMap<String, SelectionPopupProvider>();
}
