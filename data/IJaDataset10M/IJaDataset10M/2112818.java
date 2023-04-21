package net.sourceforge.banu.heuristics.impl;

import net.sourceforge.banu.core.Bayes;
import net.sourceforge.banu.core.BayesProcessor;
import net.sourceforge.banu.core.impl.BayesProcessorImpl;
import net.sourceforge.banu.database.DatabaseConnection;

public class LocalSearchDoubles extends LocalSearchImpl {

    public Bayes leftNeighbour(Bayes bayes, DatabaseConnection dc) throws Exception {
        BayesProcessor bp = new BayesProcessorImpl(dc);
        bayes.setSplitValue((Double) bayes.getSplitValue() - bayes.getDelta());
        bayes = bp.confusionMatrix(bayes);
        return bayes;
    }

    public Bayes rightNeighbour(Bayes bayes, DatabaseConnection dc) throws Exception {
        BayesProcessor bp = new BayesProcessorImpl(dc);
        bayes.setSplitValue((Double) bayes.getSplitValue() + bayes.getDelta());
        bayes = bp.confusionMatrix(bayes);
        return bayes;
    }
}
