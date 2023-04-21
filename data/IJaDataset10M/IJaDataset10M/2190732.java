package net.jadoth.util.bytes;

import java.util.ArrayList;
import net.jadoth.lang.concurrent.VM;
import net.jadoth.persistence.OidResolving;
import net.jadoth.persistence.Persistence;
import net.jadoth.persistence.Persister;
import net.jadoth.persistence.PersistingFunction;
import sun.misc.Unsafe;

public final class BinaryHandlerNativeArrayList extends AbstractBinaryHandlerNative<ArrayList<?>> {

    private static final Unsafe vm = VM.getUnsafe();

    private static final long LENGTH_CAPACITY = 4L;

    public BinaryHandlerNativeArrayList(final long typeId) {
        super(typeId);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Class<ArrayList<?>> getType() {
        return (Class) ArrayList.class;
    }

    @Override
    public long getBinaryTotalLength() {
        return 0L;
    }

    @Override
    public long getBinaryReferenceLength(final long[] bytes, final long offset) {
        return BinaryPersistence.getInstanceDataLength(bytes, offset) - LENGTH_CAPACITY;
    }

    @Override
    public long getBinaryReferenceOffset(final long[] bytes, final long offset) {
        return LENGTH_CAPACITY;
    }

    @Override
    public void store(final Persister<Binary> persister, final Binary bytes, final ArrayList<?> instance, final long oid) {
        final Object[] elements;
        BinaryPersistence.storeHeader(bytes, ((long) instance.size() << 3) + LENGTH_CAPACITY, this.getTypeId(), oid);
        vm.putInt(bytes.data, bytes.offset, (elements = VM.accessStorage(instance)).length);
        bytes.offset += LENGTH_CAPACITY;
        BinaryPersistence.storeArrayObjectContent(bytes, persister, this.getTypeId(), oid, elements, 0, instance.size());
    }

    @Override
    public ArrayList<?> create(final Binary bytes) {
        return new ArrayList<Object>(vm.getInt(bytes.data, bytes.offset));
    }

    @Override
    public void update(final Binary bytes, final ArrayList<?> instance, final OidResolving oidResolver) {
        BinaryPersistence.updateArrayList(instance, bytes.data, bytes.offset, oidResolver);
    }

    @Override
    public void iterateReferences(final ArrayList<?> instance, final PersistingFunction iterator) {
        final Object[] array = VM.accessStorage(instance);
        Persistence.iterateReferences(iterator, array, 0, instance.size());
    }
}
