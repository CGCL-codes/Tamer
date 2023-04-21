package org.bastion.adapter;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.bastion.message.GetConfigMessage;
import org.bastion.service.ConfigService;
import org.junit.Test;

/**
 * @author Danny Lagrouw
 */
public class GetConfigAdapterTest {

    @Test
    public void testGetConfigAdapter() {
        MockConfigService service = new MockConfigService();
        GetConfigAdapter adapter = new GetConfigAdapter(service);
        assertNotNull(adapter);
    }

    @Test
    public void testHandle() {
        MockConfigService service = new MockConfigService();
        GetConfigAdapter adapter = new GetConfigAdapter(service);
        GetConfigMessage message = new GetConfigMessage("X");
        adapter.handle(message);
        assertEquals(MockConfigService.PROPERTY_VALUE, message.getSingleResult());
    }

    @Test
    public void testHandle_Map() {
        MockConfigService service = new MockConfigService();
        GetConfigAdapter adapter = new GetConfigAdapter(service);
        GetConfigMessage message = new GetConfigMessage(Pattern.compile(".*"));
        adapter.handle(message);
        assertEquals(MockConfigService.PROPERTY_VALUE, message.getSingleResult());
    }

    class MockConfigService implements ConfigService {

        public static final String PROPERTY_NAME = "ABC";

        public static final String PROPERTY_VALUE = "XYZ";

        public Object getConfig(String propertyFilter) {
            return PROPERTY_VALUE;
        }

        public Map<String, Object> getConfig(Pattern propertyFilter) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(PROPERTY_NAME, PROPERTY_VALUE);
            return map;
        }
    }
}
