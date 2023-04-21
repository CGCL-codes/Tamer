package net.sf.dozer.util.mapping;

import java.util.ArrayList;
import java.util.List;
import net.sf.dozer.util.mapping.util.MapperConstants;

/**
 * Public Singleton wrapper for the DozerBeanMapper. Only supports a single mapping file named dozerBeanMapping.xml, so
 * configuration is very limited. Instead of using the DozerBeanMapperSingletonWrapper, it is recommended that the
 * DozerBeanMapper(MapperIF) instance is configured via an IOC framework, such as Spring, with singleton property set to
 * "true"
 * 
 * @author garsombke.franz
 */
public class DozerBeanMapperSingletonWrapper {

    private static MapperIF instance;

    private DozerBeanMapperSingletonWrapper() {
    }

    public static synchronized MapperIF getInstance() {
        if (instance == null) {
            List mappingFiles = new ArrayList();
            mappingFiles.add(MapperConstants.DEFAULT_MAPPING_FILE);
            instance = new DozerBeanMapper(mappingFiles);
        }
        return instance;
    }
}
