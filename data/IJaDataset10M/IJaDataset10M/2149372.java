package org.qtnew.simulator;

import org.jdom.Element;
import org.qtnew.util.XmlHelper;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Malykh
 * Date: 25.12.2006
 * Time: 12:24:58
 * To change this template use File | Settings | File Templates.
 */
public class ScriptedSimulator extends IterativeSimulator {

    private HashMap<String, Element> simDef = new HashMap<String, Element>();

    private String loopCode;

    public ScriptedSimulator(Simulator parent, Element e) throws ConfiguratorException {
        super(parent, e);
        for (Element subSim : (List<Element>) e.getChildren("Simulator")) simDef.put(subSim.getAttributeValue("name"), subSim);
        loopCode = getString(e.getAttributeValue("loopcode"));
    }

    public void simulate() {
        start();
        getContext().evaluateString(loopCode);
        stop();
    }

    public Simulator createSimulator(String name) throws ConfiguratorException {
        if (!simDef.containsKey(name)) throw new ConfiguratorException("No simulator named " + name);
        return XmlHelper.resolveSimulator(this, simDef.get(name));
    }
}
