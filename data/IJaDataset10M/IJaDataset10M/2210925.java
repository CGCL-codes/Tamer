package com.frinika.videosynth;

import com.frinika.FrinikaMain;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;

/**
 * Launch Frinika in separate thread
 * @author Peter Johan Salomonsen
 */
public class LaunchFrinikaFromFX {

    public static void launch() {
        new Thread() {

            @Override
            public void run() {
                try {
                    FrinikaMain.main(new String[] {});
                } catch (Exception ex) {
                    Logger.getLogger(LaunchFrinikaFromFX.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            System.out.println(info.getName());
        }
    }
}
