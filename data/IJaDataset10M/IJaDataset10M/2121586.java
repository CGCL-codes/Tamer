package dataImport.action;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import dataImport.action.helper.ReaderFunctionalTest;
import dataImport.action.helper.XMLReader;
import dataImport.action.manager.EntityReaderManager;
import dataImport.exception.DuplicateAttributeException;
import dataImport.exception.EntityNotFoundException;
import dataImport.exception.MissingAttributeException;
import dataImport.model.CustomAction;
import dataImport.model.Text;

public class CustomActionReaderFunctionalTest extends ReaderFunctionalTest {

    private EntityReaderManager entityReaderManager;

    @BeforeMethod
    public void setUp() throws Exception {
        this.entityReaderManager = new EntityReaderManager();
        this.entityReaderManager.getCommandReader().read(XMLReader.getInstance().getDocument());
    }

    @Test(expectedExceptions = MissingAttributeException.class)
    public void testRead_OneCustomAction_NoAttributeCommandIdThrowsException() throws Exception {
        final CustomActionReader customActionReader = new CustomActionReader(this.entityReaderManager);
        customActionReader.read(XMLReader.getInstance().getDocument());
    }

    @Test(expectedExceptions = MissingAttributeException.class)
    public void testRead_OneCustomAction_NoAttributeIdThrowsException() throws Exception {
        final CustomActionReader customActionReader = new CustomActionReader(this.entityReaderManager);
        customActionReader.read(XMLReader.getInstance().getDocument());
    }

    @Test(expectedExceptions = MissingAttributeException.class)
    public void testRead_OneCustomAction_ReplaceDependent_NoAttributeReplaceThrowsException() throws Exception {
        final CustomActionReader customActionReader = new CustomActionReader(this.entityReaderManager);
        customActionReader.read(XMLReader.getInstance().getDocument());
    }

    @Test(expectedExceptions = EntityNotFoundException.class)
    public void testRead_OneCustomAction_ReplaceDependent_WrongAttributeReplaceThrowsException() throws Exception {
        final CustomActionReader customActionReader = new CustomActionReader(this.entityReaderManager);
        customActionReader.read(XMLReader.getInstance().getDocument());
    }

    @Test
    public void testRead_OneCustomAction_ReplaceIndependent_NoAttributeReplaceDoesNotThrowException() throws Exception {
        final CustomActionReader customActionReader = new CustomActionReader(this.entityReaderManager);
        customActionReader.read(XMLReader.getInstance().getDocument());
    }

    @Test
    public void testRead_OneCustomActionWithReplacement_WithAllRequiredAttributes() throws Exception {
        final CustomActionReader customActionReader = new CustomActionReader(this.entityReaderManager);
        customActionReader.read(XMLReader.getInstance().getDocument());
        Assert.assertTrue(customActionReader.containsEntityWithId("action1"));
        final CustomAction customAction = (CustomAction) customActionReader.getEntityWithId("action1");
        Assert.assertEquals(customAction.getId(), "action1");
        Assert.assertEquals(customAction.getPattern(), new Text("read book"));
    }

    @Test
    public void testRead_OneCustomActionWithoutReplacement_WithAllRequiredAttributes() throws Exception {
        final CustomActionReader customActionReader = new CustomActionReader(this.entityReaderManager);
        customActionReader.read(XMLReader.getInstance().getDocument());
        Assert.assertTrue(customActionReader.containsEntityWithId("action1"));
        final CustomAction customAction = (CustomAction) customActionReader.getEntityWithId("action1");
        Assert.assertEquals(customAction.getId(), "action1");
        Assert.assertEquals(customAction.getPattern(), new Text("list"));
    }

    @Test(expectedExceptions = DuplicateAttributeException.class)
    public void testRead_TwoCustomActionsWithSameId() throws Exception {
        final CustomActionReader customActionReader = new CustomActionReader(this.entityReaderManager);
        customActionReader.read(XMLReader.getInstance().getDocument());
    }

    @Test
    public void testRead_ThreeDistinctCustomActions() throws Exception {
        final CustomActionReader customActionReader = new CustomActionReader(this.entityReaderManager);
        customActionReader.read(XMLReader.getInstance().getDocument());
        Assert.assertTrue(customActionReader.containsEntityWithId("action1"));
        Assert.assertTrue(customActionReader.containsEntityWithId("action2"));
        Assert.assertTrue(customActionReader.containsEntityWithId("action3"));
        final CustomAction customAction1 = (CustomAction) customActionReader.getEntityWithId("action1");
        final CustomAction customAction2 = (CustomAction) customActionReader.getEntityWithId("action2");
        final CustomAction customAction3 = (CustomAction) customActionReader.getEntityWithId("action3");
        Assert.assertEquals(customAction1.getId(), "action1");
        Assert.assertEquals(customAction1.getPattern(), new Text("smile"));
        Assert.assertEquals(customAction2.getId(), "action2");
        Assert.assertEquals(customAction2.getPattern(), new Text("read book"));
        Assert.assertEquals(customAction3.getId(), "action3");
        Assert.assertEquals(customAction3.getPattern(), new Text("read article"));
    }

    @Test
    public void testRead_ThreeCustomAction_WithTwoReplacements() throws Exception {
        final CustomActionReader customActionReader = new CustomActionReader(this.entityReaderManager);
        customActionReader.read(XMLReader.getInstance().getDocument());
        Assert.assertTrue(customActionReader.containsEntityWithId("action1"));
        Assert.assertTrue(customActionReader.containsEntityWithId("action2"));
        Assert.assertTrue(customActionReader.containsEntityWithId("action3"));
        final CustomAction customAction1 = (CustomAction) customActionReader.getEntityWithId("action1");
        final CustomAction customAction2 = (CustomAction) customActionReader.getEntityWithId("action2");
        final CustomAction customAction3 = (CustomAction) customActionReader.getEntityWithId("action3");
        Assert.assertEquals(customAction1.getId(), "action1");
        Assert.assertEquals(customAction1.getPattern(), new Text("buy book from vendor"));
        Assert.assertEquals(customAction2.getId(), "action2");
        Assert.assertEquals(customAction2.getPattern(), new Text("book buy vendor from"));
        Assert.assertEquals(customAction3.getId(), "action3");
        Assert.assertEquals(customAction3.getPattern(), new Text("sell book vendor from"));
    }
}
