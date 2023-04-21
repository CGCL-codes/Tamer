package it.cnr.stlab.xd.plugin;

import it.cnr.stlab.xd.selection.IPatternSelectionService;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A central registry of pattern selection systems
 * 
 * @author Enrico Daga
 * @since 20/07/2009
 */
public final class SelectionServiceRegistry {

    private Set<IPatternSelectionService> serviceSet = null;

    private static SelectionServiceRegistry registry = null;

    private SelectionServiceRegistry() {
        if (registry != null) throw new RuntimeException("This class is a singleton!");
        serviceSet = new HashSet<IPatternSelectionService>();
        registry = this;
    }

    public static SelectionServiceRegistry getInstance() {
        if (registry == null) registry = new SelectionServiceRegistry();
        return registry;
    }

    /**
	 * 
	 * @param service
	 */
    public void addPatternSelectionService(IPatternSelectionService service) {
        serviceSet.add(service);
    }

    public synchronized Iterator getIterator() {
        return serviceSet.iterator();
    }
}
