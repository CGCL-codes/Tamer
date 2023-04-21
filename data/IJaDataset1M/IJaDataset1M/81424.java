package visad.data.dods;

import dods.dap.*;
import java.rmi.RemoteException;
import visad.*;

/**
 * Provides support for adapting a DODS {@link UInt32PrimitiveVector} to the
 * VisAD data-import context.
 *
 * <P>Instances are immutable.</P>
 *
 * @author Steven R. Emmerson
 */
public final class UInt32VectorAdapter extends FloatVectorAdapter {

    private final Valuator valuator;

    /**
     * Constructs from a DODS vector and a factory for creating DODS variable
     * adapters.
     *
     * @param vector		A DODS vector to be adapted.
     * @param das		The DODS DAS in which the attribute
     *				table for the DODS vector is embedded.
     * @param factory		A factory for creating adapters of DODS
     *				variables.
     * @throws BadFormException	The DODS information is corrupt.
     * @throws VisADException	VisAD failure.
     * @throws RemoteException	Java RMI failure.
     */
    public UInt32VectorAdapter(UInt32PrimitiveVector vector, DAS das, VariableAdapterFactory factory) throws VisADException, RemoteException {
        super(vector, das, factory);
        valuator = Valuator.valuator(attributeTable(das, vector.getTemplate()), Attribute.UINT32);
    }

    /**
     * Returns the numeric values of a compatible DODS primitive vector.
     *
     * @param vec		A DODS primitive vector that is compatible with
     *				the primitive vector used to construct this
     *				instance.
     * @param copy		If true, then a copy is returned.
     * @return			The numeric values of the primitive vector.
     */
    public float[] getFloats(PrimitiveVector vec, boolean copy) {
        UInt32PrimitiveVector vector = (UInt32PrimitiveVector) vec;
        float[] values = new float[vector.getLength()];
        for (int i = 0; i < values.length; ++i) values[i] = vector.getValue(i);
        return valuator.process(values);
    }
}
