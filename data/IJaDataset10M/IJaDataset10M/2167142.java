package mpi;

import mpjbuf.*;

public class SimplePackerObject extends SimplePacker {

    public SimplePackerObject(int numEls) {
        super(numEls);
    }

    public void pack(mpjbuf.Buffer mpjbuf, Object buf, int offset) throws MPIException {
        try {
            ((mpjbuf.Buffer) mpjbuf).write((Object[]) buf, offset, numEls);
        } catch (Exception e) {
            throw new MPIException(e);
        }
    }

    public void unpack(mpjbuf.Buffer mpjbuf, Object buf, int offset) throws MPIException {
        try {
            ((mpjbuf.Buffer) mpjbuf).read((Object[]) buf, offset, numEls);
        } catch (Exception e) {
            throw new MPIException(e);
        }
    }

    public void unpackPartial(mpjbuf.Buffer mpjbuf, int length, Object buf, int offset) throws MPIException {
        try {
            ((mpjbuf.Buffer) mpjbuf).read((Object[]) buf, offset, length);
        } catch (Exception e) {
            throw new MPIException(e);
        }
    }

    public void pack(mpjbuf.Buffer mpjbuf, Object buf, int offset, int count) throws MPIException {
        try {
            ((mpjbuf.Buffer) mpjbuf).write((Object[]) buf, offset, count * numEls);
        } catch (Exception e) {
            throw new MPIException(e);
        }
    }

    public void unpack(mpjbuf.Buffer mpjbuf, Object buf, int offset, int count) throws MPIException {
        try {
            ((mpjbuf.Buffer) mpjbuf).read((Object[]) buf, offset, count * numEls);
        } catch (Exception e) {
            throw new MPIException(e);
        }
    }

    public void unpack(mpjbuf.Buffer mpjbuf, int length, Object buf, int offset, int count) throws MPIException {
        if (count * numEls < length) {
            throw new MPIException("Error in SimplePacker : count <" + (count * numEls) + "> is less than length <" + length + ">");
        }
        try {
            ((mpjbuf.Buffer) mpjbuf).read((Object[]) buf, offset, length);
        } catch (Exception e) {
            throw new MPIException(e);
        }
    }
}
