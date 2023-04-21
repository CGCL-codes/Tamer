package edu.collablab.brenthecht.wikapidia.sr;

import edu.collablab.brenthecht.wikapidia.ExplainedValue;
import edu.collablab.brenthecht.wikapidia.LanguagedString;
import edu.collablab.brenthecht.wikapidia.WikapidiaDatabase;
import edu.collablab.brenthecht.wikapidia.WikapidiaException;
import edu.collablab.brenthecht.wikapidia.worldknowledge.WorldKnowledge;

public class SRMeasureGM extends SRMeasure {

    private GESAFunctionGM gesaFunction;

    public SRMeasureGM(WorldKnowledge wk, boolean useExplanations) throws WikapidiaException {
        super(wk.getLanguageSet().getDefaultLanguageCode(), wk.getDatabase(), useExplanations);
        gesaFunction = new GESAFunctionGM(wk);
    }

    @Override
    public Double getDefaultMeasure(String term1, String term2, String langCode) throws WikapidiaException {
        return this.getMonoSR(term1, term2);
    }

    @Override
    public Double getHyperSD(String term1, String term2, String langCode) throws WikapidiaException {
        return null;
    }

    @Override
    public Double getHyperSR(String term1, String term2, String langCode) throws WikapidiaException {
        return null;
    }

    @Override
    public Double getMonoSDImpl(String term1, String term2, String langCode) throws WikapidiaException {
        return 1 - getMonoSRImpl(term1, term2, langCode);
    }

    @Override
    public Double getMonoSRImpl(String term1, String term2, String langCode) throws WikapidiaException {
        if (!langCode.equals(this.ls.getDefaultLanguageCode())) {
            return null;
        }
        GESAVector t1 = gesaFunction.getGESAVector(new LanguagedString(term1, langCode));
        GESAVector t2 = gesaFunction.getGESAVector(new LanguagedString(term2, langCode));
        try {
            ExplainedValue rVal = t1.getCosineSimilarity(t2, 0);
            return rVal.value;
        } catch (GESAException e) {
            return 0.0;
        }
    }

    @Override
    public String getName() {
        return "GM";
    }
}
