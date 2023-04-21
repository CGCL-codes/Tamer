package org.jul.dsm.constructor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Array;
import org.jul.dsm.Change;
import org.jul.dsm.ConstructorFactory;
import org.jul.dsm.IdEncoder;
import org.jul.dsm.Representation;
import org.jul.dsm.RepresentationConstructor;
import org.jul.dsm.RepresentationException;
import org.jul.dsm.SharedMemory;
import org.jul.dsm.UnitializedConstructorException;
import org.jul.dsm.Utils;
import org.jul.dsm.representation.PrimitiveArrayRepresentation;

public class PrimitiveArrayConstructor extends RepresentationConstructor {

    private static final int UNITIALIZED_LENGTH = Integer.MIN_VALUE;

    private int length = UNITIALIZED_LENGTH;

    private Class<?> type = null;

    public void decode(DataInput input, IdEncoder idEncoder, ClassLoader classLoader) throws IOException {
        length = input.readInt();
        type = Utils.getPrimitiveType(input.readByte());
    }

    public void encode(DataOutput output, IdEncoder idEncoder) throws IOException {
        try {
            if (length == UNITIALIZED_LENGTH || type == null) {
                throw new UnitializedConstructorException();
            }
            output.writeInt(length);
            output.writeByte(Utils.getPrimitiveType(type));
        } catch (UnitializedConstructorException e) {
            throw (IOException) new IOException().initCause(e);
        } catch (IllegalArgumentException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    @Override
    public boolean canHandle(Class<?> clazz, Object reference) {
        return clazz.isArray() && clazz.getComponentType().isPrimitive();
    }

    @Override
    protected Representation createFromReference0(SharedMemory memory, Representation parentRepresentation, long id, Object reference, Class<?> clazz) {
        return new PrimitiveArrayRepresentation(memory, parentRepresentation, id, reference);
    }

    @Override
    public Representation create(SharedMemory memory, long id) throws RepresentationException {
        if (length == UNITIALIZED_LENGTH) {
            throw new UnitializedConstructorException();
        } else {
            try {
                return new PrimitiveArrayRepresentation(memory, null, id, Array.newInstance(type, length));
            } catch (NegativeArraySizeException e) {
                throw new RepresentationException("Invalid length for array creation!", e);
            }
        }
    }

    @Override
    public Change getChangeInstance() {
        return new PrimitiveArrayRepresentation.PrimitiveArrayChange(getConstructorFactory());
    }

    @Override
    public String toString() {
        return super.toString() + "<" + type + "[]> [length = " + length + "]";
    }

    public PrimitiveArrayConstructor(ConstructorFactory constructorFactory, int length, Class<?> type) {
        super(constructorFactory);
        this.length = length;
        this.type = type;
    }

    public PrimitiveArrayConstructor(ConstructorFactory constructorFactory) {
        super(constructorFactory);
    }
}
