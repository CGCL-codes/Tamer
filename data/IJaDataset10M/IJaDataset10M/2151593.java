package flit;

public class FlitArray {

    private Flit[] flits;

    private int dims;

    public FlitArray() {
        this(1);
    }

    public FlitArray(int numberOfChannels) {
        this.dims = numberOfChannels;
        this.flits = new Flit[numberOfChannels];
    }

    public int dims() {
        return this.dims;
    }

    public Flit getDim(int dim) {
        if (dim > -1 && dim < this.dims()) {
            return this.flits[dim];
        } else return null;
    }

    public void setDim(int dim, Flit f) {
        this.flits[dim] = f;
    }
}
