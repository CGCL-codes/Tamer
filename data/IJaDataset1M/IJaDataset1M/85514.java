package pl.sind.blip;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import pl.sind.blip.BlipConnector;
import pl.sind.blip.BlipConnectorException;
import pl.sind.blip.entities.Status;
import pl.sind.http.HttpRequestException;

@RunWith(JUnit4.class)
public class BlipConnectorTest extends BaseBlipTestCase {

    @Test
    public void testGetBliposphere() throws BlipConnectorException, HttpRequestException {
        BlipConnector bc = newConnector();
        List<Status> ret = bc.getBliposphere(2, 2, 0, BlipConnector.INCLUDE_USER | BlipConnector.INCLUDE_USER_AVATAR);
        System.out.println(ret);
    }
}
