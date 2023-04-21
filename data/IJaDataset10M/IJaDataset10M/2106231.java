package org.sourceforge.jemm.collections.internal.map;

import org.sourceforge.jemm.client.shared.ValueEncoder;
import org.sourceforge.jemm.collections.internal.StoredValue;
import org.sourceforge.jemm.lifecycle.ObjectRequest;

public class ContainsValueRequest extends ObjectRequest<ContainsValueRequest> {

    private static final long serialVersionUID = 1L;

    private final StoredValue value;

    public ContainsValueRequest(Object value) {
        this(new StoredValue(value));
    }

    public ContainsValueRequest(StoredValue value) {
        super(MapActionType.CONTAINS_VALUE);
        this.value = value;
    }

    public StoredValue getValue() {
        return value;
    }

    @Override
    public ContainsValueRequest encode(ValueEncoder encoder) {
        return new ContainsValueRequest(encoder.encode(value));
    }
}
