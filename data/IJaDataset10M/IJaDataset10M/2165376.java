package fr.cnes.sitools.ext.jeobrowser.tests;

/**
 * Test the JeoBrowser resource
 * 
 * @author m.gond
 */
public class JeoSearchResourceTestCase extends JeoSearchSvaTestCase {

    /** The url of the search resource */
    private String searchResourceUrl = "/search";

    @Override
    public String getServiceUrl() {
        return getHostUrl() + getDatasetUrl() + searchResourceUrl;
    }
}
