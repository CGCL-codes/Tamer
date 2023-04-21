package il.ac.biu.cs.grossmm.tests.test3;

import static il.ac.biu.cs.grossmm.api.data.NodeTypeByInterface.*;
import static il.ac.biu.cs.grossmm.api.keys.PatternFactory.*;
import static il.ac.biu.cs.grossmm.api.presence.BaseVocabulary.*;
import il.ac.biu.cs.grossmm.api.data.Node;
import il.ac.biu.cs.grossmm.api.data.Root;
import il.ac.biu.cs.grossmm.api.flow.DataProcessorBase;
import il.ac.biu.cs.grossmm.api.flow.NodeManipulator;
import il.ac.biu.cs.grossmm.api.flow.NotificationPoint;
import il.ac.biu.cs.grossmm.api.flow.Notifier;
import il.ac.biu.cs.grossmm.api.flow.Track;
import il.ac.biu.cs.grossmm.api.keys.Key;
import il.ac.biu.cs.grossmm.api.presence.Pidf;
import il.ac.biu.cs.grossmm.api.presence.PidfTuple;
import il.ac.biu.cs.grossmm.api.presence.ResourceType;
import java.util.logging.Logger;

public class ProcessorC extends DataProcessorBase implements Notifier<Pidf> {

    static Logger logger = Logger.getLogger("ProcessorC");

    private NotificationPoint<Pidf> np;

    public ProcessorC() {
        super("Processor C");
        logger.info("Created");
    }

    @Override
    public synchronized void run() {
        logger.info("Started");
        np = adm.createNotificationPoint(this, pattern(String.class, mandatory(RESOURCE_TYPE, ResourceType.class)), nodeType(Pidf.class), 1.0);
        started();
        waitForStop();
        stopped();
        logger.info("Stopped");
    }

    public void fetch(Key key, Track track, Root<Pidf> node, NodeManipulator manipulator) {
        throw new RuntimeException("Not implemented");
    }

    public boolean subscribe(Key key, Track track) throws Exception {
        logger.info("key=" + key);
        Root<Pidf> pidfRoot = np.activated(key);
        NodeManipulator man = np.writeLock(pidfRoot);
        man.setValue(pidfRoot, Pidf.ENTITY, (String) key.value());
        Node<PidfTuple> tuple = man.createSubnode(pidfRoot, Pidf.TUPLES);
        man.setValue(tuple, PidfTuple.BASIC_STATUS, false);
        np.writeUnlock(pidfRoot);
        return true;
    }

    public void unsubscribe(Key key) {
        throw new RuntimeException("Not implemented");
    }
}
