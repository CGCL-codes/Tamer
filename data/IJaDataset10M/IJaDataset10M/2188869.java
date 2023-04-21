package org.bastion.adapter;

import static org.junit.Assert.*;
import org.bastion.domain.Alert;
import org.bastion.domain.Alert.Level;
import org.bastion.message.GetAlertsMessage;
import org.junit.Test;

/**
 * @author Danny Lagrouw
 */
public class GetAlertsAdapterTest {

    @Test
    public void testGetAlertsAdapter() {
        MockAlertService service = new MockAlertService();
        GetAlertsAdapter adapter = new GetAlertsAdapter(service);
        assertNotNull(adapter);
    }

    @Test
    public void testHandle() {
        Alert alert = new Alert("X", Level.ERROR);
        MockAlertService service = new MockAlertService();
        service.addAlert(alert);
        GetAlertsAdapter adapter = new GetAlertsAdapter(service);
        GetAlertsMessage message = new GetAlertsMessage(false);
        adapter.handle(message);
        assertEquals(service.getAlerts(false), message.getAlerts());
    }

    @Test
    public void testHandle_ClearAfterwards() {
        Alert alert = new Alert("X", Level.ERROR);
        MockAlertService service = new MockAlertService();
        service.addAlert(alert);
        GetAlertsAdapter adapter = new GetAlertsAdapter(service);
        GetAlertsMessage message = new GetAlertsMessage(true);
        adapter.handle(message);
        adapter.handle(message);
        assertTrue(message.getAlerts().isEmpty());
    }
}
