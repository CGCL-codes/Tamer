package com.gusto.engine.colfil.formula.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.gusto.engine.colfil.Distance;
import com.gusto.engine.colfil.Evaluation;
import com.gusto.engine.colfil.formula.ItemCorrelation;
import com.gusto.engine.colfil.formula.UserCorrelation;

/**
 * <p>Implementation of the Pearson's correlation.<br/>
 * Properties:
 * <ul>
 * 	<li>The measure considers only the items in common.
 *  <li>The ratings are normalized by the mean rating.
 *  <li>1 : means a perfect match
 *  <li>-1 : means completely inverse ratings 
 * </ul>
 * <a href="http://en.wikipedia.org/wiki/Correlation">Wikipedia entry</a>.
 * </p>
 * 
 * @author amokrane.belloui@gmail.com
 * 
 */
public class PearsonCorrelation implements ItemCorrelation, UserCorrelation {

    private Logger log = Logger.getLogger(getClass());

    private double SOMETHING = 0.00000000000000001;

    private List<? extends Evaluation> createIfNull(List<? extends Evaluation> evals) {
        if (evals == null) {
            evals = new ArrayList<Evaluation>();
        }
        return evals;
    }

    private Double getMean(List<? extends Evaluation> evals) {
        Double mean = 0.0;
        for (Evaluation e : evals) {
            mean = mean + e.getValue();
        }
        mean = (mean / evals.size()) + SOMETHING;
        return mean;
    }

    public Distance userCorrelation(long user1, long user2, List<? extends Evaluation> evals1, List<? extends Evaluation> evals2) {
        log.debug("Calculating User correlation " + user1 + " " + user2);
        evals1 = createIfNull(evals1);
        evals2 = createIfNull(evals2);
        Double mean1 = getMean(evals1);
        Double mean2 = getMean(evals2);
        Double total = 0.0;
        Double totalsquareUser1 = 0.0;
        Double totalsquareUser2 = 0.0;
        Integer count = 0;
        for (Evaluation e1 : evals1) {
            for (Evaluation e2 : evals2) {
                if (e1.getItemId() == e2.getItemId()) {
                    Double val1 = e1.getValue() - mean1;
                    Double val2 = e2.getValue() - mean2;
                    total += (val1 * val2);
                    totalsquareUser1 += (val1 * val1);
                    totalsquareUser2 += (val2 * val2);
                    count++;
                    log.debug("Common : " + e1.getValue() + " " + e2.getValue() + " (" + val1 + " " + val2 + ")");
                }
            }
        }
        log.debug("Mean 1 " + mean1);
        log.debug("Mean 2 " + mean2);
        log.debug("Total " + total);
        log.debug("Total Square 1 " + totalsquareUser1);
        log.debug("Total Square 2 " + totalsquareUser2);
        Double corr = total / (Math.sqrt(totalsquareUser1) * Math.sqrt(totalsquareUser2));
        Distance dist = new Distance();
        dist.setId1(user1);
        dist.setId2(user2);
        dist.setCount(count);
        dist.setDistance(corr);
        log.info("Calculating User correlation " + user1 + " " + user2 + " => " + dist);
        return dist;
    }

    public Distance itemCorrelation(long item1, long item2, List<? extends Evaluation> evals1, List<? extends Evaluation> evals2) {
        log.debug("Calculating Item correlation " + item1 + " " + item2);
        evals1 = createIfNull(evals1);
        evals2 = createIfNull(evals2);
        Double mean1 = getMean(evals1);
        Double mean2 = getMean(evals2);
        Double total = 0.0;
        Double totalsquareItem1 = 0.0;
        Double totalsquareItem2 = 0.0;
        Integer count = 0;
        for (Evaluation e1 : evals1) {
            for (Evaluation e2 : evals2) {
                if (e1.getUserId() == e2.getUserId()) {
                    Double val1 = e1.getValue() - mean1;
                    Double val2 = e2.getValue() - mean2;
                    total += (val1 * val2);
                    totalsquareItem1 += (val1 * val1);
                    totalsquareItem2 += (val2 * val2);
                    count++;
                    log.debug("Common : " + e1.getValue() + " " + e2.getValue() + " (" + val1 + " " + val2 + ")");
                }
            }
        }
        log.debug("Mean 1 " + mean1);
        log.debug("Mean 2 " + mean2);
        log.debug("Total " + total);
        log.debug("Total Square 1 " + totalsquareItem1);
        log.debug("Total Square 2 " + totalsquareItem2);
        Double corr = total / (Math.sqrt(totalsquareItem1 * totalsquareItem2));
        Distance dist = new Distance();
        dist.setId1(item1);
        dist.setId2(item2);
        dist.setCount(count);
        dist.setDistance(corr);
        log.info("Calculating Item correlation " + item1 + " " + item2 + " => " + dist);
        return dist;
    }
}
