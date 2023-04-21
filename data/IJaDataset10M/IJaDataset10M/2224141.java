package gov.nasa.jpf.util.script;

public class Script extends ScriptElementContainer {

    /***** the stuff we need for traversal *****/
    public Script() {
        super(null, 0);
    }

    public String toString() {
        return toString("Script");
    }

    public void process(ElementProcessor p) {
        processChildren(p);
    }
}
