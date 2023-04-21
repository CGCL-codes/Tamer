package org.gapjump.security.keystone.ctrl;

import java.security.*;
import org.gapjump.security.keystone.ctrl.CreateKeyPairControl;
import org.gapjump.security.keystone.ctrl.KeyPairEvent;
import org.gapjump.security.keystone.ctrl.KeyPairEventListener;
import junit.framework.TestCase;

public class TestCreateKeyPairControl extends TestCase {

    CreateKeyPairControl control;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestCreateKeyPairControl.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        control = new CreateKeyPairControl();
    }

    public void testCreateKeyPair() {
        Exception e = null;
        try {
            Provider provider = Security.getProvider("SunJCE");
            String keyAlg = "DiffieHellman";
            int keySize = 1024;
            control.setProvider(provider);
            control.setAlgorithm(keyAlg);
            control.setSize(keySize);
            control.setAlias("testKeyPair");
            TestKeyEventListener listener = new TestKeyEventListener();
            control.getKeyPairEventListeners().add(listener);
            control.doAction(null);
            Thread.sleep(2000);
            assertTrue(listener.receivedEvent);
            KeyPair key = listener.keyPair;
            assertNotNull(key);
        } catch (Exception ex) {
            e = ex;
        }
        assertNull(e);
    }

    private class TestKeyEventListener implements KeyPairEventListener {

        boolean receivedEvent = false;

        KeyPair keyPair;

        public void handleEvent(KeyPairEvent evt) throws java.security.GeneralSecurityException {
            receivedEvent = evt.getId() == KeyPairEvent.CREATE;
            keyPair = evt.getKeyPair();
        }
    }
}
