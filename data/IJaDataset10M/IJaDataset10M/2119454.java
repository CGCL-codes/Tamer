package backend.model;

import backend.adt.ParamSet;

/**
 * model component that represents "Build" tools.
 */
public class Build implements SubPart {

    public ParamSet paramSet = null;

    protected int primarySketchID;

    protected Part part;

    public final int ID;

    public Build(Part part, int primarySketchID, int ID) {
        this.part = part;
        this.primarySketchID = primarySketchID;
        this.ID = ID;
        if (part.getSketchByID(primarySketchID) == null) {
            System.out.println("Tried to create a BUILD on an invalid primarySketch ID!!! :(");
        }
    }

    public Part getParentPart() {
        return this.part;
    }

    public Sketch getPrimarySketch() {
        return part.getSketchByID(primarySketchID);
    }

    public int getUniqueID() {
        return ID;
    }

    public Build getBuild() {
        return this;
    }

    public Modify getModify() {
        return null;
    }

    public Sketch getSketch() {
        return null;
    }
}
