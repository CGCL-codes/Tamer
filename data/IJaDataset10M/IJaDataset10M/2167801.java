package org.ikasan.connector.basefiletransfer.outbound.command;

import javax.resource.ResourceException;
import junit.framework.TestCase;
import org.ikasan.connector.base.command.AbstractTransactionalResourceCommand;
import org.ikasan.connector.base.command.ExecutionContext;
import org.ikasan.connector.base.command.XidImpl;
import org.ikasan.connector.basefiletransfer.net.ClientCommandRenameException;
import org.ikasan.connector.basefiletransfer.net.FileTransferClient;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 * Test class for the RenameFileCommand
 * 
 * @author Ikasan Development Team
 */
public class RenameFileCommandTest extends TestCase {

    /**
     * tests the execute method
     * 
     * @throws ResourceException
     * @throws ClientCommandRenameException
     */
    public void testExecute() throws ResourceException, ClientCommandRenameException {
        Mockery clientMockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        final FileTransferClient client = clientMockery.mock(FileTransferClient.class);
        final String newPath = "newDir/newFile.new";
        final String oldPath = "oldDir/oldFile.old";
        clientMockery.checking(new Expectations() {

            {
                one(client).ensureConnection();
                one(client).rename(oldPath, newPath);
            }
        });
        final RenameFileCommand command = new RenameFileCommand();
        command.setTransactionJournal(BaseFileTransferCommandJUnitHelper.getTransactionJournal(command, 3));
        setupExecutionContext(newPath, oldPath, command);
        command.execute(client, new XidImpl(new byte[0], new byte[0], 0));
        assertTrue("command state should be executed", AbstractTransactionalResourceCommand.EXECUTED_STATE.getName().equals(command.getState()));
    }

    /**
     * @param newPath
     * @param oldPath
     * @param command
     */
    private void setupExecutionContext(final String newPath, final String oldPath, final RenameFileCommand command) {
        ExecutionContext executionContext = new ExecutionContext();
        executionContext.put(ExecutionContext.RENAMABLE_FILE_PATH_PARAM, oldPath);
        executionContext.put(ExecutionContext.NEW_FILE_PATH_PARAM, newPath);
        command.setExecutionContext(executionContext);
    }

    /**
     * tests the rollback method
     * 
     * @throws ResourceException
     * @throws ClientCommandRenameException
     */
    public void testRollback() throws ResourceException, ClientCommandRenameException {
        Mockery clientMockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        final FileTransferClient client = clientMockery.mock(FileTransferClient.class);
        final String newPath = "newDir/newFile.new";
        final String oldPath = "oldDir/oldFile.old";
        clientMockery.checking(new Expectations() {

            {
                one(client).ensureConnection();
                one(client).rename(newPath, oldPath);
                one(client).ensureConnection();
                one(client).rename(oldPath, newPath);
            }
        });
        RenameFileCommand command = new RenameFileCommand();
        command.setTransactionJournal(BaseFileTransferCommandJUnitHelper.getTransactionJournal(command, 5));
        setupExecutionContext(newPath, oldPath, command);
        assertEquals("command state should be initialised", AbstractTransactionalResourceCommand.INITIALISED_STATE.getName(), command.getState());
        command.execute(client, new XidImpl(new byte[0], new byte[0], 0));
        assertEquals("command state should be executed", AbstractTransactionalResourceCommand.EXECUTED_STATE.getName(), command.getState());
        command.rollback();
        assertEquals("command state should be rolled_back_executed", AbstractTransactionalResourceCommand.ROLLED_BACK_STATE.getName(), command.getState());
    }
}
