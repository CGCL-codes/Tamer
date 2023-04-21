package com.rpitch.i18n;

import java.util.*;
import javax.swing.*;

/**
 * This is the base ResouceBundle for RPitch.
 * 
 * @author Dave Glasser
 */
public class Resources extends ResourceBundle {

    private static final String[][] mappings = { { "interval.shortname.0", "P1 (D2)" }, { "interval.shortname.1", "m2 (A1)" }, { "interval.shortname.2", "M2" }, { "interval.shortname.3", "m3 (A2)" }, { "interval.shortname.4", "M3 (D4)" }, { "interval.shortname.5", "P4 (A3)" }, { "interval.shortname.6", "A4 (D5)" }, { "interval.shortname.7", "P5" }, { "interval.shortname.8", "m6 (A5)" }, { "interval.shortname.9", "M6 (D7)" }, { "interval.shortname.10", "m7 (A6)" }, { "interval.shortname.11", "M7 (D8)" }, { "interval.shortname.12", "P8 (A7)" }, { "interval.longname.0", "Perfect First / Unison (Diminished Second)" }, { "interval.longname.1", "Minor Second (Augmented First)" }, { "interval.longname.2", "Major Second" }, { "interval.longname.3", "Minor Third (Augmented Second)" }, { "interval.longname.4", "Major Third (Diminished Fourth)" }, { "interval.longname.5", "Perfect Fourth (Augmented Third)" }, { "interval.longname.6", "Augmented Fourth (Diminished Fifth" }, { "interval.longname.7", "Perfect Fifth" }, { "interval.longname.8", "Minor Sixth (Augmented Fifth" }, { "interval.longname.9", "Major Sixth (Diminished Seventh)" }, { "interval.longname.10", "Minor Seventh (Augmented Sixth)" }, { "interval.longname.11", "Major Seventh (Diminished Eighth)" }, { "interval.longname.12", "Perfect Eighth / Octave (Augmented Seventh)" }, { "interval.checkbox.tooltip", "Include/exclude this interval from drills." }, { "interval.listenbutton.tooltip", "Play an example of this interval." }, { "button.label.PLAY_INTERVAL", "Play Interval" }, { "button.mnemonic.PLAY_INTERVAL", "P" }, { "button.tooltip.PLAY_INTERVAL", "Play a new interval" }, { "button.label.REPEAT_INTERVAL", "Repeat Interval" }, { "button.mnemonic.REPEAT_INTERVAL", "R" }, { "button.tooltip.REPEAT_INTERVAL", "Repeat the last played interval" }, { "button.label.NEW_DRILL", "New Drill" }, { "button.mnemonic.NEW_DRILL", "N" }, { "button.tooltip.NEW_DRILL", "Begin a new drill" }, { "button.label.SHOW_HELP", "Help" }, { "button.mnemonic.SHOW_HELP", "H" }, { "button.tooltip.SHOW_HELP", "Display the online help" }, { "button.label.EXIT_PROGRAM", "Exit" }, { "button.mnemonic.EXIT_PROGRAM", "X" }, { "button.tooltip.EXIT_PROGRAM", "Exit the program" }, { "boxtitle.SOUND_CONTROLS", "Sound Controls" }, { "boxtitle.INTERVALS", "Intervals" }, { "boxtitle.DRILL_SETTINGS", "Drill Settings" }, { "boxtitle.PROGRESS", "Progress" }, { "label.INSTRUMENTS", "Instruments" }, { "label.NOTE_DURATION", "Note Duration" }, { "label.INTERVAL_TIME_SPREAD", "Interval Time Spread" }, { "label.ROOT_OCTAVE", "Root Octave" }, { "label.ROOT_NOTE", "Root Note" }, { "label.ORDER", "Order" }, { "label.NUMBER_OF_INTERVALS", "Number of Intervals" }, { "label.SELECT_SCALE", "Select Scale" }, { "label.OTHER_SETTINGS", "Other Settings" }, { "octave.name.1", "First" }, { "octave.name.2", "Second" }, { "octave.name.3", "Third" }, { "octave.name.4", "Fourth (Middle C)" }, { "octave.name.5", "Fifth" }, { "octave.name.6", "Sixth" }, { "octave.name.7", "Seventh" }, { "list.item.RANDOM", "<Random>" }, { "list.item.CONTINUOUS", "<Continuous>" }, { "list.item.ASCENDING", "Ascending" }, { "list.item.DESCENDING", "Descending" }, { "drill.setting.ALLOW_REPEAT", "Allow repeat of interval" }, { "drill.setting.ALLOW_EXAMPLES", "Enable examples during drill" }, { "drill.setting.RANDOM_SEVENTH", "Use seventh octave as random" }, { "drill.setting.MULTIPLE_GUESSES", "Allow multiple guesses" }, { "drill.setting.ENABLE_SOUND_CONTROLS", "Enable sound controls during drill" }, { "note.name.C", "C" }, { "note.name.C#", "C Sharp" }, { "note.name.D", "D" }, { "note.name.D#", "D Sharp" }, { "note.name.E", "E" }, { "note.name.F", "F" }, { "note.name.F#", "F Sharp" }, { "note.name.G", "G" }, { "note.name.G#", "G Sharp" }, { "note.name.A", "A" }, { "note.name.A#", "A Sharp" }, { "note.name.B", "B" }, { "scale.name.CHROMATIC", "Chromatic" }, { "scale.name.MAJOR", "Major" }, { "scale.name.NATURAL_MINOR", "Natural Minor" }, { "scale.name.HARMONIC_MINOR", "Harmonic Minor" }, { "status.message.INITIAL", "After adjusting the controls, press the spacebar or click the \"Play Interval\" button to begin the next drill." }, { "status.message.TURN_COMPLETE", "Press the spacebar or click the \"Play Interval\" button to hear another interval." }, { "status.message.INITIAL_PLAY", "Click an Interval button to identify this interval." }, { "status.message.WRONG_GUESS", "Press the spacebar or click the \"Repeat Interval\" button to repeat the last interval." }, { "status.message.DRILL_COMPLETE", " " }, { "progress.guessresponse.correct.first", "Correct!" }, { "progress.guessresponse.correct.subsequent", "Correct! ({0,number,integer} guesses.)" }, { "progress.guessresponse.incorrect.allowmultiple.first", "Sorry, try again." }, { "progress.guessresponse.incorrect.allowmultiple.subsequent", "Sorry, try again. ({0,number,integer} guesses.)" }, { "progress.guessresponse.incorrect.nomultiple", "Sorry, that's incorrect." }, { "progress.current.score", "{0,number,integer}/{1,number,integer} ({2, number, percent})" }, { "drill.final.popup.title", "Drill Complete" }, { "drill.final.popup.message", "Intervals played: {0,number,integer}\nCorrectly identified: {1,number,integer}\nYour score: {2,number,percent}" }, { "helpdialog.title", "Online Help" }, { "mainwindow.title", "- Relative Pitch Ear Training Software" }, { "message.title.APPLICATION_ERROR", "Application Error" }, { "message.text.WRONG_JAVA_VERSION", "This program requires Java version 1.4 or later to run.\nYou are running version {0}." }, { "message.text.MIDI_UNAVAILABLE", "The MIDI sound device required by this program is missing or unavailable." }, { "message.text.UNEXPECTED_ERROR", "An unexpected application error has occurred:\n\n{0}\n\nThe program will now exit." }, { "message.title.INVALID_SELECTION", "Invalid Selection" }, { "message.text.ENABLED_INTERVAL_REQUIRED", "You must keep at least one interval enabled." } };

