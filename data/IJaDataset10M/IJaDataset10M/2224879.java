package gaea.junit.common.exception;

import gaea.junit.Constants;
import junit.framework.TestCase;
import org.gaea.common.GaeaError;
import org.gaea.common.command.Command;
import org.gaea.common.command.CommandManager;
import org.gaea.common.command.CommandResult;
import org.gaea.common.command.CommandVerb;
import org.gaea.common.command.IConnector;
import org.gaea.common.command.QueueExecutionHandler;
import org.gaea.common.command.QueueManagementHandler;
import org.gaea.common.command.database.versant.Database;
import org.gaea.common.command.impl.jdo.JDOCommandFactory;
import org.gaea.common.command.operator.IConnectionOperator;
import org.gaea.common.command.operator.IInsertionOperator;
import org.gaea.common.command.operator.ISelectionOperator;
import org.gaea.common.command.output.OutputConnect;
import org.gaea.common.command.output.OutputDisconnect;
import org.gaea.common.command.output.OutputInsert;
import org.gaea.common.command.output.OutputRollback;
import org.gaea.common.command.output.OutputSelect;
import org.gaea.common.exception.GaeaException;
import org.gaea.common.exception.GaeaException.Error;
import org.gaea.common.metadata.__gaea_classes;

/**
 * Tests error
 * 
 * @author jsgoupil
 */
public class JDOErrorTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @SuppressWarnings("unchecked")
    public void testJDOError() {
        IConnector connector = CommandManager.getInstance().initialize(JDOCommandFactory.class);
        Command<OutputConnect> commandConnect = (Command<OutputConnect>) CommandManager.getInstance().create(CommandVerb.Connect);
        ((IConnectionOperator) commandConnect).setup(new Database(), Constants.DATABASE_HOSTNAME, Constants.DATABASE_USERNAME, Constants.DATABASE_PASSWORD, Constants.DATABASE_DATABASE);
        try {
            commandConnect.execute(connector);
        } catch (GaeaException ex) {
            assertTrue("Connexion non r�ussie", false);
        }
        boolean wentToCatch = false;
        Command<OutputSelect> commandSelect = (Command<OutputSelect>) CommandManager.getInstance().create(CommandVerb.Select);
        ((ISelectionOperator) commandSelect).setup(Constants.class);
        try {
            commandSelect.execute(connector);
        } catch (GaeaException ex) {
            wentToCatch = true;
            assertTrue(ex.getError() == Error.SELECT_NullPointer);
        } finally {
            if (!wentToCatch) {
                assertTrue(false);
            }
        }
        commandSelect = (Command<OutputSelect>) CommandManager.getInstance().create(CommandVerb.Select);
        boolean succeed = false;
        ((ISelectionOperator) commandSelect).setup(__gaea_classes.class);
        try {
            CommandResult<OutputSelect> result = commandSelect.execute(connector);
            succeed = (result.getErrorCode() == GaeaError.NO_ERROR);
            assertTrue(succeed);
        } catch (GaeaException ex) {
            assertTrue(false);
        }
        wentToCatch = false;
        Command<OutputInsert> cmdInsert = (Command<OutputInsert>) CommandManager.getInstance().create(CommandVerb.Insert);
        __gaea_classes test = new __gaea_classes();
        test.setName("Data");
        ((IInsertionOperator) cmdInsert).setup(test);
        try {
            cmdInsert.execute(connector);
        } catch (GaeaException ex) {
            wentToCatch = true;
            assertTrue(ex.getError() == Error.JDO_NoActiveTransaction);
        } finally {
            if (!wentToCatch) {
                assertTrue(false);
            }
        }
        QueueManagementHandler qmh = CommandManager.getInstance().startQueue();
        wentToCatch = false;
        cmdInsert = (Command<OutputInsert>) CommandManager.getInstance().create(CommandVerb.Insert);
        JDOErrorTest test2 = new JDOErrorTest();
        ((IInsertionOperator) cmdInsert).setup(test2);
        qmh.append(CommandVerb.Begin, null);
        qmh.append(cmdInsert);
        qmh.append(CommandVerb.Commit, null);
        QueueExecutionHandler qeh = qmh.end();
        try {
            qeh.execute(connector);
        } catch (GaeaException ex) {
            wentToCatch = true;
            assertTrue(ex.getError() == Error.JDO_NoPersistenceCapable);
        } finally {
            if (!wentToCatch) {
                assertTrue(false);
            }
        }
        wentToCatch = false;
        Command<OutputDisconnect> cmdDisconnect = (Command<OutputDisconnect>) CommandManager.getInstance().create(CommandVerb.Disconnect);
        try {
            cmdDisconnect.execute(connector);
        } catch (GaeaException ex) {
            wentToCatch = true;
            assertTrue(ex.getError() == Error.JDO_CloseErrorBecauseActiveTransaction);
        } finally {
            if (!wentToCatch) {
                assertTrue(false);
            }
        }
        qmh = CommandManager.getInstance().startQueue();
        wentToCatch = false;
        cmdInsert = (Command<OutputInsert>) CommandManager.getInstance().create(CommandVerb.Insert);
        __gaea_classes testgaeaclasses = new __gaea_classes();
        testgaeaclasses.setName("TestForJavaGaea");
        ((IInsertionOperator) cmdInsert).setup(testgaeaclasses);
        qmh.append(CommandVerb.Begin, null);
        qmh.append(cmdInsert);
        qmh.append(CommandVerb.Commit, null);
        qeh = qmh.end();
        try {
            qeh.execute(connector);
        } catch (GaeaException ex) {
            wentToCatch = true;
            assertTrue(ex.getError() == Error.JDO_NoPersistenceCapable);
        } finally {
            if (!wentToCatch) {
                assertTrue(false);
            }
        }
        Command<OutputRollback> cmdRollback = (Command<OutputRollback>) CommandManager.getInstance().create(CommandVerb.Rollback);
        try {
            CommandResult<OutputRollback> result = cmdRollback.execute(connector);
            assertTrue(result.getErrorCode() == GaeaError.NO_ERROR);
        } catch (GaeaException ex) {
            assertTrue(ex.getError() == Error.JDO_CloseErrorBecauseActiveTransaction);
        }
        wentToCatch = false;
        cmdRollback = (Command<OutputRollback>) CommandManager.getInstance().create(CommandVerb.Rollback);
        try {
            CommandResult<OutputRollback> result = cmdRollback.execute(connector);
            assertTrue(result.getErrorCode() == GaeaError.NO_ERROR);
        } catch (GaeaException ex) {
            wentToCatch = true;
            assertTrue(ex.getError() == Error.JDO_NoActiveTransaction);
        } finally {
            if (!wentToCatch) {
                assertTrue(false);
            }
        }
    }
}
