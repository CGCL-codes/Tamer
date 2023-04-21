package de.mpiwg.vspace.metamodel.application;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.emf.common.notify.AdapterFactory;
import de.mpiwg.vspace.common.project.ProjectObservable;
import de.mpiwg.vspace.metamodel.provider.ExhibitionItemProviderAdapterFactory;

public class AdapterFactoryRegistrant implements Observer {

    public void update(Observable arg0, Object arg1) {
        if (arg0 instanceof ProjectObservable && arg1 instanceof Integer && (Integer) arg1 == ProjectObservable.PROJECT_OPENED) {
            List<AdapterFactory> factories = ProjectObservable.INSTANCE.getEditingDomain().getResourceSet().getAdapterFactories();
            boolean contains = false;
            for (AdapterFactory fac : factories) {
                if (fac instanceof ExhibitionItemProviderAdapterFactory) {
                    contains = true;
                    break;
                }
            }
            if (!contains) factories.add(new ExhibitionItemProviderAdapterFactory());
        }
    }
}
