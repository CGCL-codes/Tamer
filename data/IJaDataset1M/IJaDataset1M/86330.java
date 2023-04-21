package org.phramer.v1.constraints.blockorder.hard.simple;

import java.util.*;
import org.phramer.v1.constraints.blockorder.core.*;
import org.phramer.v1.constraints.blockorder.core.inphrase.*;
import org.phramer.v1.decoder.PhraseTranslationVariant;
import org.phramer.v1.decoder.table.wordalignment.*;

public class ConstraintObject {

    public ConstraintObject(int n) {
        this.n = n;
        this.bc = null;
        this.oc = null;
        this.inPhraseAnalysisDescriptor = null;
    }

    public final int n;

    public BlockConstraint[] bc;

    public OrderConstraint[] oc;

    public InPhraseAnalysisDescriptor inPhraseAnalysisDescriptor;

    public void setConstraints(Constraint[] constraints, boolean inPhraseAnalysis) {
        HashSet<BlockConstraint> bcs = new HashSet<BlockConstraint>();
        HashSet<OrderConstraint> ocs = new HashSet<OrderConstraint>();
        for (Constraint c : constraints) {
            PrimitiveConstraint[] ps = c.getPrimitiveConstraint(n, inPhraseAnalysis);
            for (PrimitiveConstraint p : ps) if (!p.isTrivial(n, inPhraseAnalysis)) if (p instanceof BlockConstraint) bcs.add((BlockConstraint) p); else if (p instanceof OrderConstraint) ocs.add((OrderConstraint) p);
        }
        bc = bcs.toArray(new BlockConstraint[bcs.size()]);
        oc = ocs.toArray(new OrderConstraint[ocs.size()]);
    }

    public void setTranslationVariants(PhraseTranslationVariant[][][] phraseTableVariants, WordAlignmentBuilder wordAlignmentBuilder, boolean inPhraseAnalysis) {
        if (inPhraseAnalysis) inPhraseAnalysisDescriptor = InPhraseAnalysisTools.getDescriptor(phraseTableVariants, wordAlignmentBuilder, bc, oc);
    }
}
