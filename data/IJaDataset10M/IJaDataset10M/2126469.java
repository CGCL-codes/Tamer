package macaw.test.curation;

import java.util.ArrayList;
import macaw.businessLayer.OntologyTerm;
import macaw.system.MacawErrorType;
import macaw.system.MacawException;
import macaw.util.DisplayableListSorter;

public class TestCurateOntologyTerms extends MacawCurationTestCase {

    public TestCurateOntologyTerms() {
        super("Test Ontology Term");
    }

    /**
	 * validation fails if term is left blank
	 */
    public void testValidationN1() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("");
            ontologyTerm1.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_ONTOLOGY_TERM);
            assertEquals(1, numberOfErrors);
            log.logException(exception);
        }
    }

    /**
	 * validation fails if ontology name is left blank
	 */
    public void testValidationN2() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("morbidity");
            ontologyTerm1.setOntologyName("");
            ontologyTerm1.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_ONTOLOGY_TERM);
            assertEquals(1, numberOfErrors);
            log.logException(exception);
        }
    }

    /**
	 * validation fails if ontology name space is left blank
	 */
    public void testValidationN3() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("morbidity");
            ontologyTerm1.setOntologyName("LHA");
            ontologyTerm1.setNameSpace("");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_ONTOLOGY_TERM);
            assertEquals(1, numberOfErrors);
            log.logException(exception);
        }
    }

    /**
	 * validation fails if multiple errors happen
	 */
    public void testValidationN4() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("");
            ontologyTerm1.setOntologyName("");
            ontologyTerm1.setNameSpace("");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(3, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.INVALID_ONTOLOGY_TERM);
            assertEquals(3, numberOfErrors);
        }
    }

    /**
	 * add ontology term to list
	 */
    public void testAddOntologyTermN1() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("morbidity");
            ontologyTerm1.setDescription("The rate of incidence of a disease.");
            ontologyTerm1.setOntologyName("LHA");
            ontologyTerm1.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            OntologyTerm ontologyTerm2 = new OntologyTerm();
            ontologyTerm2.setTerm("allergies");
            ontologyTerm2.setDescription("A misguided reaction to foreign substances by the immune system.");
            ontologyTerm2.setOntologyName("LHA");
            ontologyTerm2.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm2);
            OntologyTerm ontologyTerm3 = new OntologyTerm();
            ontologyTerm3.setTerm("dexterity");
            ontologyTerm3.setDescription("Skill and grace in physical movement, especially in the use of the hands.");
            ontologyTerm3.setOntologyName("LHA");
            ontologyTerm3.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm3);
            ArrayList<OntologyTerm> termsSoFar = curationService.getAllOntologyTerms(demoUser);
            DisplayableListSorter<OntologyTerm> sorter = new DisplayableListSorter<OntologyTerm>();
            sorter.sort(termsSoFar);
            assertEquals(3, termsSoFar.size());
            assertEquals("allergies-LHA", termsSoFar.get(0).getDisplayName());
            assertEquals("dexterity-LHA", termsSoFar.get(1).getDisplayName());
            assertEquals("morbidity-LHA", termsSoFar.get(2).getDisplayName());
        } catch (MacawException exception) {
            log.logException(exception);
            fail();
        }
    }

    /**
	 * add two ontology terms that have the same term but come from different
	 * ontology names 
	 */
    public void testAddOntologyTermA1() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("dexterity");
            ontologyTerm1.setDescription("Skill and grace in physical movement, especially in the use of the hands.");
            ontologyTerm1.setOntologyName("LHA");
            ontologyTerm1.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            OntologyTerm ontologyTerm2 = new OntologyTerm();
            ontologyTerm2.setTerm("dexterity");
            ontologyTerm2.setDescription("Mental skill or adroitness; cleverness.");
            ontologyTerm2.setOntologyName("CP");
            ontologyTerm2.setNameSpace("www.cognitive-psychology.org");
            curationService.addOntologyTerm(demoUser, ontologyTerm2);
            ArrayList<OntologyTerm> termsSoFar = curationService.getAllOntologyTerms(demoUser);
            DisplayableListSorter<OntologyTerm> sorter = new DisplayableListSorter<OntologyTerm>();
            sorter.sort(termsSoFar);
            assertEquals(2, termsSoFar.size());
            assertEquals("dexterity-CP", termsSoFar.get(0).getDisplayName());
            assertEquals("dexterity-LHA", termsSoFar.get(1).getDisplayName());
        } catch (MacawException exception) {
            log.logException(exception);
            fail();
        }
    }

    /**
	 * add two ontology terms that have the same term and the same ontology
	 * names 
	 */
    public void testAddOntologyTermE1() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("dexterity");
            ontologyTerm1.setDescription("Skill and grace in physical movement, especially in the use of the hands.");
            ontologyTerm1.setOntologyName("LHA");
            ontologyTerm1.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            OntologyTerm ontologyTerm2 = new OntologyTerm();
            ontologyTerm2.setTerm("dexterity");
            ontologyTerm2.setDescription("Mental skill or adroitness; cleverness.");
            ontologyTerm2.setOntologyName("LHA");
            ontologyTerm2.setNameSpace("www.cognitive-psychology.org");
            curationService.addOntologyTerm(demoUser, ontologyTerm2);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.DUPLICATE_ONTOLOGY_TERM);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * update ontology term to list
	 */
    public void testUpdateOntologyTermN1() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("morbidity");
            ontologyTerm1.setDescription("The rate of incidence of a disease.");
            ontologyTerm1.setOntologyName("LHA");
            ontologyTerm1.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            int identifier = curationService.getOntologyTermIdentifier(demoUser, ontologyTerm1);
            ontologyTerm1.setIdentifier(identifier);
            ontologyTerm1.setTerm("aaa");
            curationService.updateOntologyTerm(demoUser, ontologyTerm1);
            ArrayList<OntologyTerm> ontologyTermsSoFar = curationService.getAllOntologyTerms(demoUser);
            assertEquals("aaa", ontologyTermsSoFar.get(0).getTerm());
            ontologyTerm1.setDescription("bbb bbb");
            curationService.updateOntologyTerm(demoUser, ontologyTerm1);
            ontologyTermsSoFar = curationService.getAllOntologyTerms(demoUser);
            assertEquals("bbb bbb", ontologyTermsSoFar.get(0).getDescription());
            ontologyTerm1.setOntologyName("bbb bbb");
            curationService.updateOntologyTerm(demoUser, ontologyTerm1);
            ontologyTermsSoFar = curationService.getAllOntologyTerms(demoUser);
            assertEquals("bbb bbb", ontologyTermsSoFar.get(0).getOntologyName());
            ontologyTerm1.setNameSpace("ccc");
            curationService.updateOntologyTerm(demoUser, ontologyTerm1);
            ontologyTermsSoFar = curationService.getAllOntologyTerms(demoUser);
            assertEquals("ccc", ontologyTermsSoFar.get(0).getNameSpace());
        } catch (MacawException exception) {
            log.logException(exception);
            fail();
        }
    }

    /**
	 * make sure update doesn't result in a duplicate by changing the name space
	 */
    public void testUpdateOntologyTermE1() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("dexterity");
            ontologyTerm1.setDescription("Skill and grace in physical movement, especially in the use of the hands.");
            ontologyTerm1.setOntologyName("LHA");
            ontologyTerm1.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            OntologyTerm ontologyTerm2 = new OntologyTerm();
            ontologyTerm2.setTerm("dexterity");
            ontologyTerm2.setDescription("Mental skill or adroitness; cleverness.");
            ontologyTerm2.setOntologyName("CP");
            ontologyTerm2.setNameSpace("www.cognitive-psychology.org");
            curationService.addOntologyTerm(demoUser, ontologyTerm2);
            int identifier2 = curationService.getOntologyTermIdentifier(demoUser, ontologyTerm2);
            ontologyTerm2.setIdentifier(identifier2);
            ontologyTerm2.setNameSpace("www.lha.ac.uk:LHA");
            curationService.updateOntologyTerm(demoUser, ontologyTerm2);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.DUPLICATE_ONTOLOGY_TERM);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * make sure update doesn't result in a duplicate by changing the ontology name
	 */
    public void testUpdateOntologyTermE2() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("dexterity");
            ontologyTerm1.setDescription("Skill and grace in physical movement, especially in the use of the hands.");
            ontologyTerm1.setOntologyName("LHA");
            ontologyTerm1.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            OntologyTerm ontologyTerm2 = new OntologyTerm();
            ontologyTerm2.setTerm("dexterity");
            ontologyTerm2.setDescription("Mental skill or adroitness; cleverness.");
            ontologyTerm2.setOntologyName("CP");
            ontologyTerm2.setNameSpace("www.cognitive-psychology.org");
            curationService.addOntologyTerm(demoUser, ontologyTerm2);
            int identifier2 = curationService.getOntologyTermIdentifier(demoUser, ontologyTerm2);
            ontologyTerm2.setIdentifier(identifier2);
            ontologyTerm2.setOntologyName("LHA");
            curationService.updateOntologyTerm(demoUser, ontologyTerm2);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.DUPLICATE_ONTOLOGY_TERM);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * reject update of non-existent term
	 */
    public void testUpdateOntologyTermE3() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("dexterity");
            ontologyTerm1.setDescription("Skill and grace in physical movement, especially in the use of the hands.");
            ontologyTerm1.setOntologyName("LHA");
            ontologyTerm1.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            OntologyTerm ontologyTerm2 = new OntologyTerm();
            ontologyTerm2.setIdentifier(2342);
            ontologyTerm2.setTerm("dexterity");
            ontologyTerm2.setDescription("Mental skill or adroitness; cleverness.");
            ontologyTerm2.setOntologyName("CP");
            ontologyTerm2.setNameSpace("www.cognitive-psychology.org");
            curationService.updateOntologyTerm(demoUser, ontologyTerm2);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.NON_EXISTENT_ONTOLOGY_TERM);
            assertEquals(1, numberOfErrors);
        }
    }

    /**
	 * delete ontology terms from list
	 */
    public void testDeleteOntologyTermN1() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("morbidity");
            ontologyTerm1.setDescription("The rate of incidence of a disease.");
            ontologyTerm1.setOntologyName("LHA");
            ontologyTerm1.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            int identifier1 = curationService.getOntologyTermIdentifier(demoUser, ontologyTerm1);
            ontologyTerm1.setIdentifier(identifier1);
            OntologyTerm ontologyTerm2 = new OntologyTerm();
            ontologyTerm2.setTerm("allergies");
            ontologyTerm2.setDescription("A misguided reaction to foreign substances by the immune system.");
            ontologyTerm2.setOntologyName("LHA");
            ontologyTerm2.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm2);
            int identifier2 = curationService.getOntologyTermIdentifier(demoUser, ontologyTerm2);
            ontologyTerm2.setIdentifier(identifier2);
            OntologyTerm ontologyTerm3 = new OntologyTerm();
            ontologyTerm3.setTerm("dexterity");
            ontologyTerm3.setDescription("Skill and grace in physical movement, especially in the use of the hands.");
            ontologyTerm3.setOntologyName("LHA");
            ontologyTerm3.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm3);
            ArrayList<OntologyTerm> itemsToDelete = new ArrayList<OntologyTerm>();
            itemsToDelete.add(ontologyTerm1);
            itemsToDelete.add(ontologyTerm2);
            curationService.deleteOntologyTerms(demoUser, itemsToDelete);
            ArrayList<OntologyTerm> termsSoFar = curationService.getAllOntologyTerms(demoUser);
            assertEquals(1, termsSoFar.size());
            assertEquals("dexterity", termsSoFar.get(0).getTerm());
        } catch (MacawException exception) {
            log.logException(exception);
            fail();
        }
    }

    /**
	 * delete item from a one-item list
	 */
    public void testDeleteOntologyTermA1() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("morbidity");
            ontologyTerm1.setDescription("The rate of incidence of a disease.");
            ontologyTerm1.setOntologyName("LHA");
            ontologyTerm1.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            int identifier1 = curationService.getOntologyTermIdentifier(demoUser, ontologyTerm1);
            ontologyTerm1.setIdentifier(identifier1);
            ArrayList<OntologyTerm> itemsToDelete = new ArrayList<OntologyTerm>();
            itemsToDelete.add(ontologyTerm1);
            curationService.deleteOntologyTerms(demoUser, itemsToDelete);
            ArrayList<OntologyTerm> termsSoFar = curationService.getAllOntologyTerms(demoUser);
            assertEquals(0, termsSoFar.size());
        } catch (MacawException exception) {
            log.logException(exception);
            fail();
        }
    }

    /**
	 * delete non-existent ontology term from list
	 */
    public void testDeleteOntologyTermE1() {
        try {
            OntologyTerm ontologyTerm1 = new OntologyTerm();
            ontologyTerm1.setTerm("morbidity");
            ontologyTerm1.setDescription("The rate of incidence of a disease.");
            ontologyTerm1.setOntologyName("LHA");
            ontologyTerm1.setNameSpace("www.lha.ac.uk:LHA");
            curationService.addOntologyTerm(demoUser, ontologyTerm1);
            OntologyTerm ontologyTerm2 = new OntologyTerm();
            ontologyTerm2.setIdentifier(2432);
            ontologyTerm2.setTerm("allergies");
            ontologyTerm2.setDescription("A misguided reaction to foreign substances by the immune system.");
            ontologyTerm2.setOntologyName("LHA");
            ontologyTerm2.setNameSpace("www.lha.ac.uk:LHA");
            ArrayList<OntologyTerm> itemsToDelete = new ArrayList<OntologyTerm>();
            itemsToDelete.add(ontologyTerm2);
            curationService.deleteOntologyTerms(demoUser, itemsToDelete);
            fail();
        } catch (MacawException exception) {
            int totalNumberOfErrors = exception.getNumberOfErrors();
            assertEquals(1, totalNumberOfErrors);
            int numberOfErrors = exception.getNumberOfErrors(MacawErrorType.NON_EXISTENT_ONTOLOGY_TERM);
            assertEquals(1, numberOfErrors);
        }
    }
}
