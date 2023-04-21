package net.sf.asyncobjects.io.util;

import net.sf.asyncobjects.ACloseable;
import net.sf.asyncobjects.AsyncUnicastServer;
import net.sf.asyncobjects.Promise;
import net.sf.asyncobjects.io.AOutput;
import net.sf.asyncobjects.io.BatchedData;
import net.sf.asyncobjects.util.RequestQueue;
import net.sf.asyncobjects.util.Serialized;
import net.sf.asyncobjects.util.Wait;
import net.sf.asyncobjects.util.WaitAllException;

/**
 * A stream that copies data written to it into streams passed to it as
 * arguments. Close operation closes all streams. A error in one stream causes
 * an {@link WaitAllException} to be thrown.
 * 
 * @author const
 * @param <D>
 *            a batched data type
 * @param <O>
 *            an output type
 */
public class MulticastOutput<D extends BatchedData<D>, O extends AOutput<D>> extends AsyncUnicastServer<O> implements AOutput<D> {

    /** output streams */
    final O[] outputs;

    /** request queue */
    final RequestQueue requests = new RequestQueue();

    /**
	 * A constructor
	 * 
	 * @param outputs
	 *            outputs to wrap
	 */
    public MulticastOutput(final O... outputs) {
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] == null) {
                throw new NullPointerException("outputs cannot contain null: " + i);
            }
        }
        this.outputs = outputs;
    }

    /**
	 * Write data
	 * 
	 * @param data
	 *            a data to write
	 * @return a promise that resolves when write operation finishes
	 * @see AOutput#write(net.sf.asyncobjects.io.BatchedData)
	 */
    public Promise<Void> write(final D data) {
        return new Serialized<Void>(requests) {

            @Override
            protected Promise<Void> run() throws Throwable {
                Promise<?>[] ops = new Promise<?>[outputs.length];
                for (int i = 0; i < outputs.length; i++) {
                    try {
                        ops[i] = outputs[i].write(data);
                    } catch (Throwable t) {
                        ops[i] = Promise.smashed(t);
                    }
                }
                return Wait.all(ops).toVoid();
            }
        }.promise();
    }

    /**
	 * Flush data
	 * 
	 * @return a promise that resolves when flush operation finishes
	 * @see AOutput#flush()
	 */
    public Promise<Void> flush() {
        return new Serialized<Void>(requests) {

            @Override
            protected Promise<Void> run() throws Throwable {
                Promise<?>[] ops = new Promise<?>[outputs.length];
                for (int i = 0; i < outputs.length; i++) {
                    try {
                        ops[i] = outputs[i].flush();
                    } catch (Throwable t) {
                        ops[i] = Promise.smashed(t);
                    }
                }
                return Wait.all(ops).toVoid();
            }
        }.promise();
    }

    /**
	 * Close stream
	 * 
	 * @return a promise that resolves when close operation finishes
	 * @see ACloseable#close()
	 */
    public Promise<Void> close() {
        return new Serialized<Void>(requests) {

            @Override
            protected Promise<Void> run() throws Throwable {
                Promise<?>[] ops = new Promise<?>[outputs.length];
                for (int i = 0; i < outputs.length; i++) {
                    try {
                        ops[i] = outputs[i].close();
                    } catch (Throwable t) {
                        ops[i] = Promise.smashed(t);
                    }
                }
                return Wait.all(ops).toVoid();
            }
        }.promise();
    }
}
