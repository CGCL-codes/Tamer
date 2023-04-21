package net.jadoth.util.bytes;

import java.lang.reflect.Field;
import net.jadoth.collections.types.XGettingEnum;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.persistence.OidResolving;
import net.jadoth.persistence.PersistenceTypeStateDescriptionException;
import net.jadoth.persistence.Persister;
import net.jadoth.persistence.PersistingFunction;

public class BinaryHandlerNativeArray_long implements BinaryArrayObjectStateDescriptor<long[]>, BinaryTypeHandler<long[]> {

    private final long typeId;

    public BinaryHandlerNativeArray_long(final long typeId) {
        super();
        this.typeId = typeId;
    }

    @Override
    public BinaryTypeStateDescriptor<long[]> getTypeDescriptor() {
        return this;
    }

    @Override
    public long getTypeId() {
        return this.typeId;
    }

    @Override
    public Class<long[]> getType() {
        return long[].class;
    }

    @Override
    public void store(final Persister<Binary> persister, final Binary bytes, final long[] array, final long oid) {
        BinaryPersistence.storeArray_long(bytes, oid, array);
    }

    @Override
    public long[] create(final Binary bytes) {
        return BinaryPersistence.createArray_long(bytes.data, bytes.offset);
    }

    @Override
    public void update(final Binary bytes, final long[] instance, final OidResolving oidResolver) {
        BinaryPersistence.updateArray_long(instance, bytes.data, bytes.offset);
    }

    @Override
    public long getBinaryReferenceOffset(final long[] bytes, final long offset) {
        return 0L;
    }

    @Override
    public long getBinaryReferenceLength(final long[] bytes, final long offset) {
        return 0L;
    }

    @Override
    public long getBinaryTotalLength() {
        return 0L;
    }

    @Override
    public XGettingEnum<Field> getAllFields() {
        return BinaryPersistence.NO_FIELDS;
    }

    @Override
    public XGettingEnum<Field> getPrimitiveFields() {
        return BinaryPersistence.NO_FIELDS;
    }

    @Override
    public XGettingEnum<Field> getReferenceFields() {
        return BinaryPersistence.NO_FIELDS;
    }

    @Override
    public void iterateReferences(final long[] instance, final PersistingFunction iterator) {
    }

    @Override
    public void vaildatePersistentOrderFields(final XGettingSequence<Field> binaryPersistentOrderedFields) throws PersistenceTypeStateDescriptionException {
        if (binaryPersistentOrderedFields.isEmpty()) {
            return;
        }
        throw new PersistenceTypeStateDescriptionException(BinaryPersistence.FIELD_VALIDATION_EXCEPTION_ARRAYS);
    }
}
