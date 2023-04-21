package com.ibm.celldt.environment.cellsimulator.core.common;

import java.util.HashMap;
import java.util.Map;
import com.ibm.celldt.environment.EnvironmentPlugin;
import com.ibm.celldt.environment.cellsimulator.conf.CommonDefaultValues;
import com.ibm.celldt.ui.swt.ComboGroupItem;
import com.ibm.celldt.utils.verification.ControlAttributes;

/**
 * Defines configuration attributes to build the target configuration from an
 * attribute hash map. The attributes are divided into three groups. One that
 * are specific how to launch the simulator, another that are related to the
 * connection from the environment to the simulator.
 * 
 * Two maps are used. The {@link #currentMap} contains entries for attributes
 * that were provided by the user.
 * The {@link #defaultMap} contains default valid entries for all attributes
 * and is used for fallback when the entry in {@link #currentMap} is missing
 * or is damaged.
 * 
 * @author Daniel Felix Ferber
 * @since 1.0
 */
public abstract class CommonConfigurationBean {

    protected Map currentMap = null;

    protected Map defaultMap = null;

    protected ControlAttributes attributes = null;

    public CommonConfigurationBean() {
        createDefaultMap();
        createCurrentMapFromPreferences();
        attributes = new ControlAttributes(currentMap, defaultMap);
    }

    public CommonConfigurationBean(Map newMap) {
        createDefaultMap();
        if (newMap == null) {
            createCurrentMapFromPreferences();
        } else {
            currentMap = new HashMap(newMap);
        }
        attributes = new ControlAttributes(currentMap, defaultMap);
    }

    public Map getMap() {
        return currentMap;
    }

    public ControlAttributes getAttributes() {
        return attributes;
    }

    protected void createDefaultMap() {
        defaultMap = new HashMap();
        defaultMap.put(ATTR_SIMULATOR_BASE_DIRECTORY, CommonDefaultValues.SIMULATOR_BASE_DIRECTORY);
        defaultMap.put(ATTR_ARCHITECTURE_ID, CommonDefaultValues.ARCHITECTURE_ID);
        defaultMap.put(ATTR_MEMORY_SIZE, CommonDefaultValues.MEMORY_SIZE);
        defaultMap.put(ATTR_PROFILE_ID, CommonDefaultValues.PROFILE_ID);
        defaultMap.put(ATTR_EXTRA_COMMAND_LINE_SWITCHES, CommonDefaultValues.EXTRA_COMMAND_LINE_SWITCHES);
        defaultMap.put(ATTR_AUTOMATIC_AUTHENTICATION, CommonDefaultValues.AUTOMATIC_AUTHENTICATION);
        defaultMap.put(ATTR_USERNAME, CommonDefaultValues.USERNAME);
        defaultMap.put(ATTR_PASSWORD, CommonDefaultValues.PASSWORD);
        defaultMap.put(ATTR_TIMEOUT, CommonDefaultValues.TIMEOUT);
        defaultMap.put(ATTR_SIMULATOR_CIPHER_TYPE, AbstractTargetControl.DEFAULT_SIMULATOR_CIPHER);
        defaultMap.put(ATTR_EXTRA_IMAGE_INIT, CommonDefaultValues.EXTRA_IMAGE_INIT);
        defaultMap.put(ATTR_EXTRA_IMAGE_PATH, CommonDefaultValues.EXTRA_IMAGE_PATH);
        defaultMap.put(ATTR_EXTRA_IMAGE_PERSISTENCE, CommonDefaultValues.EXTRA_IMAGE_PERSISTENCE);
        defaultMap.put(ATTR_EXTRA_IMAGE_JOURNAL_PATH, CommonDefaultValues.EXTRA_IMAGE_JOURNAL_PATH);
        defaultMap.put(ATTR_EXTRA_IMAGE_TYPE, CommonDefaultValues.EXTRA_IMAGE_TYPE);
        defaultMap.put(ATTR_EXTRA_IMAGE_MOUNTPOINT, CommonDefaultValues.EXTRA_IMAGE_MOUNTPOINT);
        defaultMap.put(ATTR_KERNEL_IMAGE_PATH, CommonDefaultValues.KERNEL_IMAGE_PATH);
        defaultMap.put(ATTR_ROOT_IMAGE_PATH, CommonDefaultValues.ROOT_IMAGE_PATH);
        defaultMap.put(ATTR_ROOT_IMAGE_PERSISTENCE, CommonDefaultValues.ROOT_IMAGE_PERSISTENCE);
        defaultMap.put(ATTR_ROOT_IMAGE_JOURNAL_PATH, CommonDefaultValues.ROOT_IMAGE_JOURNAL_PATH);
        defaultMap.put(ATTR_CUSTOMIZATION_SCRIPT, CommonDefaultValues.CUSTOMIZATION_SCRIPT);
    }

    protected void createCurrentMapFromPreferences() {
        currentMap = new HashMap();
    }

    public static final String ATTR_SIMULATOR_BASE_DIRECTORY = "simulator-base-directory";

    public static final String ATTR_ARCHITECTURE_ID = "architecture-id";

