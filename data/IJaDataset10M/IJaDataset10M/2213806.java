package weka.gui.beans;

import java.beans.*;

/**
 * BeanInfo class for AbstractTrainingSetProducer
 *
 * @author <a href="mailto:mhall@cs.waikato.ac.nz">Mark Hall</a>
 * @version $Revision: 1.2 $
 */
public class AbstractTrainingSetProducerBeanInfo extends SimpleBeanInfo {

    /**
   * Returns event set descriptors for this type of bean
   *
   * @return an <code>EventSetDescriptor[]</code> value
   */
    public EventSetDescriptor[] getEventSetDescriptors() {
        try {
            EventSetDescriptor[] esds = { new EventSetDescriptor(TrainingSetProducer.class, "trainingSet", TrainingSetListener.class, "acceptTrainingSet") };
            return esds;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
