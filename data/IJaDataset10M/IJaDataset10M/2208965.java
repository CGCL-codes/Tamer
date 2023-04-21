package gnu.classpath.examples.midi;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.sound.midi.*;

/**
 * An example how javax.sound.midi facilities work.
 */
public class Demo extends Frame implements ItemListener {

    Choice midiInChoice = new Choice();

    Choice midiOutChoice = new Choice();

    MidiDevice inDevice = null;

    MidiDevice outDevice = null;

    ArrayList inDevices = new ArrayList();

    ArrayList outDevices = new ArrayList();

    public Demo() throws Exception {
        MenuBar mb = new MenuBar();
        Menu menu = new Menu("File");
        MenuItem quit = new MenuItem("Quit", new MenuShortcut('Q'));
        quit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(quit);
        mb.add(menu);
        setTitle("synthcity: the GNU Classpath MIDI Demo");
        setLayout(new FlowLayout());
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
            if (device.getMaxReceivers() > 0) {
                midiOutChoice.addItem(infos[i].getDescription());
                outDevices.add(device);
            }
            if (device.getMaxTransmitters() > 0) {
                midiInChoice.addItem(infos[i].getDescription());
                inDevices.add(device);
            }
        }
        setMenuBar(mb);
        add(new Label("MIDI IN: "));
        add(midiInChoice);
        add(new Label("   MIDI OUT: "));
        add(midiOutChoice);
        midiInChoice.addItemListener(this);
        midiOutChoice.addItemListener(this);
        pack();
        show();
    }

    public void itemStateChanged(ItemEvent e) {
        try {
            if (e.getItemSelectable() == midiInChoice) {
                if (inDevice != null) inDevice.close();
                inDevice = (MidiDevice) inDevices.get(midiInChoice.getSelectedIndex());
            }
            if (e.getItemSelectable() == midiOutChoice) {
                if (outDevice != null) outDevice.close();
                outDevice = (MidiDevice) outDevices.get(midiOutChoice.getSelectedIndex());
            }
            if (inDevice != null && outDevice != null) {
                if (!inDevice.isOpen()) inDevice.open();
                if (!outDevice.isOpen()) outDevice.open();
                Transmitter t = inDevice.getTransmitter();
                if (t == null) System.err.println(inDevice + ".getTransmitter() == null");
                Receiver r = outDevice.getReceiver();
                if (r == null) System.err.println(outDevice + ".getReceiver() == null");
                if (t != null && r != null) t.setReceiver(r);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
        new Demo();
    }
}
