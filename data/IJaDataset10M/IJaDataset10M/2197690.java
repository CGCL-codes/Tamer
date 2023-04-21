package org.extwind.osgi.repository.loader.ebr;

import org.extwind.osgi.ebr.Ebr;
import org.extwind.osgi.ebr.EbrService;
import org.extwind.osgi.repository.RepositoryLoader;
import org.extwind.osgi.repository.RepositoryLoaderProvider;

/**
 * @author donf.yang
 * 
 */
public class EBRLoaderProvider implements RepositoryLoaderProvider {

    public static final String PREFIX_EBR = "ebr:";

    @Override
    public RepositoryLoader createLoader(String repositoryLocation) throws Exception {
        if (repositoryLocation == null || !repositoryLocation.startsWith(PREFIX_EBR)) {
            throw new Exception("Invalid repository - " + repositoryLocation);
        }
        EbrService service = EbrService.defaultService;
        if (service == null) {
            throw new Exception("EBR service not found");
        }
        Ebr ebr = service.newEbr(repositoryLocation);
        return new EBRLoader(ebr);
    }

    @Override
    public String getPrefix() {
        return PREFIX_EBR;
    }
}
