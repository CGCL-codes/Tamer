package org.jul.dsm.constructor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.jul.dsm.Change;
import org.jul.dsm.ConstructorFactory;
import org.jul.dsm.IdEncoder;
import org.jul.dsm.Representation;
import org.jul.dsm.RepresentationConstructor;
import org.jul.dsm.RepresentationException;
import org.jul.dsm.SharedMemory;
import org.jul.dsm.UnitializedConstructorException;
import org.jul.dsm.Utils;
import org.jul.dsm.representation.PrimitiveValueRepresentation;

public class PrimitiveValueConstructor extends RepresentationConstructor {

    private Object value = null;

    public void decode(DataInput input, IdEncoder idEncoder, ClassLoader classLoader) throws IOException {
        value = Utils.readPrimitive(input);
    }

    public void encode(DataOutput output, IdEncoder idEncoder) throws IOException {
        try {
            if (value == null) {
                throw new UnitializedConstructorException();
            }
            Utils.writePrimitive(output, value);
        } catch (IllegalArgumentException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    @Override
    public boolean canHandle(Class<?> clazz, Object reference) {
        return clazz.isPrimitive() || Utils.isPrimitiveTypeWrapper(clazz);
    }

    @Override
    protected Representation createFromReference0(SharedMemory memory, Representation parentRepresentation, long id, Object reference, Class<?> clazz) {
        return new PrimitiveValueRepresentation(memory, parentRepresentation, id, reference, true);
    }

    @Override
    public Representation create(SharedMemory memory, long id) throws RepresentationException {
        if (value == null) {
            throw new UnitializedConstructorException();
        } else {
            return new PrimitiveValueRepresentation(memory, null, id, value, false);
        }
    }

    @Override
    public Change getChangeInstance() {
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + " [value = " + value + "]";
    }

    public PrimitiveValueConstructor(ConstructorFactory constructorFactory, Object value) {
        super(constructorFactory);
        this.value = value;
    }

    public PrimitiveValueConstructor(ConstructorFactory constructorFactory) {
        super(constructorFactory);
    }
}
