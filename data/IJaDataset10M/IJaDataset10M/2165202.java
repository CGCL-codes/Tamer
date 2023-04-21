package uk.ac.imperial.ma.metric.applets.metricApplet;

import java.awt.GridLayout;
import javax.swing.JPanel;
import uk.ac.imperial.ma.metric.exercises.AnimatedGraphicalExerciseInterface;
import uk.ac.imperial.ma.metric.exercises.QuestionInterface;
import uk.ac.imperial.ma.metric.gui.AnimatedGraphicalExercisePanel;
import java.net.URL;

/**
 * The panel used by the <code>RunExercise</code> application.
 * 
 * @author Daniel J. R. May
 * @version 1.0.1
 * @see uk.ac.imperial.ma.metric.apps.runExercise.RunExercise
 */
public class AnimatedGraphicalExerciseAppletPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** The metric GUI component for a FreeformExercise.*/
    private AnimatedGraphicalExercisePanel gep;

    /** 
     * Contains the <code>notesPanel</code> as its bottom component, 
     * and either <code>ffep</code> or <code>mcep</code> as its top component.
     */
    public AnimatedGraphicalExerciseAppletPanel() {
        super();
        setLayout(new GridLayout(1, 1));
    }

    /** 
     * Constructs a <code>FreeformExercisePanel</code> or <code>MCExercisePanel</code> object
     * depending on the type of the supplied exercise. Sets the <code>FreeformExercisePanel</code> 
     * or <code>MCExercisePanel</code> as the top component in <code>jsp</code> and calls 
     * <code>jsp.resetToPreferredSizes();</code>
     *
     * @param exercise the <code>packagename.classname</code> of the 
     * exercise data class (which implements the <code>ExerciseInterface<code> interface).
     * @exception Exception This could be called for a number of reasons:
     * <ul>
     * <li>Could not get a <code>ClassLoader</code> to load the exercise data class</li>
     * <li>Could not get an instance of the exercise data class</li>
     * <li>The exercise data file does not implement <code>ExerciseInterface</code></li>
     * <li>The exercise data was of an unkown type</li>
     * </ul>
     * @see uk.ac.imperial.ma.metric.exercises.ExerciseInterface
     */
    public void load(AnimatedGraphicalExerciseInterface exercise) throws Exception {
        if (exercise.getExerciseType() == AnimatedGraphicalExerciseInterface.ANIMATED_GRAPHICAL_EXERCISE_TYPE_1) {
            gep = new AnimatedGraphicalExercisePanel(exercise);
            this.add(gep);
        } else {
            throw new Exception("Unknown Exercise Type. Exercise claims to be of type " + exercise.getExerciseType() + ", compare that with those in the ExerciseInterface class.");
        }
        this.setVisible(true);
    }

    public void init() {
        if (gep != null) {
            gep.init();
        }
    }
}
