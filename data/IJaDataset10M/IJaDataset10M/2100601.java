package com.google.common.collect;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * This class contains static utility methods for writing {@code Multiset} GWT
 * field serializers. Serializers should delegate to
 * {@link #serialize(SerializationStreamWriter, Multiset)} and to either
 * {@link #instantiate(SerializationStreamReader, ImmutableMultiset.Builder)} or
 * {@link #populate(SerializationStreamReader, Multiset)}.
 * 
 * @author Chris Povirk
 */
final class Multiset_CustomFieldSerializerBase {

    static ImmutableMultiset<Object> instantiate(SerializationStreamReader reader, ImmutableMultiset.Builder<Object> builder) throws SerializationException {
        int distinctElements = reader.readInt();
        for (int i = 0; i < distinctElements; i++) {
            Object element = reader.readObject();
            int count = reader.readInt();
            builder.addCopies(element, count);
        }
        return builder.build();
    }

    static Multiset<Object> populate(SerializationStreamReader reader, Multiset<Object> multiset) throws SerializationException {
        int distinctElements = reader.readInt();
        for (int i = 0; i < distinctElements; i++) {
            Object element = reader.readObject();
            int count = reader.readInt();
            multiset.add(element, count);
        }
        return multiset;
    }

    static void serialize(SerializationStreamWriter writer, Multiset<?> instance) throws SerializationException {
        int entryCount = instance.entrySet().size();
        writer.writeInt(entryCount);
        for (Multiset.Entry<?> entry : instance.entrySet()) {
            writer.writeObject(entry.getElement());
            writer.writeInt(entry.getCount());
        }
    }

    private Multiset_CustomFieldSerializerBase() {
    }
}
