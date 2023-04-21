package net.sourceforge.javautil.classloader.impl;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.javautil.common.ClassNameUtil;

/**
 * Composite information about a package and facilities to get information
 * about parent packages.
 * 
 * @author ponder
 * @author $Author: ponderator $
 * @version $Id: PackageSearchInfo.java 588 2009-08-18 17:52:25Z ponderator $
 */
public class PackageSearchInfo {

    private final String packageName;

    private final String packagePath;

    private final List<String> packages = new ArrayList<String>();

    /**
	 * @param packageName The package name to encapsulate
	 */
    public PackageSearchInfo(String packageName) {
        this.packageName = packageName;
        this.packagePath = ClassNameUtil.toRelativePackagePath(packageName);
        this.packages.addAll(ClassNameUtil.getPackagesFromPackage(packageName));
    }

    /**
	 * @return The full package name for the search
	 */
    public String getPackageName() {
        return packageName;
    }

    /**
	 * @return The relative class path for the package
	 */
    public String getPackagePath() {
        return packagePath;
    }

    /**
   * @return The list of all packages (recursive till reaching top parent package)
   */
    public List<String> getPackages() {
        return packages;
    }
}
