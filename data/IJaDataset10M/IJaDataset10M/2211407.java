package org.aeroivr.appserver.common;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createStrictControl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import org.easymock.classextension.IMocksControl;

/**
 *
 * @author Andriy Petlyovanyy
 */
public class SettingsTest extends AbstractServiceLocatorTest {

    private IMocksControl control;

    private Properties propertiesMock;

    private Settings settingsMock;

    private File fileMock;

    private ServiceLocator serviceLocatorMock;

    public SettingsTest(final String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws NoSuchMethodException {
        control = createStrictControl();
        propertiesMock = control.createMock(Properties.class);
        fileMock = control.createMock(File.class);
        settingsMock = control.createMock(Settings.class, new Method[] { Settings.class.getDeclaredMethod("getSettingsFileName") });
        serviceLocatorMock = control.createMock(ServiceLocator.class, new Method[] { ServiceLocator.class.getMethod("getFileAsInputStream", String.class), ServiceLocator.class.getMethod("getFileAsOutputStream", String.class), ServiceLocator.class.getMethod("getProperties"), ServiceLocator.class.getMethod("getFile", String.class) });
    }

    private void loadSequence() throws IOException {
        final String fileName = "testSettings.properties";
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        serviceLocatorMock.getProperties();
        expectLastCall().andReturn(propertiesMock).once();
        settingsMock.getSettingsFileName();
        expectLastCall().andReturn(fileName).once();
        serviceLocatorMock.getFile(eq(fileName));
        expectLastCall().andReturn(fileMock).once();
        fileMock.exists();
        expectLastCall().andReturn(true).once();
        control.checkOrder(false);
        settingsMock.getSettingsFileName();
        expectLastCall().andReturn(fileName).once();
        expect(serviceLocatorMock.getFileAsInputStream(eq(fileName))).andReturn(inputStream).once();
        control.checkOrder(true);
        propertiesMock.load(eq(inputStream));
        expectLastCall().once();
    }

    public void testLoadSettings() throws IOException {
        loadSequence();
        control.replay();
        ServiceLocator.load(serviceLocatorMock);
        settingsMock.loadSettings();
        control.verify();
    }

    public void testSaveSettings() throws IOException {
        final String fileName = "testSettings.properties";
        final OutputStream outputStream = new ByteArrayOutputStream();
        loadSequence();
        settingsMock.getSettingsFileName();
        expectLastCall().andReturn(fileName).once();
        serviceLocatorMock.getFile(eq(fileName));
        expectLastCall().andReturn(fileMock).once();
        fileMock.exists();
        expectLastCall().andReturn(false).once();
        fileMock.createNewFile();
        expectLastCall().andReturn(null).once();
        settingsMock.getSettingsFileName();
        expectLastCall().andReturn(fileName).once();
        expect(serviceLocatorMock.getFileAsOutputStream(eq(fileName))).andReturn(outputStream).once();
        propertiesMock.store(eq(outputStream), eq(""));
        expectLastCall().once();
        control.replay();
        ServiceLocator.load(serviceLocatorMock);
        settingsMock.loadSettings();
        settingsMock.saveSettings();
        control.verify();
    }

    public void testGetInstance() {
        assertTrue("Settings object should not be null", null != Settings.getInstance());
    }

    public void testGetAdminPassword() throws IOException {
        final String adminPassword = "admPWD";
        loadSequence();
        expect(propertiesMock.getProperty(eq(Settings.ADMIN_PASSWORD), eq(""))).andReturn(adminPassword).once();
        control.replay();
        ServiceLocator.load(serviceLocatorMock);
        settingsMock.loadSettings();
        assertEquals("Password should be equal ", settingsMock.getAdminPassword(), adminPassword);
        control.verify();
    }

    public void testSetAdminPassword() throws IOException {
        final String adminPassword = "admPWD";
        loadSequence();
        expect(propertiesMock.setProperty(eq(Settings.ADMIN_PASSWORD), eq(adminPassword))).andReturn(null).once();
        control.replay();
        ServiceLocator.load(serviceLocatorMock);
        settingsMock.loadSettings();
        settingsMock.setAdminPassword(adminPassword);
        control.verify();
    }
}
