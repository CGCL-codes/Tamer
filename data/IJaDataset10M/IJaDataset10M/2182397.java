package org.digitall.extras.ekit;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JFrame;

/** Ekit
  * App for editing and saving HTML in a Java text component
  *
  * @author Howard Kistler
  * @version 0.9h
  *
  * REQUIREMENTS
  * Java 2 (JDK 1.3 or 1.4)
  * Swing Library
  */
public class Ekit extends JFrame implements WindowListener {

    private EkitCore ekitCore;

    private File currentFile = (File) null;

    /** Master Constructor
	  * @param sDocument         [String]  A text or HTML document to load in the editor upon startup.
	  * @param sStyleSheet       [String]  A CSS stylesheet to load in the editor upon startup.
	  * @param sRawDocument      [String]  A document encoded as a String to load in the editor upon startup.
	  * @param urlStyleSheet     [URL]     A URL reference to the CSS style sheet.
	  * @param includeToolBar    [boolean] Specifies whether the app should include the toolbar.
	  * @param showViewSource    [boolean] Specifies whether or not to show the View Source window on startup.
	  * @param showMenuIcons     [boolean] Specifies whether or not to show icon pictures in menus.
	  * @param editModeExclusive [boolean] Specifies whether or not to use exclusive edit mode (recommended on).
	  * @param sLanguage         [String]  The language portion of the Internationalization Locale to run Ekit in.
	  * @param sCountry          [String]  The country portion of the Internationalization Locale to run Ekit in.
	  * @param base64            [boolean] Specifies whether the raw document is Base64 encoded or not.
	  * @param debugMode         [boolean] Specifies whether to show the Debug menu or not.
	  * @param useSpellChecker   [boolean] Specifies whether to include the spellchecker or not.
	  * @param multiBar          [boolean] Specifies whether to use multiple toolbars or one big toolbar.
	  */
    public Ekit(String sDocument, String sStyleSheet, String sRawDocument, URL urlStyleSheet, boolean includeToolBar, boolean showViewSource, boolean showMenuIcons, boolean editModeExclusive, String sLanguage, String sCountry, boolean base64, boolean debugMode, boolean useSpellChecker, boolean multiBar) {
        if (useSpellChecker) {
            ekitCore = new EkitCoreSpell(sDocument, sStyleSheet, sRawDocument, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, true, multiBar);
        } else {
            ekitCore = new EkitCore(sDocument, sStyleSheet, sRawDocument, urlStyleSheet, includeToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, debugMode, false, multiBar);
        }
        ekitCore.setFrame(this);
        if (includeToolBar) {
            if (multiBar) {
                this.getContentPane().setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.anchor = GridBagConstraints.NORTH;
                gbc.gridheight = 1;
                gbc.gridwidth = 1;
                gbc.weightx = 1.0;
                gbc.weighty = 0.0;
                gbc.gridx = 1;
                gbc.gridy = 1;
                this.getContentPane().add(ekitCore.getToolBarMain(includeToolBar), gbc);
                gbc.gridy = 2;
                this.getContentPane().add(ekitCore.getToolBarFormat(includeToolBar), gbc);
                gbc.gridy = 3;
                this.getContentPane().add(ekitCore.getToolBarStyles(includeToolBar), gbc);
                gbc.anchor = GridBagConstraints.SOUTH;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weighty = 1.0;
                gbc.gridy = 4;
                this.getContentPane().add(ekitCore, gbc);
            } else {
                this.getContentPane().setLayout(new BorderLayout());
                this.getContentPane().add(ekitCore, BorderLayout.CENTER);
                this.getContentPane().add(ekitCore.getToolBar(includeToolBar), BorderLayout.NORTH);
            }
        } else {
            this.getContentPane().setLayout(new BorderLayout());
            this.getContentPane().add(ekitCore, BorderLayout.CENTER);
        }
        this.setJMenuBar(ekitCore.getMenuBar());
        this.addWindowListener(this);
        this.updateTitle();
        this.pack();
        this.show();
    }

    public Ekit() {
        this(null, null, null, null, true, false, true, true, null, null, false, false, false, true);
    }

    public void windowClosing(WindowEvent we) {
        this.dispose();
        System.exit(0);
    }

    public void windowOpened(WindowEvent we) {
        ;
    }

    public void windowClosed(WindowEvent we) {
        ;
    }

    public void windowActivated(WindowEvent we) {
        ;
    }

    public void windowDeactivated(WindowEvent we) {
        ;
    }

    public void windowIconified(WindowEvent we) {
        ;
    }

    public void windowDeiconified(WindowEvent we) {
        ;
    }

    /** Convenience method for updating the application title bar
	  */
    private void updateTitle() {
        this.setTitle(ekitCore.getAppName() + (currentFile == null ? "" : " - " + currentFile.getName()));
    }

