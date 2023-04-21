package genomemap.data.provider.impl.organismdata;

import provider.impl.genomemap.data.OrganismDataProviderImpl;
import commons.provider.ProviderException;
import java.io.File;
import java.net.URISyntaxException;
import org.junit.Test;

/**
 * @version 1.0 Jan 4, 2012
 * @author Susanta Tewari
 */
public class OrganismDataProviderImplTest {

    @Test
    public void testCreateData() throws ProviderException, URISyntaxException {
        OrganismDataProviderImpl impl = new OrganismDataProviderImpl();
        File dir = new File(getClass().getResource("assignment.xml").toURI()).getParentFile();
        impl.setDataDirectory(dir);
        impl.setLinkageGroups(impl.getAvailableLinkageGroups());
        impl.create();
    }
}
