package LONI.tree.GraphObject;

import Core.Pair;
import Galaxy.Tree.Workflow.Step;
import LONI.visitor.DFSVisitor;
import LONI.visitor.LoniToGalaxyConverter;
import Taverna.Tree.DataFlowImpl.Link;

public class GraphObject {

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    private int posX = 0;

    private int posY = 0;

    protected String id;

    public GraphObject() {
    }

    public GraphObject(int posX, int posY, String id) {
        this.posX = posX;
        this.posY = posY;
        this.id = id;
    }

    public GraphObject(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.id = null;
    }

    public String getId() {
        return id;
    }

    public Link getSink(String port) {
        return null;
    }

    public Link getSource(String port) {
        return null;
    }

    public Object accept(DFSVisitor.ModuleVisitor caller) {
        return caller.visit(this);
    }

    public <W, U, V> Pair<W, U> accept(DFSVisitor<U, V>.ModuleVisitor<W> caller, V o) {
        return caller.visit(this, o);
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setId(String id) {
        this.id = id;
    }
}
