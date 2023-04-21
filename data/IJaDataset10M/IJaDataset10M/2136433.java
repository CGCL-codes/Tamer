package com.icteam.fiji.model;

import java.util.Date;

public class LocazLite implements java.io.Serializable {

    private Long CEntBsnsLocaz;

    private Long CEntBsnsInd;

    private Long CEntBsnsSoc;

    private Long CEntBsnsAreaGeogr;

    private TipLocaz tipLocaz;

    private String NLocaz;

    private String NDnmn;

    private String NInd1;

    private String NAreaGeogr;

    private String XTreePath;

    private String ZLat;

    private String ZLong;

    private Long CEntBsnsLocazMast;

    private String FInt;

    private TipEntBsns tipEntBsnsSoc;

    private Date DFin;

    public LocazLite() {
    }

    public LocazLite(Long p_CEntBsnsLocaz, Long p_CEntBsnsLocazMast, TipLocaz p_tipLocaz, String p_NLocaz, Long p_CEntBsnsInd, String p_NInd1, Long p_CEntBsnsSoc, String p_NDnmn, Long p_CEntBsnsAreaGeogr, String p_NAreaGeogr, String p_ZLat, String p_ZLong, String p_FInt, String p_XTreePath, TipEntBsns p_tipEntBsnsSoc, Date p_DFin, String p_ZLatFiji, String p_ZLongFiji) {
        CEntBsnsLocaz = p_CEntBsnsLocaz;
        CEntBsnsInd = p_CEntBsnsInd;
        CEntBsnsSoc = p_CEntBsnsSoc;
        CEntBsnsAreaGeogr = p_CEntBsnsAreaGeogr;
        tipLocaz = p_tipLocaz;
        NLocaz = p_NLocaz;
        NDnmn = p_NDnmn;
        NInd1 = p_NInd1;
        NAreaGeogr = p_NAreaGeogr;
        XTreePath = p_XTreePath;
        if (p_ZLatFiji != null && p_ZLongFiji != null) {
            ZLat = p_ZLatFiji;
            ZLong = p_ZLongFiji;
        } else {
            ZLat = p_ZLat;
            ZLong = p_ZLong;
        }
        CEntBsnsLocazMast = p_CEntBsnsLocazMast;
        FInt = p_FInt;
        tipEntBsnsSoc = p_tipEntBsnsSoc;
        DFin = p_DFin;
    }

    public Long getCEntBsnsLocaz() {
        return CEntBsnsLocaz;
    }

    public void setCEntBsnsLocaz(Long CEntBsnsLocaz) {
        if (CEntBsnsLocaz != null && CEntBsnsLocaz <= 0) this.CEntBsnsLocaz = null; else this.CEntBsnsLocaz = CEntBsnsLocaz;
    }

    public Long getCEntBsnsInd() {
        return CEntBsnsInd;
    }

    public void setCEntBsnsInd(Long CEntBsnsInd) {
        if (CEntBsnsInd != null && CEntBsnsInd <= 0) this.CEntBsnsInd = null; else this.CEntBsnsInd = CEntBsnsInd;
    }

    public Long getCEntBsnsSoc() {
        return CEntBsnsSoc;
    }

    public void setCEntBsnsSoc(Long CEntBsnsSoc) {
        if (CEntBsnsSoc != null && CEntBsnsSoc <= 0) this.CEntBsnsSoc = null; else this.CEntBsnsSoc = CEntBsnsSoc;
    }

    public Long getCEntBsnsAreaGeogr() {
        return CEntBsnsAreaGeogr;
    }

    public void setCEntBsnsAreaGeogr(Long CEntBsnsAreaGeogr) {
        if (CEntBsnsAreaGeogr != null && CEntBsnsAreaGeogr <= 0) this.CEntBsnsAreaGeogr = null; else this.CEntBsnsAreaGeogr = CEntBsnsAreaGeogr;
    }

    public TipLocaz getTipLocaz() {
        return tipLocaz;
    }

    public void setTipLocaz(TipLocaz tipLocaz) {
        this.tipLocaz = tipLocaz;
    }

    public String getNLocaz() {
        return NLocaz;
    }

    public void setNLocaz(String NLocaz) {
        this.NLocaz = NLocaz;
    }

    public String getNDnmn() {
        return NDnmn;
    }

    public void setNDnmn(String NDnmn) {
        this.NDnmn = NDnmn;
    }

    public String getNInd1() {
        return NInd1;
    }

    public void setNInd1(String NInd1) {
        this.NInd1 = NInd1;
    }

    public String getNAreaGeogr() {
        return NAreaGeogr;
    }

    public void setNAreaGeogr(String NAreaGeogr) {
        this.NAreaGeogr = NAreaGeogr;
    }

    public String getZLat() {
        return ZLat;
    }

    public void setZLat(String ZLat) {
        this.ZLat = ZLat;
    }

    public String getZLong() {
        return ZLong;
    }

    public void setZLong(String ZLong) {
        this.ZLong = ZLong;
    }

    public String getXTreePath() {
        return XTreePath;
    }

    public void setXTreePath(String XTreePath) {
        this.XTreePath = XTreePath;
    }

    public Long getCEntBsnsLocazMast() {
        return CEntBsnsLocazMast;
    }

    public void setCEntBsnsLocazMast(Long CEntBsnsLocazMast) {
        this.CEntBsnsLocazMast = CEntBsnsLocazMast;
    }

    public Date getDFin() {
        return DFin;
    }

    public void setDFin(Date DFin) {
        this.DFin = DFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocazLite)) return false;
        LocazLite locazLite = (LocazLite) o;
        Long cEntBsnsLocaz = locazLite.getCEntBsnsLocaz();
        if (CEntBsnsLocaz != null ? !CEntBsnsLocaz.equals(cEntBsnsLocaz) : cEntBsnsLocaz != null) return false;
        Long cEntBsnsInd = locazLite.getCEntBsnsInd();
        if (CEntBsnsInd != null ? !CEntBsnsInd.equals(cEntBsnsInd) : cEntBsnsInd != null) return false;
        Long cEntBsnsSoc = locazLite.getCEntBsnsSoc();
        if (CEntBsnsSoc != null ? !CEntBsnsSoc.equals(cEntBsnsSoc) : cEntBsnsSoc != null) return false;
        Long cEntBsnsAreaGeogr = locazLite.getCEntBsnsAreaGeogr();
        if (CEntBsnsAreaGeogr != null ? !CEntBsnsAreaGeogr.equals(cEntBsnsAreaGeogr) : cEntBsnsAreaGeogr != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (CEntBsnsLocaz != null ? CEntBsnsLocaz.hashCode() : 0);
        result = 31 * result + (CEntBsnsInd != null ? CEntBsnsInd.hashCode() : 0);
        result = 31 * result + (CEntBsnsSoc != null ? CEntBsnsSoc.hashCode() : 0);
        result = 31 * result + (CEntBsnsAreaGeogr != null ? CEntBsnsAreaGeogr.hashCode() : 0);
        return result;
    }

    public String getFInt() {
        return FInt;
    }

    public void setFInt(String FInt) {
        this.FInt = FInt;
    }

    public TipEntBsns getTipEntBsnsSoc() {
        return tipEntBsnsSoc;
    }

    public void setTipEntBsnsSoc(TipEntBsns tipEntBsnsSoc) {
        this.tipEntBsnsSoc = tipEntBsnsSoc;
    }
}
