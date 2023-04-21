package de.tudresden.inf.lat.jcel.core.completion.alt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI2Axiom;
import de.tudresden.inf.lat.jcel.coreontology.axiom.RI2Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-8 : <b>if</b> A &#8849; &exist; s<sup>-</sup> <i>.</i> B &isin;
 * <i>T</i> , <u>(r, x, y) &isin; R</u>, (y, A) &isin; S, s &#8849; r &isin;
 * <i>T</i> , f(r<sup>-</sup>) <br />
 * <b>then</b> S := S &cup; {(x, B)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR8RAltRule implements RObserverRule {

    /**
	 * Constructs a new completion rule CR-8 (R).
	 */
    public CR8RAltRule() {
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

    private Collection<XEntry> applyRule(ClassifierStatus status, int r, int x, int y) {
        List<XEntry> ret = new ArrayList<XEntry>();
        int rMinus = status.getInverseObjectPropertyOf(r);
        if (status.getExtendedOntology().getFunctionalObjectProperties().contains(rMinus)) {
            for (int a : status.getSubsumers(y)) {
                for (GCI2Axiom axiom : status.getExtendedOntology().getGCI2Axioms(a)) {
                    int sMinus = axiom.getPropertyInSuperClass();
                    int s = status.getInverseObjectPropertyOf(sMinus);
                    Set<RI2Axiom> axiomSet = new HashSet<RI2Axiom>();
                    axiomSet.addAll(status.getExtendedOntology().getRI2rAxioms(s));
                    axiomSet.retainAll(status.getExtendedOntology().getRI2sAxioms(r));
                    if (!axiomSet.isEmpty()) {
                        int b = axiom.getClassInSuperClass();
                        ret.add(new SEntryImpl(x, b));
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
