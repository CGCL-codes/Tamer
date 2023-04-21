package com.google.code.jtracert.samples;

import org.junit.Test;
import com.google.code.jtracert.model.MethodCall;
import java.util.NoSuchElementException;

public class CallTraceSizeTest extends JTracertTestCase {

    @Test
    public void testCallTraceSize() throws Exception {
        JTracertSerializableTcpServer tcpServer = startJTracertTcpServer(60002);
        Process process = startJavaProcessWithJTracert("deploy/callTraceSize.jar");
        int exitCode = process.waitFor();
        assertEquals(0, exitCode);
        try {
            MethodCall methodCall = tcpServer.getMethodCall();
            assertNull(methodCall);
        } catch (NoSuchElementException e) {
            assertNotNull(e);
        }
    }
}
