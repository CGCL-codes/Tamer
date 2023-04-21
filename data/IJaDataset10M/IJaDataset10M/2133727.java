package fr.cantor.commore.comm.address;

import org.junit.Test;
import fr.cantor.commore.comm.Protocol;
import fr.cantor.commore.comm.address.Address;
import fr.cantor.commore.comm.address.TcpProtocolData;
import junit.framework.TestCase;

public class AddressTest extends TestCase {

    @Test
    public void testTcp() {
        Address address = Address.fromString("myClass@TCPIP:localhost:8080");
        assertNotNull(address);
        assertNull(address.serviceName);
        assertEquals(Protocol.TCPIP, address.protocol);
        TcpProtocolData data = (TcpProtocolData) address.addressData;
        assertEquals("localhost", data.host);
        assertEquals(8080, data.port);
        assertTrue(address.hasServiceClassName());
        assertFalse(address.hasServiceName());
        assertTrue(address.hasProtocol() && address.hasProtocolData());
    }

    @Test
    public void testTcpService() {
        Address address = Address.fromString("TotoClass:toto@TCPIP:localhost:8080");
        assertNotNull(address);
        assertEquals("toto", address.serviceName);
        assertEquals("TotoClass", address.serviceClassName);
        assertEquals(Protocol.TCPIP, address.protocol);
        TcpProtocolData data = (TcpProtocolData) address.addressData;
        assertEquals("localhost", data.host);
        assertEquals(8080, data.port);
        assertTrue(address.hasServiceClassName());
        assertTrue(address.hasServiceName());
        assertTrue(address.hasProtocol() && address.hasProtocolData());
    }

    @Test
    public void testTcpInvalid() {
        Address address = Address.fromString("IPOT:localhost:8080");
        assertNull(address);
    }
}
