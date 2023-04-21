package org.apache.harmony.jpda.tests.jdwp.VirtualMachine;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

/**
 * JDWP Unit test for VirtualMachine.Capabilities command.
 */
public class CapabilitiesTest extends JDWPSyncTestCase {

    protected String getDebuggeeClassName() {
        return "org.apache.harmony.jpda.tests.jdwp.share.debuggee.HelloWorld";
    }

    /**
     * This testcase exercises VirtualMachine.Capabilities command.
     * <BR>At first the test starts HelloWorld debuggee.
     * <BR> Then the test performs VirtualMachine.Capabilities command and checks that:
     * all returned capabilities' values are expected values and that
     * there are no extra data in the reply packet;
     */
    public void testCapabilities001() {
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);
        CommandPacket packet = new CommandPacket(JDWPCommands.VirtualMachineCommandSet.CommandSetID, JDWPCommands.VirtualMachineCommandSet.CapabilitiesCommand);
        logWriter.println("\trequest capabilities");
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "VirtualMachine::Capabilities command");
        boolean canWatchFieldModification = reply.getNextValueAsBoolean();
        boolean canWatchFieldAccess = reply.getNextValueAsBoolean();
        boolean canGetBytecodes = reply.getNextValueAsBoolean();
        boolean canGetSyntheticAttribute = reply.getNextValueAsBoolean();
        boolean canGetOwnedMonitorInfo = reply.getNextValueAsBoolean();
        boolean canGetCurrentContendedMonitor = reply.getNextValueAsBoolean();
        boolean canGetMonitorInfo = reply.getNextValueAsBoolean();
        logWriter.println("\tcanWatchFieldModification\t= " + canWatchFieldModification);
        assertTrue("canWatchFieldModification must be true", canWatchFieldModification);
        logWriter.println("\tcanWatchFieldAccess\t\t= " + canWatchFieldAccess);
        assertTrue("canWatchFieldAccess must be true", canWatchFieldAccess);
        logWriter.println("\tcanGetBytecodes\t\t\t= " + canGetBytecodes);
        assertTrue("canGetBytecodes must be true", canGetBytecodes);
        logWriter.println("\tcanGetSyntheticAttribute\t= " + canGetSyntheticAttribute);
        assertTrue("canGetSyntheticAttribute must be true", canGetSyntheticAttribute);
        logWriter.println("\tcanGetOwnedMonitorInfo\t\t= " + canGetOwnedMonitorInfo);
        assertTrue("canGetOwnedMonitorInfo must be true", canGetOwnedMonitorInfo);
        logWriter.println("\tcanGetCurrentContendedMonitor\t= " + canGetCurrentContendedMonitor);
        assertTrue("canGetCurrentContendedMonitor must be true", canGetCurrentContendedMonitor);
        logWriter.println("\tcanGetMonitorInfo\t\t= " + canGetMonitorInfo);
        assertTrue("canGetMonitorInfo must be true", canGetMonitorInfo);
        assertAllDataRead(reply);
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(CapabilitiesTest.class);
    }
}
