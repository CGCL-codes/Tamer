package weka.gui.beans;

import java.beans.*;

/**
 * Bean info class for the Filter bean
 *
 * @author <a href="mailto:mhall@cs.waikato.ac.nz">Mark Hall</a>
 * @version $Revision: 1.4 $
 */
public class FilterBeanInfo extends SimpleBeanInfo {

    /**
   * Get the event set descriptors for this bean
   *
   * @return an <code>EventSetDescriptor[]</code> value
   */
    public EventSetDescriptor[] getEventSetDescriptors() {
        try {
            EventSetDescriptor[] esds = { new EventSetDescriptor(TrainingSetProducer.class, "trainingSet", TrainingSetListener.class, "acceptTrainingSet"), new EventSetDescriptor(TestSetProducer.class, "testSet", TestSetListener.class, "acceptTestSet"), new EventSetDescriptor(DataSource.class, "dataSet", DataSourceListener.class, "acceptDataSet"), new EventSetDescriptor(DataSource.class, "instance", InstanceListener.class, "acceptInstance") };
            return esds;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
   * Get the bean descriptor for this bean
   *
   * @return a <code>BeanDescriptor</code> value
   */
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(weka.gui.beans.Filter.class, FilterCustomizer.class);
    }
}
