package icescrum2.listeners;

import icescrum2.service.impl.ConfigurationServiceImpl;
import java.util.Properties;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class CustomPHC extends PropertyPlaceholderConfigurer {

    public static final String contextRoot = "contextRoot";

    @Override
    protected void convertProperties(Properties props) {
        props.setProperty("contextRoot", ConfigurationServiceImpl.contextRoot);
        super.convertProperties(props);
    }

    @Override
    public void setLocations(Resource[] locations) {
        locations[locations.length - 1] = new FileSystemResource(System.getProperty("user.home") + "/.icescrum/" + ConfigurationServiceImpl.contextRoot + "/configuration.properties");
        super.setLocations(locations);
    }
}
