package org.yacmmf.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.yacmmf.predicate.Predicate;

/**
 * Some {@link Component}s don't need to be drilled down any further, services
 * have to be able to work with them directly, e.g. an editor has to be able to
 * edit a {@link String} directly.
 */
public class AtomicComponent<V> implements Component {

    private static final List<Class<?>> ATOMIC_TYPES = new ArrayList<Class<?>>();

    static {
        addAtomicType(String.class);
        addAtomicType(StringBuilder.class);
        addAtomicType(StringBuffer.class);
        addAtomicType(Number.class);
        addAtomicType(Date.class);
        addAtomicType(Calendar.class);
        addAtomicType(Void.class);
        addAtomicType(Boolean.class);
        addAtomicType(Byte.class);
        addAtomicType(Character.class);
    }

    public static void addAtomicType(Class<?> type) {
        ATOMIC_TYPES.add(type);
    }

    public static boolean isAtomic(Class<?> type) {
        if (type.isPrimitive()) return true;
        for (final Class<?> primitive : ATOMIC_TYPES) {
            if (primitive.isAssignableFrom(type)) {
                return true;
            }
        }
        return false;
    }

    private final V value;

    private final MetaComponent metaComponent = new MetaComponent() {

        @Override
        public Component create() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void delete(Component component) {
        }

        @Override
        public Collection<? extends Attribute> getAttributes() {
            return Collections.emptyList();
        }

        @Override
        public Collection<? extends Event> getEvents() {
            return Collections.emptyList();
        }

        @Override
        public <T> T getMetaData(Attribute attribute, AttributeMetaDataKey<T> key) {
            return null;
        }

        @Override
        public <T> T getMetaData(ComponentMetaDataKey<T> key) {
            return null;
        }

        @Override
        public <T> T getMetaData(Event event, EventMetaDataKey<T> key) {
            return null;
        }

        @Override
        public <T> T getMetaData(Operation operation, OperationMetaDataKey<T> key) {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Collection<? extends Operation> getOperations() {
            return Collections.emptyList();
        }

        @Override
        public String getSimpleName() {
            return null;
        }

        @Override
        public Component locate(Predicate<Component> predicate) {
            return null;
        }

        @Override
        public <T> void putMetaData(Attribute attribute, AttributeMetaDataKey<T> key, T value) {
        }

        @Override
        public <T> void putMetaData(ComponentMetaDataKey<T> key, T value) {
        }

        @Override
        public <T> void putMetaData(Event event, EventMetaDataKey<T> key, T value) {
        }

        @Override
        public <T> void putMetaData(Operation operation, OperationMetaDataKey<T> key, T value) {
        }

        @Override
        public Iterable<Component> search(Predicate<Component> predicate) {
            return null;
        }

        @Override
        public void visit(AttributeVisitor attributeVisitor) {
        }
    };

    public AtomicComponent(V value) {
        this.value = value;
    }

    @Override
    public MetaComponent getMetaComponent() {
        return metaComponent;
    }

    public V getValue() {
        return value;
    }
}
