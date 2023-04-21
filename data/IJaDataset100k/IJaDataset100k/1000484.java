package org.sourceforge.jemm.collections.internal.map;

import org.sourceforge.jemm.client.shared.ValueEncoder;
import org.sourceforge.jemm.collections.internal.StoredValue;
import org.sourceforge.jemm.lifecycle.ObjectResponse;
import org.sourceforge.jemm.lifecycle.ValueVisitor;

public class MapPutResponse extends ObjectResponse<MapPutResponse> {

    private static final long serialVersionUID = 1L;

    private final boolean success;

    private final Object previousValue;

    private final StoredValue[] possibleMatches;

    public MapPutResponse(boolean success, Object previousValue, StoredValue[] possibleMatches) {
        this.success = success;
        this.previousValue = previousValue;
        this.possibleMatches = possibleMatches;
    }

    @Override
    public MapPutResponse encode(ValueEncoder encoder) {
        return new MapPutResponse(isSuccess(), encoder.encode(getPreviousValue()), encoder.encodeArray(getPossibleMatches()));
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getPreviousValue() {
        return previousValue;
    }

    public StoredValue[] getPossibleMatches() {
        return possibleMatches;
    }

    @Override
    public void visit(ValueVisitor visitor) {
        visitor.visit(previousValue);
        visitor.visit(possibleMatches);
    }
}
