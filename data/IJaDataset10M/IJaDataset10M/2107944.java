package yajdr.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import yajdr.core.DieRollerMain;
import yajdr.dice.DiceRollInformation;
import yajdr.interfaces.ThreadProgress;
import yajdr.interfaces.ThreadProgressListener;
import yajdr.threads.RollDice;
import yajdr.util.StringUtil;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Andrew Thorburn
 * @version 6.0
 */
public class WorldOfDarknessGui extends JPanel implements ActionListener, ThreadProgressListener<WorldOfDarknessThreadInformation> {

    private static final long serialVersionUID = 6746653445152761677L;

    private static final Log log = LogFactory.getLog(WorldOfDarknessGui.class);

    private SpinnerNumberModel difficultyModel = new SpinnerNumberModel(6, 2, 10, 1);

    private JSpinner difficulty = new JSpinner(difficultyModel);

    private SpinnerNumberModel numAgainModel = new SpinnerNumberModel(10, 8, 10, 1);

    private JSpinner numAgainSpinner = new JSpinner(numAgainModel);

    private SpinnerNumberModel numDiceModel = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);

    private JSpinner numDice = new JSpinner(numDiceModel);

    private JTextField init = new JTextField("0", 2);

    private JTextArea output = new JTextArea(6, 45);

    private JTextArea successOutput = new JTextArea(1, 45);

    private JLabel numDiceLab = new JLabel("Number of Dice to roll: ");

    private JLabel diffLab = new JLabel("Difficulty: ");

    private JLabel initLab = new JLabel("Initiative Mod: ");

    private JLabel initOutput = new JLabel("Initiative: ");

    private JButton bells = new JButton("Bells");

    private JButton whistle = new JButton("Whistles");

    private JButton god = new JButton("... like God");

    private JButton rollDice = new JButton("Roll Dice");

    private JButton reset = new JButton("Set to Exalted");

    private JButton resetWOD = new JButton("Set to WoD");

    private JButton setNWOD = new JButton("Set to NWOD");

    private JButton rollInit = new JButton("Roll Initiative");

    private JButton viewHist = new JButton("View Roll History");

    private int presetdice = 10;

    private JCheckBox doubleTens = new JCheckBox("Tens count as two successes", true);

    private JCheckBox onesCancel = new JCheckBox("Ones cancel successes", false);

    private JCheckBox tensReRoll = new JCheckBox(" Again", false);

    private JCheckBox botchable = new JCheckBox("Botchable", true);

    private String[] failMessages = { "I hope that didn't matter...", "Yep, that's a fail alright.", "Only cocksmokers fail like you!", "Way to fail, faily Mcfails-a-lot, why don't you go fail some more?" };

    private JScrollPane scrollPane = new JScrollPane(output);

    private boolean whistleB = false, bellsB = false;

    private boolean threadsAreRunning = false;

    private ThreadProgress parent;

    private RollHistory rollh = new RollHistory();

    public WorldOfDarknessGui() {
        log.trace("Constructing Wod GUI...");
        bells.addActionListener(this);
        whistle.addActionListener(this);
        god.addActionListener(this);
        rollDice.addActionListener(this);
        reset.addActionListener(this);
        rollInit.addActionListener(this);
        resetWOD.addActionListener(this);
        setNWOD.addActionListener(this);
        viewHist.addActionListener(this);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
        successOutput.setBorder(BorderFactory.createLineBorder(Color.black));
        init.setBorder(BorderFactory.createLineBorder(Color.black));
        numDice.setBorder(BorderFactory.createLineBorder(Color.black));
        difficulty.setBorder(BorderFactory.createLineBorder(Color.black));
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        numDice.requestFocus();
        buildGridBagLayout();
        resetWoD();
    }

    private void buildGridBagLayout() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagLayout gbl2;
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagConstraints panelConst = new GridBagConstraints();
        setLayout(gbl);
        gbc.anchor = GridBagConstraints.NORTH;
        panelConst.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelConst.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbl2 = new GridBagLayout();
        JPanel checkBoxPanel = new JPanel(gbl2);
        panelConst.weightx = 0;
        gbl2.setConstraints(numAgainSpinner, panelConst);
        checkBoxPanel.add(numAgainSpinner);
        panelConst.weightx = 1;
        gbl2.setConstraints(tensReRoll, panelConst);
        checkBoxPanel.add(tensReRoll);
        gbl2.setConstraints(onesCancel, panelConst);
        checkBoxPanel.add(onesCancel);
        gbl2.setConstraints(doubleTens, panelConst);
        checkBoxPanel.add(doubleTens);
        gbl2.setConstraints(botchable, panelConst);
        checkBoxPanel.add(botchable);
        gbl.setConstraints(checkBoxPanel, gbc);
        add(checkBoxPanel);
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(numDiceLab);
        inputPanel.add(numDice);
        inputPanel.add(diffLab);
        inputPanel.add(difficulty);
        gbl.setConstraints(inputPanel, gbc);
        add(inputPanel);
        JPanel initPanel = new JPanel(new FlowLayout());
        initPanel.add(initLab);
        initPanel.add(init);
        initPanel.add(initOutput);
        gbl.setConstraints(initPanel, gbc);
        add(initPanel);
        gbc.fill = GridBagConstraints.BOTH;
        JPanel buttonPanel = new JPanel(new GridLayout());
        buttonPanel.add(rollDice);
        buttonPanel.add(reset);
        buttonPanel.add(rollInit);
        buttonPanel.add(resetWOD);
        buttonPanel.add(setNWOD);
        gbl.setConstraints(buttonPanel, gbc);
        add(buttonPanel);
        gbl2 = new GridBagLayout();
        JPanel outputPanel = new JPanel(gbl2);
        gbl2.setConstraints(scrollPane, gbc);
        outputPanel.add(scrollPane);
        gbl.setConstraints(outputPanel, gbc);
        add(outputPanel);
        gbl2 = new GridBagLayout();
        JPanel sucOutputPanel = new JPanel(gbl2);
        gbl2.setConstraints(successOutput, gbc);
        sucOutputPanel.add(successOutput);
        gbl.setConstraints(sucOutputPanel, gbc);
        add(sucOutputPanel);
        JPanel presetDicePanel = new JPanel(new GridLayout(2, presetdice / 2));
        for (int i = 1; i <= presetdice; i++) {
            JButton presetDice = new JButton("Roll " + i + " dice");
            presetDice.setActionCommand(String.valueOf(i));
            presetDice.addActionListener(this);
            presetDicePanel.add(presetDice);
        }
        gbl.setConstraints(presetDicePanel, gbc);
        add(presetDicePanel);
        JPanel bellsAndWhistles = new JPanel(new GridLayout());
        bellsAndWhistles.add(bells);
        bellsAndWhistles.add(whistle);
        bellsAndWhistles.add(god);
        gbl.setConstraints(bellsAndWhistles, gbc);
        add(bellsAndWhistles);
        JPanel results = new JPanel(new GridLayout());
        results.add(viewHist);
        gbl.setConstraints(results, gbc);
        add(results);
    }

    public void setParent(ThreadProgress tp) {
        parent = tp;
    }

    public void actionPerformed(ActionEvent ae) {
        Object buttonPressed = ae.getSource();
        String actionCommand = ae.getActionCommand();
        if (!(buttonPressed instanceof JButton)) return;
        if (buttonPressed == rollDice || StringUtil.isInteger(actionCommand) || buttonPressed == god) {
            if (threadsAreRunning) return;
            threadsAreRunning = true;
            DiceRollInformation info = new DiceRollInformation();
            info.setDifficulty(difficultyModel.getNumber().intValue());
            info.setRollAgainValue((Integer) this.numAgainSpinner.getValue());
            info.setTensCountTwice(doubleTens.isSelected());
            info.setRerollTens(tensReRoll.isSelected());
            if (buttonPressed == rollDice) {
                info.setDiceToRoll((Integer) numDice.getValue());
            } else if (buttonPressed == god) {
                info.setDiceToRoll(10000);
                info.setDifficulty(2);
            } else {
                info.setDiceToRoll(Integer.parseInt(actionCommand));
                numDice.setValue(info.getDiceToRoll());
            }
            info.setNewThreadSize(Math.max(100, info.getDiceToRoll() / 100));
            new RollDice(this, info).start();
        } else if (buttonPressed == reset) {
            Reset();
            numDice.requestFocus();
        } else if (buttonPressed == resetWOD) {
            resetWoD();
            numDice.requestFocus();
        } else if (buttonPressed == rollInit) {
            String initStr = init.getText();
            if (IsInteger(initStr)) {
                RollInit(initStr);
            }
        } else if (buttonPressed == bells) {
            if (!bellsB) {
                JOptionPane.showMessageDialog(null, "I'm sorry, this function is currently out of order. Try again after using the whistles");
                bellsB = true;
            } else if (bellsB && whistleB) {
                JOptionPane.showMessageDialog(null, "We are sorry. This is still not working. The computer is trying hard to fix this problem.");
                bellsB = false;
                whistleB = false;
            }
        } else if (buttonPressed == whistle) {
            if (!whistleB) {
                JOptionPane.showMessageDialog(null, "I'm sorry, this function is currently out of order. Try again after pressing bells");
                whistleB = true;
            } else if (bellsB && whistleB) {
                JOptionPane.showMessageDialog(null, "We are sorry. This is still not working. The computer is trying hard to fix this problem.");
                whistleB = false;
                bellsB = false;
            }
        } else if (buttonPressed == setNWOD) {
            setNWOD();
        } else if (buttonPressed == viewHist) {
            rollh.setVisible(true);
        }
    }

    private void showResult(int numSuc, int numBotch, String dieResult) {
        output.setText("You Rolled: \n" + dieResult + "\nAt difficulty " + difficultyModel.getNumber().intValue());
        if (onesCancel.isSelected() && botchable.isSelected()) {
            if (numSuc == 0 && numBotch >= 1) {
                successOutput.setText("You botched, rolling " + numBotch + " ones");
            } else if (numSuc - numBotch >= 1) {
                successOutput.setText("You got " + (numSuc - numBotch) + " success" + (numSuc > 1 ? "es" : ""));
            } else {
                failMessage();
                successOutput.setText("Failure. Sux: " + numSuc + " Ones: " + numBotch);
            }
        } else if (!botchable.isSelected() && onesCancel.isSelected()) {
            if (numSuc - numBotch >= 1) {
                successOutput.setText("You got " + (numSuc - numBotch) + " successes");
            } else {
                failMessage();
            }
        } else if (!botchable.isSelected() && !onesCancel.isSelected()) {
            if (numSuc >= 1) {
                successOutput.setText("You got " + numSuc + " successes");
            } else {
                failMessage();
            }
        } else {
            if (numSuc == 0 && numBotch >= 1) {
                successOutput.setText("You botched, rolling " + numBotch + " ones");
            } else if (numSuc >= 1) {
                successOutput.setText("You got " + numSuc + " successes");
            } else {
                failMessage();
            }
        }
    }

    private boolean IsInteger(String inputTest) {
        if (StringUtil.isInteger(inputTest)) return true;
        JOptionPane.showMessageDialog(this, "You didn't enter an integer. Please try again.");
        return false;
    }

    private void Reset() {
        output.setText("");
        successOutput.setText("");
        numDice.setValue(4);
        difficultyModel.setValue(7);
        difficultyModel.setMaximum(10);
        difficultyModel.setMinimum(2);
        doubleTens.setSelected(true);
        tensReRoll.setSelected(false);
        onesCancel.setSelected(false);
        botchable.setSelected(true);
        initOutput.setText("Initiative: ");
        init.setText("0");
    }

    private void RollInit(String initStr) {
        double dieRoll = Math.floor(((Math.random()) * 10) + 1);
        initOutput.setText("Initiative: " + (int) (dieRoll + Integer.parseInt(initStr)));
    }

    private void resetWoD() {
        output.setText("");
        successOutput.setText("");
        numDice.setValue(4);
        difficultyModel.setValue(6);
        difficultyModel.setMaximum(10);
        difficultyModel.setMinimum(2);
        difficultyModel.setStepSize(1);
        doubleTens.setSelected(false);
        tensReRoll.setSelected(false);
        onesCancel.setSelected(true);
        botchable.setSelected(true);
        initOutput.setText("Initiative: ");
        init.setText("0");
    }

    private void setNWOD() {
        output.setText("");
        successOutput.setText("");
        numDice.setValue(4);
        difficultyModel.setValue(8);
        difficultyModel.setMaximum(8);
        difficultyModel.setMinimum(8);
        doubleTens.setSelected(false);
        onesCancel.setSelected(false);
        tensReRoll.setSelected(true);
        botchable.setSelected(false);
        initOutput.setText("Initiative: ");
        init.setText("0");
    }

    private void failMessage() {
        successOutput.setText(failMessages[(int) (Math.floor(Math.random() * failMessages.length))]);
    }

    private class RollHistory extends JFrame {

        private static final long serialVersionUID = 8017900674245960916L;

        private JPanel contentPanel;

        private GridBagLayout frameLayout;

        private GridBagConstraints frameConstraints;

        public RollHistory() {
            frameLayout = new GridBagLayout();
            contentPanel = new JPanel(frameLayout);
            frameConstraints = new GridBagConstraints();
            frameConstraints.anchor = GridBagConstraints.NORTH;
            frameConstraints.fill = GridBagConstraints.HORIZONTAL;
            frameConstraints.gridwidth = GridBagConstraints.REMAINDER;
            frameConstraints.weightx = 1.0;
            frameConstraints.weighty = 1.0;
            JScrollPane jsp = new JScrollPane(contentPanel);
            getContentPane().add(jsp);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("Roll History");
        }

        public void addRoll(JTextArea output, JTextArea result) {
            GridBagLayout gbl = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            JPanel panel = new JPanel(gbl);
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            JTextArea o = new JTextArea(output.getRows(), output.getColumns());
            o.setText(output.getText());
            o.setEditable(false);
            o.setLineWrap(true);
            o.setWrapStyleWord(true);
            JScrollPane jspa = new JScrollPane(o);
            JTextArea r = new JTextArea(result.getRows(), result.getColumns());
            r.setText(result.getText());
            r.setEditable(false);
            gbl.setConstraints(jspa, gbc);
            panel.add(jspa);
            gbc.weighty = 0.0;
            gbl.setConstraints(r, gbc);
            panel.add(r);
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            frameLayout.setConstraints(panel, frameConstraints);
            contentPanel.add(panel, 0);
        }

        public void setVisible(boolean b) {
            if (b) {
                pack();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                if (getSize().height > d.height) {
                    setSize(getSize().width, d.height);
                }
                setLocation(DieRollerMain.centreOnScreen(getSize()));
            }
            super.setVisible(b);
        }
    }

    public boolean isThreadsRunning() {
        return threadsAreRunning;
    }

    public void setThreadsRunning(boolean b) {
        threadsAreRunning = b;
    }

    public void threadFinished(WorldOfDarknessThreadInformation threadInfo) {
        showResult(threadInfo.getSuccessCount(), threadInfo.getBotchCount(), threadInfo.getResult());
        rollh.addRoll(output, successOutput);
        parent.resetProgressBar();
        parent.update("Done");
        threadsAreRunning = false;
    }

    public void threadProgress(WorldOfDarknessThreadInformation threadInfo) {
        parent.update(threadInfo.getCompletedThreadCount(), threadInfo.getTotalThreadCount());
        int maximumRolled = (threadInfo.getCompletedThreadCount()) * threadInfo.getDiceRollInformation().getNewThreadSize();
        int totalDice = threadInfo.getDiceRollInformation().getDiceToRoll();
        parent.update("Rolling dice... " + (totalDice - maximumRolled) + " dice left to go");
    }

    public void threadStarted(WorldOfDarknessThreadInformation threadInfo) {
        parent.update("Started Rolling...");
    }
}
