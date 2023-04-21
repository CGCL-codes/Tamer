package weka.gui.beans;

import java.beans.*;

/**
 * Bean info class for the data visualizer
 *
 * @author <a href="mailto:mhall@cs.waikato.ac.nz">Mark Hall</a>
 * @version $Revision: 1.3 $
 */
public class DataVisualizerBeanInfo extends SimpleBeanInfo {

    /**
   * Get the event set descriptors for this bean
   *
   * @return an <code>EventSetDescriptor[]</code> value
   */
    public EventSetDescriptor[] getEventSetDescriptors() {
        try {
            EventSetDescriptor[] esds = { new EventSetDescriptor(DataVisualizer.class, "dataSet", DataSourceListener.class, "acceptDataSet") };
            return esds;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
