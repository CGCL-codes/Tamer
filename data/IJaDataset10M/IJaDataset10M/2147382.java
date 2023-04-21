package org.qedeq.kernel.se.dto.module;

import java.util.ArrayList;
import java.util.List;
import org.qedeq.base.utility.EqualsUtility;
import org.qedeq.kernel.se.base.module.Proof;
import org.qedeq.kernel.se.base.module.ProofList;

/**
 * List of proofs.
 *
 * @author  Michael Meyling
 */
public class ProofListVo implements ProofList {

    /** Contains all list elements. */
    private final List list;

    /**
     * Constructs an empty list of proofs.
     */
    public ProofListVo() {
        this.list = new ArrayList();
    }

    /**
     * Add proof to this list.
     *
     * @param   proof   Proof to add.
     */
    public final void add(final ProofVo proof) {
        list.add(proof);
    }

    public final int size() {
        return list.size();
    }

    public final Proof get(final int index) {
        return (Proof) list.get(index);
    }

    public boolean equals(final Object obj) {
        if (!(obj instanceof ProofListVo)) {
            return false;
        }
        final ProofListVo otherList = (ProofListVo) obj;
        if (size() != otherList.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (!EqualsUtility.equals(get(i), otherList.get(i))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < size(); i++) {
            hash = hash ^ (i + 1);
            if (get(i) != null) {
                hash = hash ^ get(i).hashCode();
            }
        }
        return hash;
    }

    public String toString() {
        final StringBuffer buffer = new StringBuffer("Proofs:\n");
        for (int i = 0; i < size(); i++) {
            if (i != 0) {
                buffer.append("\n");
            }
            buffer.append((i + 1) + ":\t");
            buffer.append(get(i) != null ? get(i).toString() : null);
        }
        return buffer.toString();
    }
}
