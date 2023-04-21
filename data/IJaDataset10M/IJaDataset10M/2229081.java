package sisc.ser;

import java.io.*;
import java.util.*;
import sisc.data.Expression;
import sisc.data.Singleton;
import sisc.data.Value;
import sisc.interpreter.AppContext;
import sisc.util.InternedValue;

public abstract class SLL2Serializer extends SerializerImpl {

    private static class SerJobEnd {

        public int posi;

        public int sizeStartOffset;

        public SerJobEnd(int posi, int sizeStartOffset) {
            this.posi = posi;
            this.sizeStartOffset = sizeStartOffset;
        }
    }

    private LinkedList serQueue;

    protected SLL2Serializer(AppContext ctx, ObjectOutput out) throws IOException {
        super(ctx, out);
        serQueue = new LinkedList();
    }

    /**
     * Required call which actually writes out the bytes of an expression
     * 
     * @param e
     * @param flush
     * @throws IOException
     */
    protected abstract void writeExpression(Expression e, boolean flush) throws IOException;

    protected abstract void serializeEnd(int posi, int sizeStartOffset);

    public void writeExpression(Expression e) throws IOException {
        writeExpressionHelper(e, false);
    }

    public void writeInitializedExpression(Expression e) throws IOException {
        writeExpressionHelper(e, true);
    }

    /**
     * Serializes expressions. We distinguish betweeen six types of
     * expressions:
     * Type 0: normal expression
     * Type 1: null
     * Type 2: first encounter of entry point / shared expression
     * Type 3: interned value
     * Type 4: entry point into other library
     * Type 16+n: reference to entry point / shared expression n
     *
     * @param e the expression to serialize
     * @param flush force complete, immediate serialisation
     * @exception IOException if an error occurs
     */
    private void writeExpressionHelper(Expression e, boolean flush) throws IOException {
        if (e == null) {
            writeInt(1);
            return;
        }
        writeExpression(e, flush);
    }

    public void serialize(Expression e) throws IOException {
        int start = serQueue.size();
        writeExpression(e);
        serLoop(start);
    }

    protected boolean writeExpression(Expression e, int pos, int offset, boolean flush) throws IOException {
        SerJobEnd job = new SerJobEnd(pos, offset);
        boolean contiguous;
        if (e instanceof Singleton) {
            contiguous = writeExpressionSerialization(e, job, flush);
        } else {
            LibraryBinding lb = lookupLibraryBinding(e);
            contiguous = (lb == null) ? writeExpressionSerialization(e, job, flush) : writeLibraryReference(lb, job, flush);
        }
        return contiguous;
    }

    private void serLoop(int start) throws IOException {
        while (serQueue.size() > start) {
            Object o = serQueue.removeFirst();
            if (o instanceof Expression) {
                serializeDetails((Expression) o);
            } else if (o instanceof SerJobEnd) {
                SerJobEnd job = (SerJobEnd) o;
                serializeEnd(job.posi, job.sizeStartOffset);
            }
        }
    }

    private void serializeDetails(Expression e) throws IOException {
        e.serialize(this);
        e.serializeAnnotations(this);
    }

    public void close() throws IOException {
        flush();
        super.close();
    }

    public void flush() throws IOException {
        serLoop(0);
        super.flush();
    }

    protected void writeSeenEntryPoint(int posi) throws IOException {
        writeInt(posi + 16);
    }

    protected void writeNewEntryPointMarker(int posi, Expression e) throws IOException {
        writeInt(2);
        writeInt(posi);
    }

    private boolean writeExpressionSerialization(Expression e, SerJobEnd end, boolean flush) throws IOException {
        if (e instanceof Value) {
            InternedValue iv = InternedValue.lookupByValue((Value) e);
            if (iv == null) {
                writeInt(0);
            } else {
                writeInt(3);
                writeInitializedExpression(iv.getName());
            }
        } else {
            writeInt(0);
        }
        writeClass(e.getClass());
        if (e instanceof Singleton) {
            e.serialize(this);
            return true;
        } else {
            int start = serQueue.size();
            serQueue.addFirst(end);
            serQueue.addFirst(e);
            if (flush) serLoop(start);
            return false;
        }
    }

    private boolean writeLibraryReference(LibraryBinding lb, SerJobEnd end, boolean flush) throws IOException {
        writeInt(4);
        writeUTF(lb.name);
        writeInt(lb.epid);
        return true;
    }
}
