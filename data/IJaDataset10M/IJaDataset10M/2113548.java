package br.biofoco.p2p.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import br.biofoco.p2p.config.ConfigConstants;

public class JSONServiceLoader implements ServiceLoader {

    private static final String BASE_DIR = System.getProperty("APP_HOME", ".");

    @SuppressWarnings("unchecked")
    public Collection<? extends Service<?>> loadServices() throws IOException {
        String namefile = BASE_DIR + File.separator + ConfigConstants.CONFIG_DIR + File.separator + ConfigConstants.SERVICES_FILE;
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(namefile);
        Map<String, Object> userData = mapper.readValue(file, Map.class);
        List<Service<?>> serviceList = populateListMap(userData);
        return serviceList;
    }

    private List<Service<?>> populateListMap(Map<String, Object> userData) {
        List<Service<?>> list = new ArrayList<Service<?>>();
        if (userData.containsKey("service")) {
        }
        return list;
    }
}
