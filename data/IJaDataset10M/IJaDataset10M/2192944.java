package edu.gsbme.geometrykernel.data.brep.topoGraph;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Topological Tree Structure
 * @author David
 *
 */
public class topoGraph {

    public HashMap<String, adjObject> face_lib;

    public HashMap<String, adjObject> edge_lib;

    public HashMap<String, adjObject> vtx_lib;

    public HashMap<String, adjObject> pedge_lib;

    public HashMap<String, adjObject> pvtx_lib;

    public HashMap<String, adjObject> entity_lib;

    public ArrayList<adjObject> root;

    int dimension;

    public topoGraph(int dimension) {
        root = new ArrayList<adjObject>();
        this.dimension = dimension;
        vtx_lib = new HashMap<String, adjObject>();
        if (dimension >= 2) {
            edge_lib = new HashMap<String, adjObject>();
            pvtx_lib = new HashMap<String, adjObject>();
        }
        if (dimension >= 3) {
            face_lib = new HashMap<String, adjObject>();
            pedge_lib = new HashMap<String, adjObject>();
        }
        entity_lib = new HashMap<String, adjObject>();
    }

    public void setRoots() {
        root.clear();
        if (dimension == 1) {
            root.addAll(vtx_lib.values());
        } else if (dimension == 2) {
            root.addAll(edge_lib.values());
        } else {
            root.addAll(face_lib.values());
        }
    }

    public void insertRoot(adjObject obj) {
        root.add(obj);
    }
}
