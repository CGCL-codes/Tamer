package variableinformation;

import others.EdgeInText;
import others.NodeInText;

public interface IGetNodeEdge {

    public NodeInText GetNodeFromID(String id);

    public EdgeInText GetEdgeFromSource(String source);
}
