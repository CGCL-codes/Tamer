package org.jmlspecs.eclipse.ui;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jmlspecs.eclipse.jmlchecker.Options;
import org.jmlspecs.eclipse.util.Log;

/**
 * @author David Cok
 * 
 * This class implements the preference page for the plugin
 */
public class Preferences extends org.eclipse.jface.preference.PreferencePage implements IWorkbenchPreferencePage {

    public void init(IWorkbench workbench) {
    }

    /** This class defines all the items that have a persistent
   * presence in the Preferences store. */
    public static class POptions {

        /** The prefix to be put on each key. */
        public static final String prefix = "org.jmlspecs.JML.";

        /** The preference store key for the debug option. */
        public static final String debugKey = prefix + "debug";

        /** The preference store key for the debugast option. */
        public static final String debugastKey = prefix + "debugast";

        /** The preference store key for the verbosity (quiet, nowarnings, verbose) option. */
        public static final String verbosityKey = prefix + "verbosity";

        /** The preference store key for the source option. */
        public static final String sourceKey = prefix + "javaSourceVersion";

        /** The preference store key for the classpath option. */
        public static final String classpathKey = prefix + "classpath";

        /** The preference store key for the destination option. */
        public static final String destinationKey = prefix + "destination";

        /** The preference store key for the specspath option. */
        public static final String specspathKey = prefix + "specspath";

        /** The preference store key for the specsProjectName option. */
        public static final String specsProjectNameKey = prefix + "specsProjectName";

        /** The preference store key for the parsePlus option. */
        public static final String parsePlusKey = prefix + "parsePlus";

        /** The preference store key for the rac option. */
        public static final String racKey = prefix + "rac";

        /** A temporary copy of the options structure, just used to get
     * the initial defaults.
     */
        private Options defaultOptions = new Options();

        /** The object controlling the preference store entry for the debug option. */
        public AbstractPreference.BooleanOption debug = new AbstractPreference.BooleanOption(debugKey, defaultOptions.debug, "debug", "When on, debug information is emitted");

        /** The object controlling the preference store entry for the debugast option. */
        public AbstractPreference.BooleanOption debugast = new AbstractPreference.BooleanOption(debugastKey, defaultOptions.debugast, "debugast", "When on, debug AST information is emitted");

        /** The object controlling the preference store entry for the verbosity option. */
        public AbstractPreference.IntOption verbosity = new AbstractPreference.IntOption(verbosityKey, defaultOptions.verbosity, "verbosity", "Amount of information emitted");

        /** The object controlling the preference store entry for the source option. */
        public AbstractPreference.StringOption source = new AbstractPreference.StringOption(sourceKey, defaultOptions.source, "Java source", "Version of Java source that is recognized");

        /** The object controlling the preference store entry for the destination option. */
        public AbstractPreference.StringOption destination = new AbstractPreference.StringOption(destinationKey, defaultOptions.destination, "Destination directory", "Directory in which to put compiled files");

        /** The object controlling the preference store entry for the specsProjectName option. */
        public AbstractPreference.StringOption specsProjectName = new AbstractPreference.StringOption(specsProjectNameKey, defaultOptions.specsProjectName, "Specs Project Name", "Name of the container containing links to specification path folders and jar files");

        /** The object controlling the preference store entry for the parsePlus option. */
        public AbstractPreference.BooleanOption parsePlus = new AbstractPreference.BooleanOption(parsePlusKey, defaultOptions.parsePlus, "parsePlus", "When on, comments beginning with +@ are JML comments, as well as those beginning with @");

        /** The object controlling the preference store entry for the rac option. */
        public AbstractPreference.BooleanOption rac = new AbstractPreference.BooleanOption(racKey, defaultOptions.rac, "runtime assertion checking", "When on, appropriate files are compiled with runtime assertion checks");
    }

    /** An instance of the object that holds all of the preference store items. */
    static POptions poptions = new POptions();

