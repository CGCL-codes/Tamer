package com.entelience.objects.audit;

import com.entelience.objects.Id;

public class AuditComplianceId extends Id implements java.io.Serializable {

    public AuditComplianceId() {
        super();
    }

    public AuditComplianceId(Id other) {
        super(other);
    }

    public AuditComplianceId(int id, int objectSerial) {
        super(id, objectSerial);
    }
}
