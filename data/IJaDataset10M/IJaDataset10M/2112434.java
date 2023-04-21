package edu.gsbme.wasabi.Algorithm;

import org.w3c.dom.Element;
import edu.gsbme.wasabi.DataModel.XMLModel;

public abstract class ModifierAlgorithm extends Algorithm {

    public abstract Object run(XMLModel owner, Element modifier);
}
