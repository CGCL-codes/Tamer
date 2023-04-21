package util.templateGen.schemes;

import util.templateGen.*;

public class XmlTasksLinearFanOut extends XmlTasks {

    public XmlTasksLinearFanOut(int numOfTasks) {
        super(numOfTasks);
    }

    public void createTrafficPattern() {
        XmlTask root = new XmlTask("root", 0);
        for (int i = 1; i < numOfTasks - 1; i++) {
            root.addOutgoingPacket(i - 1, i);
            XmlTask interior = new XmlTask("interior", i);
            if (i != 1) {
                interior.addIncomingPacket(0, i - 1);
            }
            interior.addIncomingPacket(i - 1, 0);
            interior.addOutgoingPacket(0, i + 1);
            tasks.setElementAt(interior, interior.id);
        }
        root.addOutgoingPacket(numOfTasks - 2, numOfTasks - 1);
        tasks.setElementAt(root, 0);
        XmlTask leaf = new XmlTask("leaf", numOfTasks - 1);
        leaf.addIncomingPacket(0, numOfTasks - 2);
        leaf.addIncomingPacket(numOfTasks - 2, 0);
        tasks.setElementAt(leaf, leaf.id);
    }
}
