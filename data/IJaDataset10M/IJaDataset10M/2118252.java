package demo;

import com.dreamfabric.DKnob;
import com.frinika.softsynth.string.InstrumentGuiIF;
import com.frinika.softsynth.string.Valueizer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.sound.midi.Instrument;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author pjl
 */
public class InstrumentPanel extends JPanel {

    InstrumentGuiIF instr;

    GridBagConstraints c;

    int row;

    InstrumentGuiIF inst;

    public InstrumentPanel() {
        setPreferredSize(new Dimension(200, 300));
        setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
    }

    void setInstrument(Instrument inst) {
        row = 0;
        removeAll();
        if (inst == null) {
            return;
        }
        c.gridy = row;
        c.gridx = 1;
        c.ipadx = 6;
        c.ipady = 6;
        add(new JLabel("MIN"), c);
        c.gridx = 2;
        add(new JLabel("MAX"), c);
        row++;
        if (inst instanceof InstrumentGuiIF) {
            this.inst = (InstrumentGuiIF) inst;
            for (Valueizer v : this.inst.getValueizers()) {
                createPanel(v);
                row++;
            }
        }
        validate();
        repaint();
    }

    private void createPanel(final Valueizer v) {
        c.gridy = row;
        c.gridx = 0;
        add(new JLabel(v.effect), c);
        c.gridx++;
        final DKnob knobMin = new DKnob();
        knobMin.setValue(v.min);
        final DKnob knobMax = new DKnob();
        knobMax.setValue(v.max);
        add(knobMin, c);
        c.gridx++;
        add(knobMax, c);
        knobMin.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                v.setMin(Math.min(knobMin.getValue(), .99f));
            }
        });
        knobMax.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                v.setMax(Math.max(knobMax.getValue(), .01f));
            }
        });
    }
}
