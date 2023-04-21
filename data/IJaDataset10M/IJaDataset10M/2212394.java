package com.sri.emo.wizard.completion.management;

import com.jcorporate.expresso.core.controller.ExpressoResponse;
import com.jcorporate.expresso.core.db.DBException;
import com.sri.emo.dbobj.WizDefinition;
import com.sri.emo.wizard.completion.model.CompletionBean;
import com.sri.emo.wizard.completion.model.CompletionPartsBean;
import com.sri.emo.wizard.completion.persistence.CompletionDBObjConverterImpl;
import com.sri.emo.wizard.completion.persistence.ExpressoCompletionRepository;
import java.util.*;

/**
 * @author Michael Rimov
 * @version 1.0
 */
public class TestDoChooseCriteria extends ChooseCompletionTestBase {

    final int starwarsId = 4;

    private NewCompletionFixtureTemplate completionTemplate;

    private static class DoChooseCriteriaCompletionFixture extends NewCompletionFixtureTemplate {

        public DoChooseCriteriaCompletionFixture(String stateName, int targetNodeId) {
            super(stateName, targetNodeId);
        }

        protected void modifyParameters(Map<String, String> parameters) {
            parameters.put("0_freeText", "checkbox");
            parameters.put("0_minEntries", "1");
            parameters.put("0_maxEntries", "1");
            parameters.put("0_directive", "");
            parameters.put("0_helpText", "");
            parameters.put("2_minEntries", "1");
            parameters.put("2_maxEntries", "1");
            parameters.put("2_directive", "Please select a value for User Rating");
            parameters.put("2_helpText", "Ratings are 1-5 stars. Since this is your own movie, We'd strongly suggest that you give it a '5'.");
            parameters.put("3_minEntries", "3");
            parameters.put("3_maxEntries", "5");
            parameters.put("3_directive", "Please enter some user reviews.");
            parameters.put("3_helpText", "See imdb.com for some examples.");
            parameters.put("4_minEntries", "0");
            parameters.put("4_maxEntries", "0");
            parameters.put("4_directive", "Please select the top three actors to receive billing.");
            parameters.put("4_helpText", "");
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        completionTemplate = new DoChooseCriteriaCompletionFixture(DoChooseCriteria.STATE_NAME, starwarsId);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        completionTemplate = null;
    }

    public void testHandleAddRequest() throws Exception {
        int beforeCount = new WizDefinition().count();
        ExpressoResponse response = completionTemplate.invokeController();
        assertNotNull(response);
        assertTrue("Received Errors: " + response.getErrors(), response.getErrors() == null || response.getErrors().size() == 0);
        assertEquals((beforeCount + 1), new WizDefinition().count());
    }

    public void testHandleAddRequestGeneratesProperValues() throws Exception {
        int oneId = this.createAddedWizardAndGetId();
        ExpressoCompletionRepository completion = new ExpressoCompletionRepository(new CompletionDBObjConverterImpl());
        CompletionBean cb = completion.findById(oneId);
        assertTrue(cb != null);
        assertEquals(oneId, cb.getWizardId().intValue());
        HashSet<CompletionPartsBean> completionParts = new HashSet<CompletionPartsBean>(cb.getWizardCompletionParts());
        assertNotNull(completionParts);
        assertTrue(completionParts.size() == 3);
        for (Iterator<CompletionPartsBean> i = completionParts.iterator(); i.hasNext(); ) {
            CompletionPartsBean onePart = i.next();
            assertNotNull(onePart);
            assertNotNull(onePart.getPart());
            assertNotNull(onePart.getMinEntries());
            assertNotNull(onePart.getMaxEntries());
            switch(onePart.getPart().getPartNumInt()) {
                case 0:
                    assertEquals(1, onePart.getMinEntries().intValue());
                    assertEquals(1, onePart.getMaxEntries().intValue());
                    assertEquals("", onePart.getDirective());
                    assertEquals("", onePart.getHelpText());
                    break;
                case 2:
                    assertEquals(1, onePart.getMinEntries().intValue());
                    assertEquals(1, onePart.getMaxEntries().intValue());
                    assertEquals("Please select a value for User Rating", onePart.getDirective());
                    assertEquals("Ratings are 1-5 stars. Since this is your own movie, We'd strongly suggest " + "that you give it a '5'.", onePart.getHelpText());
                    break;
                case 3:
                    assertEquals(3, onePart.getMinEntries().intValue());
                    assertEquals(5, onePart.getMaxEntries().intValue());
                    break;
                case 4:
                    assertEquals(0, onePart.getMinEntries().intValue());
                    assertEquals(0, onePart.getMaxEntries().intValue());
                    break;
                default:
                    fail("Encountered unknown part");
                    break;
            }
        }
    }

    private Set loadAllWizardIds() throws DBException {
        Set<String> allPreviousIds = new HashSet<String>();
        List allWizards = new WizDefinition().searchAndRetrieveList();
        for (Iterator i = allWizards.iterator(); i.hasNext(); ) {
            WizDefinition oneWizDef = (WizDefinition) i.next();
            allPreviousIds.add(oneWizDef.getId());
        }
        return allPreviousIds;
    }

