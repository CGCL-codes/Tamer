package edu.upmc.opi.caBIG.caTIES.installer.pipes.ties.lucenefinder.ae.strategy;

import java.util.ArrayList;
import edu.upmc.opi.caBIG.caTIES.installer.pipes.ties.lucenefinder.ae.ODIE_IndexFinderAnnotation;

public interface ODIE_LuceneNerStrategyInterface {

    public void setSortedTokens(ArrayList<ODIE_IndexFinderAnnotation> sentenceTokensAnnots);

    public void execute();

    public void setMaxHits(int maxHits);

    public ArrayList<ODIE_IndexFinderAnnotation> getResultingConcepts();
}
