package org.tzi.use.gen.assl.statics;

import org.tzi.use.uml.mm.MAssociation;
import org.tzi.use.util.StringUtil;
import java.util.List;

/**
 * @see org.tzi.use.gen.assl.statics
 * @author  Joern Bohling
 */
public class GInstrTry_Assoc_LinkendSeqs extends GInstruction {

    private MAssociation fAssociation;

    private List fLinkendSequences;

    public GInstrTry_Assoc_LinkendSeqs(MAssociation assoc, List seqs) {
        fAssociation = assoc;
        fLinkendSequences = seqs;
    }

    public MAssociation association() {
        return fAssociation;
    }

    public List linkendSequences() {
        return fLinkendSequences;
    }

    public String toString() {
        return "Try(" + fAssociation + "," + StringUtil.fmtSeq(fLinkendSequences.iterator(), ",") + ")";
    }
}
