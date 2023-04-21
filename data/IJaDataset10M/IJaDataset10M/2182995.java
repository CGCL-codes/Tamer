package fr.cnes.sitools.xml;

import org.restlet.data.MediaType;
import fr.cnes.sitools.AbstractDatastorageFilterPluginTestCase;
import fr.cnes.sitools.api.DocAPI;

public class DatastorageFilterPluginTestCase extends AbstractDatastorageFilterPluginTestCase {

    static {
        setMediaTest(MediaType.APPLICATION_XML);
        docAPI = new DocAPI(AuthorizationTestCase.class, "DatastorageFilterPlugin class API with XML format");
        docAPI.setActive(true);
        docAPI.setMediaTest(MediaType.APPLICATION_XML);
    }

    /**
   * Default constructor
   */
    public DatastorageFilterPluginTestCase() {
        super();
    }
}
