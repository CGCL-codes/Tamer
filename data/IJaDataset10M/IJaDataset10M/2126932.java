package uk.ac.osswatch.simal.rest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestPersonAPI extends AbstractAPITest {

    @Test
    public void testAllColleaguesJSON() throws SimalAPIException {
        RESTCommand command = RESTCommand.createCommand(RESTCommand.ALL_COLLEAGUES + RESTCommand.PARAM_PERSON_ID + testDeveloperID + RESTCommand.FORMAT_JSON);
        IAPIHandler handler = SimalHandlerFactory.createHandler(command, getRepo());
        String result = handler.execute();
        assertNotNull(result);
    }

    @Test
    public void testAllColleaguesXML() throws SimalAPIException {
        final RESTCommand command = RESTCommand.createCommand(RESTCommand.ALL_COLLEAGUES + RESTCommand.PARAM_PERSON_ID + testDeveloperID + RESTCommand.FORMAT_XML);
        final IAPIHandler handler = SimalHandlerFactory.createHandler(command, getRepo());
        String result = handler.execute();
        assertNotNull("No XML Returned by getAllColleagues", result);
        assertFalse("There should be no people with null IDs", result.contains("id=\"null\""));
        assertFalse("The viewer should not be in the viewer friends list", result.contains("<friend>15</friend>"));
    }

    @Test
    public void testGetPerson() throws SimalAPIException {
        RESTCommand command = RESTCommand.createCommand(RESTCommand.PERSON + RESTCommand.PARAM_PERSON_ID + testDeveloperID + RESTCommand.FORMAT_XML);
        IAPIHandler handler = SimalHandlerFactory.createHandler(command, getRepo());
        String result = handler.execute();
        assertNotNull("No XML Returned by getPerson", result);
        assertTrue("XML file does not appear to describe a person", result.contains("Person>"));
    }

    @Test
    public void testGetAllPeopleAsJSON() throws SimalAPIException {
        RESTCommand command = RESTCommand.createCommand(RESTCommand.ALL_PEOPLE + RESTCommand.FORMAT_JSON);
        IAPIHandler handler = SimalHandlerFactory.createHandler(command, getRepo());
        String result = handler.execute();
        assertNotNull("No JSON Returned by getAllPeoplen", result);
        assertTrue("JSON does not include person name: JSON = " + result, result.contains("\"label\":\"Joe Blogs Maintainer\""));
    }

    @Test
    public void testGetPersonByEmail() throws SimalAPIException {
        RESTCommand command = RESTCommand.createCommand(RESTCommand.PERSON + RESTCommand.PARAM_PERSON_EMAIL + testDeveloperEMail + RESTCommand.FORMAT_XML);
        IAPIHandler handler = SimalHandlerFactory.createHandler(command, getRepo());
        String result = handler.execute();
        assertNotNull("No XML Returned by getPerson", result);
        assertTrue("XML file does not appear to describe a person", result.contains("Person>"));
    }
}
