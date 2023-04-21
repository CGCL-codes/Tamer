package de.tudresden.inf.lat.jcel.core.completion.ext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import de.tudresden.inf.lat.jcel.core.completion.common.ClassifierStatus;
import de.tudresden.inf.lat.jcel.core.completion.common.REntry;
import de.tudresden.inf.lat.jcel.core.completion.common.REntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.RObserverRule;
import de.tudresden.inf.lat.jcel.core.completion.common.SEntryImpl;
import de.tudresden.inf.lat.jcel.core.completion.common.XEntry;
import de.tudresden.inf.lat.jcel.core.graph.VNodeImpl;

/**
 * <p>
 * <ul>
 * <li>CR-10 (optimized) : <b>if</b> <u>(r, x, y<sub>1</sub>) &isin; R</u>, (r,
 * x, y<sub>2</sub>) &isin; R, &hellip;, (r, x, y<sub>n</sub>) &isin; R,
 * y<sub>i</sub> = (&#8868; , &psi;<sub>i</sub>) for 1 &le; i &le; n,
 * y<sub>i</sub> &ne; y<sub>j</sub> for 1 &le; i < j &le; n, f(r) <br />
 * <b>then</b> v := (&#8868; , &psi;<sub>1</sub> &cup; &hellip; &cup
 * &psi;<sub>n</sub>) <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; <b>if</b> v &notin; V <b>then</b> V := V &cup; {v} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; S := S &cup; {(v, k) | (y<sub>1</sub>, k) &isin; S}
 * &cup; &hellip; &cup; {(v, k) | (y<sub>n</sub>, k) &isin; S} <br />
 * &nbsp;&nbsp;&nbsp;&nbsp; R := R &cup; {(r, x, v)}</li>
 * </ul>
 * </p>
 * 
 * @author Julian Mendez
 * 
 */
public class CR10OptRule implements RObserverRule {

    /**
	 * Constructs a new completion rule CR-10 optimized.
	 */
    public CR10OptRule() {
    }

    @Override
    public List<XEntry> apply(ClassifierStatus status, REntry entry) {
        if (status == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        if (entry == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        return Collections.unmodifiableList(applyRule(status, entry.getProperty(), entry.getLeftClass(), entry.getRightClass()));
    }

    private List<XEntry> applyRule(ClassifierStatus status, Integer r, Integer x, Integer y) {
        List<XEntry> ret = new ArrayList<XEntry>();
        if (status.getNode(y).getClassId().equals(status.getClassTopElement())) {
            if (status.getExtendedOntology().getFunctionalObjectProperties().contains(r)) {
                Set<Integer> valid = new HashSet<Integer>();
                valid.add(y);
                for (Integer yi : status.getSecondByFirst(r, x)) {
                    if (status.getNode(yi).getClassId().equals(status.getClassTopElement())) {
                        valid.add(yi);
                    }
                }
                if (valid.size() > 1) {
                    VNodeImpl newNode = new VNodeImpl(status.getClassTopElement());
                    for (Integer yi : valid) {
                        newNode.addExistentialsOf(status.getNode(yi));
                    }
                    Integer v = status.createOrGetNodeId(newNode);
                    for (Integer yi : valid) {
                        for (Integer p : status.getSubsumers(yi)) {
                            ret.add(new SEntryImpl(v, p));
                        }
                    }
                    ret.add(new REntryImpl(r, x, v));
                }
            }
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        return getClass().equals(o.getClass());
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
