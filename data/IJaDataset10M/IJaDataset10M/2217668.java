package edu.collablab.brenthecht.wikapidia.sr;

import java.util.HashMap;
import java.util.Vector;
import edu.collablab.brenthecht.wikapidia.ExplainedValue;
import edu.collablab.brenthecht.wikapidia.LanguagedString;
import edu.collablab.brenthecht.wikapidia.LinkCollection;
import edu.collablab.brenthecht.wikapidia.UniversalArticle;
import edu.collablab.brenthecht.wikapidia.UniversalConcept;
import edu.collablab.brenthecht.wikapidia.WikapidiaDatabase;
import edu.collablab.brenthecht.wikapidia.WikapidiaException;
import edu.collablab.brenthecht.wikapidia.dbentity.LocalConcept;
import edu.collablab.brenthecht.wikapidia.language.LanguageSet;
import edu.collablab.brenthecht.wikapidia.localconceptchooser.FastFirstLocalConceptChooser;
import edu.collablab.brenthecht.wikapidia.sis.SpatialFunction;
import edu.collablab.brenthecht.wikapidia.worldknowledge.WorldKnowledge;

public class SimplifiedMWFunction implements SpatialFunction {

    private LanguageSet ls;

    private int W;

    private double logW;

    private HashMap<Integer, LinkCollection> cache;

    public SimplifiedMWFunction(LanguageSet ls, WikapidiaDatabase db) throws WikapidiaException {
        this.ls = ls;
        try {
            this.W = db.getNumberOfLocalConcepts(ls, db.getArticleType());
        } catch (WikapidiaException e) {
            e.printStackTrace();
        }
        logW = Math.log(W);
        cache = new HashMap<Integer, LinkCollection>();
    }

    public String getExplanationName() {
        return "eModMW";
    }

    public String getName() {
        return "ModMW";
    }

    public ExplainedValue sampleAtLocation(LanguagedString s, UniversalConcept uc, WikapidiaDatabase db) throws WikapidiaException {
        LocalConcept tempLC = db.getLocalConcept(s.string, s.langCode, true, db.getArticleType());
        UniversalArticle queryArticle = new UniversalArticle(tempLC.getUnivID(), db);
        LinkCollection queryLC;
        if (cache.containsKey(queryArticle.getUnivID())) {
            queryLC = cache.get(queryArticle.getUnivID());
        } else {
            queryLC = queryArticle.getLinks(ls, false, true);
            cache.put(queryArticle.getUnivID(), queryLC);
        }
        LinkCollection locationLC = ((UniversalArticle) uc).getLinks(ls, false, true);
        int querySize = queryLC.getNumberOfLinkGroups();
        int locationSize = locationLC.getNumberOfLinkGroups();
        Vector<Integer> intersection = queryLC.getIntersection(locationLC);
        double numerator;
        if (intersection.size() > 0) {
            numerator = Math.log(Math.max(querySize, locationSize) - Math.log(intersection.size()));
        } else {
            numerator = 0;
        }
        double denominator = logW - Math.log(Math.min(querySize, locationSize));
        StringBuilder exp = new StringBuilder();
        UniversalArticle ua;
        for (Integer curID : intersection) {
            ua = new UniversalArticle(curID, db);
            if (ua.getLocalConcepts(s.langCode).size() > 0) {
                exp.append(ua.getLocalConcept(s.langCode, new FastFirstLocalConceptChooser()).getOriginalTitle());
            } else {
                exp.append(ua.getLocalConcepts().get(0).getOriginalTitle());
            }
            exp.append(";");
        }
        ExplainedValue rVal = new ExplainedValue(numerator / denominator, exp.toString());
        return rVal;
    }
}
