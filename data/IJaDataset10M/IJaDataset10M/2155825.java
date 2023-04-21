package fr.cnes.sitools.xml;

import org.restlet.data.MediaType;
import fr.cnes.sitools.AbstractFilterNotificationTestCase;
import fr.cnes.sitools.api.DocAPI;

/**
 * Filter notification test case for XML format
 * 
 *
 * @author m.marseille (AKKA Technologies)
 */
public class FilterNotificationTestCase extends AbstractFilterNotificationTestCase {

    static {
        setMediaTest(MediaType.APPLICATION_XML);
        docAPI = new DocAPI(ConverterTestCase.class, "Converter Administration API with XML format");
        docAPI.setActive(true);
        docAPI.setMediaTest(MediaType.APPLICATION_XML);
    }

    /**
   * Default constructor
   */
    public FilterNotificationTestCase() {
        super();
    }
}
