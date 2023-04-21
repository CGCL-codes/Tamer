package de.fau.cs.dosis.acceptance.test;

import org.junit.Before;
import org.junit.Test;
import utils.ServerConfiguration;
import de.fau.cs.dosis.acceptance.AbstractAcceptanceTest;
import de.fau.cs.dosis.acceptance.RestletBot;

public class Feature029 extends AbstractAcceptanceTest {

    @Override
    protected ServerConfiguration getServerConfig() {
        return ServerConfiguration.RESTLET;
    }

    private RestletBot restletBot;

    @Before
    public void setup() throws Exception {
        this.initServer();
        this.initDbUnit();
        restletBot = new RestletBot(getServerConfig(), "admin", "admin");
    }

    @Test
    public void testReceiveNote() throws Exception {
        loadXMLDataSetClean("src/test/resources/acceptance/UnReviewedIoDbSet.xml");
        loadXMLDataSetUpdate("src/test/resources/acceptance/ReviewedIoDbSet.xml");
        restletBot.getPersonalNote("2675");
    }

    @Test
    public void testStoreNote() throws Exception {
        loadXMLDataSetClean("src/test/resources/acceptance/UnReviewedIoDbSet.xml");
        loadXMLDataSetUpdate("src/test/resources/acceptance/ReviewedIoDbSet.xml");
        restletBot.getPersonalNote("2675");
    }
}