    private Hashtable map = new Hashtable();

    public Resources() {
        for (int j = 0; j < mappings.length; j++) {
            map.put(mappings[j][0], mappings[j][1]);
        }
    }

    protected Object handleGetObject(String key) {
        return map.get(key);
    }

    public Enumeration getKeys() {
        return map.keys();
    }

    private static ResourceBundle defaultBundle = new Resources();

    private static ResourceBundle getBundle() {
        ResourceBundle bundle = ResourceBundle.getBundle("com.rpitch.i18n.Resources");
        if (bundle == null) bundle = defaultBundle;
        return bundle;
    }

    public static void configureButton(JButton button, String command) {
        ResourceBundle bundle = getBundle();
        button.setActionCommand(command);
        String s = bundle.getString("button.label." + command);
        if (s != null && (s = s.trim()).length() > 0) {
            button.setText(s);
        } else {
            button.setText(null);
        }
        s = bundle.getString("button.mnemonic." + command);
        if (s != null && (s = s.trim()).length() > 0) {
            button.setMnemonic(s.charAt(0));
        } else {
            button.setMnemonic(0);
        }
        s = bundle.getString("button.tooltip." + command);
        if (s != null && (s = s.trim()).length() > 0) {
            button.setToolTipText(s);
        } else {
            button.setToolTipText(null);
        }
    }

    public static JLabel makeLabel(String key) {
        return new JLabel(getBundle().getString("label." + key));
    }

    /**
     * This will output all of the key-value mappings from
     * this ResourceBundle to System.out, in properties file format. This
     * is useful for starting a new bundle based on a properties file.
     */
    public static void main(String[] args) throws Exception {
        String ENDL = System.getProperty("line.separator");
        for (int j = 0; j < mappings.length; j++) {
            String val = mappings[j][1];
            if (val != null) {
                val = val.replaceAll("\n", "\\\\n");
            }
            if ("_COMMENT_".equals(mappings[j][0])) {
                System.out.println(ENDL + "# " + val);
            } else {
                System.out.println(mappings[j][0] + "=" + val);
            }
        }
    }
}
