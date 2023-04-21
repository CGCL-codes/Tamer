package ctf;

import cartago.*;

public class TerrainTopology implements AbstractWorkspaceTopology, java.io.Serializable {

    @Override
    public double getDistance(AbstractWorkspacePoint p0, AbstractWorkspacePoint p1) {
        return ((P3d) p0).getDistanceFrom((P3d) p1);
    }
}
