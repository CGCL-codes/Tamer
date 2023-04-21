package org.conann.metadata;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.Maps;
import org.conann.common.ConcurrentStaticCache;
import org.conann.metadata.records.TypeRecord;
import org.conann.metadata.scanning.ClassScanner;
import java.util.concurrent.ConcurrentMap;

public class BasicRecordsRegistry extends ConcurrentStaticCache<Class<?>, TypeRecord<?>> implements RecordsRegistry {

    private final ConcurrentMap<Class<?>, TypeRecord<?>> records = Maps.newConcurrentHashMap();

    private final ClassScanner classScanner = new ClassScanner();

    @Override
    public TypeRecord<?> create(Class<?> type) {
        return classScanner.scan(type);
    }

    @SuppressWarnings({ "unchecked" })
    public <T> TypeRecord<T> getRecord(Class<T> type) {
        checkNotNull(type);
        TypeRecord<T> record = (TypeRecord<T>) records.get(type);
        if (record == null) {
            records.putIfAbsent(type, (TypeRecord<T>) classScanner.scan(type));
            record = (TypeRecord<T>) records.get(type);
        }
        return checkNotNull(record, "No record found for " + type);
    }

    public boolean hasRecord(Class<?> type) {
        checkNotNull(type);
        return records.containsKey(type);
    }
}
