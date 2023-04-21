package example.btsppecho;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.bluetooth.DiscoveryAgent;

class SettingsList extends List implements CommandListener {

    private static final String SERVER = "Server";

    private static final String CLIENT = "Client";

    private static final String AUTHENTICATION_TRUE = "Authen.: true";

    private static final String AUTHENTICATION_FALSE = "Authen.: false";

    private static final String AUTHORIZATION_TRUE = "Authoriz.: true";

    private static final String AUTHORIZATION_FALSE = "Authoriz.: false";

    private static final String ENCRYPTION_TRUE = "Encrypt: true";

    private static final String ENCRYPTION_FALSE = "Encrypt: false";

    private static final String RETRIEVE_DEVICES_TRUE = "Use known devices";

    private static final String RETRIEVE_DEVICES_FALSE = "Use inquiry";

    private static final String INQUIRY_TYPE_LIAC = "LIAC";

    private static final String INQUIRY_TYPE_GIAC = "GIAC";

    private static final String INQUIRY_TYPE_NOT_DISCOVERABLE = "not discoverable";

    private static final String INQUIRY_TYPE_CACHED = "cached";

    private static final String INQUIRY_TYPE_PREKNOWN = "preknown";

    private int inquiryType;

    private int protocol;

    private boolean isServer;

    private boolean useAuthorization;

    private boolean useAuthentication;

    private boolean useEncryption;

    private final MIDletApplication midlet;

    private final Command startCommand;

    private final Command propCommand;

    private final Command exitCommand;

    SettingsList(MIDletApplication midlet) {
        super("Settings", List.IMPLICIT);
        this.midlet = midlet;
        inquiryType = DiscoveryAgent.LIAC;
        isServer = false;
        useAuthentication = false;
        useEncryption = false;
        useAuthorization = false;
        updateListElements();
        startCommand = new Command("Start application", Command.SCREEN, 0);
        propCommand = new Command("BT properties", Command.SCREEN, 1);
        exitCommand = new Command("Exit", Command.EXIT, 0);
        addCommand(startCommand);
        addCommand(propCommand);
        addCommand(exitCommand);
        setCommandListener(this);
    }

    private void updateListElements() {
        while (size() > 0) {
            delete(0);
        }
        String string;
        if (isServer) {
            string = SERVER;
        } else {
            string = CLIENT;
        }
        append(string, null);
        if (inquiryType == DiscoveryAgent.LIAC) {
            append(makeInquiryLabel(isServer, INQUIRY_TYPE_LIAC), null);
        } else if (inquiryType == DiscoveryAgent.GIAC) {
            append(makeInquiryLabel(isServer, INQUIRY_TYPE_GIAC), null);
        } else if (inquiryType == DiscoveryAgent.PREKNOWN) {
            append(makeInquiryLabel(isServer, INQUIRY_TYPE_PREKNOWN), null);
        } else if (inquiryType == DiscoveryAgent.CACHED) {
            append(makeInquiryLabel(isServer, INQUIRY_TYPE_CACHED), null);
        } else if (inquiryType == DiscoveryAgent.NOT_DISCOVERABLE) {
            append(makeInquiryLabel(isServer, INQUIRY_TYPE_CACHED), null);
        }
        if (useAuthentication) {
            append(AUTHENTICATION_TRUE, null);
            if (useEncryption) {
                append(ENCRYPTION_TRUE, null);
            } else {
                append(ENCRYPTION_FALSE, null);
            }
            if (!isServer) {
                if (useAuthorization) {
                    append(AUTHORIZATION_TRUE, null);
                } else {
                    append(AUTHORIZATION_FALSE, null);
                }
            }
        } else {
            useAuthentication = false;
            useEncryption = false;
            append(AUTHENTICATION_FALSE, null);
        }
    }

    private String makeInquiryLabel(boolean searching, String string) {
        if (searching) {
            return "Discover: " + string;
        } else {
            return "Discoverable: " + string;
        }
    }

    public void commandAction(Command command, Displayable d) {
        if (command == startCommand) {
            midlet.settingsListStart(isServer, inquiryType, useAuthentication, useAuthorization, useEncryption);
        } else if (command == propCommand) {
            midlet.settingsListPropertiesRequest();
        } else if (command == exitCommand) {
            midlet.settingsListExitRequest();
        } else if (command == List.SELECT_COMMAND) {
            int index = getSelectedIndex();
            switch(index) {
                case 0:
                    isServer = !isServer;
                    break;
                case 1:
                    if (inquiryType == DiscoveryAgent.LIAC) {
                        inquiryType = DiscoveryAgent.GIAC;
                    } else {
                        inquiryType = DiscoveryAgent.LIAC;
                    }
                    break;
                case 2:
                    useAuthentication = !useAuthentication;
                    if (!useAuthentication) {
                        if (size() == 5) {
                            delete(4);
                            useAuthorization = false;
                        }
                        this.delete(3);
                        useEncryption = false;
                    }
                    break;
                case 3:
                    useEncryption = !useEncryption;
                    break;
                case 4:
                    useAuthorization = !useAuthorization;
                    break;
            }
            updateListElements();
            setSelectedIndex(index, true);
        }
    }
}
