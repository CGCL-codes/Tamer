package edu.ucla.stat.SOCR.experiments;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.*;
import edu.ucla.stat.SOCR.util.*;

/**
 * The coin-die experiment consists of tossing a coin, and then rolling one of
 * two dice, depending on the value of the coin
 */
public class CoinDieExperiment extends Experiment {

    private double p = 0.5;

    private int coinScore, dieScore;

    private JPanel toolbar = new JPanel();

    private JPanel objectPanel = new JPanel();

    private Die die = new Die();

    private Coin coin = new Coin(p, 32);

    private JButton die0Button = new JButton("Green Die");

    private JButton die1Button = new JButton("Red Die");

    private DieDistribution die0Dist = new DieDistribution(DieDistribution.FAIR);

    private DieDistribution die1Dist = new DieDistribution(DieDistribution.FLAT16);

    private MixtureDistribution dieDist = new MixtureDistribution(die0Dist, die1Dist, p);

    private RandomVariable dieRV = new RandomVariable(dieDist, "X");

    private RandomVariableGraph dieRVGraph = new RandomVariableGraph(dieRV);

    private RandomVariableTable dieRVTable = new RandomVariableTable(dieRV);

    private DieProbabilityDialog dieProbabilityDialog;

    private Frame frame;

    public CoinDieExperiment() {
        setName("Coin Die Experiment");
        frame = GUIUtil.getTopestParent(getMainPanel());
        createValueSetter("p", Distribution.CONTINUOUS, 0, 1);
        die0Button.addActionListener(this);
        die1Button.addActionListener(this);
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(die0Button);
        toolbar.add(die1Button);
        addToolbar(toolbar);
        objectPanel.setLayout(new FlowLayout());
        objectPanel.add(coin);
        objectPanel.add(die);
        addGraph(objectPanel);
        addGraph(dieRVGraph);
        addTable(dieRVTable);
        reset();
    }

    public void doExperiment() {
        super.doExperiment();
        if (Math.random() < p) {
            coinScore = 1;
            dieRV.setValue(die1Dist.simulate());
        } else {
            coinScore = 0;
            dieRV.setValue(die0Dist.simulate());
        }
        dieScore = (int) dieRV.getValue();
    }

    /**
     * This method runs the the experiment one time, and add sounds depending on
     * the outcome of the experiment.
     */
    public void step() {
        doExperiment();
        update();
        String url = "sounds/" + dieScore + ".au";
        try {
            play(url);
        } catch (Exception e) {
            ;
        }
    }

    public void reset() {
        super.reset();
        coin.setVisible(false);
        die.setVisible(false);
        dieRV.reset();
        getRecordTable().append("\tX\tY");
        dieRVGraph.reset();
        dieRVTable.reset();
    }

    public void update() {
        super.update();
        coin.setValue(coinScore);
        if (coinScore == 1) die.setBackColor(Color.red); else die.setBackColor(Color.green);
        die.setValue(dieScore);
        coin.setVisible(true);
        coin.repaint();
        die.setVisible(true);
        die.repaint();
        getRecordTable().append("\t" + coinScore + "\t" + dieScore);
        dieRVGraph.repaint();
        dieRVTable.update();
    }

    /**
     * This method handles the button events associated with changes in the die
     * probabilities.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == die0Button) {
            Point fp = frame.getLocationOnScreen(), dp;
            Dimension fs = frame.getSize(), ds = dieProbabilityDialog.getSize();
            dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
            dieProbabilityDialog.setLocation(dp);
            dieProbabilityDialog.setProbabilities(die0Dist.getProbabilities());
            dieProbabilityDialog.setTitle("Green Die Probabilities");
            dieProbabilityDialog.setVisible(true);
            if (dieProbabilityDialog.isOK()) {
                die0Dist.setProbabilities(dieProbabilityDialog.getProbabilities());
                setDistribution();
            }
        } else if (e.getSource() == die1Button) {
            Point fp = frame.getLocationOnScreen(), dp;
            Dimension fs = frame.getSize(), ds = dieProbabilityDialog.getSize();
            dp = new Point(fp.x + fs.width / 2 - ds.width / 2, fp.y + fs.height / 2 - ds.height / 2);
            dieProbabilityDialog.setLocation(dp);
            dieProbabilityDialog.setProbabilities(die1Dist.getProbabilities());
            dieProbabilityDialog.setTitle("Green Die Probabilities");
            dieProbabilityDialog.setVisible(true);
            if (dieProbabilityDialog.isOK()) {
                die1Dist.setProbabilities(dieProbabilityDialog.getProbabilities());
                setDistribution();
            }
        } else super.actionPerformed(e);
    }

    public void update(Observable o, Object arg) {
        p = getValueSetter(0).getValue();
        setDistribution();
    }

    public void setDistribution() {
        dieDist.setParameters(die0Dist, die1Dist, p);
        reset();
    }
}
