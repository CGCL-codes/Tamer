package peertrust.filter.interfaces;

import java.util.Set;
import com.hp.hpl.jena.shared.JenaException;

/**
 * Checks if a resource is protected or if the client fulfills the policies
 * protecting it.
 * @author Sebastian Wittler
 */
public interface IProtectedResourceChecker {

    public void loadPolicies(String strSchemaFile, String strModelFile) throws JenaException;

    /**
	 * Gets policies that protect a given resource.
	 * @param strResource The resource.
	 * @return Policies that protect this resource, null if unprotected.
	 * @throws JenaException
	 */
    public Set getPolicyForResource(String strResource) throws JenaException;
}
