package generalise;

public class Instance {

    private String name;

    private int instanceID;

    private int classID;

    private int containedInClassID;

    public Instance(String nm, int inst, int clsID, int contClsID) {
        this.name = nm;
        this.instanceID = inst;
        this.classID = clsID;
        this.containedInClassID = contClsID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(int instanceID) {
        this.instanceID = instanceID;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public int getContainedInClassID() {
        return containedInClassID;
    }

    public void setContainedInClassID(int containedInClassID) {
        this.containedInClassID = containedInClassID;
    }
}
