package org.chernovia.sims.wondrous;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.sound.midi.Instrument;
import org.chernovia.lib.misc.awt.LabeledScrollbar;
import org.chernovia.lib.music.midi.JMIDI;
import org.chernovia.lib.sims.ca.CA_Listener;

public class JWonderPanel extends Panel implements CA_Listener, AdjustmentListener, ActionListener, ItemListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Choice CB_INST;

    private LabeledScrollbar LSB_CHAN, LSB_VOL, LSB_SPD, LSB_MIN_PIT, LSB_MAX_PIT, LSB_START, LSB_END, LSB_LIM, LSB_MULT;

    private Button BUTT_STAR;

    private WonderBox WB_BOX;

    private JWondrousMachine JWM;

    public JWonderPanel(JWondrousMachine jwm) {
        setBackground(Color.BLUE);
        setForeground(Color.BLACK);
        JWM = jwm;
        JWM.addListener(this);
        LSB_CHAN = new LabeledScrollbar("C", Scrollbar.VERTICAL, JWM.getChannel(), 1, 0, JWondrousMachine.MAX_CHAN, 1);
        LSB_CHAN.getScrollbar().addAdjustmentListener(this);
        Instrument[] instruments = JMIDI.getInstruments();
        CB_INST = new Choice();
        CB_INST.setForeground(Color.BLACK);
        for (int i = 0; i < instruments.length; i++) {
            String N = i + ". " + instruments[i].getName();
            if (N.length() > 20) CB_INST.add(N.substring(0, 20)); else CB_INST.add(N);
        }
        CB_INST.select(JWM.getInstrument());
        CB_INST.addItemListener(this);
        add(CB_INST);
        LSB_VOL = new LabeledScrollbar("V", Scrollbar.VERTICAL, JWM.getVolume(), 1, 0, JWondrousMachine.MAX_VOL, 1);
        LSB_VOL.getScrollbar().addAdjustmentListener(this);
        LSB_VOL.addToContainer(this);
        LSB_SPD = new LabeledScrollbar("T", Scrollbar.VERTICAL, JWM.getSpeed(), 1, JWondrousMachine.MIN_SPD, JWondrousMachine.MAX_SPD, 25);
        LSB_SPD.getScrollbar().addAdjustmentListener(this);
        LSB_SPD.addToContainer(this);
        LSB_MIN_PIT = new LabeledScrollbar("mP", Scrollbar.VERTICAL, JWM.getMinPitch(), 1, JWondrousMachine.MIN_PITCH, JWondrousMachine.MAX_PITCH, 1);
        LSB_MIN_PIT.getScrollbar().addAdjustmentListener(this);
        LSB_MIN_PIT.addToContainer(this);
        LSB_MAX_PIT = new LabeledScrollbar("MP", Scrollbar.VERTICAL, JWM.getMaxPitch(), 1, JWondrousMachine.MIN_PITCH, JWondrousMachine.MAX_PITCH, 1);
        LSB_MAX_PIT.getScrollbar().addAdjustmentListener(this);
        LSB_MAX_PIT.addToContainer(this);
        LSB_START = new LabeledScrollbar("SV", Scrollbar.VERTICAL, JWM.getStartValue(), 1, JWondrousMachine.MIN_VAL, JWondrousMachine.MAX_VAL, 25);
        LSB_START.getScrollbar().addAdjustmentListener(this);
        LSB_START.addToContainer(this);
        LSB_END = new LabeledScrollbar("EV", Scrollbar.VERTICAL, JWM.getEndValue(), 1, JWondrousMachine.MIN_VAL, JWondrousMachine.MAX_VAL, 25);
        LSB_END.getScrollbar().addAdjustmentListener(this);
        LSB_END.addToContainer(this);
        LSB_LIM = new LabeledScrollbar("L", Scrollbar.VERTICAL, JWM.getLimit(), 1, JWondrousMachine.MIN_LIMIT, JWondrousMachine.MAX_LIMIT, 5);
        LSB_LIM.getScrollbar().addAdjustmentListener(this);
        LSB_LIM.addToContainer(this);
        LSB_MULT = new LabeledScrollbar("M", Scrollbar.VERTICAL, JWM.getMultiplier(), 1, JWondrousMachine.MIN_MULT, JWondrousMachine.MAX_MULT, 1);
        LSB_MULT.getScrollbar().addAdjustmentListener(this);
        LSB_MULT.addToContainer(this);
        BUTT_STAR = new Button("+");
        BUTT_STAR.addActionListener(this);
        add(BUTT_STAR);
        WB_BOX = new WonderBox(100, 100, JWM.getLimit());
        add(WB_BOX);
        setVisible(true);
    }

    public void startingRun() {
        for (int i = 0; i < JWondrousMachine.MACHINES.size(); i++) {
            ((Thread) (JWondrousMachine.MACHINES.elementAt(i))).interrupt();
        }
        JWondrousMachine.MACHINES.add(JWM);
        System.out.println(JWM);
    }

    public void finishedRun() {
        JMIDI.getChannel(JWM.getChannel()).allNotesOff();
        JWondrousMachine.MACHINES.remove(JWM);
        JWM = new JWondrousMachine(JWM);
        JWM.addListener(this);
    }

    public void nextTick(int tick) {
        WB_BOX.wonderize(JWM);
    }

    public void resize(int size) {
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (e.getSource() == LSB_CHAN.getScrollbar()) JWM.setChannel(LSB_CHAN.getValue()); else if (e.getSource() == LSB_VOL.getScrollbar()) JWM.setVolume(LSB_VOL.getValue()); else if (e.getSource() == LSB_SPD.getScrollbar()) JWM.setSpeed(LSB_SPD.getValue()); else if (e.getSource() == LSB_MIN_PIT.getScrollbar()) JWM.setMinPitch(LSB_MIN_PIT.getValue()); else if (e.getSource() == LSB_MAX_PIT.getScrollbar()) JWM.setMaxPitch(LSB_MAX_PIT.getValue()); else if (e.getSource() == LSB_MULT.getScrollbar()) JWM.setMultiplier(LSB_MULT.getValue()); else if (e.getSource() == LSB_LIM.getScrollbar()) JWM.setLimit(LSB_LIM.getValue()); else if (e.getSource() == LSB_END.getScrollbar()) {
            JWM.setEndValue(LSB_END.getValue());
        } else if (e.getSource() == LSB_START.getScrollbar()) {
            JWM.setStartValue(LSB_START.getValue());
            JWM.setCurrentValue(LSB_START.getValue());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == BUTT_STAR) {
            if (JWM.isRunning()) {
                JWM.stopRun();
                BUTT_STAR.setLabel("+");
            } else {
                JWM.start();
                BUTT_STAR.setLabel("-");
            }
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == CB_INST) JWM.setInstrument(CB_INST.getSelectedIndex());
    }
}
