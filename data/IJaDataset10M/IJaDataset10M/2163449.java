package de.hu_berlin.informatik.wbi.darq.cache;

import java.io.Serializable;
import com.hp.hpl.jena.graph.Triple;

public class CacheTripleVariables implements Serializable {

    Triple a, b;

    String ss = "-";

    String sp = "-";

    String so = "-";

    String ps = "-";

    String pp = "-";

    String po = "-";

    String os = "-";

    String op = "-";

    String oo = "-";

    public CacheTripleVariables(Triple a, Triple b, String ss, String sp, String so, String ps, String pp, String po, String os, String op, String oo) {
        this.a = a;
        this.b = b;
        this.ss = ss;
        this.sp = sp;
        this.so = so;
        this.ps = ps;
        this.pp = pp;
        this.po = po;
        this.os = os;
        this.op = op;
        this.oo = oo;
    }

    public CacheTripleVariables(Triple a, Triple b) {
        this.a = a;
        this.b = b;
    }

    public boolean equals(Object obj) {
        if (obj instanceof CacheTripleVariables) {
            CacheTripleVariables cTV = (CacheTripleVariables) obj;
            if ((cTV.a.equals(a) && cTV.b.equals(b)) || (cTV.a.equals(b) && cTV.b.equals(a))) {
                if (variablesEqual(cTV)) return true;
            }
        }
        return false;
    }

    private boolean variablesEqual(CacheTripleVariables cTV) {
        if (cTV.ss.equals(ss) && cTV.sp.equals(sp) && cTV.so.equals(so) && cTV.ps.equals(ps) && cTV.pp.equals(pp) && cTV.po.equals(po) && cTV.os.equals(os) && cTV.op.equals(op) && cTV.oo.equals(oo)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hc;
        hc = a.hashCode() ^ b.hashCode() ^ ss.hashCode() ^ sp.hashCode() ^ so.hashCode() ^ ps.hashCode() ^ pp.hashCode() ^ po.hashCode() ^ os.hashCode() ^ op.hashCode() ^ oo.hashCode();
        return hc;
    }

    public void output() {
        System.out.println("Triple A: " + a.toString());
        System.out.println("Triple B: " + b.toString());
        System.out.println("Equal Variables:");
        System.out.println(" A/B      Subject  Predicate  Object");
        System.out.println("Subject      " + ss + "         " + sp + "        " + so);
        System.out.println("Predicate    " + ps + "         " + pp + "        " + po);
        System.out.println("Object       " + os + "         " + op + "        " + oo);
    }

    public String getSs() {
        return ss;
    }

    public String getSp() {
        return sp;
    }

    public String getSo() {
        return so;
    }

    public String getPs() {
        return ps;
    }

    public String getPp() {
        return pp;
    }

    public String getPo() {
        return po;
    }

    public String getOs() {
        return os;
    }

    public String getOp() {
        return op;
    }

    public String getOo() {
        return oo;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setOo(String oo) {
        this.oo = oo;
    }
}
