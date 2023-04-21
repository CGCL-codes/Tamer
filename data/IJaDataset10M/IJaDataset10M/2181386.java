package edu.washington.assist.annotation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class MainSoldierPanel extends JPanel implements ActionListener {

    private enum MotionState {

        VEHICLE, LINGERING, WALKING, RUNNING, UPSTAIRS, DOWNSTAIRS, UNK
    }

    ;

    private static final long serialVersionUID = 1L;

    private JButton indoor, outdoor, unknown, keyword;

    Map<String, JButton> buttons;

    private SoldierPanel soldierA;

    private SoldierPanel soldierB;

    Date date;

    public MainSoldierPanel(FileReader fileReader, SoldierPanel soldierA, SoldierPanel soldierB) {
        this.soldierA = soldierA;
        this.soldierB = soldierB;
        JPanel motionstate = new JPanel();
        JPanel indoorState = new JPanel();
        JPanel soldierPanel = new JPanel();
        JPanel keywordPanel = new JPanel();
        motionstate.setBackground(Color.WHITE);
        indoorState.setBackground(Color.WHITE);
        soldierPanel.setBackground(Color.WHITE);
        keywordPanel.setBackground(Color.WHITE);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titled = BorderFactory.createTitledBorder(loweredetched, "A & B");
        Border emptySpace = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border cmpd = BorderFactory.createCompoundBorder(emptySpace, titled);
        this.setBorder(cmpd);
        titled = BorderFactory.createTitledBorder(loweredetched, "MS");
        emptySpace = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        cmpd = BorderFactory.createCompoundBorder(emptySpace, titled);
        motionstate.setBorder(cmpd);
        titled = BorderFactory.createTitledBorder(loweredetched, "I/O");
        emptySpace = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        cmpd = BorderFactory.createCompoundBorder(emptySpace, titled);
        indoorState.setBorder(cmpd);
        indoor = new JButton("I");
        indoor.addActionListener(this);
        indoor.setActionCommand("INDOOR");
        indoor.setPreferredSize(new Dimension(50, 45));
        outdoor = new JButton("O");
        outdoor.addActionListener(this);
        outdoor.setActionCommand("OUTDOOR");
        outdoor.setPreferredSize(new Dimension(50, 45));
        unknown = new JButton("U");
        unknown.addActionListener(this);
        unknown.setActionCommand("UNKNOWN");
        unknown.setPreferredSize(new Dimension(50, 45));
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        indoorState.setLayout(gridbag);
        c.insets = new Insets(0, 5, 5, 0);
        c.gridx = 1;
        c.gridy = 1;
        indoorState.add(indoor, c);
        c.gridy = 2;
        indoorState.add(outdoor, c);
        c.gridy = 3;
        indoorState.add(unknown, c);
        buttons = new HashMap<String, JButton>();
        for (MotionState state : MotionState.values()) {
            JButton button = new JButton(state.toString().substring(0, 1));
            button.addActionListener(this);
            button.setActionCommand(state.toString());
            button.setPreferredSize(new Dimension(50, 45));
            buttons.put(state.toString(), button);
        }
        System.out.println(buttons.keySet());
        motionstate.setLayout(gridbag);
        c.gridy = 1;
        motionstate.add(buttons.get(MotionState.VEHICLE.toString()), c);
        c.gridy = 2;
        motionstate.add(buttons.get(MotionState.RUNNING.toString()), c);
        c.gridy = 3;
        motionstate.add(buttons.get(MotionState.WALKING.toString()), c);
        c.gridy = 4;
        motionstate.add(buttons.get(MotionState.LINGERING.toString()), c);
        c.gridy = 5;
        motionstate.add(buttons.get(MotionState.UPSTAIRS.toString()), c);
        c.gridy = 6;
        motionstate.add(buttons.get(MotionState.DOWNSTAIRS.toString()), c);
        c.gridy = 7;
        motionstate.add(buttons.get(MotionState.UNK.toString()), c);
        keyword = new JButton("K");
        keyword.addActionListener(this);
        keyword.setActionCommand("KEYWORD");
        keyword.setPreferredSize(new Dimension(50, 45));
        titled = BorderFactory.createTitledBorder(loweredetched, "K");
        emptySpace = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        cmpd = BorderFactory.createCompoundBorder(emptySpace, titled);
        keywordPanel.setBorder(cmpd);
        keywordPanel.add(keyword);
        this.add(soldierPanel);
        this.add(indoorState);
        this.add(motionstate);
        this.add(keywordPanel);
    }

    public void setState(String button) {
        soldierB.setState(button);
        soldierA.setState(button);
        if (button.equals("NONE")) {
            for (MotionState state : MotionState.values()) {
                buttons.get(state.toString()).setEnabled(false);
            }
            indoor.setEnabled(false);
            outdoor.setEnabled(false);
            keyword.setEnabled(false);
            unknown.setEnabled(false);
        } else if (button.equals("ALL")) {
            for (MotionState state : MotionState.values()) {
                buttons.get(state.toString()).setEnabled(true);
            }
            indoor.setEnabled(true);
            outdoor.setEnabled(true);
            keyword.setEnabled(true);
            unknown.setEnabled(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        setState(e.getActionCommand());
    }
}
