package com.entelience.objects.vuln;

import com.entelience.objects.Id;

public class VulnCommentId extends Id implements java.io.Serializable {

    public VulnCommentId() {
        super();
    }

    public VulnCommentId(Id other) {
        super(other);
    }

    public VulnCommentId(int id, int objectSerial) {
        super(id, objectSerial);
    }
}
