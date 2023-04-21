package it.unimib.disco.itis.polimar.discovery.nfpEvaluation;

import it.unimib.disco.itis.polimar.discovery.wsml.NfpCouple;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class DomCalculator implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Hashtable<String, Hashtable<String, AbstractEvaluation>> functionMap = null;

    private Hashtable<String, Hashtable<String, List<String>>> domainCaches = null;

    public double calculate(NfpCouple couple) {
        String rOp = couple.getrNfp().getOperator();
        String oOp = couple.getoNfp().getOperator();
        Set<String> rPar = couple.getrNfp().getParametersSet();
        Set<String> oPar = couple.getoNfp().getParametersSet();
        return functionMap.get(rOp).get(oOp).calculateDom(rPar, oPar);
    }

    public void generateMap(Hashtable<String, Hashtable<String, List<String>>> caches) {
        this.domainCaches = caches;
        if (functionMap == null) {
            functionMap = new Hashtable<String, Hashtable<String, AbstractEvaluation>>();
            Hashtable<String, AbstractEvaluation> functionSet = new Hashtable<String, AbstractEvaluation>();
            functionSet.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#equal\"", new EqualEqualEvaluation());
            functionSet.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#lessEqual\"", new EqualLessEqualEvaluation());
            functionMap.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#equal\"", functionSet);
            functionSet = new Hashtable<String, AbstractEvaluation>();
            functionSet.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#equal\"", new LessEqualEqualEvaluation());
            functionSet.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#lessEqual\"", new LessEqualLessEqualEvaluation());
            functionSet.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#interval\"", new LessEqualIntervalEvaluation());
            functionMap.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#lessEqual\"", functionSet);
            functionSet = new Hashtable<String, AbstractEvaluation>();
            functionSet.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#equal\"", new AtLeastEqualEvaluation());
            functionSet.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#lessEqual\"", new AtLeastLessEqualEvaluation());
            functionMap.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#atLeast\"", functionSet);
            functionSet = new Hashtable<String, AbstractEvaluation>();
            functionSet.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#all\"", new ExistAllEvaluation(domainCaches));
            functionMap.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#exist\"", functionSet);
            functionSet = new Hashtable<String, AbstractEvaluation>();
            functionSet.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#all\"", new AllAllEvaluation(domainCaches));
            functionMap.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#all\"", functionSet);
            functionSet = new Hashtable<String, AbstractEvaluation>();
            functionSet.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#all\"", new IncludeAllEvaluation(domainCaches));
            functionMap.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#include\"", functionSet);
            functionSet = new Hashtable<String, AbstractEvaluation>();
            functionSet.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#equal\"", new IntervalEqualEvaluation());
            functionSet.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#interval\"", new IntervalIntervalEvaluation());
            functionMap.put("_\"http://www.siti.disco.unimib.it/research/ontologies/pcm#interval\"", functionSet);
        }
    }

    public void setDomainCaches(Hashtable<String, Hashtable<String, List<String>>> domainCaches) {
        this.domainCaches = domainCaches;
    }
}
