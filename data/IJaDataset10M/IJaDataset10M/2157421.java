package com.icteam.fiji.model;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

public class CnttoEntBsns extends Auditable implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long CCnttoEntBsns;

    private TipCnlCntto CTipCnlCntto;

    private Long CEntBsns;

    private TipUtlCntto CTipUtlCntto;

    private SortedSet<ParmCntto> parametriContatto = new TreeSet<ParmCntto>();

    public CnttoEntBsns() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((CCnttoEntBsns == null) ? 0 : CCnttoEntBsns.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof CnttoEntBsns)) return false;
        CnttoEntBsns other = (CnttoEntBsns) obj;
        if (CCnttoEntBsns == null) {
            if (other.CCnttoEntBsns != null) return false;
        } else if (!CCnttoEntBsns.equals(other.CCnttoEntBsns)) return false;
        return true;
    }

    public Long getCCnttoEntBsns() {
        return CCnttoEntBsns;
    }

    public void setCCnttoEntBsns(Long cnttoEntBsns) {
        if (cnttoEntBsns != null && cnttoEntBsns <= 0) CCnttoEntBsns = null; else CCnttoEntBsns = cnttoEntBsns;
    }

    public TipCnlCntto getCTipCnlCntto() {
        return CTipCnlCntto;
    }

    public void setCTipCnlCntto(TipCnlCntto tipCnlCntto) {
        CTipCnlCntto = tipCnlCntto;
    }

    public Long getCEntBsns() {
        return CEntBsns;
    }

    public void setCEntBsns(Long entBsns) {
        if (entBsns != null && entBsns <= 0) CEntBsns = null; else CEntBsns = entBsns;
    }

    public TipUtlCntto getCTipUtlCntto() {
        return CTipUtlCntto;
    }

    public void setCTipUtlCntto(TipUtlCntto tipUtlCntto) {
        CTipUtlCntto = tipUtlCntto;
    }

    public SortedSet<ParmCntto> getParametriContatto() {
        return parametriContatto;
    }

    public void setParametriContatto(SortedSet<ParmCntto> parametriContatto) {
        this.parametriContatto = parametriContatto;
    }
}
