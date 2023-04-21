package org.sink.media;

import org.sink.exceptions.ConfigException;
import org.sink.Settings;
import java.util.*;
import javax.media.*;
import javax.media.format.*;

/**
 * @author Michael Salcher
 */
public class DeviceRegistry {

    Settings settings;

    private ArrayList videoDevices;

    private ArrayList audioDevices;

    private DeviceInformation selectedVideoDevice;

    private DeviceInformation selectedAudioDevice;

    public DeviceRegistry() throws ConfigException {
        settings = Settings.getSettings();
        videoDevices = readVideoDevicesFromFile();
        audioDevices = readAudioDevicesFromFile();
        selectedVideoDevice = readSelectedVideoDeviceFromFile();
        selectedAudioDevice = readSelectedAudioDeviceFromFile();
    }

    /**
     * Looks for available devices and stores them in the sink config file.
     * Devices might have to be registered with the JMF registry first to be found by this method.
     * 
     * @throws ConfigException
     */
    public void findDevices() throws ConfigException {
        videoDevices = new ArrayList();
        audioDevices = new ArrayList();
        Vector deviceListVector = CaptureDeviceManager.getDeviceList(null);
        for (int i = 0; i < deviceListVector.size(); i++) {
            CaptureDeviceInfo deviceInfo = (CaptureDeviceInfo) deviceListVector.elementAt(i);
            String deviceName = deviceInfo.getName();
            String deviceLocator = deviceInfo.getLocator().toString();
            Format deviceFormats[] = deviceInfo.getFormats();
            for (int j = 0; j < deviceFormats.length; j++) {
                Format currentFormat = deviceFormats[j];
            }
            if (deviceFormats.length > 0) {
                if (deviceFormats[0] instanceof VideoFormat) {
                    videoDevices.add(new DeviceInformation(deviceLocator, deviceName));
                } else if (deviceFormats[0] instanceof AudioFormat) {
                    audioDevices.add(new DeviceInformation(deviceLocator, deviceName));
                }
            }
        }
        saveDevicesToFile();
    }

    /**
     * Saves found devices to the sink config file.
     * 
     * @throws ConfigException
     */
    private void saveDevicesToFile() throws ConfigException {
        Iterator it;
        int i;
        StringBuffer videoDevicesCSV = new StringBuffer();
        StringBuffer audioDevicesCSV = new StringBuffer();
        it = videoDevices.iterator();
        i = 0;
        while (it.hasNext()) {
            if (i > 0) videoDevicesCSV.append(";");
            DeviceInformation di = (DeviceInformation) it.next();
            String deviceLocator = di.getLocator();
            String deviceName = di.getName();
            String merged = deviceLocator + "," + deviceName;
            videoDevicesCSV.append(merged);
            i++;
        }
        it = audioDevices.iterator();
        i = 0;
        while (it.hasNext()) {
            if (i > 0) audioDevicesCSV.append(";");
            DeviceInformation di = (DeviceInformation) it.next();
            String deviceLocator = di.getLocator();
            String deviceName = di.getName();
            String merged = deviceLocator + "," + deviceName;
            audioDevicesCSV.append(merged);
            i++;
        }
        settings.setProperty("videoDevices", videoDevicesCSV.toString());
        settings.setProperty("audioDevices", audioDevicesCSV.toString());
        settings.store();
    }

    /**
     * Reads availabe video devices from the sink config file.
     * 
     * The config file looks like this:
     * 
     *    videoDevices=locator1, name1; locator2, name2
     * 
     * Therefore 2 StringTokenizers have to be used:
     * stok to seperate each device via ";"
     * and stok2 to seperate locator from name via ","
     * 
     * @return ArrayList of DeviceInformation objects of available video devices.
     */
    private ArrayList readVideoDevicesFromFile() {
        ArrayList videoDevicesFromFile = new ArrayList();
        String videoDeviceLineFromFile = settings.getProperty("videoDevices");
        StringTokenizer stok = new StringTokenizer(videoDeviceLineFromFile, ";");
        while (stok.hasMoreElements()) {
            StringTokenizer stok2 = new StringTokenizer(stok.nextToken(), ",");
            for (int i = 0; i < stok2.countTokens(); i = i + 2) {
                videoDevicesFromFile.add(new DeviceInformation(stok2.nextToken(), stok2.nextToken()));
            }
        }
        return videoDevicesFromFile;
    }

    /**
     * Reads availabe audio devices from the sink config file.
     * 
     * The config file looks like this:
     * 
     *    audioDevices=locator1, name1; locator2, name2
     * 
     * Therefore 2 StringTokenizers have to be used:
     * stok to seperate each device via ";"
     * and stok2 to seperate locator from name via ","
     * 
     * @return ArrayList of DeviceInformation objects of available audio devices.
     */
    private ArrayList readAudioDevicesFromFile() {
        ArrayList audioDevicesFromFile = new ArrayList();
        String videoDeviceLineFromFile = settings.getProperty("audioDevices");
        StringTokenizer stok = new StringTokenizer(videoDeviceLineFromFile, ";");
        while (stok.hasMoreElements()) {
            StringTokenizer stok2 = new StringTokenizer(stok.nextToken(), ",");
            for (int i = 0; i < stok2.countTokens(); i = i + 2) {
                audioDevicesFromFile.add(new DeviceInformation(stok2.nextToken(), stok2.nextToken()));
            }
        }
        return audioDevicesFromFile;
    }

