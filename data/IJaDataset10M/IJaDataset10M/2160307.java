package ag.ion.noa4e.internal.search.core;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.MultiStatus;

/**
 * Calcluator for the search effort. This class is nearly a mirror
 * of the class org.eclipse.search.internal.core.text.AmountOfWorkCalculator.
 * 
 * @author Andreas Br�ker
 * @version $Revision: 9195 $
 * @date 10.07.2006
 */
public class EffortCalculator implements IResourceProxyVisitor {

    private SearchScope searchScope = null;

    private MultiStatus status = null;

    private int numberOfFilesToScan = 0;

    /**
	 * Constructs new EffortCalculator.
	 * 
	 * @param searchScope search scope to be used
	 * @param status status to be used
	 * 
	 * @author Andreas Br�ker
	 * @date 10.07.2006
	 */
    public EffortCalculator(SearchScope searchScope, MultiStatus status) {
        assert searchScope != null;
        assert status != null;
        this.status = status;
        this.searchScope = searchScope;
    }

    /** 
	 * Visits the given resource.
	 *
	 * @param proxy for requesting information about the resource being visited;
	 * this object is only valid for the duration of the invocation of this
	 * method, and must not be used after this method has completed
	 * 
	 * @return <code>true</code> if the resource's members should
	 *		be visited; <code>false</code> if they should be skipped
	 *
	 * @exception CoreException if the visit fails for some reason
	 * 
	 * @author Andreas Br�ker
	 * @date 10.07.2006
	 */
    public boolean visit(IResourceProxy proxy) {
        if (proxy.getType() != IResource.FILE) {
            return true;
        }
        if (searchScope.matchesFileName(proxy.getName())) {
            numberOfFilesToScan++;
        }
        return true;
    }

    /**
	 * Calculates the number of file to be scan.
	 * 
	 * @return number of file to be scan
	 * 
	 * @author Andreas Br�ker
	 * @date 10.07.2006
	 */
    public int calculateEffort() {
        IResource[] roots = searchScope.getRootElements();
        for (int i = 0; i < roots.length; i++) {
            try {
                roots[i].accept(this, 0);
            } catch (CoreException coreException) {
                status.add(coreException.getStatus());
            }
        }
        return numberOfFilesToScan;
    }
}
