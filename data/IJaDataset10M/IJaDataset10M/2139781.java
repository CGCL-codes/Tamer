package uk.ac.ebi.intact.util.msd.model;

/**
 * Created by IntelliJ IDEA. User: krobbe Date: 23-Mar-2006 Time: 15:47:24 To change this template use File | Settings |
 * File Templates.
 */
public class PmidBean {

    private String pdbCode;

    private String pmid;

    private String ordinal;

    public String getPdbCode() {
        return pdbCode;
    }

    public void setPdbCode(String pdbCode) {
        this.pdbCode = pdbCode;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(String ordinal) {
        this.ordinal = ordinal;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PmidBean pmidBean = (PmidBean) o;
        if (ordinal != null ? !ordinal.equals(pmidBean.ordinal) : pmidBean.ordinal != null) {
            return false;
        }
        if (pdbCode != null ? !pdbCode.equals(pmidBean.pdbCode) : pmidBean.pdbCode != null) {
            return false;
        }
        if (pmid != null ? !pmid.equals(pmidBean.pmid) : pmidBean.pmid != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = (pdbCode != null ? pdbCode.hashCode() : 0);
        result = 29 * result + (pmid != null ? pmid.hashCode() : 0);
        result = 29 * result + (ordinal != null ? ordinal.hashCode() : 0);
        return result;
    }
}