    /**
     * Reads the selected video device from the config file
     * 
     * @return DeviceInformation object of the selected video device
     */
    private DeviceInformation readSelectedVideoDeviceFromFile() {
        String selectedVideoDeviceLineFromFile = settings.getProperty("selectedVideoDevice");
        StringTokenizer stok = new StringTokenizer(selectedVideoDeviceLineFromFile, ",");
        DeviceInformation di = new DeviceInformation(stok.nextToken(), stok.nextToken());
        return di;
    }

    /**
     * Reads the selected audio device from the config file
     * 
     * @return DeviceInformation object of the selected audio device
     */
    private DeviceInformation readSelectedAudioDeviceFromFile() {
        String selectedVideoDeviceLineFromFile = settings.getProperty("selectedAudioDevice");
        StringTokenizer stok = new StringTokenizer(selectedVideoDeviceLineFromFile, ",");
        DeviceInformation di = new DeviceInformation(stok.nextToken(), stok.nextToken());
        return di;
    }

    /**
     * If audio device is enabled, this method searches through availabe audio devices
     * to see if the given on has a match. It then gets the name of that devices stores it as 
     * 'selected' in the config file.
     * If 'enabled' is false, it will set 'none, none' as 'selectedAudioDevice'
     * 
     * @param locator the locator of the device to be selected
     * @param enabled is this device enabled?
     * 
     * @throws SinkDeviceNotFoundException of the specified locator is not an availabe device.
     * @throws ConfigException if the config file cannot be read or written.
     */
    public void selectAudioDevice(String locator, boolean enabled) throws SinkDeviceNotFoundException, ConfigException {
        if (enabled == true) {
            boolean found = false;
            for (int i = 0; i < audioDevices.size(); i++) {
                DeviceInformation di = (DeviceInformation) audioDevices.get(i);
                if (di.getLocator().equals(locator)) {
                    found = true;
                    settings.setProperty("selectedAudioDevice", di.getLocator() + "," + di.getName());
                    settings.store();
                    break;
                }
            }
            if (found == false) {
                throw new SinkDeviceNotFoundException(locator);
            }
        } else {
            settings.setProperty("selectedAudioDevice", "none,none");
            settings.store();
        }
    }

    /**
     * If video device is enabled, this method searches through availabe video devices
     * to see if the given on has a match. It then gets the name of that devices stores it as 
     * 'selected' in the config file.
     * If 'enabled' is false, it will set 'none, none' as 'selectedVideoDevice'
     * 
     * @param locator the locator of the device to be selected
     * @param enabled is this device enabled?
     * 
     * @throws SinkDeviceNotFoundException of the specified locator is not an availabe device.
     * @throws ConfigException if the config file cannot be read or written.
     */
    public void selectVideoDevice(String locator, boolean enabled) throws SinkDeviceNotFoundException, ConfigException {
        if (enabled == true) {
            boolean found = false;
            for (int i = 0; i < videoDevices.size(); i++) {
                DeviceInformation di = (DeviceInformation) videoDevices.get(i);
                if (di.getLocator().equals(locator)) {
                    found = true;
                    settings.setProperty("selectedVideoDevice", di.getLocator() + "," + di.getName());
                    settings.store();
                    break;
                }
            }
            if (found == false) {
                throw new SinkDeviceNotFoundException(locator);
            }
        } else {
            settings.setProperty("selectedVideoDevice", "none,none");
            settings.store();
        }
    }

    /**
     * @return returns the selected video device name
     */
    public String getSelectedVideoDeviceName() {
        selectedVideoDevice = readSelectedVideoDeviceFromFile();
        return selectedVideoDevice.getName();
    }

    /**
     * @return returns the selected video device locator
     */
    public String getSelectedVideoDeviceLocator() {
        selectedVideoDevice = readSelectedVideoDeviceFromFile();
        return selectedVideoDevice.getLocator();
    }

    /**
     * @return returns the selected audio device name
     */
    public String getSelectedAudioDeviceName() {
        selectedAudioDevice = readSelectedAudioDeviceFromFile();
        return selectedAudioDevice.getName();
    }

    /**
     * @return returns the selected audio device locator
     */
    public String getSelectedAudioDeviceLocator() {
        selectedAudioDevice = readSelectedAudioDeviceFromFile();
        return selectedAudioDevice.getLocator();
    }

    /**
     * Returns all info about the config file.
     * 
     * @return a string which holds info about the config file.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Video devices:\n\n");
        for (int i = 0; i < videoDevices.size(); i++) {
            sb.append(((DeviceInformation) videoDevices.get(i)).toString() + "\n");
        }
        sb.append("\naudio devices:\n\n");
        for (int i = 0; i < audioDevices.size(); i++) {
            sb.append(((DeviceInformation) audioDevices.get(i)).toString() + "\n");
        }
        sb.append("\nselected video device: " + selectedVideoDevice + "\n");
        sb.append("\nselected audio device: " + selectedAudioDevice + "\n");
        return sb.toString();
    }

    /**
     * @return an ArrayList of DeviceInformation objects
     */
    public ArrayList getAudioDevices() {
        return audioDevices;
    }

    /**
     * @return an ArrayList of DeviceInformation objects
     */
    public ArrayList getVideoDevices() {
        return videoDevices;
    }
}
