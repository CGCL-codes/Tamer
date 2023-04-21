package org.adempierelbr.nfe.beans;

public class ICMSGrupoBean {

    private String orig;

    private String CST;

    private String modBC;

    private String pRedBC;

    private String vBC;

    private String pICMS;

    private String vICMS;

    private String modBCST;

    private String pMVAST;

    private String pRedBCST;

    private String vBCST;

    private String pICMSST;

    private String vICMSST;

    public String getOrig() {
        return orig;
    }

    public void setOrig(String orig) {
        if (orig != null) orig = orig.trim();
        this.orig = orig;
    }

    public String getCST() {
        return CST;
    }

    public void setCST(String cST) {
        if (cST != null) cST = cST.trim();
        CST = cST;
    }

    public String getModBC() {
        return modBC;
    }

    public void setModBC(String modBC) {
        if (modBC != null) modBC = modBC.trim();
        this.modBC = modBC;
    }

    public String getpRedBC() {
        return pRedBC;
    }

    public void setpRedBC(String pRedBC) {
        if (pRedBC != null) pRedBC = pRedBC.trim();
        this.pRedBC = pRedBC;
    }

    public String getpICMS() {
        return pICMS;
    }

    public String getvBC() {
        return vBC;
    }

    public void setvBC(String vBC) {
        if (vBC != null) vBC = vBC.trim();
        this.vBC = vBC;
    }

    public void setpICMS(String pICMS) {
        if (pICMS != null) pICMS = pICMS.trim();
        this.pICMS = pICMS;
    }

    public String getvICMS() {
        return vICMS;
    }

    public void setvICMS(String vICMS) {
        if (vICMS != null) vICMS = vICMS.trim();
        this.vICMS = vICMS;
    }

    public String getModBCST() {
        return modBCST;
    }

    public void setModBCST(String modBCST) {
        if (modBCST != null) modBCST = modBCST.trim();
        this.modBCST = modBCST;
    }

    public String getpMVAST() {
        return pMVAST;
    }

    public void setpMVAST(String pMVAST) {
        if (pMVAST != null) pMVAST = pMVAST.trim();
        this.pMVAST = pMVAST;
    }

    public String getpRedBCST() {
        return pRedBCST;
    }

    public void setpRedBCST(String pRedBCST) {
        if (pRedBCST != null) pRedBCST = pRedBCST.trim();
        this.pRedBCST = pRedBCST;
    }

    public String getvBCST() {
        return vBCST;
    }

    public void setvBCST(String vBCST) {
        if (vBCST != null) vBCST = vBCST.trim();
        this.vBCST = vBCST;
    }

    public String getpICMSST() {
        return pICMSST;
    }

    public void setpICMSST(String pICMSST) {
        if (pICMSST != null) pICMSST = pICMSST.trim();
        this.pICMSST = pICMSST;
    }

    public String getvICMSST() {
        return vICMSST;
    }

    public void setvICMSST(String vICMSST) {
        if (vICMSST != null) vICMSST = vICMSST.trim();
        this.vICMSST = vICMSST;
    }
}
