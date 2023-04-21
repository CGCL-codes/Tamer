package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.metadata.value.CompositeValue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Implementation of {@link CompositeValue}.
 */
abstract class CompositeValueImpl extends MetaDataValueImpl implements CompositeValue {

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
