package gov.nih.niaid.bcbb.nexplorer3.server.datamodels;

import gov.nih.niaid.bcbb.nexplorer3.server.interfaces.CDAOEdgeView;
import org.cdao.wrapper.CDAOEdgeImpl;
import salvo.jesus.graph.Vertex;

@SuppressWarnings("serial")
public class CDAOEdgeViewImpl extends CDAOEdgeImpl implements CDAOEdgeView {

    public CDAOEdgeViewImpl(Vertex v1, Vertex v2) {
        super(v1, v2);
    }
}
