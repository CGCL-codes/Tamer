package de.jlab.lab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import de.jlab.GlobalsLocator;
import de.jlab.communication.BoardCommunication;

public class CommandFileProcessor {

    static Logger stdlog = Logger.getLogger(CommandFileProcessor.class.getName());

    public static void processFile(File aFile, Lab theLab) {
        int lineCounter = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(aFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.length() < 3) continue;
                BoardCommunication comm = theLab.getAnyCommChannel();
                int adrPos = 0;
                if (line.contains(";")) {
                    comm = theLab.getCommChannelByName(line.substring(0, line.indexOf(';')));
                    adrPos = line.indexOf(';') + 1;
                }
                comm.sendCommand(line.substring(adrPos) + "\r\n");
                stdlog.fine("Sent " + line);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
                lineCounter++;
            }
            JOptionPane.showMessageDialog(GlobalsLocator.getMainFrame(), "Sent " + lineCounter + " Commands to c't Lab(s)");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(GlobalsLocator.getMainFrame(), "Line " + lineCounter + ": unable to open/process file: " + aFile.getAbsolutePath());
            stdlog.log(Level.SEVERE, "Unable to open/process file: " + aFile.getAbsolutePath(), e);
            return;
        }
    }
}
