package net.sf.gateway.mef.databases.tables.vt;

import java.io.Serializable;
import java.math.BigInteger;

public class RsEfileFormsWork implements Serializable {

    static final long serialVersionUID = 1;

    private String primarySsn;

    public String getPrimarySsn() {
        return this.primarySsn;
    }

    public void setPrimarySsn(String primarySsn) {
        this.primarySsn = primarySsn;
    }

    private String rsn;

    public String getRsn() {
        return this.rsn;
    }

    public void setRsn(String rsn) {
        this.rsn = rsn;
    }

    private String vircsDocType;

    public String getVircsDocType() {
        return this.vircsDocType;
    }

    public void setVircsDocType(String vircsDocType) {
        this.vircsDocType = vircsDocType;
    }

    private BigInteger formSeqNr;

    public BigInteger getFormSeqNr() {
        return this.formSeqNr;
    }

    public void setFormSeqNr(BigInteger formSeqNr) {
        this.formSeqNr = formSeqNr;
    }

    private BigInteger labelCnt;

    public BigInteger getLabelCnt() {
        return this.labelCnt;
    }

    public void setLabelCnt(BigInteger labelCnt) {
        this.labelCnt = labelCnt;
    }
}
