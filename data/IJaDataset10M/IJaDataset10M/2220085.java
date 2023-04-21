package org.extwind.osgi.console.service;

import java.io.InputStream;
import org.osgi.framework.Bundle;

/**
 * @author donf.yang
 * 
 */
public interface LaunchService {

    public String getDescription();

    public boolean isReadOnly() throws Exception;

    public void refreshPackages(Bundle[] bundles) throws Exception;

    public int getBundleStartLevel(Bundle bundle) throws Exception;

    public Bundle[] getBundles() throws Exception;

    public Bundle getBundle(long id) throws Exception;

    public String getStateName(long id) throws Exception;

    public Bundle installBundle(String location) throws Exception;

    public Bundle installBundle(String location, InputStream input) throws Exception;

    public Bundle installBundle(Repository repository, String location) throws Exception;

    public String getFrameworkLocation() throws Exception;

    public FrameworkDescription getFrameworkDescription() throws Exception;

    public RepositoryFactory getRepositoryFactory() throws Exception;
}
