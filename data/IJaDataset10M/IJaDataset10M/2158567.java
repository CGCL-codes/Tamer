package edu.gsbme.geometrykernel.data.attributes;

public class Scalar extends IAttribute {

    public double value;

    public Scalar(String id) {
        super(id);
    }

    public Scalar(Attr attr) {
        super(attr);
    }

    public Scalar(String id, double value) {
        super(id);
        this.value = value;
    }

    public Scalar(Attr attr, double value) {
        super(attr);
        this.value = value;
    }

    @Override
    public IAttribute clone() {
        Scalar clone = new Scalar(this.getID());
        clone.value = this.value;
        return clone;
    }

    @Override
    public void dispose() {
    }
}