    /** This method fills in an Options structure whose values are set from
   * the current preference store settings (which should match those in the UI).
   * We use the preference store instead of the UI widgets so that this method
   * works even if the preference page has not yet been generated.  If the 
   * argument is null, a new Options structure is allocated; otherwise the
   * fields of the argument are filled in.  The argument or newly allocated 
   * object is returned.
   * @param options if not null, the structure to fill in
   * @return An Options structure initialized to the current preference store settings.
   */
    public static Options extractOptions(Options options) {
        if (options == null) options = new Options();
        options.debug = poptions.debug.getValue();
        options.verbosity = poptions.verbosity.getValue();
        options.source = poptions.source.getValue();
        options.destination = poptions.destination.getValue();
        options.specsProjectName = poptions.specsProjectName.getValue();
        options.parsePlus = poptions.parsePlus.getValue();
        options.debugast = poptions.debugast.getValue();
        options.rac = poptions.rac.getValue();
        Log.log("Extracted options");
        return options;
    }

    /**
   * This is the list of widgets in the JmlEclipse options section of the
   * preferences page
   */
    private static final PreferenceWidget[] eclipseOptions = new PreferenceWidget[] { new PreferenceWidget.IntWidget(poptions.verbosity, new String[] { "errors only", "errors and warnings (quiet)", "normal", "verbose" }) };

    /**
   * This is the list of widgets in the JmlEclipse options section of the
   * preferences page
   */
    private static final PreferenceWidget[] javaOptions = new PreferenceWidget[] { new PreferenceWidget.StringWidget(poptions.source), new PreferenceWidget.StringWidget(poptions.destination) };

    /**
   * An array of the JML option widgets.
   */
    private static final PreferenceWidget[] jmlOptions = { new PreferenceWidget.StringWidget(poptions.specsProjectName), new PreferenceWidget.BooleanWidget(poptions.parsePlus), new PreferenceWidget.BooleanWidget(poptions.rac) };

    /**
   * An array of widgets for debugging options.
   */
    private static final PreferenceWidget[] debugOptions = { new PreferenceWidget.BooleanWidget(poptions.debug), new PreferenceWidget.BooleanWidget(poptions.debugast) };

    /**
   * Creates the property page controls and initializes them.
   * 
   * @param parent The UI object into which to insert the controls
   * @return The new control that is added to 'parent'
   */
    protected Control createContents(Composite parent) {
        Control composite = addControl(parent);
        return composite;
    }

    /**
   * Constructs the view of the property page by adding different features like
   * labels, and setting the layout. Just a helper to <code>createContents()
   * </code>.
   * 
   * @param parent The parent composite to which the controls are added
   * @return The resulting control that defined the looking of the property page
   */
    private Control addControl(Composite parent) {
        Composite composite0 = new Widgets.VComposite(parent);
        new Label(composite0, SWT.CENTER).setText("These options are workspace options that apply to every JML-enabled Java project.");
        new Widgets.LabeledSeparator(composite0, "Options relating to Eclipse");
        addWidgets(eclipseOptions, composite0);
        new Widgets.LabeledSeparator(composite0, "Options relating to Java");
        addWidgets(javaOptions, composite0);
        new Widgets.LabeledSeparator(composite0, "Options relating to JML");
        addWidgets(jmlOptions, composite0);
        new Widgets.LabeledSeparator(composite0, "Options for debugging");
        addWidgets(debugOptions, composite0);
        return composite0;
    }

    /**
   * @see org.eclipse.jface.preference.IPreferencePage#performOk()
   */
    public boolean performOk() {
        setOptionValue(eclipseOptions);
        setOptionValue(javaOptions);
        setOptionValue(jmlOptions);
        setOptionValue(debugOptions);
        AbstractPreference.notifyListeners();
        return true;
    }

    public void performDefaults() {
        setDefaults(eclipseOptions);
        setDefaults(javaOptions);
        setDefaults(jmlOptions);
        setDefaults(debugOptions);
    }

    public void setDefaults(PreferenceWidget[] ws) {
        for (int i = 0; i < ws.length; ++i) {
            ws[i].setDefault();
        }
    }

    public void setOptionValue(PreferenceWidget[] ws) {
        for (int i = 0; i < ws.length; ++i) {
            ws[i].setOptionValue();
        }
    }

    protected IPreferenceStore doGetPreferenceStore() {
        return Activator.getDefault().getPreferenceStore();
    }

    public void performHelp() {
    }

    public void addWidgets(PreferenceWidget[] ws, Composite composite) {
        addWidgets(ws, 0, ws.length, composite);
    }

    public void addWidgets(PreferenceWidget[] ws, int offset, int num, Composite composite) {
        for (int i = 0; i < num; ++i) {
            ws[offset + i].addWidget(composite);
        }
    }
}
