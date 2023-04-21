package weka.gui.beans;

import java.util.EventListener;

/**
 * Interface to something that can accept and process training set events
 *
 * @author <a href="mailto:mhall@cs.waikato.ac.nz">Mark Hall</a>
 * @version $Revision: 1.4 $
 */
public interface TrainingSetListener extends EventListener {

    /**
   * Accept and process a training set
   *
   * @param e a <code>TrainingSetEvent</code> value
   */
    void acceptTrainingSet(TrainingSetEvent e);
}
