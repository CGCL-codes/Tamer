package edu.gsbme.geometrykernel.data.brep.utility;

import java.util.ArrayList;
import edu.gsbme.geometrykernel.data.brep.table.EdgeAdjacency;

/**
 * Edge routing utility
 * @author David
 *
 */
public class EdgeAdjUtility {

    public static ArrayList<EdgeAdjacency> TraceRoute(EdgeAdjacency[] edge_array, ArrayList<EdgeAdjacency> result, EdgeAdjacency starting_point) {
        String vtx2 = starting_point.vtx2;
        String up = starting_point.up;
        String down = starting_point.down;
        EdgeAdjacency next = findEdgeByVtx1(edge_array, vtx2, up, down);
        if (next != null) {
            if (!result.contains(next)) {
                result.add(next);
                return TraceRoute(edge_array, result, next);
            } else {
                return result;
            }
        }
        return result;
    }

    public static EdgeAdjacency findEdgeByVtx1(EdgeAdjacency[] edge_array, String vtx1, String up, String down) {
        for (int i = 0; i < edge_array.length; i++) {
            if (edge_array[i].vtx1.equals(vtx1) && edge_array[i].up.equals(up) && edge_array[i].down.equals(down)) return edge_array[i];
        }
        return null;
    }

    public static EdgeAdjacency findEdgeByVtx2(EdgeAdjacency[] edge_array, String vtx2, String up, String down) {
        for (int i = 0; i < edge_array.length; i++) {
            if (edge_array[i].vtx2.equals(vtx2) && edge_array[i].up.equals(up) && edge_array[i].down.equals(down)) return edge_array[i];
        }
        return null;
    }
}
