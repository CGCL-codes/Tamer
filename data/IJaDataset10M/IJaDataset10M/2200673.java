package example.btsppecho;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.ServiceRecord;
import example.btsppecho.client.ConnectionService;

public class MIDletApplication extends MIDlet {

    private static final String UUID = "A55665EE9F9146109085C2055C888B39";

    private static final String SERVICE_URL = "btspp://localhost:" + UUID;

    private final SettingsList settingsList;

    private boolean restoreDiscoverableModeOnExit;

    private int initialDiscoverableMode;

    private ConnectionService ConnectionService = null;

    private ClientForm clientForm = null;

    private ServiceDiscoveryList serviceDiscoveryList = null;

    private ServerForm serverForm = null;

    boolean serverUseAuthentication = false;

    boolean serverUseEncryption = false;

    public MIDletApplication() {
        try {
            restoreDiscoverableModeOnExit = true;
            initialDiscoverableMode = LocalDevice.getLocalDevice().getDiscoverable();
        } catch (BluetoothStateException e) {
            restoreDiscoverableModeOnExit = false;
        }
        settingsList = new SettingsList(this);
    }

    private void exit() {
        destroyApp(false);
        notifyDestroyed();
    }

    public void startApp() {
        Display display = Display.getDisplay(this);
        display.setCurrent(settingsList);
        ErrorScreen.init(null, display);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        if (serviceDiscoveryList != null) {
            serviceDiscoveryList.cancelPendingSearches();
            serviceDiscoveryList.abort();
        }
        if (ConnectionService != null) {
            ConnectionService.close();
        }
        if (clientForm != null) {
            clientForm.closeAll();
        }
        if (serverForm != null) {
            serverForm.closeAll();
        }
        if (restoreDiscoverableModeOnExit) {
            try {
                LocalDevice ld = LocalDevice.getLocalDevice();
                ld.setDiscoverable(initialDiscoverableMode);
            } catch (BluetoothStateException e) {
            }
        }
    }

    public void clientFormExitRequest() {
        exit();
    }

    public void clientFormViewLog(Displayable next) {
        LogScreen logScreen = new LogScreen(this, next, "Log", "Back");
        Display.getDisplay(this).setCurrent(logScreen);
    }

    public void settingsListStart(boolean isServer, int inquiryAccessCode, boolean useAuthentication, boolean useAuthorization, boolean useEncryption) {
        if (!isServer) {
            try {
                LocalDevice.getLocalDevice().setDiscoverable(inquiryAccessCode);
            } catch (BluetoothStateException e) {
                String msg = "Error changing inquiry access type: " + e.getMessage();
                ErrorScreen.showError(msg, settingsList);
            }
        }
        if (isServer) {
            serverUseAuthentication = useAuthentication;
            serverUseEncryption = useEncryption;
            serviceDiscoveryList = new ServiceDiscoveryList(this, UUID, inquiryAccessCode);
            Display.getDisplay(this).setCurrent(serviceDiscoveryList);
        } else {
            clientForm = new ClientForm(this);
            String url = SERVICE_URL;
            if (useAuthentication) {
                url += ";authenticate=true";
            } else {
                url += ";authenticate=false";
            }
            if (useAuthorization) {
                url += ";authorize=true";
            } else {
                url += ";authorize=false";
            }
            if (useEncryption) {
                url += ";encrypt=true";
            } else {
                url += ";encrypt=false";
            }
            url += ";name=btsppEcho";
            ConnectionService = new ConnectionService(url, clientForm);
            Display.getDisplay(this).setCurrent(clientForm);
        }
    }

    public void settingsListPropertiesRequest() {
        String[] keys = { "bluetooth.api.version", "bluetooth.connected.devices.max", "bluetooth.connected.inquiry", "bluetooth.connected.inquiry.scan", "bluetooth.connected.page", "bluetooth.connected.page.scan", "bluetooth.l2cap.receiveMTU.max", "bluetooth.master.switch", "bluetooth.sd.attr.retrievable.max", "bluetooth.sd.trans.max" };
        String str = "";
        try {
            str = "my bluetooth address: " + LocalDevice.getLocalDevice().getBluetoothAddress() + "\n";
        } catch (BluetoothStateException e) {
        }
        for (int i = 0; i < keys.length; i++) {
            str += keys[i] + ": " + LocalDevice.getProperty(keys[i]) + "\n";
        }
        TextScreen textScreen = new TextScreen(this, settingsList, "Device properties", str, "Back");
        Display.getDisplay(this).setCurrent(textScreen);
    }

    public void settingsListExitRequest() {
        exit();
    }

    public void serviceDiscoveryListFatalError(String errorMessage) {
        ErrorScreen.showError(errorMessage, serviceDiscoveryList);
        Display.getDisplay(this).setCurrent(settingsList);
    }

    public void serviceDiscoveryListError(String errorMessage) {
        ErrorScreen.showError(errorMessage, serviceDiscoveryList);
    }

    public void serviceDiscoveryListOpen(Vector selectedServiceRecords) {
        int security;
        if (serverUseAuthentication) {
            if (serverUseEncryption) {
                security = ServiceRecord.AUTHENTICATE_ENCRYPT;
            } else {
                security = ServiceRecord.AUTHENTICATE_NOENCRYPT;
            }
        } else {
            security = ServiceRecord.NOAUTHENTICATE_NOENCRYPT;
        }
        if (serverForm == null) {
            serverForm = new ServerForm(this);
        }
        serverForm.makeConnections(selectedServiceRecords, security);
        Display.getDisplay(this).setCurrent(serverForm);
    }

    public void serviceDiscoveryListExitRequest() {
        exit();
    }

    public void serviceDiscoveryListBackRequest(Displayable next) {
        Display.getDisplay(this).setCurrent(next);
    }

    public void serviceDiscoveryListViewLog(Displayable next) {
        LogScreen logScreen = new LogScreen(this, next, "Log", "Back");
        Display.getDisplay(this).setCurrent(logScreen);
    }

    public void textScreenClosed(Displayable next) {
        Display.getDisplay(this).setCurrent(next);
    }

    public void logScreenClosed(Displayable next) {
        Display.getDisplay(this).setCurrent(next);
    }

    public void serverFormSearchRequest(int numConnectionsOpen) {
        serviceDiscoveryList.init(numConnectionsOpen);
        if (numConnectionsOpen > 0) {
            serviceDiscoveryList.addBackCommand(serverForm);
        }
        Display.getDisplay(this).setCurrent(serviceDiscoveryList);
    }

    public void serverFormExitRequest() {
        exit();
    }

    public void serverFormAddConnection(Vector alreadyOpen) {
        serviceDiscoveryList.remove(alreadyOpen);
        serviceDiscoveryList.addBackCommand(serverForm);
        Display.getDisplay(this).setCurrent(serviceDiscoveryList);
    }

    public void serverFormViewLog() {
        LogScreen logScreen = new LogScreen(this, serverForm, "Log", "Back");
        Display.getDisplay(this).setCurrent(logScreen);
    }
}
