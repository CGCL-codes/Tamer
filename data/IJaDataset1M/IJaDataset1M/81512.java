package edu.ucla.stat.SOCR.experiments;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * A basic experiment that tosses n coins, generating a random sample from the
 * Bernoulli distribution
 */
public class CoinSampleExperiment extends Experiment {

    private int n = 10, coins = 100;

    private double p = 0.5;

    private JPanel toolbar = new JPanel();

    private CoinBox coinBox = new CoinBox(coins);

    private JLabel definitionLabel = new JLabel("Ij: Score of Coin j");

    /** Initialize the experiment: construct toolbar, add tables and graphs */
    public CoinSampleExperiment() {
        setName("Coin Sample Experiment");
        createValueSetter("n", Distribution.DISCRETE, 1, coins, n);
        createValueSetter("p", Distribution.CONTINUOUS, 0, 1);
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(definitionLabel);
        addToolbar(toolbar);
        addGraph(coinBox);
        reset();
    }

    /** Define the experiment */
    public void doExperiment() {
        super.doExperiment();
        coinBox.toss(n);
    }

    /** Reset the experiment */
    public void reset() {
        super.reset();
        String recordText = "";
        for (int i = 0; i < n; i++) recordText = recordText + "\tI" + (i + 1);
        coinBox.showCoins(0);
        getRecordTable().append(recordText);
    }

    /** Update the experiment */
    public void update() {
        super.update();
        coinBox.showCoins(n);
        String recordText = "";
        for (int i = 0; i < n; i++) recordText = recordText + "\t" + coinBox.getCoin(i).getValue();
        getRecordTable().append(recordText);
    }

    /** Scrollbar events: adjust parameters n and p */
    public void update(Observable o, Object arg) {
        n = getValueSetter(0).getValueAsInt();
        if (arg == getValueSetter(0)) {
            n = getValueSetter(0).getValueAsInt();
        } else {
            p = getValueSetter(1).getValue();
            coinBox.setProbability(p);
        }
        reset();
    }
}
