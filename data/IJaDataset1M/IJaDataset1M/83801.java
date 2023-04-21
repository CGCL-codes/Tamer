package org.jomp.prototype.client.battlefield;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FieldData implements Serializable {

    int index;

    private String typeId;

    @SuppressWarnings("unused")
    private FieldData() {
    }

    public FieldData(int index, String type) {
        this.index = index;
        this.typeId = type;
    }

    public int getIndex() {
        return index;
    }

    public String getTypeId() {
        return typeId;
    }
}
