package fr.univartois.cril.xtext2.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import fr.univartois.cril.xtext2.ui.activator.AlsActivator;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */
public class AlloyPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public static final String PDF_GRAPH = "pdf";

    public static final String PNG_GRAPH = "png";

    private IntegerFieldEditor widthGraph;

    private IntegerFieldEditor resolutionGraph;

    public AlloyPreferencePage() {
        super(GRID);
        setPreferenceStore(AlsActivator.getInstance().getPreferenceStore());
        setDescription("You can set up your Alloy preferences here:");
    }

    /**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
    public void createFieldEditors() {
        addField(new BooleanFieldEditor(PreferenceConstants.P_BOOLEAN_EXECUTE_TASKS_LOCALLY, "Execute Alloy tasks &Locally instead of as a separate Java sub-process", getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.P_BOOLEAN_WRITE_SHOW_ANSWER, "Automatically &Show graphical view of models or counter examples after command execution", getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.P_BOOLEAN_CLEAR_CONSOLE_FOR_EACH_COMMAND, "Clear console output before launching a command", getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.CLOSE_VIEW_UNSAT, "Close previous solution/counter-example when the answer becomes UNSAT", getFieldEditorParent()));
        addField(new RadioGroupFieldEditor(PreferenceConstants.P_SOLVER_CHOICE, "Choose solver for commands:", 1, new String[][] { { "&SAT4J", PreferenceConstants.V_SOLVER_SAT4J }, { "&Berkmin", PreferenceConstants.V_SOLVER_BERKMIN }, { "MiniSatProver &JNI", PreferenceConstants.V_SOLVER_MiniSatProverJNI }, { "MiniSatProver &Unsat Core", PreferenceConstants.V_SOLVER_MiniSatProverUnsatCore }, { "&ZChaffJNI", PreferenceConstants.V_SOLVER_ZChaffJNI } }, getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.P_BOOLEAN_SHOW_DEBUG_MESSAGES, "Show &Debug Messages", getFieldEditorParent()));
        addField(new RadioGroupFieldEditor(PreferenceConstants.V_GRAPH_CONVERSION, "Choose graph conversion:", 1, new String[][] { { "pdf", "pdf" }, { "png", "png" } }, getFieldEditorParent()));
        widthGraph = new IntegerFieldEditor(PreferenceConstants.WIDTH__GRAPH, "Choose the specific width:", getFieldEditorParent());
        resolutionGraph = new IntegerFieldEditor(PreferenceConstants.RESOLUTION__GRAPH, "Choose the specific resolution:", getFieldEditorParent());
        addField(widthGraph);
        addField(resolutionGraph);
        addField(new DirectoryFieldEditor(PreferenceConstants.P_SOLVERS_PATH, "Path to SAT solvers binaries and libraries:", getFieldEditorParent()));
        addField(new DirectoryFieldEditor(PreferenceConstants.P_A4_SAMPLE_MODELS_PATH, "Path to Alloy 4 sample models:", getFieldEditorParent()));
        addField(new StringFieldEditor(PreferenceConstants.DEFAULT_LAUNCH_OPTION, "Default Options when launching a Predicate or an Assertion: for", getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.P_BOOLEAN_LAUNCH_EXACTLY, "Launch exactly", getFieldEditorParent()));
        addField(new IntegerFieldEditor(PreferenceConstants.MAX_HEAP_SIZE, "Choose the maximum heap size for the external JVM (MB):", getFieldEditorParent()));
        addField(new IntegerFieldEditor(PreferenceConstants.MAX_STACK_SIZE, "Choose the maximum stack size for the external JVM (KB):", getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
        setPreferenceStore(AlsActivator.getInstance().getPreferenceStore());
    }

    public static boolean getShowDebugMessagesPreference() {
        return AlsActivator.getInstance().getPreferenceStore().getBoolean(PreferenceConstants.P_BOOLEAN_SHOW_DEBUG_MESSAGES);
    }

    public static String getShowGraphConversionMessagesPreference() {
        return AlsActivator.getInstance().getPreferenceStore().getString(PreferenceConstants.V_GRAPH_CONVERSION);
    }

    public static boolean getClearConsoleForEachCommand() {
        return AlsActivator.getInstance().getPreferenceStore().getBoolean(PreferenceConstants.P_BOOLEAN_CLEAR_CONSOLE_FOR_EACH_COMMAND);
    }

    public static String getA4SampleModelsPath() {
        return AlsActivator.getInstance().getPreferenceStore().getString(PreferenceConstants.P_A4_SAMPLE_MODELS_PATH);
    }

    public static int getResolutionGraph() {
        return AlsActivator.getInstance().getPreferenceStore().getInt(PreferenceConstants.RESOLUTION__GRAPH);
    }

    public static double getWidthGraph() {
        return AlsActivator.getInstance().getPreferenceStore().getDouble(PreferenceConstants.WIDTH__GRAPH);
    }
}
