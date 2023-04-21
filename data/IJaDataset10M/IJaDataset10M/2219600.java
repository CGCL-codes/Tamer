package org.mockftpserver.stub.command;

import org.mockftpserver.core.command.AbstractCommandHandlerTest;
import org.mockftpserver.core.command.Command;
import org.mockftpserver.core.command.CommandNames;
import org.mockftpserver.core.command.ReplyCodes;

/**
 * Tests for the SmntCommandHandler class
 * 
 * @version $Revision: 184 $ - $Date: 2008-12-03 17:52:39 -0500 (Wed, 03 Dec 2008) $
 * 
 * @author Chris Mair
 */
public final class SmntCommandHandlerTest extends AbstractCommandHandlerTest {

    private SmntCommandHandler commandHandler;

    private Command command1;

    private Command command2;

    /**
     * Test the handleCommand(Command,Session) method
     * @throws Exception
     */
    public void testHandleCommand() throws Exception {
        session.sendReply(ReplyCodes.SMNT_OK, replyTextFor(ReplyCodes.SMNT_OK));
        session.sendReply(ReplyCodes.SMNT_OK, replyTextFor(ReplyCodes.SMNT_OK));
        replay(session);
        commandHandler.handleCommand(command1, session);
        commandHandler.handleCommand(command2, session);
        verify(session);
        verifyNumberOfInvocations(commandHandler, 2);
        verifyOneDataElement(commandHandler.getInvocation(0), SmntCommandHandler.PATHNAME_KEY, DIR1);
        verifyOneDataElement(commandHandler.getInvocation(1), SmntCommandHandler.PATHNAME_KEY, DIR2);
    }

    /**
     * Test the handleCommand() method, when no pathname parameter has been specified
     */
    public void testHandleCommand_MissingPathnameParameter() throws Exception {
        testHandleCommand_InvalidParameters(commandHandler, CommandNames.SMNT, EMPTY);
    }

    /**
     * Perform initialization before each test
     * @see org.mockftpserver.core.command.AbstractCommandHandlerTest#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        commandHandler = new SmntCommandHandler();
        commandHandler.setReplyTextBundle(replyTextBundle);
        command1 = new Command(CommandNames.SMNT, array(DIR1));
        command2 = new Command(CommandNames.SMNT, array(DIR2));
    }
}
