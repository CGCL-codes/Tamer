package se.inera.ifv.medcert.core.entity;

import java.util.Date;
import static org.apache.commons.lang.StringUtils.*;

/**
 * @author Pär Wenåker
 *
 */
public class CertificateBuilder {

    private String id;

    private String careUnitId;

    private String patientName;

    private String patientSsn;

    private Date signedAt;

    private CreatorOrigin origin;

    public CertificateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public CertificateBuilder setCareUnitId(String careUnitId) {
        this.careUnitId = careUnitId;
        return this;
    }

    public CertificateBuilder setPatientName(String patientName) {
        this.patientName = patientName;
        return this;
    }

    public CertificateBuilder setPatientSsn(String patientSsn) {
        this.patientSsn = patientSsn;
        return this;
    }

    public CertificateBuilder setSignedAt(Date signedAt) {
        this.signedAt = signedAt;
        return this;
    }

    public CertificateBuilder setOrigin(CreatorOrigin origin) {
        this.origin = origin;
        return this;
    }

    public Certificate build() {
        if (isEmpty(id)) throw new IllegalArgumentException("id is empty or null");
        if (isEmpty(careUnitId)) throw new IllegalArgumentException("careUnitId is empty or null!");
        if (isEmpty(patientName)) throw new IllegalArgumentException("patientName is empty or null!");
        if (isEmpty(patientSsn)) throw new IllegalArgumentException("patientSsn is empty or null!");
        if (origin == null) throw new IllegalArgumentException("origin=null!");
        return new Certificate(id, careUnitId, patientName, patientSsn, signedAt, origin);
    }
}
