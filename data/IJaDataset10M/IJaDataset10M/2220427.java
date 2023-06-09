package org.apache.harmony.jpda.tests.jdwp.VirtualMachine;

import org.apache.harmony.jpda.tests.framework.jdwp.CommandPacket;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPCommands;
import org.apache.harmony.jpda.tests.framework.jdwp.JDWPConstants;
import org.apache.harmony.jpda.tests.framework.jdwp.ReplyPacket;
import org.apache.harmony.jpda.tests.jdwp.share.JDWPSyncTestCase;
import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;

/**
 * JDWP Unit test for VirtualMachine.ClassesBySignature command.
 */
public class ClassesBySignatureTest extends JDWPSyncTestCase {

    static final String SIGNATURE001 = "Lorg/apache/harmony/jpda/tests/jdwp/share/debuggee/HelloWorld;";

    protected String getDebuggeeClassName() {
        return "org.apache.harmony.jpda.tests.jdwp.share.debuggee.HelloWorld";
    }

    /**
     * This testcase exercises VirtualMachine.ClassesBySignature command.
     * <BR>At first the test starts HelloWorld debuggee.
     * <BR> Then the test performs VirtualMachine.ClassesBySignature command 
     * for signature of HelloWorld class and checks that:
     * <BR>&nbsp;&nbsp; - number of returned reference types is equal to 1;
     * <BR>&nbsp;&nbsp; - the JDWP command ReferenceType.Signature for
     * returned referenceTypeID returns the expected string signature;
     * <BR>&nbsp;&nbsp; - there are no extra data in the reply packet;
     */
    public void testClassesBySignature001() {
        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_READY);
        CommandPacket packet = new CommandPacket(JDWPCommands.VirtualMachineCommandSet.CommandSetID, JDWPCommands.VirtualMachineCommandSet.ClassesBySignatureCommand);
        packet.setNextValueAsString(SIGNATURE001);
        ReplyPacket reply = debuggeeWrapper.vmMirror.performCommand(packet);
        checkReplyPacket(reply, "VirtualMachine::ClassesBySignature command");
        byte refTypeTag;
        long typeID;
        String signature;
        int status;
        ReplyPacket replySignature;
        int classes = reply.getNextValueAsInt();
        logWriter.println("Number of reference types = " + classes);
        if (classes != 1) {
            logWriter.printError("Wrong Number of reference types");
            fail("Wrong Number of reference types");
        }
        for (int i = 0; i < classes; i++) {
            refTypeTag = reply.getNextValueAsByte();
            typeID = reply.getNextValueAsReferenceTypeID();
            status = reply.getNextValueAsInt();
            logWriter.println("\trefTypeTag = " + JDWPConstants.TypeTag.getName(refTypeTag) + "(" + refTypeTag + ")");
            if (JDWPConstants.TypeTag.CLASS != refTypeTag) {
                printErrorAndFail("refTypeTag must be " + JDWPConstants.TypeTag.getName(JDWPConstants.TypeTag.CLASS) + "(" + JDWPConstants.TypeTag.CLASS + ")");
            }
            packet = new CommandPacket(JDWPCommands.ReferenceTypeCommandSet.CommandSetID, JDWPCommands.ReferenceTypeCommandSet.SignatureCommand);
            packet.setNextValueAsReferenceTypeID(typeID);
            replySignature = debuggeeWrapper.vmMirror.performCommand(packet);
            signature = replySignature.getNextValueAsString();
            logWriter.println("\ttypeID = " + typeID + "(" + signature + ")");
            if (!SIGNATURE001.equals(signature)) {
                printErrorAndFail("Signature must be " + SIGNATURE001);
            }
            logWriter.println("\tstatus = " + JDWPConstants.ClassStatus.getName(status) + "(" + status + ")");
        }
        assertAllDataRead(reply);
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(ClassesBySignatureTest.class);
    }
}
