package com.mtp.pounder;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import com.mtp.pounder.controller.PounderController;

public final class CommandLineRecorder {

    private static String pndFileName;

    private static PounderModel model;

    private static void save() {
        PounderController pc = new PounderController(model);
        model.getPreferences().saveDataToSystem();
        try {
            final File file = new File(pndFileName);
            new PounderWriter().write(model, file);
            model.getFileModel().setFile(file);
        } catch (final IOException ex) {
            ex.printStackTrace();
        } catch (final TransformerException ex) {
            ex.printStackTrace();
        } catch (final ParserConfigurationException ex) {
            ex.printStackTrace();
        }
        pc.getStopAction().actionPerformed(null);
    }

    public static void main(final String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java com.mtp.pounder.CommandLineRecorder <class> <pnd>");
            System.out.println("       Where:");
            System.out.println("       <class> is the name of the component or conduit class");
            System.out.println("       <pnd> is the name of a resulting pounder script.");
            System.exit(1);
        }
        final String className = args[0];
        pndFileName = args[1];
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                save();
            }
        });
        final VerbatimRecordingOptions vro = new VerbatimRecordingOptions();
        vro.doKeyEvents = true;
        vro.doMouseDragEvents = true;
        vro.doMouseInputEvents = true;
        vro.doMouseMotionEvents = true;
        vro.doWindowEvents = true;
        final PounderPrefs prefs = new PounderPrefs();
        prefs.setVerbatimRecordingOptions(vro);
        prefs.saveDataToSystem();
        model = new PounderModel();
        model.setTestClass(className);
        model.setPreferences(prefs);
        model.beginVerbatimRecording();
    }
}
