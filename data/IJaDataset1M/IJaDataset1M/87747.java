package org.mathematux.math.basic.view;

import java.util.Observable;
import org.mathematux.core.model.CoreManager;
import org.mathematux.math.basic.model.SimpleAdditionExercise;

public class SimpleAdditionExerciseGUI extends AbstractBasicExerciseGUI {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private SimpleAdditionExercise exercise;

    public void update(Observable arg0, Object arg1) {
        this.showQuestion();
    }

    public void showQuestion() {
        exercise = (SimpleAdditionExercise) CoreManager.getInstance().getCurrentCourse();
        super.showQuestion(exercise.getFirstTerm() + " + " + exercise.getSecondTerm() + " = ");
    }
}
