package de.tudresden.inf.lat.jcel.core.completion.ext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI2Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-8 : <b>if</b> A &#8849; &exist; r<sub>2</sub><sup>-</sup> <i>.</i> B
 * &isin; <i>T</i> , <u>(r<sub>1</sub>, x, y) &isin; R</u>, (y, A) &isin; S,
 * r<sub>1</sub> &#8849;<sub><i>T</i></sub> s, r<sub>2</sub>
 * &#8849;<sub><i>T</i></sub> s, f(s<sup>-</sup>) <br />
 * <b>then</b> S := S &cup; {(x, B)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR8RExtRule implements RObserverRule {

    /**
	 * Constructs a new completion rule CR-8 (R).
	 */
    public CR8RExtRule() {
    }

    @Override
    public Collection<XEntry> apply(ClassifierStatus status, REntry entry) {
        if (status == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        if (entry == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        return Collections.unmodifiableCollection(applyRule(status, entry.getProperty(), entry.getLeftClass(), entry.getRightClass()));
    }

    private Collection<XEntry> applyRule(ClassifierStatus status, int r1, int x, int y) {
        List<XEntry> ret = new ArrayList<XEntry>();
        for (int s : status.getSuperObjectProperties(r1)) {
            int sMinus = status.getInverseObjectPropertyOf(s);
            if (status.getExtendedOntology().getFunctionalObjectProperties().contains(sMinus)) {
                for (int a : status.getSubsumers(y)) {
                    for (GCI2Axiom axiom : status.getExtendedOntology().getGCI2Axioms(a)) {
                        int r2Minus = axiom.getPropertyInSuperClass();
                        int r2 = status.getInverseObjectPropertyOf(r2Minus);
                        if (status.getSubObjectProperties(s).contains(r2)) {
                            int b = axiom.getClassInSuperClass();
                            ret.add(new SEntryImpl(x, b));
                        }
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        return (o != null) && getClass().equals(o.getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
