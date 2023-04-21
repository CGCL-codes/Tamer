package com.csol.chem.util.reaction;

import com.csol.chem.core.*;
import com.csol.chem.util.structure.Assembler;
import com.csol.chem.util.structure.LinkSite;
import com.csol.globals.M;

/**
 *
 */
public class COHtoCOC implements MergeReaction {

    public static final double bondAngleCOC = M.RAD * 112;

    /**
     * Construct a COH to COC merge reaction object.
     */
    public COHtoCOC() {
        super();
    }

    /** (non-Javadoc)
     * Merge the two molecules by the COH - HOC => COC reaction, 
     * merging the graphs and storing in a new molecule.
     * The HO of the second molecule and H of the first molecule are removed
     * from the atom graph, then the two graphs are merged.
     * @see com.csol.chem.util.reaction.MergeReaction#merge(com.csol.chem.core.Molecule, com.csol.chem.util.structure.LinkSite, com.csol.chem.core.Molecule, com.csol.chem.util.structure.LinkSite, double, double)
     */
    public Molecule merge(Molecule m1, LinkSite s1, Molecule m2, LinkSite s2) {
        Molecule m = new Molecule();
        m.merge(m1);
        m.merge(m2);
        Assembler.merge(s1, s2);
        s1.lead.unbindAll();
        s2.lead.unbindAll();
        s2.head.unbindAll();
        m.delAtom(s1.lead);
        m.delAtom(s2.lead);
        m.delAtom(s2.head);
        for (Atom atom : s1.leavingAtoms) {
            atom.unbindAll();
            m.delAtom(atom);
        }
        for (Atom atom : s2.leavingAtoms) {
            atom.unbindAll();
            m.delAtom(atom);
        }
        return m;
    }
}
