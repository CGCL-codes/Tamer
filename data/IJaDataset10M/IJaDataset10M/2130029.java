package net.diet_rich.jabak.main.jabak;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import net.diet_rich.util.PropertyChangeListener;
import net.diet_rich.util.StringSettings;
import net.diet_rich.util.StringUtils;
import net.diet_rich.util.io.FileOP;
import net.diet_rich.util.ref.BoolRef;
import net.diet_rich.util.ref.FlaggedRef;
import net.diet_rich.util.ref.LongCounter;
import net.diet_rich.util.ref.StringRef;
import net.diet_rich.util.ref.StringSetter;

/**
 * in this class, all information needed to initialize the jabak backup core is
 * stored. all reference members of this class can directly be accessed through
 * command line or config file settings.
 * 
 * @author Georg Dietrich
 */
public class Settings {

    public final ParameterSettings p = new ParameterSettings();

    /**
	 * in this array, command names for the command line and config files are
	 * listed that are not interpreted by this settings class, but earlier in
	 * the backup system.
	 * <p>
	 * configfile (optional): the config file to load settings from. settings
	 * can be the same as on the command line. command line settings have
	 * precedence before config file settings.
	 * <p>
	 * nodefaultconfigfile (optional): switch whether to skip parsing the
	 * default configfile. config file settings have precedence before default
	 * config file settings. the boolean value is determined by
	 * {@link StringUtils#evalBoolString(String)}.
	 * <p>
	 * debug (optional): switch whether to accept commands intended for
	 * debugging from command line and config files. debug commands start with
	 * an "_".
	 * <p>
	 * help (optional): start the gui with the help window enabled. if help was
	 * the only command from command line and config file, do not show the full
	 * gui, only the help window.
	 * TODO 5: implement help window
	 */
    public final String[] additionalCommands = { "configfile", "nodefaultconfigfile", "debug", "help" };

    /**
	 * flag to indicate all settings are ok.
	 */
    public final StringRef ok = new StringRef("");

    /**
	 * flag to indicate whether input in general should be enabled. this is set
	 * to false during backup.
	 */
    public final BoolRef enable = new BoolRef(true);

    /**
	 * the files to back up
	 */
    public SortedSet<File> files = null;

    /**
	 * the undo list for all file operations
	 */
    public final FileOP undoList = new FileOP();

    /**
	 * set of objects claiming that backup may not yet start.
	 */
    public final Set<Object> problems = new HashSet<Object>();

    /**
	 * list of the command names that are not interpreted by this settings
	 * class, but earlier in the backup system. a list is easier to search than
	 * an array...
	 */
    public final List<String> ignoreCommands = Arrays.asList(additionalCommands);

    /**
	 * the string settings used to obtain these settings
	 */
    public final StringSettings stringSettings;

    /**
	 * the command keys that could not be interpreted
	 */
    public final Set<String> offendingKeys = new HashSet<String>();

    /**
	 * the counter of files to go
	 */
    public final LongCounter filecount = new LongCounter();

    /**
	 * the counter of the remaining source size in bytes
	 */
    public final LongCounter sourcesize = new LongCounter();

    /**
	 * the target directory to write the backup into
	 */
    public File targetDir = null;

    /**
	 * the name of the database including time stamp
	 */
    public String dbname = null;

    /**
	 * the counter of bytes already written to the backup repository
	 */
    public final LongCounter sizecount = new LongCounter();

    /**
	 * the actual database directory used
	 */
    public File dbdir = null;

    /**
	 * the number of files skipped due to backup size restrictions
	 */
    public int skippedFiles = 0;

    /**
	 * initialize this settings object with string settings. keys that were not
	 * recognized as valid command keys are added to the offending keys set.
	 * 
	 * @param stringSettings the string settings to use for initialization
	 */
    public Settings(StringSettings stringSettings) {
        this.stringSettings = stringSettings;
        StringSetter setter = new StringSetter(p);
        boolean debug = StringUtils.evalBoolString(stringSettings.get("debug"));
        for (String key : stringSettings.keys()) if (!ignoreCommands.contains(key)) if (!debug && key.startsWith("_")) offendingKeys.add(key); else try {
            setter.set(key, stringSettings.get(key));
        } catch (IllegalArgumentException e) {
            offendingKeys.add(key);
        }
        initListeners();
    }

    /**
	 * initialize the listeners checking the settings.
	 */
    private void initListeners() {
        p.source.addChangeListener(new PropertyChangeListener<File>() {

            public void changed(File file) {
                if (file.isDirectory() && file.canRead() && file.listFiles().length > 0) updateStatus(p.source, ""); else updateStatus(p.source, "source must be a non-empty readable directory");
            }
        });
        p.target.addChangeListener(new PropertyChangeListener<File>() {

            public void changed(File file) {
                if (file.isDirectory() && file.canWrite()) updateStatus(p.target, ""); else updateStatus(p.target, "target must be a writeable directory");
            }
        });
    }

    private <T> void updateStatus(FlaggedRef<T> ref, String status) {
        ref.status.set(status);
        if (status == null || status.equals("")) problems.remove(ref); else problems.add(ref);
        if (problems.isEmpty()) ok.set(""); else ok.set(problems.size() + " problems");
    }
}
