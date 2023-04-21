package unikrakadmin.graph;

import unikrakadmin.*;
import unikrakadmin.map.*;
import java.util.ArrayList;

public class EditorNode {

    public Vertex pos;

    public int id;

    public ArrayList<Integer> edges;

    public int type;

    public String description;

    public int floor;

    public boolean moving = false;

    public int buildingId = -1;

    public Vertex mouseRel = null;

    private void setCommons(int id, double x, double y, int floor, int type, String desc) {
        this.id = id;
        this.pos = new Vertex(x, y);
        this.floor = floor;
        this.type = type;
        this.edges = new ArrayList<Integer>();
        this.description = desc;
    }

    public EditorNode(Node n) {
        setCommons(n.id, Math.round(n.x * 100.0) * 0.01, Math.round(n.y * 100.0) * 0.01, n.floor, n.type, n.description);
        for (int i = 0; i < n.edges.length; i++) this.edges.add(new Integer(n.edges[i]));
        this.buildingId = n.buildingId;
    }

    public EditorNode(int id, double x, double y, int floor, int type) {
        setCommons(id, x, y, floor, 0, "");
    }

    public EditorNode(int id, double x, double y, int floor, int type, String desc) {
        setCommons(id, x, y, floor, 0, desc);
    }

    public EditorNode(int id, double x, double y, int floor, int[] e, int type) {
        setCommons(id, x, y, floor, type, "");
        for (int i = 0; i < e.length; i++) this.edges.add(new Integer(e[i]));
    }

    public EditorNode(int id, double x, double y, int floor, int[] e, int type, String desc) {
        setCommons(id, x, y, floor, type, desc);
        for (int i = 0; i < e.length; i++) this.edges.add(new Integer(e[i]));
    }
}
