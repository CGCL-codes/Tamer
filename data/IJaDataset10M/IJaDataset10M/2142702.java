package edu.jas.gbufd;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import edu.jas.poly.ExpVector;
import edu.jas.poly.GenPolynomial;
import edu.jas.structure.RegularRingElem;

/**
 * Polynomial regular ring pseudo reduction sequential use algorithm. Implements
 * fraction free normalform algorithm.
 * @param <C> coefficient type
 * @author Heinz Kredel
 */
public class RPseudoReductionSeq<C extends RegularRingElem<C>> extends RReductionSeq<C> implements RPseudoReduction<C> {

    private static final Logger logger = Logger.getLogger(RPseudoReductionSeq.class);

    private final boolean debug = logger.isDebugEnabled();

    /**
     * Constructor.
     */
    public RPseudoReductionSeq() {
    }

    /**
     * Normalform using r-reduction.
     * @param Ap polynomial.
     * @param Pp polynomial list.
     * @return r-nf(Ap) with respect to Pp.
     */
    @Override
    @SuppressWarnings("unchecked")
    public GenPolynomial<C> normalform(List<GenPolynomial<C>> Pp, GenPolynomial<C> Ap) {
        if (Pp == null || Pp.isEmpty()) {
            return Ap;
        }
        if (Ap == null || Ap.isZERO()) {
            return Ap;
        }
        int l;
        GenPolynomial<C>[] P;
        synchronized (Pp) {
            l = Pp.size();
            P = (GenPolynomial<C>[]) new GenPolynomial[l];
            for (int i = 0; i < Pp.size(); i++) {
                P[i] = Pp.get(i);
            }
        }
        Map.Entry<ExpVector, C> m;
        ExpVector[] htl = new ExpVector[l];
        C[] lbc = (C[]) new RegularRingElem[l];
        GenPolynomial<C>[] p = (GenPolynomial<C>[]) new GenPolynomial[l];
        int i;
        int j = 0;
        for (i = 0; i < l; i++) {
            if (P[i] == null) {
                continue;
            }
            p[i] = P[i].abs();
            m = p[i].leadingMonomial();
            if (m != null) {
                p[j] = p[i];
                htl[j] = m.getKey();
                lbc[j] = m.getValue();
                j++;
            }
        }
        l = j;
        ExpVector e, f;
        C a, b;
        C r = null;
        boolean mt = false;
        GenPolynomial<C> R = Ap.ring.getZERO();
        GenPolynomial<C> Q = null;
        GenPolynomial<C> S = Ap;
        GenPolynomial<C> Rp, Sp;
        while (S.length() > 0) {
            m = S.leadingMonomial();
            e = m.getKey();
            a = m.getValue();
            for (i = 0; i < l; i++) {
                mt = e.multipleOf(htl[i]);
                if (mt) {
                    C c = lbc[i];
                    r = a.idempotentAnd(c);
                    mt = !r.isZERO();
                    if (mt) {
                        f = e.subtract(htl[i]);
                        if (a.remainder(c).isZERO()) {
                            a = a.divide(c);
                            if (a.isZERO()) {
                                throw new ArithmeticException("a.isZERO()");
                            }
                        } else {
                            c = c.fillOne();
                            S = S.multiply(c);
                            R = R.multiply(c);
                        }
                        Q = p[i].multiply(a, f);
                        S = S.subtract(Q);
                        f = S.leadingExpVector();
                        if (!e.equals(f)) {
                            a = Ap.ring.coFac.getZERO();
                            break;
                        }
                        a = S.leadingBaseCoefficient();
                    }
                }
            }
            if (!a.isZERO()) {
                R = R.sum(a, e);
                S = S.reductum();
            }
        }
        return R.abs();
    }

