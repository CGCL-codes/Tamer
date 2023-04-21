package edu.collablab.brenthecht.wikapidia.sr;

import java.util.HashMap;
import java.util.Vector;
import edu.collablab.brenthecht.wikapidia.ExplainedValue;
import edu.collablab.brenthecht.wikapidia.LanguagedString;
import edu.collablab.brenthecht.wikapidia.UniversalConcept;
import edu.collablab.brenthecht.wikapidia.Wikapidia;
import edu.collablab.brenthecht.wikapidia.WikapidiaDatabase;
import edu.collablab.brenthecht.wikapidia.WikapidiaException;
import edu.collablab.brenthecht.wikapidia.dbentity.LocalArticle;
import edu.collablab.brenthecht.wikapidia.dbentity.LocalConcept;
import edu.collablab.brenthecht.wikapidia.language.Language;
import edu.collablab.brenthecht.wikapidia.text.Index;
import edu.collablab.brenthecht.wikapidia.worldknowledge.WorldKnowledge;

public class GESAFunctionGM extends GESAFunction {

    private HashMap<String, Index> indices;

    public GESAFunctionGM(WorldKnowledge wk) throws WikapidiaException {
        super(wk);
        indices = new HashMap<String, Index>();
        for (Language lang : wk.getLanguageSet().getLangs()) {
            indices.put(lang.getLanguageCode(), new Index(lang.getLanguageCode(), wk.getDatabase()));
        }
    }

    @Override
    public String getName() {
        return "GM";
    }

    public String getExplanationName() {
        return "eGM";
    }

    @Override
    protected GESAVector getGESAVector(LanguagedString s) throws WikapidiaException {
        Wikapidia.printSecondOrderMessage("Generating GESA Vector for " + s.string);
        GESAVector rVal = new GESAVector(this.getName(), wk.getName());
        double curVal;
        GESADimension d1, d2;
        LocalArticle curLA;
        for (int i = 0; i < wk.getUniversalArticles().size(); i++) {
            curLA = wk.getLocalArticle(i, s.langCode);
            curVal = indices.get(s.langCode).getCosineSimilarity(s.string, curLA.getLocalID(), s.langCode);
            d1 = new GESADimension(curLA.getLocalID(), curVal);
            rVal.add(d1);
        }
        return rVal;
    }
}
