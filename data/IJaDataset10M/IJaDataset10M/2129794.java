package com.wigball.mp3libexporter;

import java.util.ArrayList;
import java.util.Locale;
import com.wigball.mp3libexporter.gui.ApplicationEntry;
import com.wigball.mp3libexporter.util.Configuration;

/**
 * main class to start
 * 
 * @author $Author: lazydays $
 * @version $Revision: 4 $
 * 
 * $Date: 2007-03-01 16:42:14 -0500 (Thu, 01 Mar 2007) $
 *
 */
public class StartMeUp {

    /**
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length > 0) {
            ArrayList errors = new ArrayList();
            boolean parsedOk = true;
            Locale language = Configuration.DEFAULT_LANGUAGE;
            boolean debug = Configuration.DEFAULT_DEBUG;
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--help")) {
                    parsedOk = false;
                    break;
                } else if (args[i].equals("--lang")) {
                    if (args.length == i + 1) {
                        parsedOk = false;
                        errors.add("no language given");
                    } else {
                        String givenLanguage = args[i + 1];
                        i++;
                        boolean foundLanguage = false;
                        for (int j = 0; j < Configuration.AVAILABLE_LANGUAGES.size(); j++) {
                            Locale lng = (Locale) Configuration.AVAILABLE_LANGUAGES.get(j);
                            if (givenLanguage.equals(lng.getISO3Language())) {
                                foundLanguage = true;
                                language = lng;
                                break;
                            }
                        }
                        if (!foundLanguage) {
                            parsedOk = false;
                            errors.add("The given language \"" + givenLanguage + "\" is not known!");
                        }
                    }
                } else if (args[i].equals("--debug")) {
                    debug = true;
                } else {
                    parsedOk = false;
                    errors.add("unknown parameter \"" + args[i] + "\"");
                }
            }
            if (!parsedOk) {
                System.out.println("This is MP3LibExporter. Legal parameters are:");
                System.out.println("\t--help (prints this page and quits)");
                StringBuffer lngParam = new StringBuffer();
                lngParam.append("\t--lang [");
                for (int i = 0; i < Configuration.AVAILABLE_LANGUAGES.size(); i++) {
                    if (i > 0) {
                        lngParam.append('|');
                    }
                    Locale l = (Locale) Configuration.AVAILABLE_LANGUAGES.get(i);
                    lngParam.append(l.getISO3Language());
                }
                lngParam.append("]");
                System.out.println(lngParam.toString());
                if (errors.size() > 0) {
                    System.out.println('\n');
                    System.out.println("Application could not be started due to some errors:");
                    for (int i = 0; i < errors.size(); i++) {
                        System.out.println("\t- " + errors.get(i));
                    }
                }
            } else {
                System.out.println("arguments parsed ok");
                new ApplicationEntry(language, debug);
            }
        } else {
            System.out.println("no arguments");
            new ApplicationEntry(Configuration.DEFAULT_LANGUAGE, Configuration.DEFAULT_DEBUG);
        }
    }
}