    /**
     * Normalform using r-reduction.
     * @param Pp polynomial list.
     * @param Ap polynomial.
     * @return ( nf(Ap), mf ) with respect to Pp and mf as multiplication factor
     *         for Ap.
     */
    @SuppressWarnings("unchecked")
    public PseudoReductionEntry<C> normalformFactor(List<GenPolynomial<C>> Pp, GenPolynomial<C> Ap) {
        if (Ap == null) {
            return null;
        }
        C mfac = Ap.ring.getONECoefficient();
        PseudoReductionEntry<C> pf = new PseudoReductionEntry<C>(Ap, mfac);
        if (Pp == null || Pp.isEmpty()) {
            return pf;
        }
        if (Ap.isZERO()) {
            return pf;
        }
        int l;
        GenPolynomial<C>[] P;
        synchronized (Pp) {
            l = Pp.size();
            P = (GenPolynomial<C>[]) new GenPolynomial[l];
            for (int i = 0; i < Pp.size(); i++) {
                P[i] = Pp.get(i);
            }
        }
        Map.Entry<ExpVector, C> m;
        ExpVector[] htl = new ExpVector[l];
        C[] lbc = (C[]) new RegularRingElem[l];
        GenPolynomial<C>[] p = (GenPolynomial<C>[]) new GenPolynomial[l];
        int i;
        int j = 0;
        for (i = 0; i < l; i++) {
            if (P[i] == null) {
                continue;
            }
            p[i] = P[i].abs();
            m = p[i].leadingMonomial();
            if (m != null) {
                p[j] = p[i];
                htl[j] = m.getKey();
                lbc[j] = m.getValue();
                j++;
            }
        }
        l = j;
        ExpVector e, f;
        C a, b;
        C r = null;
        boolean mt = false;
        GenPolynomial<C> R = Ap.ring.getZERO();
        GenPolynomial<C> Q = null;
        GenPolynomial<C> S = Ap;
        GenPolynomial<C> Rp, Sp;
        while (S.length() > 0) {
            m = S.leadingMonomial();
            e = m.getKey();
            a = m.getValue();
            for (i = 0; i < l; i++) {
                mt = e.multipleOf(htl[i]);
                if (mt) {
                    C c = lbc[i];
                    r = a.idempotentAnd(c);
                    mt = !r.isZERO();
                    if (mt) {
                        f = e.subtract(htl[i]);
                        if (a.remainder(c).isZERO()) {
                            a = a.divide(c);
                            if (a.isZERO()) {
                                throw new ArithmeticException("a.isZERO()");
                            }
                        } else {
                            c = c.fillOne();
                            S = S.multiply(c);
                            R = R.multiply(c);
                            mfac = mfac.multiply(c);
                        }
                        Q = p[i].multiply(a, f);
                        S = S.subtract(Q);
                        f = S.leadingExpVector();
                        if (!e.equals(f)) {
                            a = Ap.ring.coFac.getZERO();
                            break;
                        }
                        a = S.leadingBaseCoefficient();
                    }
                }
            }
            if (!a.isZERO()) {
                R = R.sum(a, e);
                S = S.reductum();
            }
        }
        pf = new PseudoReductionEntry<C>(R, mfac);
        return pf;
    }

    /**
     * Normalform with recording. <b>Note:</b> Only meaningfull if all
     * divisions are exact. Compute first the multiplication factor
     * <code>m</code> with <code>normalform(Pp,Ap,m)</code>, then call this
     * method with <code>normalform(row,Pp,m*Ap)</code>.
     * @param row recording matrix, is modified.
     * @param Pp a polynomial list for reduction.
     * @param Ap a polynomial.
     * @return nf(Pp,Ap), the normal form of Ap wrt. Pp.
     */
    @Override
    @SuppressWarnings("unchecked")
    public GenPolynomial<C> normalform(List<GenPolynomial<C>> row, List<GenPolynomial<C>> Pp, GenPolynomial<C> Ap) {
        if (Pp == null || Pp.isEmpty()) {
            return Ap;
        }
        if (Ap == null || Ap.isZERO()) {
            return Ap;
        }
        int l;
        GenPolynomial<C>[] P;
        synchronized (Pp) {
            l = Pp.size();
            P = (GenPolynomial<C>[]) new GenPolynomial[l];
            for (int i = 0; i < Pp.size(); i++) {
                P[i] = Pp.get(i);
            }
        }
        Map.Entry<ExpVector, C> m;
        ExpVector[] htl = new ExpVector[l];
        C[] lbc = (C[]) new RegularRingElem[l];
        GenPolynomial<C>[] p = (GenPolynomial<C>[]) new GenPolynomial[l];
        int i;
        int j = 0;
        for (i = 0; i < l; i++) {
            p[i] = P[i];
            m = p[i].leadingMonomial();
            if (m != null) {
                p[j] = p[i];
                htl[j] = m.getKey();
                lbc[j] = m.getValue();
                j++;
            }
        }
        l = j;
        ExpVector e, f;
        C a, b;
        C r = null;
        boolean mt = false;
        GenPolynomial<C> fac = null;
        GenPolynomial<C> zero = Ap.ring.getZERO();
        GenPolynomial<C> R = Ap.ring.getZERO();
        GenPolynomial<C> Q = null;
        GenPolynomial<C> S = Ap;
        while (S.length() > 0) {
            m = S.leadingMonomial();
            e = m.getKey();
            a = m.getValue();
            for (i = 0; i < l; i++) {
                mt = e.multipleOf(htl[i]);
                if (mt) {
                    C c = lbc[i];
                    r = a.idempotentAnd(c);
                    mt = !r.isZERO();
                    if (mt) {
                        b = a.remainder(c);
                        if (b.isZERO()) {
                            a = a.divide(c);
                            if (a.isZERO()) {
                                throw new ArithmeticException("a.isZERO()");
                            }
                        } else {
                            c = c.fillOne();
                            S = S.multiply(c);
                            R = R.multiply(c);
                        }
                        f = e.subtract(htl[i]);
                        Q = p[i].multiply(a, f);
                        S = S.subtract(Q);
                        fac = row.get(i);
                        if (fac == null) {
                            fac = zero.sum(a, f);
                        } else {
                            fac = fac.sum(a, f);
                        }
                        row.set(i, fac);
                        f = S.leadingExpVector();
                        if (!e.equals(f)) {
                            a = Ap.ring.coFac.getZERO();
                            break;
                        }
                        a = S.leadingBaseCoefficient();
                    }
                }
            }
            if (!a.isZERO()) {
                R = R.sum(a, e);
                S = S.reductum();
            }
        }
        return R;
    }
}
