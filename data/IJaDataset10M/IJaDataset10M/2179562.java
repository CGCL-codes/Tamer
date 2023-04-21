package org.mitre.dm.qud.conditions;

import org.mitre.midiki.agent.*;
import org.mitre.midiki.state.*;
import org.mitre.midiki.logic.*;
import java.util.*;
import java.util.logging.*;

public class SpeakerIs extends Condition {

    private static Logger logger = Logger.getLogger("org.mitre.dm.qud.conditions.SpeakerIs");

    Object spkr;

    public SpeakerIs(Object o) {
        super();
        spkr = o;
    }

    public boolean test(ImmutableInfoState infoState, Bindings bindings) {
        Object speaker = infoState.cell("is").cell("shared").cell("lu").get("speaker");
        logger.logp(Level.FINER, "org.mitre.dm.qud.conditions.SpeakerIs", "test", "check is.shared.lu.speaker == " + spkr, speaker);
        return infoState.getUnifier().matchTerms(speaker, spkr, bindings);
    }
}
