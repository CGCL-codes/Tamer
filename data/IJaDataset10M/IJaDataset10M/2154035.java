package uk.ac.imperial.ma.metric.apps.runExercise;

import java.awt.GridLayout;
import java.io.File;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import uk.ac.imperial.ma.metric.exercises.AnimatedDynamicGraphicalExerciseInterface;
import uk.ac.imperial.ma.metric.gui.NotesPanel;
import uk.ac.imperial.ma.metric.gui.AnimatedDynamicGraphicalExercisePanel;
import uk.ac.imperial.ma.metric.util.Loader;

/**
 * This the frame used by the <code>RunExercise</code> application
 * for graphical exercises.
 * 
 * @author Daniel J. R. May
 * @version 1.0.0
 * @see uk.ac.imperial.ma.metric.apps.runExercise.RunExercise
 */
public class AnimatedDynamicGraphicalExerciseFrame extends JFrame {

    /** The metric GUI component for a FreeformExercise.*/
    private AnimatedDynamicGraphicalExercisePanel gep;

    /**
     * Constructs all of the components required for this instance apart from the 
     * <code>GraphicalExercisePanel</code> in the <code>loadExercise</code> method.
     *
     * @param strExercise the title of the exercise which will appear in the frames titlebar. 
     * @see #loadExercise(String exerciseClassName) 
     */
    public AnimatedDynamicGraphicalExerciseFrame(String strExercise) {
        super("Running Animated Dynamic Graphical Exercise: " + strExercise);
        getContentPane().setLayout(new GridLayout(1, 1));
        setSize(1000, 600);
        setVisible(true);
    }

    /**
     * Constructs all of the components required for this instance apart from the 
     * <code>GraphicalExercisePanel</code> in the <code>loadExercise</code> method.
     *
     * @param strExercise the title of the exercise which will appear in the frames titlebar. 
     * @see #loadExercise(String exerciseClassName) 
     */
    public AnimatedDynamicGraphicalExerciseFrame(String strExercise, boolean runningAsApplication) {
        super("Running Animated Dynamic Graphical Exercise: " + strExercise);
        if (runningAsApplication) {
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }
        getContentPane().setLayout(new GridLayout(1, 1));
        setSize(1000, 600);
        setVisible(true);
    }

    /** 
     * Constructs a <code>GraphicalExercisePanel</code> or 
     * <code>GraphicalMultipleChoiceExercisePanel</code> and sets it as 
     * the top component in <code>jsp</code> and calls 
     * <code>jsp.resetToPreferredSizes()</code>.
     *
     * @param exercise the graphical exercise data class (which implements 
     * the <code>GraphicalExerciseInterface<code> interface).
     * @exception Exception This could be called for a number of reasons:
     * <ul>
     * <li>The exercise data was of an unknown type</li>
     * </ul>
     * @see uk.ac.imperial.ma.metric.exercises.GraphicalExerciseInterface
     */
    public void load(AnimatedDynamicGraphicalExerciseInterface exercise) throws Exception {
        switch(exercise.getExerciseType()) {
            case AnimatedDynamicGraphicalExerciseInterface.ANIMATED_DYNAMIC_GRAPHICAL_EXERCISE_TYPE_1:
                gep = new AnimatedDynamicGraphicalExercisePanel(exercise);
                this.add(gep);
                break;
            default:
                throw new Exception("Unknown Exercise Type. Exercise claims to be of type " + exercise.getExerciseType() + ", compare that with those in the GraphicalExerciseInterface class.");
        }
        this.setVisible(true);
    }
}
