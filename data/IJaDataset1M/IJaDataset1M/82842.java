package net.sf.joafip.store.service.synchronizer;

import java.util.Collection;
import java.util.List;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.store.service.objectio.manager.ISubsituteSynchronizer;

/**
 * synchronize {@link List} implementation
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class ListSynchronizer implements ISubsituteSynchronizer {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void synchronizeSubstitute(final Object object, final Object objectSubstitute) {
        final Collection sourceList = (Collection) object;
        final List targetList = (List) objectSubstitute;
        targetList.addAll(sourceList);
    }
}
