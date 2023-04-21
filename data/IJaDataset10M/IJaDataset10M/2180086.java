package dataImport.action;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import dataImport.action.helper.ReaderFunctionalTest;
import dataImport.action.helper.XMLReader;
import dataImport.action.manager.EntityReaderManager;
import dataImport.exception.DuplicateAttributeException;
import dataImport.exception.MissingAttributeException;
import dataImport.model.Command;

public class CommandReaderFunctionalTest extends ReaderFunctionalTest {

    private EntityReaderManager entityReaderManager;

    @BeforeMethod
    public void setUp() throws Exception {
        this.entityReaderManager = new EntityReaderManager();
    }

    @Test(expectedExceptions = MissingAttributeException.class)
    public void testRead_OneCommand_NoAttributeIdThrowsException() throws Exception {
        final CommandReader commandReader = new CommandReader(this.entityReaderManager);
        commandReader.read(XMLReader.getInstance().getDocument());
    }

    @Test(expectedExceptions = MissingAttributeException.class)
    public void testRead_OneCommand_NoAttributePatternThrowsException() throws Exception {
        final CommandReader commandReader = new CommandReader(this.entityReaderManager);
        commandReader.read(XMLReader.getInstance().getDocument());
    }

    @Test
    public void testRead_OneCommandNpc() throws Exception {
        this.entityReaderManager.getCommandReader().read(XMLReader.getInstance().getDocument());
        final Command command = (Command) this.entityReaderManager.getCommandReader().getEntityWithId("cmd1");
        Assert.assertEquals(command.getId(), "cmd1");
        Assert.assertEquals(command.getPattern(), "talk to @");
    }

    @Test
    public void testRead_OneCommandObject() throws Exception {
        this.entityReaderManager.getCommandReader().read(XMLReader.getInstance().getDocument());
        final Command command = (Command) this.entityReaderManager.getCommandReader().getEntityWithId("cmd1");
        Assert.assertEquals(command.getId(), "cmd1");
        Assert.assertEquals(command.getPattern(), "read @");
    }

    @Test
    public void testRead_OneCommandPlace() throws Exception {
        this.entityReaderManager.getCommandReader().read(XMLReader.getInstance().getDocument());
        final Command command = (Command) this.entityReaderManager.getCommandReader().getEntityWithId("cmd1");
        Assert.assertEquals(command.getId(), "cmd1");
        Assert.assertEquals(command.getPattern(), "go to @");
    }

    @Test
    public void testRead_OneCommandObjectNpc() throws Exception {
        this.entityReaderManager.getCommandReader().read(XMLReader.getInstance().getDocument());
        final Command command = (Command) this.entityReaderManager.getCommandReader().getEntityWithId("cmd1");
        Assert.assertEquals(command.getId(), "cmd1");
        Assert.assertEquals(command.getPattern(), "sell @ to @");
    }

    @Test
    public void testRead_OneCommand_WithAllRequiredAttributes() throws Exception {
        this.entityReaderManager.getCommandReader().read(XMLReader.getInstance().getDocument());
        final CommandReader commandReader = new CommandReader(this.entityReaderManager);
        commandReader.read(XMLReader.getInstance().getDocument());
        final Command command = (Command) commandReader.getEntityWithId("cmd1");
        Assert.assertEquals(command.getId(), "cmd1");
        Assert.assertEquals(command.getPattern(), "talk to @");
    }

    @Test(expectedExceptions = DuplicateAttributeException.class)
    public void testRead_TwoCommandsWithSameId() throws Exception {
        final CommandReader commandReader = new CommandReader(this.entityReaderManager);
        commandReader.read(XMLReader.getInstance().getDocument());
    }

    @Test(expectedExceptions = DuplicateAttributeException.class)
    public void testRead_TwoCommandsWithSamePattern() throws Exception {
        final CommandReader commandReader = new CommandReader(this.entityReaderManager);
        commandReader.read(XMLReader.getInstance().getDocument());
    }

    @Test
    public void testRead_TwoDistinctCommands() throws Exception {
        this.entityReaderManager.getCommandReader().read(XMLReader.getInstance().getDocument());
        final CommandReader commandReader = new CommandReader(this.entityReaderManager);
        commandReader.read(XMLReader.getInstance().getDocument());
        final Command command1 = (Command) commandReader.getEntityWithId("cmd1");
        final Command command2 = (Command) commandReader.getEntityWithId("cmd2");
        Assert.assertEquals(command1.getId(), "cmd1");
        Assert.assertEquals(command1.getPattern(), "talk to @");
        Assert.assertEquals(command2.getId(), "cmd2");
        Assert.assertEquals(command2.getPattern(), "read @");
    }
}
