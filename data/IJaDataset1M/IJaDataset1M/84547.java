package mx4j.tools.stats;

/**
 * Management interface description for TimedStatisticsRecorder MBeans.
 *
 * @version $Revision: 1.3 $
 */
public class TimedStatisticsRecorderMBeanDescription extends ObserverStatisticsRecorderMBeanDescription {

    public String getAttributeDescription(String attribute) {
        if ("Granularity".equals(attribute)) {
            return "How often the MBean will poll the variable value";
        }
        return super.getAttributeDescription(attribute);
    }
}
