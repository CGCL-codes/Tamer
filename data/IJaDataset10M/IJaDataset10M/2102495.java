package aurora.hwc.control;

import java.io.*;
import org.w3c.dom.Node;
import aurora.*;
import aurora.hwc.*;

/**
 * Base class for queue controllers.
 * @author Alex Kurzhanskiy
 * @version $Id: AbstractQueueController.java 44 2010-02-18 20:05:29Z akurzhan $
 */
public abstract class AbstractQueueController {

    protected AbstractControllerSimpleHWC myController;

    protected boolean inOverride;

    public boolean usesensors = false;

    public SensorLoopDetector queuesensor;

    public Object computeInput(AbstractNodeHWC nd, AbstractLinkHWC lk) {
        return null;
    }

    public AbstractQueueController deepCopy() {
        return null;
    }

    /**
	 * Returns queue controller description.
	 */
    public String getDescription() {
        return "Queue Controller";
    }

    /**
	 * Initializes the queue controller from given DOM structure.
	 * @param p DOM node.
	 * @return <code>true</code> if operation succeeded, <code>false</code> - otherwise.
	 * @throws ExceptionConfiguration
	 */
    public boolean initFromDOM(Node p) throws ExceptionConfiguration {
        try {
            if (p.getAttributes().getNamedItem("usesensors") != null) usesensors = Boolean.parseBoolean(p.getAttributes().getNamedItem("usesensors").getNodeValue());
        } catch (Exception e) {
            throw new ExceptionConfiguration(e.getMessage());
        }
        return true;
    }

    /**
	 * Generates XML description of the Queue Override controller.<br>
	 * If the print stream is specified, then XML buffer is written to the stream.
	 * @param out print stream.
	 * @throws IOException
	 */
    public void xmlDump(PrintStream out) throws IOException {
        if (out == null) out = System.out;
        out.print("<qcontroller type=\"" + getTypeLetterCode() + "\">");
        return;
    }

    /**
	 * Returns letter code of the queue controller type.
	 */
    public abstract String getTypeLetterCode();

    public void setMyController(AbstractControllerSimpleHWC c) {
        myController = c;
    }

    /**
	 * Additional initialization.
	 * @return <code>true</code> if operation succeeded, <code>false</code> - otherwise.
	 * @throws ExceptionConfiguration
	 */
    public boolean initialize() throws ExceptionConfiguration {
        if (usesensors) {
            AbstractNodeComplex myNetwork = myController.getMyLink().getMyNetwork();
            queuesensor = (SensorLoopDetector) myNetwork.getSensorByLinkId(myController.getMyLink().getId());
        }
        return true;
    }
}
