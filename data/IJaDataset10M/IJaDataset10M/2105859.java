package model.storage;

import model.storage.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.storage.Artifact;
import model.storage.Group;
import model.storage.GroupNotFoundException;
import model.storage.WorkingFile;
import model.storage.Artifact.artifactType;

public class StorageHandlerTest {

    StorageHandler storageHandler;

    String fileInvalid01XRM = "./src/model/storage/testinput/invalid01.xrm";

    String fileInvalid02XRM = "./src/model/storage/testinput/invalid02.xrm";

    String fileValid01XRM = "./src/model/storage/testinput/valid01.xrm";

    String fileXSD = "rmt.xsd";

    String jabRef = "jabref.xsl";

    String OutputTemp = "jabref-test.bib";

    ArrayList<Artifact> validArtifacts;

    String[] validGroup = { "greatreads", "fantasticreads", "eye-damaging reads", "i am now dumber for having read this", "no, seriously" };

    WorkingFile wf;

    @Before
    public void setUp() {
        storageHandler = new StorageHandler();
        storageHandler.setXSDFile(fileXSD);
        validArtifacts = new ArrayList<Artifact>();
        Artifact art1 = new Artifact();
        Artifact art2 = new Artifact();
        Artifact art3 = new Artifact();
        art1.setTitle("UML Sequence");
        art1.setType(Artifact.artifactType.article);
        art1.setAbstract("A beginner's guide to UML");
        art1.setYearAsString("2000");
        art1.setAuthorsAsString("John Smith; David Jones");
        art2.setEditorsAsString("Tom Jones; Barry White");
        art1.setJournal("This is journal field");
        art1.setVolumeAsString("1");
        art1.setPageRange("50 - 100");
        validArtifacts.add(art1);
        art2.setTitle("Software Engineering");
        art2.setType(Artifact.artifactType.incollection);
        art2.setKeywordsAsString("software; engineering");
        art2.setAuthorsAsString("Jim Carey");
        art2.setYearAsString("2008");
        art2.setPublisher("Kerri-Anne");
        art2.setBookTitle("Team Management");
        validArtifacts.add(art2);
        art3.setTitle("Article 3");
        art3.setType(Artifact.artifactType.article);
        art3.setAbstract("An artifact that is type article.");
        art3.setYearAsString("2010");
        art3.setAuthorsAsString("Tracy Xu; Kylie Diep");
        art3.setJournal("This is journal field for article 3");
        art3.setVolumeAsString("3");
        art3.setPageRange("30 - 100");
        validArtifacts.add(art3);
        art3.addIncoming(art1);
        art1.addOutgoing(art3);
        wf = new WorkingFile();
        wf.setPdfDir(new String("C:\\Gavan\\Pdf"));
        wf.setPsDir(new String("C:\\Gavan\\Ps"));
        try {
            wf.addArtifactWithoutConflictCheck(art1);
            wf.addArtifactWithoutConflictCheck(art2);
            wf.addArtifactWithoutConflictCheck(art3);
        } catch (AddArtifactException e) {
            fail("Failed to add artifacts");
        }
        try {
            wf.addNewGroup("greatreads");
            wf.putArtifactInGroup(art1, "greatreads");
            wf.addNewGroup("fantasticreads");
            wf.putArtifactInGroup(art2, "fantasticreads");
            wf.moveGroup("fantasticreads", "greatreads");
            wf.addNewGroup("eye-damaging reads");
            wf.addNewGroup("i am now dumber for having read this");
            wf.addNewGroup("no, seriously");
            wf.moveGroup("i am now dumber for having read this", "eye-damaging reads");
            wf.moveGroup("no, seriously", "i am now dumber for having read this");
        } catch (AddGroupException e) {
        } catch (GroupNotFoundException e) {
        } catch (PutArtifactInGroupException e) {
        } catch (MoveGroupException e) {
        }
    }

    @After
    public void tearDown() throws Exception {
        storageHandler = null;
    }

    @Test
    public void testValidateXRMFile() {
        File f = new File(fileXSD);
        assertTrue("Testing " + fileXSD + " exists: fail", f.exists());
        assertTrue("Testing " + fileValid01XRM + " validates: fail", storageHandler.validateFile(fileValid01XRM));
    }

    @Test
    public void testReadNonXRMFile() throws LoadXRMException {
        Boolean pass = false;
        try {
            storageHandler.readFileData(fileInvalid01XRM);
        } catch (LoadXRMException e) {
            pass = true;
        } finally {
            assertTrue("Test whether it reads non-XML files", pass);
        }
    }

    @Test
    public void testReadInvalidXML() {
        Boolean pass = false;
        try {
            storageHandler.readFileData(fileInvalid02XRM);
        } catch (Exception e) {
            pass = true;
        } finally {
            assertTrue("Testing whether it reads invalid files: fail", pass);
        }
    }

    @Test
    public void testTransformXMLValidXSLT() {
        String err_msg;
        try {
            storageHandler.transformXML(fileValid01XRM, jabRef, OutputTemp);
        } catch (Exception e) {
            err_msg = "Testing file exportation..." + "\n\t Error: ";
            assertTrue(err_msg + e.getMessage(), false);
        }
    }
}
