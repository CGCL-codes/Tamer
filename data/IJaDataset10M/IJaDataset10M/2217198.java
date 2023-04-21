package com.nithinphilips.wifimusicsync;

import java.util.Enumeration;
import javax.microedition.io.file.FileSystemRegistry;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.GridFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.component.StandardTitleBar;
import org.openqa.selenium.remote.me.util.StringUtils;
import com.nithinphilips.UiFactory;
import com.nithinphilips.wifimusicsync.components.WifiMusicSyncProperties;

public class SettingsScreen extends MainScreen {

    private EditField serverIpEdit;

    private EditField serverPortEdit;

    private ObjectChoiceField localStoreType;

    private WifiMusicSyncProperties optionProperties;

    private String sdCardLabel = "SD Card";

    private String deviceStoreLabel = "Device Memory";

    public SettingsScreen() {
        super(VERTICAL_SCROLL);
        StandardTitleBar _titleBar = new StandardTitleBar();
        _titleBar.addTitle("Options");
        _titleBar.addNotifications();
        _titleBar.addSignalIndicator();
        this.setTitle(_titleBar);
        this.setTitle(new LabelField("Options"));
        ((VerticalFieldManager) getMainManager()).setBackground(BackgroundFactory.createSolidBackground(UiFactory.COLOR_SCREEN_BACKGROUND));
        populateMainScreen(this);
        serverIpEdit.setFocus();
    }

    public void populateMainScreen(Manager mainScreen) {
        mainScreen.setBackground(BackgroundFactory.createSolidBackground(UiFactory.COLOR_SCREEN_BACKGROUND));
        optionProperties = WifiMusicSyncProperties.fetch();
        serverIpEdit = UiFactory.createEditField(optionProperties.getServerIp(), 0);
        serverPortEdit = UiFactory.createNumericEditField(optionProperties.getServerPort());
        boolean hasSDCard = false;
        String root = null;
        Enumeration e = FileSystemRegistry.listRoots();
        while (e.hasMoreElements()) {
            root = (String) e.nextElement();
            if (root.equalsIgnoreCase("sdcard/")) {
                hasSDCard = true;
                break;
            }
        }
        if (hasSDCard) localStoreType = new ObjectChoiceField("", new String[] { deviceStoreLabel, sdCardLabel }, optionProperties.getLocalStoreType(), Field.FIELD_LEFT); else localStoreType = new ObjectChoiceField("", new String[] { deviceStoreLabel }, 0, Field.USE_ALL_WIDTH);
        if ((!hasSDCard) && optionProperties.getLocalStoreType() == WifiMusicSyncProperties.LOCAL_STORE_TYPE_SDCARD) localStoreType.setDirty(true);
        VerticalFieldManager serverGroup = UiFactory.createVerticalFieldGroup("Server Settings");
        GridFieldManager serverGrid = new GridFieldManager(2, 2, GridFieldManager.AUTO_SIZE);
        serverGrid.setColumnProperty(1, GridFieldManager.AUTO_SIZE, 100);
        serverGrid.add(new LabelField("IP:"), GridFieldManager.FIELD_RIGHT | GridFieldManager.NON_FOCUSABLE);
        serverGrid.add(serverIpEdit);
        serverGrid.add(new LabelField("Port:"), GridFieldManager.FIELD_RIGHT | GridFieldManager.NON_FOCUSABLE);
        serverGrid.add(serverPortEdit);
        serverGroup.add(serverGrid);
        VerticalFieldManager storageGroup = UiFactory.createVerticalFieldGroup("Media Storage");
        storageGroup.add(new LabelField("Select the location to store downloaded media:"));
        storageGroup.add(localStoreType);
        VerticalFieldManager deviceIdGroup = UiFactory.createVerticalFieldGroup("Device ID");
        deviceIdGroup.add(new LabelField("Sync Client ID: " + optionProperties.getClientId()));
        deviceIdGroup.add(new LabelField("This string uniquely identifies your device."));
        mainScreen.add(serverGroup);
        mainScreen.add(storageGroup);
        mainScreen.add(deviceIdGroup);
    }

    public boolean isDataValid() {
        boolean result = true;
        if (StringUtils.isEmpty(serverIpEdit.getText())) {
            Dialog.alert("The Server IP you entered is not valid.");
            result = false;
        } else if (StringUtils.isEmpty(serverPortEdit.getText())) {
            Dialog.alert("The Server Port you entered is not valid.");
            result = false;
        }
        return result;
    }

    public void save() {
        String storageChoice = (String) localStoreType.getChoice(localStoreType.getSelectedIndex());
        optionProperties.setServerIp(serverIpEdit.getText());
        optionProperties.setServerPort(serverPortEdit.getText());
        if (storageChoice.equals(deviceStoreLabel)) optionProperties.setLocalStoreRoot(WifiMusicSyncProperties.LOCAL_STORE_MEMORY_PATH); else if (storageChoice.equals(sdCardLabel)) optionProperties.setLocalStoreRoot(WifiMusicSyncProperties.LOCAL_STORE_SD_PATH);
        optionProperties.save();
        optionProperties = null;
    }
}
