package org.moltools.apps.probemaker.ext.tc;

import java.util.HashMap;
import java.util.Map;
import net.sf.apptools.plugin.PlugIn;
import org.moltools.apps.probemaker.design.TSSConstructor;
import org.moltools.apps.probemaker.seq.ProbeMakerSequenceFactory;
import org.moltools.apps.probemaker.seq.ProbeMakerTarget;
import org.moltools.design.data.PropertyAcceptorNucleotideSequence;
import org.moltools.design.utils.AbstractParameterHolder;
import org.moltools.lib.seq.NucleotideSequence;
import org.moltools.lib.seq.impl.SimpleNucleotideSequence;

public class NoTSSConstructor extends AbstractParameterHolder implements TSSConstructor, PlugIn {

    public static String getBriefDescription() {
        return "No target-specific sequences";
    }

    public static String getLongDescription() {
        return "This constructor generates empty target-specific sequences";
    }

    public PropertyAcceptorNucleotideSequence get3PrimeTSS(ProbeMakerTarget target, byte type) {
        NucleotideSequence seq = new SimpleNucleotideSequence("3'", "", type);
        return ProbeMakerSequenceFactory.createTSS(seq, target.getID());
    }

    public PropertyAcceptorNucleotideSequence get5PrimeTSS(ProbeMakerTarget target, byte type) {
        NucleotideSequence seq = new SimpleNucleotideSequence("5'", "", type);
        return ProbeMakerSequenceFactory.createTSS(seq, target.getID());
    }

    @Override
    public Map<String, String> getDefaultParameters() {
        return new HashMap<String, String>();
    }
}
