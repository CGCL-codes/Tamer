package net.sf.opensoundboard;

import java.io.*;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import net.sf.opensoundboard.config.*;
import net.sf.opensoundboard.gui.SystrayIconManager;
import net.sf.opensoundboard.l10n.Text;
import org.apache.log4j.*;

/**
 * Main class (well, apparently...) - It extends the Application class from
 * JavaFX and is instantiated by JavaFX when the program is started.
 */
public final class Main extends Application {

    private final Logger log = Logger.getRootLogger();

    private final Configuration config = Configuration.getInstance();

    /**
	 * This class only defines a callback method
	 */
    private interface ContentWriter {

        /**
		 * Callback method. When the file is written, it depends on the value of
		 * {@link System.getProperty("file.encoding")} which encoding will be
		 * used for the file. In Eclipse, this will be the encoding of the main
		 * class if not specified otherwise. Otherwise, it will be the default
		 * encoding of the OS.
		 * 
		 * @see net.sf.opensoundboard.MappingParser#parseAllLines()
		 * 
		 * @param fw The filewriter for the file that the contents shall be
		 *        written to
		 */
        void write(FileWriter fw);
    }

    /**
	 * Only used for debugging as the program is normally started as a JavaFX
	 * application. Needs the JIntellitype DLL in the Windows system directory.
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
	 * Here it all begins...
	 */
    @Override
    public void start(Stage arg0) {
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-5p [%C{1}.%M]: %m%n")));
        final CommandLineOptions clo = new CommandLineOptions();
        clo.parseArgs(this.getParameters().getRaw());
        config.setCommandLineOptions(clo);
        config.init(config.getConfigFile());
        if (config.listLanguages()) {
            Text.listLanguages();
            System.exit(0);
        }
        initProgram();
        Soundboard osb = new Soundboard();
        SystrayIconManager systray = null;
        if (config.showTrayIcon()) {
            log.info("Creating system tray icon");
            systray = new SystrayIconManager(osb);
        }
        osb.start();
        if (systray != null) {
            systray.destroy();
        }
        log.info("Program exited normally");
        System.exit(0);
    }

    /**
	 * Initializes this program
	 */
    private void initProgram() {
        File appdataProgramDir = new File(Constants.APPDATA_PROGRAMDIR);
        if (!appdataProgramDir.exists()) {
            if (appdataProgramDir.mkdir()) {
                log.info("Program´s appdata directory did not exist and was created as: " + appdataProgramDir.getAbsolutePath());
            } else {
                log.fatal("Program´s appdata directory could not be created");
            }
        }
        renameOldMappingfile();
        createFileIfItDoesNotExist(config.getMappingFile(), defaultMappingFileContents);
        createFileIfItDoesNotExist(config.getConfigFile(), defaultConfigFileContents);
        initLocalization();
    }

    /**
	 * Tries to load the localization classes to see if they are ok
	 */
    private void initLocalization() {
        try {
            ResourceBundle.getBundle(Constants.BUNDLE_NAME);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not init localization classes.", Constants.PROJECT_NAME, JOptionPane.OK_CANCEL_OPTION);
            System.exit(1);
        }
    }

    /**
	 * Default contents of the config file
	 * 
	 * @return Contents to be written into the config file
	 */
    private final ContentWriter defaultConfigFileContents = new ContentWriter() {

        @Override
        public void write(FileWriter fw) {
            PrintWriter pw = new PrintWriter(fw);
            pw.println("# Show an icon in the system tray?");
            pw.println("# " + ConfigConstants.USE_TRAY_ICON + " = " + Constants.DEFAULT_USE_TRAY_ICON);
            pw.println("#");
            pw.println("# Use other files than the default ones (use \"/\" as a path separator!)");
            pw.println("# " + ConfigConstants.MAPPING_FILE + " = ");
            pw.println("# " + ConfigConstants.ICON_FILE + " = ");
            pw.println("#");
            pw.println("# Set another language than the system default");
            pw.println("# " + ConfigConstants.LANGUAGE + " = ");
            pw.close();
        }
    };

    /**
	 * Example contents of the mapping file
	 * 
	 * @return Contents to be written into the mapping file
	 */
    private final ContentWriter defaultMappingFileContents = new ContentWriter() {

        @Override
        public void write(FileWriter fw) {
            PrintWriter pw = new PrintWriter(fw);
            pw.println("# == Examples ==");
            pw.println("# For more information see README");
            pw.println("#");
            pw.println("# Win+Numpad0  = C:/OpenSoundboard/shutdown.wav");
            pw.println("# Win+F2 = C:/OpenSoundboard/lol.wav");
            pw.println("# Win+Shift+1 = C:/OpenSoundboard/drama.mp3");
            pw.println("# Win+Shift+b = C:/OpenSoundboard/drunk.mp3");
            pw.println("# Win+Space = *StopAll");
            pw.close();
        }
    };

    /**
	 * Checks wether the passed file exists and if that is not the case creates
	 * the file
	 * 
	 * @param filename The filename to check
	 * @param contents Content to be written into the file (via a callback)
	 */
    private void createFileIfItDoesNotExist(final File file, ContentWriter contents) {
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    log.info("File did not exist and was created as: " + file.getAbsolutePath());
                    FileWriter fw = new FileWriter(file);
                    contents.write(fw);
                    fw.close();
                } else {
                    log.error("File could not be created as: " + file.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            log.fatal("File could not be created or written to", e);
        }
    }

    /**
	 * Checks if a mapping file with the old filename exists and renames it.
	 * 
	 * This became necessary as the mapping file needed to be renamed because
	 * the file extension ".config" was eventually taken by some Microsoft
	 * application and couldn´t be assigned to another application. Otherwise,
	 * the file could not have been opened via the systray icon.
	 * 
	 * In case this happens with ".properties" too: The error message was
	 * "Ungültiges Menühandle" (translates to "invalid menu handle").
	 */
    private void renameOldMappingfile() {
        File old = new File(Constants.APPDATA_PROGRAMDIR + Constants.SEP + "mapping.config");
        if (old.exists()) {
            File newone = new File(Constants.DEFAULT_MAPPING_FILE);
            if (!old.renameTo(newone)) {
                log.error("Old 'mapping.config' could not be renamed! Rename it manually to '" + Constants.DEFAULT_MAPPING_FILE + "' or delete it.");
            }
        }
    }
}
