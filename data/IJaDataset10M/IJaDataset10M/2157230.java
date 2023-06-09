package edu.gatech.lbs.sim.config;

import edu.gatech.lbs.sim.Simulation;
import java.io.IOException;
import org.w3c.dom.Element;

public interface IXmlConfigInterpreter {

    public void initFromXmlElement(Element parentNode, Simulation sim) throws IOException;
}