    public static final String ATTR_MEMORY_SIZE = "memory-size";

    public static final String ATTR_PROFILE_ID = "profile-id";

    public static final String ATTR_EXTRA_COMMAND_LINE_SWITCHES = "extra-command-line-switches";

    public static final String ATTR_AUTOMATIC_AUTHENTICATION = "automatic-authentication";

    public static final String ATTR_USERNAME = "username";

    public static final String ATTR_PASSWORD = "password";

    public static final String ATTR_TIMEOUT = "timeout";

    public static final String ATTR_SIMULATOR_CIPHER_TYPE = "simulator-cipher-type";

    public static final String ATTR_EXTRA_IMAGE_INIT = "extra-image-init";

    public static final String ATTR_EXTRA_IMAGE_PATH = "extra-image-path";

    public static final String ATTR_EXTRA_IMAGE_PERSISTENCE = "extra-image-persistence";

    public static final String ATTR_EXTRA_IMAGE_JOURNAL_PATH = "extra-image-journal-path";

    public static final String ATTR_EXTRA_IMAGE_TYPE = "extra-image-type";

    public static final String ATTR_EXTRA_IMAGE_MOUNTPOINT = "extra-image-mountpoint";

    public static final String ATTR_KERNEL_IMAGE_PATH = "kernel-image-path";

    public static final String ATTR_ROOT_IMAGE_PATH = "root-image-path";

    public static final String ATTR_ROOT_IMAGE_PERSISTENCE = "root-image-persistence";

    public static final String ATTR_ROOT_IMAGE_JOURNAL_PATH = "root-image-journal-path";

    public static final String ATTR_CUSTOMIZATION_SCRIPT = "customization-script";

    public static final String ATTR_WORK_DIRECTORY = "work-directory";

    public static final String ATTR_SHOW_SIMULATOR_GUI = "show-simulator-gui";

    public static final String ATTR_CONSOLE_SHOW_LINUX = "console-show-linux";

    public static final String ATTR_CONSOLE_SHOW_SIMULATOR = "console-show-simulator";

    public static final String ATTR_AUTOMATIC_NETWORK = "automatic-network";

    public static final String ATTR_IP_HOST = "ip-host";

    public static final String ATTR_IP_SIMULATOR = "ip-simulator";

    public static final String ATTR_MAC_SIMULATOR = "mac-simulator";

    public static final String ATTR_AUTOMATIC_PORTCONFIG = "automatic-portconfig";

    public static final String ATTR_JAVA_API_SOCKET_PORT = "java-api-socket-port";

    public static final String ATTR_CONSOLE_SOCKET_PORT = "console-socket-port";

    public static final String ATTR_SYSTEM_WORKSPACE = "system-workspace-dir";

    public static final String ID_PERSISTENCE_DISCARD = "discard";

    public static final String ID_PERSISTENCE_WRITE = "write";

    public static final String ID_PERSISTENCE_JOURNAL = "journal";

    public static final String ID_FILESYSTEM_EXT3 = "ext3";

    public static final String ID_FILESYSTEM_EXT2 = "ext2";

    public static final String ID_FILESYSTEM_ISO9660 = "iso9660";

    protected static final String[] SPECIFIC_KEY_ARRAY = { ATTR_SIMULATOR_BASE_DIRECTORY, ATTR_ARCHITECTURE_ID, ATTR_MEMORY_SIZE, ATTR_PROFILE_ID, ATTR_EXTRA_COMMAND_LINE_SWITCHES, ATTR_AUTOMATIC_AUTHENTICATION, ATTR_USERNAME, ATTR_TIMEOUT, ATTR_SIMULATOR_CIPHER_TYPE, ATTR_EXTRA_IMAGE_INIT, ATTR_EXTRA_IMAGE_PATH, ATTR_EXTRA_IMAGE_PERSISTENCE, ATTR_EXTRA_IMAGE_JOURNAL_PATH, ATTR_EXTRA_IMAGE_TYPE, ATTR_EXTRA_IMAGE_MOUNTPOINT, ATTR_WORK_DIRECTORY, ATTR_SHOW_SIMULATOR_GUI, ATTR_CONSOLE_SHOW_LINUX, ATTR_CONSOLE_SHOW_SIMULATOR, ATTR_KERNEL_IMAGE_PATH, ATTR_ROOT_IMAGE_PATH, ATTR_ROOT_IMAGE_PERSISTENCE, ATTR_ROOT_IMAGE_JOURNAL_PATH, ATTR_CUSTOMIZATION_SCRIPT, ATTR_AUTOMATIC_NETWORK, ATTR_IP_HOST, ATTR_IP_SIMULATOR, ATTR_MAC_SIMULATOR, ATTR_AUTOMATIC_PORTCONFIG, ATTR_JAVA_API_SOCKET_PORT, ATTR_CONSOLE_SOCKET_PORT, ATTR_SYSTEM_WORKSPACE };

    protected static final String[] SPECIFIC_KEY_CIPHERED_ARRAY = { ATTR_PASSWORD };

    public abstract CommonConfigFactory createFactory();
}
