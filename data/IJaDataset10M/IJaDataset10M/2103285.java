package de.grogra.xl.vmx;

import de.grogra.xl.lang.*;

/**
 * A descriptor for a later invocation of a routine in a given frame context.
 * Instances of <code>RoutineDescriptor</code> are obtained by
 * {@link de.grogra.xl.vmx.VMXState#createDescriptor(Routine, int, Authorization)}.
 * 
 * @author Ole Kniemeyer
 */
public final class RoutineDescriptor implements BooleanConsumer, ByteConsumer, ShortConsumer, CharConsumer, IntConsumer, LongConsumer, FloatConsumer, DoubleConsumer, ObjectConsumer, VoidConsumer {

    final VMXState vmx;

    VMXState.VMXFrame staticLink;

    Authorization auth;

    Routine routine;

    RoutineDescriptor next;

    RoutineDescriptor(VMXState vmx) {
        this.vmx = vmx;
    }

    /**
	 * This method can be used to recycle this instance. If this
	 * descriptor is not needed any more, this method can be invoked
	 * in order to inform the VMXState that it may re-use this instance.
	 */
    public void dispose() {
        next = vmx.descriptorPool;
        vmx.descriptorPool = this;
    }

    /**
	 * Invokes the associated routine with
	 * a single <code>boolean</code> parameter.
	 * The routine has to return an instance of
	 * {@link AbruptCompletion.Return} with a <code>void</code> type.
	 */
    public void consume(boolean value) {
        vmx.ipush(value ? 1 : 0);
        vmx.invoke(this);
    }

    /**
	 * Invokes the associated routine with
	 * a single <code>byte</code> parameter.
	 * The routine has to return an instance of
	 * {@link AbruptCompletion.Return} with a <code>void</code> type.
	 */
    public void consume(byte value) {
        vmx.ipush(value);
        vmx.invoke(this);
    }

    /**
	 * Invokes the associated routine with
	 * a single <code>short</code> parameter.
	 * The routine has to return an instance of
	 * {@link AbruptCompletion.Return} with a <code>void</code> type.
	 */
    public void consume(short value) {
        vmx.ipush(value);
        vmx.invoke(this);
    }

    /**
	 * Invokes the associated routine with
	 * a single <code>char</code> parameter.
	 * The routine has to return an instance of
	 * {@link AbruptCompletion.Return} with a <code>void</code> type.
	 */
    public void consume(char value) {
        vmx.ipush(value);
        vmx.invoke(this);
    }

    /**
	 * Invokes the associated routine with
	 * a single <code>int</code> parameter.
	 * The routine has to return an instance of
	 * {@link AbruptCompletion.Return} with a <code>void</code> type.
	 */
    public void consume(int value) {
        vmx.ipush(value);
        vmx.invoke(this);
    }

    /**
	 * Invokes the associated routine with
	 * a single <code>long</code> parameter.
	 * The routine has to return an instance of
	 * {@link AbruptCompletion.Return} with a <code>void</code> type.
	 */
    public void consume(long value) {
        vmx.lpush(value);
        vmx.invoke(this);
    }

    /**
	 * Invokes the associated routine with
	 * a single <code>float</code> parameter.
	 * The routine has to return an instance of
	 * {@link AbruptCompletion.Return} with a <code>void</code> type.
	 */
    public void consume(float value) {
        vmx.fpush(value);
        vmx.invoke(this);
    }

    /**
	 * Invokes the associated routine with
	 * a single <code>double</code> parameter.
	 * The routine has to return an instance of
	 * {@link AbruptCompletion.Return} with a <code>void</code> type.
	 */
    public void consume(double value) {
        vmx.dpush(value);
        vmx.invoke(this);
    }

    /**
	 * Invokes the associated routine with
	 * a single <code>Object</code> parameter.
	 * The routine has to return an instance of
	 * {@link AbruptCompletion.Return} with a <code>void</code> type.
	 */
    public void consume(Object value) {
        vmx.apush(value);
        vmx.invoke(this);
    }

    /**
	 * Invokes the associated routine with
	 * no parameters.
	 * The routine has to return an instance of
	 * {@link AbruptCompletion.Return} with a <code>void</code> type.
	 */
    public void consume() {
        vmx.invoke(this);
    }

    /**
	 * Invokes the associated routine. Parameters have to be
	 * pushed on the stack in advance, the will be popped by
	 * the invocation.
	 * 
	 * @return the return value of the routine
	 */
    public AbruptCompletion.Return invoke() {
        return vmx.invoke(this);
    }
}
