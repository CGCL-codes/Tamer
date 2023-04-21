package de.tudresden.inf.lat.jcel.core.completion.alt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.core.graph.VNode;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;
import de.tudresden.inf.lat.jcel.coreontology.axiom.GCI3Axiom;

/**
 * <p>
 * <ul>
 * <li>CR-6 : <b>if</b> &exist; r<sup>-</sup> <i>.</i> A &#8849; B &isin;
 * <i>T</i> , <u>(r, x, y) &isin; R</u>, (x, A) &isin; S , (y, B) &notin; S, y =
 * (B', &psi;) <br />
 * <b>then</b> v := (B', &psi; &cup; {&exist; r <sup>-</sup> <i>.</i> A}) <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>if</b> v &notin; V <b>then</b> V := V &cup; {v} ,
 * S := S &cup; {(v, k) | (y, k) &isin; S} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; S := S &cup; {(v, B)} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r, x, v)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 */
public class CR6RAltRule implements RObserverRule {

    /**
	 * Constructs a new completion rule CR-6 (R).
	 */
    public CR6RAltRule() {
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
        for (int a : status.getSubsumers(x)) {
            for (GCI3Axiom axiom : status.getExtendedOntology().getGCI3rAAxioms(rMinus, a)) {
                int b = axiom.getSuperClass();
                if (!status.getSubsumers(y).contains(b)) {
                    VNode psiNode = status.getNode(y);
                    VNodeImpl newNode = new VNodeImpl(psiNode.getClassId());
                    newNode.addExistentialsOf(psiNode);
                    newNode.addExistential(rMinus, a);
                    boolean inV = status.contains(newNode);
                    int v = status.createOrGetNodeId(newNode);
                    if (!inV) {
                        for (int p : status.getSubsumers(y)) {
                            ret.add(new SEntryImpl(v, p));
                        }
                    }
                    ret.add(new SEntryImpl(v, b));
                    ret.add(new REntryImpl(r, x, v));
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
