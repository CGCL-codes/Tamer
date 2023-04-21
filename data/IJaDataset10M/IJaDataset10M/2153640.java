package wsdl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import junit.framework.TestCase;
import org.codehaus.xfire.client.Client;
import com.xsky.common.util.UUIDUtil;

public class Test extends TestCase {

    public void testWSDL() throws MalformedURLException, Exception {
        System.out.println(UUIDUtil.getUUID());
        System.out.println(UUID.randomUUID().toString());
    }
}
