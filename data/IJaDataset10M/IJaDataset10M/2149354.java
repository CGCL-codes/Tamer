package provider.impl.la.data;

import provider.impl.genomemap.data.XMLTAB08DataProviderImpl;
import commons.provider.ProviderException;
import java.io.File;
import java.net.URISyntaxException;
import org.junit.Test;

/**
 * @version 1.0 Dec 20, 2011
 * @author Susanta Tewari
 */
public class XMLTAB08DataProviderImplTest {

    @Test
    public void testCreateData() throws ProviderException, URISyntaxException {
        XMLTAB08DataProviderImpl impl = new XMLTAB08DataProviderImpl();
        impl.setDataFile(new File(getClass().getResource("tab08-data1.xml").toURI()));
        impl.create();
    }
}
