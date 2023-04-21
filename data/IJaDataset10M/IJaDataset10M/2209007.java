package org.safu.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.safu.bean.FraudResponse;
import org.safu.bean.Score;
import org.safu.bean.TransactionData;
import org.safu.tools.FraudScreener;

public class FraudProcessor {

    private List<FraudScreener> primaryScreeners;

    private List<FraudScreener> secondaryScreeners;

    private List<FraudResponse> primaryResponses = new ArrayList<FraudResponse>();

    private List<FraudResponse> secondaryResponses = new ArrayList<FraudResponse>();

    private TransactionData transactionData;

    private Score score = Score.ACCEPT;

    private static Logger log = Logger.getLogger(FraudProcessor.class);

    private ExecuteSeparateThread secondaryThread = null;

    public class ExecuteSeparateThread extends Thread implements Runnable {

        private TransactionData transactionData;

        private List<FraudScreener> fraudScreeners;

        private boolean done = false;

        public ExecuteSeparateThread(List<FraudScreener> fraudScreeners, TransactionData transactionData) {
            log.trace("ST -- Transaction Data: " + transactionData);
            log.trace("ST -- Fraud Screeners : " + fraudScreeners);
            this.transactionData = transactionData;
            this.fraudScreeners = fraudScreeners;
        }

        public void run() {
            try {
                if (fraudScreeners == null || fraudScreeners.size() == 0) {
                    log.warn("there is no scrrners defined for secondaries");
                } else {
                    Iterator<FraudScreener> screeners = fraudScreeners.iterator();
                    while (screeners.hasNext()) {
                        FraudScreener screener = screeners.next();
                        log.debug("Invoking " + screener.getConfiguration().getConfigName());
                        FraudResponse response = screener.screen(transactionData);
                        secondaryResponses.add(response);
                        score.increaseBy(response.getScore());
                    }
                }
            } catch (Exception e) {
                log.error("Unable t oexecute in separate theread:", e);
            }
            done = true;
        }

        public boolean isDone() {
            return done;
        }
    }

    public void addToPrimaryScreeners(FraudScreener screener) {
        if (primaryScreeners == null) primaryScreeners = new ArrayList<FraudScreener>();
        primaryScreeners.add(screener);
    }

    public void addToSecondaryScreeners(FraudScreener screener) {
        if (secondaryScreeners == null) secondaryScreeners = new ArrayList<FraudScreener>();
        secondaryScreeners.add(screener);
    }

    public List<FraudScreener> getPrimaryScreeners() {
        return primaryScreeners;
    }

    public void setPrimaryScreeners(List<FraudScreener> primaryScreeners) {
        this.primaryScreeners = primaryScreeners;
    }

    public List<FraudScreener> getSecondaryScreeners() {
        return secondaryScreeners;
    }

    public TransactionData getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(TransactionData transactionData) {
        this.transactionData = transactionData;
    }

    public void executePrimaries() {
        Iterator<FraudScreener> primaries = primaryScreeners.iterator();
        score = Score.ACCEPT;
        while (primaries.hasNext()) {
            FraudScreener screener = primaries.next();
            log.debug("Invoking " + screener.getConfiguration().getConfigName());
            FraudResponse response = screener.screen(getTransactionData());
            primaryResponses.add(response);
            score.increaseBy(response.getScore());
        }
    }

    public void executeSecondaries() {
        secondaryThread = new ExecuteSeparateThread(secondaryScreeners, transactionData);
        secondaryThread.start();
    }

    public Score getScore() {
        return score;
    }

    public boolean isSecondariesDone() {
        return secondaryThread.isDone();
    }

    public List<FraudResponse> getPrimaryResponses() {
        return primaryResponses;
    }

    public List<FraudResponse> getSecondaryResponses() {
        return secondaryResponses;
    }
}
