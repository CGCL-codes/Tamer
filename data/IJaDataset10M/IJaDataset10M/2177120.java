package de.frostcode.visualmon.probe;

import java.util.Collection;
import java.util.Locale;

/**
 * A container for a dynamic list of probes. Each provider will be displayed with an own tab in the
 * dashboard.
 */
public interface ProbeProvider {

    /**
   * Returns a collection of probes. May return a different collection on each invocation.
   * @return the provided probes
   */
    Collection<Probe> getProbes();

    /**
   * Returns the localized title of the provider. Will be displayed as the title of the provider
   * tab in the dashboard.
   * @param locale the target locale for the title
   * @return the localized title
   */
    String getTitle(Locale locale);
}
