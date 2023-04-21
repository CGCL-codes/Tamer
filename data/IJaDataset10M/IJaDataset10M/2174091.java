package eu.more.measurementservice.datareaders;

import org.osgi.framework.BundleContext;
import eu.more.measurementservice.MeasurementServiceActivator;

/**
 * @author pdaum
 *
 */
public interface ServiceRef {

    /**
   * Set the current {@link MeasurementServiceActivator} as reference
   *
   * @param reference
   */
    public void setMeasurementServiceActivator(MeasurementServiceActivator reference);

    /**
   * Set the current {@link BundleContext} as reference
   *
   * @param context
   */
    public void setBundleContext(BundleContext context);
}
