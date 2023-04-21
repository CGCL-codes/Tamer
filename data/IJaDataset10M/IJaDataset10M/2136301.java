package org.numbox.ndim;

/**
 *
 * @author Alexander Heusel
 */
public abstract class StencilBuffer extends Stencil {

    protected final GridLayout gridLayout;

    protected final MemLayout memLayout;

    protected final AddrTranslator addrTranslator;

    protected boolean signedCopy;

    protected StencilBuffer(final GridLayout gridLayout, final MemLayout memLayout, final int... extent) {
        super(extent);
        this.gridLayout = gridLayout;
        this.memLayout = memLayout;
        this.addrTranslator = new AddrTranslator(gridLayout, memLayout);
        signedCopy = false;
    }

    protected StencilBuffer(final GridLayout gridLayout, final MemLayout memLayout, final int[] extent, final int[] stride) {
        super(extent, stride);
        this.gridLayout = gridLayout;
        this.memLayout = memLayout;
        this.addrTranslator = new AddrTranslator(gridLayout, memLayout);
        signedCopy = false;
    }

    public final boolean signedCopy() {
        return signedCopy;
    }

    public final void setSignedCopy(final boolean value) {
        signedCopy = value;
    }

    public abstract Object data();

    public abstract void copyToBuffer(final byte[] source, final byte border, final int... pos);

    public abstract void copyToBuffer(final short[] source, final short border, final int... pos);

    public abstract void copyToBuffer(final int[] source, final int border, final int... pos);

    public abstract void copyToBuffer(final long[] source, final long border, final int... pos);

    public abstract void copyToBuffer(final float[] source, final float border, final int... pos);

    public abstract void copyToBuffer(final double[] source, final double border, final int... pos);
}
