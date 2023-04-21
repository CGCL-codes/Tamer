package net.sf.opentranquera.integration.notifier.config.impl;

import java.util.Enumeration;
import java.util.Properties;
import net.sf.opentranquera.integration.notifier.config.Config;
import net.sf.opentranquera.integration.notifier.config.ConfigReader;
import net.sf.opentranquera.integration.notifier.config.LoadConfigurationException;
import net.sf.opentranquera.util.MapUtils;
import net.sf.opentranquera.util.ReflectionUtils;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Esta clase es un ConfigReader que wrappea otro ConfigReader que es creado de acuerdo a las propiedades
 * pasadas en su constructor.<br>
 * Toma la propiedad "config.reader" y sus hijos (config.reader.*) para crear una instancia de un ConfigReader
 * particular.
 * 
 * @author <a href="mailto:diego.campodonico@eds.com">Diego Campodonico</a><br>
 */
public class WrapperConfigReader implements ConfigReader {

    /**
     * ConfigReader al cual esta clase wrappea
     */
    protected ConfigReader reader = null;

    /**
     * @param props Propiedades sobre las cuales se creara un ConfigReader.
     */
    public WrapperConfigReader(Properties props) {
        String configReaderClassName = props.getProperty("config.reader");
        Properties properties = MapUtils.getPropertyGroup(props, "config.reader", false);
        try {
            this.reader = (ConfigReader) ReflectionUtils.newInstance(configReaderClassName);
            for (Enumeration enumeration = properties.propertyNames(); enumeration.hasMoreElements(); ) {
                String propName = (String) enumeration.nextElement();
                BeanUtils.setProperty(this.reader, propName, properties.getProperty(propName));
            }
        } catch (Exception e) {
            throw new LoadConfigurationException(e);
        }
    }

    public Config loadConfig() {
        return this.reader.loadConfig();
    }
}
