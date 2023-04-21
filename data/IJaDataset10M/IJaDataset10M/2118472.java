package net.sourceforge.javautil.classloader.util;

import java.util.Map;
import net.sourceforge.javautil.classloader.resolver.ClassPackageResolverNetworkNode;
import net.sourceforge.javautil.classloader.resolver.IClassPackage;
import net.sourceforge.javautil.classloader.resolver.IClassPackageReference;
import net.sourceforge.javautil.common.ui.IUserInterfaceTool;

/**
 * An interface for implementations that can provide authorization for download
 * of {@link IClassPackage}'s via some type of UI.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface IClassPackageDownloadAuthorizer extends IUserInterfaceTool {

    /**
	 * @param requiredPackages A map of packages, the key being the reference about the package, and the value 
	 *  information about where the package will be downloaded from.
	 * 
	 * @return True if the downloads have been authorized, otherwise no
	 */
    boolean authorize(ClassPackageResolverNetworkNode... requiredPackages);

    /** 
	 * @param download The download about to start downloading
	 */
    void downloadStarted(ClassPackageResolverNetworkNode download);

    /**
	 * @param download The download that was in progress
	 * @param error The error that occurred while downloading
	 * 
	 * @return True if the download should be attempted again, otherwise false
	 */
    boolean downloadError(ClassPackageResolverNetworkNode download, Throwable error);

    /**
	 * @param download The download that has finished downloading
	 */
    void downloadFinished(ClassPackageResolverNetworkNode download);
}
