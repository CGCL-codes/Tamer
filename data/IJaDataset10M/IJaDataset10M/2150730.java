package mpi;

public interface Packer {

    public abstract void pack(mpjbuf.Buffer mpjbuf, Object buf, int offset) throws MPIException;

    public abstract void unpack(mpjbuf.Buffer mpjbuf, Object buf, int offset) throws MPIException;

    /**
   * Precondition for calling `unpackPartial()' is that `length'
   * is less than `size' (numEls).
   */
    public abstract void unpackPartial(mpjbuf.Buffer mpjbuf, int length, Object buf, int offset) throws MPIException;

    public abstract void pack(mpjbuf.Buffer mpjbuf, Object buf, int offset, int count) throws MPIException;

    public abstract void unpack(mpjbuf.Buffer mpjbuf, int length, Object buf, int offset, int count) throws MPIException;

    public abstract void unpack(mpjbuf.Buffer mpjbuf, Object buf, int offset, int count) throws MPIException;
}
