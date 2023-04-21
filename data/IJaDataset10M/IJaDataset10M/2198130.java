package g4mfs.impl.gridpeertrust.net.server;

import java.util.Date;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * the ClientEntry class holds the subjectDN, issuerDN, operation and expirationDate of client procy certificate for an authorized client
 */
public class ClientEntry {

    String subjectDN;

    String issuerDN;

    String operation;

    Date expirationDate;

    public ClientEntry(String subjectDN, String issuerDN, String operation, Date expirationDate) {
        this.subjectDN = subjectDN;
        this.issuerDN = issuerDN;
        this.operation = operation;
        this.expirationDate = expirationDate;
    }

    public String getSubjectDN() {
        return subjectDN;
    }

    public String getIssuerDN() {
        return issuerDN;
    }

    public String getOperation() {
        return operation;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
