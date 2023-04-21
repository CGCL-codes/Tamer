package org.argouml.activity2.diagram;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.diagram.UMLMutableGraphSupport;

class ActivityDiagramGraphModel extends UMLMutableGraphSupport {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ActivityDiagramGraphModel.class);

    ActivityDiagramGraphModel() {
        super();
    }

    public List getPorts(Object nodeOrEdge) {
        List res = new ArrayList();
        return res;
    }

    public List getInEdges(Object port) {
        return null;
    }

    public List getOutEdges(Object port) {
        return null;
    }

    public Object getOwner(Object port) {
        return port;
    }
}
