package net.assimilator.config;

import com.sun.jini.config.Config;
import net.jini.config.Configuration;
import net.jini.config.ConfigurationException;
import net.jini.export.Exporter;

/**
 * The ExporterConfig is a utility class used to get an 
 * {@link net.jini.export.Exporter} instance from a 
 * {@link net.jini.config.Configuration} defaulting to a special entry configured 
 * for the platform 
 * <br><br>
 * &nbsp;&nbsp;&nbsp;<tt>net.assimilator.defaultExporter</tt>
 * <p>
 * This allows deployers to configure one Exporter that all services (if service
 * configurations declare) will use. Declaring that your
 * Service will use the default Exporter is done as follows: <br><br>
 * <span style="font-family: monospace;">&lt;Configuration&gt;&nbsp;&nbsp;&nbsp;
 * </span> <br style="font-family: monospace;">
 * <span style="font-family: monospace;">&nbsp;&nbsp;&nbsp;&nbsp; &lt;Component
 * Name="net.assimilator"&gt; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span> <br
 * style="font-family: monospace;"> <span style="font-family:
 * monospace;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;Parameter
 * </span> <br style="font-family: monospace;">
 * <span style="font-family: monospace;">&nbsp;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * Name="defaultExporter"
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span> <br
 * style="font-family: monospace;"> <span style="font-family:
 * monospace;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 * Value="$net_assimilator_defaultExporter"/&gt; &nbsp;&nbsp;&nbsp; </span> <br
 * style="font-family: monospace;"> <span style="font-family:
 * monospace;">&nbsp;&nbsp;&nbsp;&nbsp; &lt;/Component&gt; </span>
 * <br style="font-family: monospace;">
 * <span style="font-family: monospace;">&lt;/Configuration&gt; </span> <br
 * style="font-family: monospace;"> <br>
 */
public class ExporterConfig {

    public static final String DEFAULT_COMPONENT = "net.assimilator";

    public static final String ENTRY_NAME = "defaultExporter";

    /**
     * Get an {@link net.jini.export.Exporter} instance from 
     * a {@link net.jini.config.Configuration} using the provided component name 
     * and entry. This method will first establish what the 
     * default Exporter is as follows:
     * <ul>
     * <li>If the  special entry 
     * <tt>net.assimilator.defaultExporter</tt> can be found, the
     * Exporter specified by this entry will be used as the default Exporter.
     * <li>
     * This method will then check if the component and entry provided
     * exists. If the entry can be found in the provided Configuration that Exporter 
     * will be used. If the entry cannot be found, then the defaultExporter will be 
     * used as the Exporter.
     * </ul>
     * @param config The Configuration to obtain the Exporter from
     * @param component The component name
     * @param entry The entry name
     * @param defaultExporter The Exporter to use as a default if the special 
     * entry <tt>net.assimilator.defaultExporter</tt> can not be found
     *
     * @return A suitable Exporter 
     * @throws ConfigurationException
     */
    public static Exporter getExporter(Configuration config, String component, String entry, Exporter defaultExporter) throws ConfigurationException {
        final Exporter exporter = (Exporter) config.getEntry(DEFAULT_COMPONENT, ENTRY_NAME, Exporter.class, defaultExporter);
        return ((Exporter) Config.getNonNullEntry(config, component, entry, Exporter.class, exporter));
    }
}
