package gate.creole.ontology.impl;

import gate.creole.ontology.OBNodeID;
import gate.creole.ontology.OURI;

/**
 *
 * @author johann
 */
public class OBNodeIDImpl extends ONodeIDImpl implements OBNodeID {

    public OBNodeIDImpl(String uri) {
        super(uri, true);
    }
}
