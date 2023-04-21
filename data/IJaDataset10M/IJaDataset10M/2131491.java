package net.sf.container;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/**
 * Container factories provide with interfaces to instantiate {@link Container}.
 *
 * <p>
 * Factories are providing with a set of container policies based on some
 * criteria, in a similar to <code>java.security.Policy</code> way.
 *
 * <p>
 * Actual policy algorithm is a subject to the particular factory implementation.
 *
 * created on Jun 9, 2005
 * @author fiykov
 * @version $Revision: 1.1 $
 * @since
 *
 * @see net.sf.container.imp.CodebaseFactory
 * @see net.sf.container.imp.CertificateAuthorityFactory
 */
public interface ContainerFactory {

    /**
	 * set policies to be used by this factory while creating containers
	 *
	 * <p>
	 * it uses a default policy file parser.
	 *
	 * @param policyFile with all permission sets
	 * @throws FileNotFoundException in case the resource does not exists
	 * @throws PolicyParseException if the fail format is not recognized
	 * by the parser
	 * @throws IOException in case of error while reading
	 */
    public void setPolicies(URL policyFile) throws FileNotFoundException, PolicyParseException, IOException;

    /**
	 * set policies to be used by this factory while creating containers
	 *
	 * @param policyFile with all permission sets
	 * @param parser to use to parse the <code>policyFile</code>
	 * @throws FileNotFoundException in case the resource does not exists
	 * @throws PolicyParseException if the fail format is not recognized
	 * by the parser
	 * @throws IOException in case of error while reading
	 */
    public void setPolicies(URL policyFile, PolicyParser parser) throws FileNotFoundException, PolicyParseException, IOException;

    /**
	 * create new container based on given permission set criteria
	 *
	 * <p>
	 * permission set criteria is implementation specific.
	 *
	 * @param permissionsCriteria to be used for that container. The object
	 * type of <code>permissionsCriteria</code> depends on the particular
	 * <code>ContainerFactory</code> implementation.
	 * @return a new <code>Container</code> instance
	 */
    public Container newContainer(Object permissionsCriteria);
}