    public void testHandleBadMinMaxEntries() throws Exception {
        completionTemplate = new DoChooseCriteriaCompletionFixture(DoChooseCriteria.STATE_NAME, starwarsId) {

            protected void modifyParameters(final Map<String, String> parameters) {
                super.modifyParameters(parameters);
                parameters.put("0_freeText", "checkbox");
                parameters.put("0_minEntries", "able");
                parameters.put("0_maxEntries", "baker");
                parameters.put("0_directive", "");
                parameters.put("0_helpText", "");
            }
        };
        ExpressoResponse response = completionTemplate.invokeController();
        assertNotNull("Should be errors in submitted form.", response.getErrors());
        assertTrue("Should be one error for each bad min/max entry (2).  Instead got: " + response.getErrors().getErrorCount() + " with data: " + response.getErrors().toString(), response.getErrors().getErrorCount() == 2);
    }

    /**
     * 'Fixture' that creates a wizard with the default values from the completion template,
     * saves it, and returns the created id by checking what's in the database
     * afterwards.
     *
     * @return int the created wizard id.
     * @throws Exception upon error.
     */
    private int createAddedWizardAndGetId() throws Exception {
        Set allPreviousIds = loadAllWizardIds();
        ExpressoResponse response = completionTemplate.invokeController();
        assertTrue("Received Errors: " + response.getErrors(), response.getErrors() == null || response.getErrors().size() == 0);
        Set allAfterIds = loadAllWizardIds();
        assertTrue(allAfterIds.removeAll(allPreviousIds));
        assertTrue(allAfterIds.size() == 1);
        String oneId = (String) allAfterIds.iterator().next();
        return Integer.parseInt(oneId);
    }

    public void testHandleUpdateRequest() throws Exception {
        int oneId = createAddedWizardAndGetId();
        ExistingWizardFixtureTemplate existingTemplate = new ExistingWizardFixtureTemplate(DoChooseCriteria.STATE_NAME, oneId) {

            protected void modifyParameters(Map<String, String> parameters) {
                parameters.put("0_freeText", "checkbox");
                parameters.put("0_minEntries", "1");
                parameters.put("0_maxEntries", "1");
                parameters.put("0_directive", "New Directive");
                parameters.put("0_helpText", "New Helptext");
                parameters.put("2_minEntries", "1");
                parameters.put("2_maxEntries", "1");
                parameters.put("2_directive", "Please select a value for User Rating");
                parameters.put("2_helpText", "Ratings are 1-5 stars. Since this is your own movie, We'd " + "strongly suggest that you give it a '5'.");
                parameters.put("3_minEntries", "0");
                parameters.put("3_maxEntries", "10");
                parameters.put("3_directive", "Please enter some user reviews.");
                parameters.put("3_helpText", "See imdb.com for some examples.");
                parameters.put("4_minEntries", "0");
                parameters.put("4_maxEntries", "0");
                parameters.put("4_directive", "Please select the top three actors to receive billing.");
                parameters.put("4_helpText", "");
            }
        };
        ExpressoResponse response = existingTemplate.invokeController();
        assertNotNull(response);
        assertTrue("Should have no errors updating.", response.getErrors() == null || response.getErrors().getErrorCount() == 0);
        ExpressoCompletionRepository completion = new ExpressoCompletionRepository(new CompletionDBObjConverterImpl());
        CompletionBean cb = completion.findById(oneId);
        assertNotNull("Should have found completion bean", cb);
        assertTrue(cb != null);
        assertEquals(oneId, cb.getWizardId().intValue());
        HashSet<CompletionPartsBean> completionParts = new HashSet<CompletionPartsBean>(cb.getWizardCompletionParts());
        assertNotNull(completionParts);
        assertTrue(completionParts.size() == 3);
        for (Iterator<CompletionPartsBean> i = completionParts.iterator(); i.hasNext(); ) {
            CompletionPartsBean onePart = i.next();
            assertNotNull(onePart);
            assertNotNull(onePart.getPart());
            assertNotNull(onePart.getMinEntries());
            assertNotNull(onePart.getMaxEntries());
            switch(onePart.getPart().getPartNumInt()) {
                case 0:
                    assertEquals(1, onePart.getMinEntries().intValue());
                    assertEquals(1, onePart.getMaxEntries().intValue());
                    assertEquals("New Directive", onePart.getDirective());
                    assertEquals("New Helptext", onePart.getHelpText());
                    break;
                case 2:
                    assertEquals(1, onePart.getMinEntries().intValue());
                    assertEquals(1, onePart.getMaxEntries().intValue());
                    assertEquals("Please select a value for User Rating", onePart.getDirective());
                    assertEquals("Ratings are 1-5 stars. Since this is your own movie, We'd strongly suggest " + "that you give it a '5'.", onePart.getHelpText());
                    break;
                case 3:
                    assertEquals(0, onePart.getMinEntries().intValue());
                    assertEquals(10, onePart.getMaxEntries().intValue());
                    break;
                case 4:
                    assertEquals(0, onePart.getMinEntries().intValue());
                    assertEquals(0, onePart.getMaxEntries().intValue());
                    break;
                default:
                    fail("Encountered unknown part");
                    break;
            }
        }
    }

    public void testBeanGetsClearedAfterSave() throws Exception {
        ExpressoResponse response = completionTemplate.invokeController();
        assertTrue(response.getErrors() == null || response.getErrors().getErrorCount() == 0);
        assertNull("Completion Bean should have been cleared from session after save.", response.getExpressoRequest().getSession().getPersistentAttribute(CompletionBeanManager.BEAN_NAME));
    }
}
