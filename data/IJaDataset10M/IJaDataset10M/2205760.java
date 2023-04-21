package org.impalaframework.web.facade;

import java.util.List;
import org.impalaframework.startup.ContextStarter;
import org.impalaframework.web.facade.AttributeServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class WebContextStarter implements ContextStarter {

    public ApplicationContext startContext(List<String> locations) {
        Assert.notNull(locations);
        final String[] locationArray = locations.toArray(new String[locations.size()]);
        for (int i = 0; i < locationArray.length; i++) {
            locationArray[i] = "classpath:" + locationArray[i];
        }
        final XmlWebApplicationContext context = new XmlWebApplicationContext() {

            @Override
            public String[] getConfigLocations() {
                return locationArray;
            }
        };
        context.setServletContext(new AttributeServletContext());
        context.refresh();
        return context;
    }
}
