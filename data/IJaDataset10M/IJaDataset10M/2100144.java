package KyleWrapper;

import java.util.EventListener;

/**
 *
 * @author fiebrink
 */
public interface TrainingExampleListener extends EventListener {

    public void fireTrainingExampleRecorded(int id, int classValue);
}
