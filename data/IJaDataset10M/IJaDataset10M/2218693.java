package junit.extensions.repo;

import java.net.URL;

/**
 * Repository that provides access to resources in a subpackage.
 * This repository is identical to <code>PackageRepo</code> in
 * functionality, except that it may be more convenient because the name of
 * the subpackage does not have to be specified with every request to
 * <code>getFile(String)</code> or <code>getResource(String)</code>.
 *
 * @author <a href="mailto:lop@users.sourceforge.net">Alexander Anderson</a>
 * @version $Id: SubpackageRepo.java,v 1.1.1.1 2002/04/26 04:55:44 lop Exp $
 */
public class SubpackageRepo extends PackageRepo {

    /** The subpackage. */
    private String path;

    /**
     * Constructs a new instance of this repository.
     * @param path the name of the subpackage as a string of '/'-separated
     *             package names under the desired package of the "outside
     *             caller".
     */
    public SubpackageRepo(String path) {
        this.path = path;
    }

    /**
     * Returns package resource from the package of the "outside
     * caller" with the name constructed from concatenating the
     * <code>path</code>, a forward slash character (<code>'/'</code>),
     * and the specified <code>name</code>.
     */
    public URL getResource(String name) {
        return getCallerClass().getResource(path + '/' + name);
    }
}