    /** Usage method
	  */
    public static void usage() {
        System.out.println("usage: com.hexidec.ekit.Ekit [-t|t+|T] [-s|S] [-m|M] [-x|X] [-b|B] [-v|V] [-fFILE] [-cCSS] [-rRAW] [-uURL] [-lLANG] [-d|D] [-h|H|?]");
        System.out.println("       Each option contained in [] brackets is optional,");
        System.out.println("       and can be one of the values separated be the | pipe.");
        System.out.println("       Each option must be proceeded by a - hyphen.");
        System.out.println("       The options are:");
        System.out.println("         t|t+|T : -t = single toolbar, -t+ = multiple toolbars, -T = no toolbar");
        System.out.println("         s|S    : -s = show source window on startup, -S hide source window");
        System.out.println("         m|M    : -m = show icons on menus, -M no menu icons");
        System.out.println("         x|X    : -x = exclusive document/source windows, -X use split window");
        System.out.println("         b|B    : -b = use Base64 document encoding, -B use regular encoding");
        System.out.println("         v|V    : -v = include spell checker, -V omit spell checker");
        System.out.println("         -fFILE : load HTML document on startup (replace FILE with file name)");
        System.out.println("         -cCSS  : load CSS stylesheet on startup (replace CSS with file name)");
        System.out.println("         -rRAW  : load raw document on startup (replace RAW with file name)");
        System.out.println("         -uURL  : load document at URL on startup (replace URL with file URL)");
        System.out.println("         -lLANG : specify the starting language (defaults to your locale)");
        System.out.println("                    replace LANG with xx_XX format (i.e., US English is en_US)");
        System.out.println("         -d|D   : -d = DEBUG mode on, -D DEBUG mode off (developers only)");
        System.out.println("         -h|H|? : print out this help information");
        System.out.println("         ");
        System.out.println("The defaults settings are equivalent to: -t+ -S -m -x -B -V -D");
        System.out.println("         ");
        System.out.println("For further information, read the README file.");
    }

    /** Main method
	  */
    public static void main(String[] args) {
        String sDocument = null;
        String sStyleSheet = null;
        String sRawDocument = null;
        URL urlStyleSheet = null;
        boolean includeToolBar = true;
        boolean multibar = true;
        boolean includeViewSource = false;
        boolean includeMenuIcons = true;
        boolean modeExclusive = true;
        String sLang = null;
        String sCtry = null;
        boolean base64 = false;
        boolean debugOn = false;
        boolean spellCheck = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h") || args[i].equals("-H") || args[i].equals("-?")) {
                usage();
            } else if (args[i].equals("-t")) {
                includeToolBar = true;
                multibar = false;
            } else if (args[i].equals("-t+")) {
                includeToolBar = true;
                multibar = true;
            } else if (args[i].equals("-T")) {
                includeToolBar = false;
                multibar = false;
            } else if (args[i].equals("-s")) {
                includeViewSource = true;
            } else if (args[i].equals("-S")) {
                includeViewSource = false;
            } else if (args[i].equals("-m")) {
                includeMenuIcons = true;
            } else if (args[i].equals("-M")) {
                includeMenuIcons = false;
            } else if (args[i].equals("-x")) {
                modeExclusive = true;
            } else if (args[i].equals("-X")) {
                modeExclusive = false;
            } else if (args[i].equals("-b")) {
                base64 = true;
            } else if (args[i].equals("-B")) {
                base64 = false;
            } else if (args[i].startsWith("-f")) {
                sDocument = args[i].substring(2, args[i].length());
            } else if (args[i].startsWith("-c")) {
                sStyleSheet = args[i].substring(2, args[i].length());
            } else if (args[i].startsWith("-r")) {
                sRawDocument = args[i].substring(2, args[i].length());
            } else if (args[i].equals("-v")) {
                spellCheck = true;
            } else if (args[i].equals("-V")) {
                spellCheck = false;
            } else if (args[i].startsWith("-u")) {
                try {
                    urlStyleSheet = new URL(args[i].substring(2, args[i].length()));
                } catch (MalformedURLException murle) {
                    murle.printStackTrace(System.err);
                }
            } else if (args[i].startsWith("-l")) {
                if (args[i].indexOf('_') == 4 && args[i].length() >= 7) {
                    sLang = args[i].substring(2, args[i].indexOf('_'));
                    sCtry = args[i].substring(args[i].indexOf('_') + 1, args[i].length());
                }
            } else if (args[i].equals("-d")) {
                debugOn = true;
            } else if (args[i].equals("-D")) {
                debugOn = false;
            }
        }
        Ekit ekit = new Ekit(sDocument, sStyleSheet, sRawDocument, urlStyleSheet, includeToolBar, includeViewSource, includeMenuIcons, modeExclusive, sLang, sCtry, base64, debugOn, spellCheck, multibar);
    }
}
