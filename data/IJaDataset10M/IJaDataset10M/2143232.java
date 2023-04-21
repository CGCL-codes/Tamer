package br.gov.frameworkdemoiselle.util;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class ResourceBundleTest {

    private ResourceBundle resourceBundle;

    private transient java.util.ResourceBundle resourceBundleMocked;

    @Before
    public void setUp() throws Exception {
        this.resourceBundleMocked = PowerMock.createMock(java.util.ResourceBundle.class);
        this.resourceBundle = new ResourceBundle(resourceBundleMocked);
    }

    @Test
    public void testContainsKey() {
        expect(this.resourceBundleMocked.containsKey("")).andReturn(true);
        replay(this.resourceBundleMocked);
        this.resourceBundle.containsKey("");
        verify(this.resourceBundleMocked);
    }

    @Test
    public void testGetKeys() {
        expect(this.resourceBundleMocked.getKeys()).andReturn(null);
        replay(this.resourceBundleMocked);
        this.resourceBundle.getKeys();
        verify(this.resourceBundleMocked);
    }

    @Test
    public void testGetLocale() {
        expect(this.resourceBundleMocked.getLocale()).andReturn(null);
        replay(this.resourceBundleMocked);
        this.resourceBundle.getLocale();
        verify(this.resourceBundleMocked);
    }

    @Test
    public void testKeySet() {
        expect(this.resourceBundleMocked.keySet()).andReturn(null);
        replay(this.resourceBundleMocked);
        this.resourceBundle.keySet();
        verify(this.resourceBundleMocked);
    }
}
