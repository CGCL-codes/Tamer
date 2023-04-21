package gui.environment;

import automata.Automaton;
import automata.fsa.FiniteStateAutomaton;
import gui.editor.EditorPane;
import gui.environment.tag.EditorTag;
import gui.environment.tag.PermanentTag;
import gui.environment.tag.Tag;
import java.io.Serializable;
import javax.swing.JOptionPane;

/**
 * The <CODE>EnvironmentFactory</CODE> creates environments for some
 * predefined types of objects with an editing pane already added.
 * For situations where a new <CODE>Environment</CODE> must be created
 * with customized trimmings, this sort of factory will be
 * inappropriate.  The intended use is for <CODE>Environment</CODE>s
 * opened in a file, or created in a new file action, or some other
 * such action.
 *
 * @author
 */
public class EnvironmentFactory {

    /**
     * Returns a new environment with an editor pane.
     *
     * @param object the object that this environment will edit
     * @return a new environment for the passed in object, ready to be
     *         edited, or <CODE>null</CODE> if no environment could be defined
     *         for this object
     */
    public static Environment getEnvironment(Serializable object) {
        if (object instanceof Automaton) {
            Automaton aut = (Automaton) object;
            Environment env = new AutomatonEnvironment(aut);
            EditorPane editor = new EditorPane(aut);
            FiniteStateAutomaton fsa = (FiniteStateAutomaton) aut;
            env.add(editor, fsa.getID(), EDITOR_PERMANENT_TAG);
            return env;
        } else {
            JOptionPane.showMessageDialog(null, "Unknown type " + object.getClass() + " read!", "Bad Type", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static EditorPane getEditor(Automaton automaton) {
        EditorPane editor = new EditorPane(automaton);
        return editor;
    }

    /**
     * A class for an editor, which in most applications one will
     * want both permanent and marked as an editor.
     */
    private static class EditorPermanentTag implements EditorTag, PermanentTag {
    }

    ;

    /**
     * An instance of such an editor permanant tag.
     */
    public static final Tag EDITOR_PERMANENT_TAG = new EditorPermanentTag();

    /**
     * The name for editor panes.
     */
    private static final String EDITOR_NAME = "Editor";
}
