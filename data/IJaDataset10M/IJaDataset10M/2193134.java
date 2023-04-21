package com.adhoco.hardware.osgi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import org.w3c.dom.Node;
import com.adhoco.system.Settings;

/**
 * This class is an enumeration of all possible hardware profiles as used by the controller. 
 * The corresponding profile names is e.g. used when serializing to XML
 *
 *@author Philipp Buluschek
 *
 */
public class ControlProfile implements XMLSerializable {

    private final String profileName;

    private final String resourceKey;

    private final HardwareType type;

    private static final HashMap correspondanceMap = new HashMap();

    private ControlProfile(String profileName, String resourceKey, HardwareType type) {
        this.profileName = profileName;
        this.type = type;
        this.resourceKey = resourceKey;
        correspondanceMap.put(profileName, this);
    }

    public String toString() {
        return resourceKey;
    }

    public static final ControlProfile DANGEROUS_APPLIANCE = new ControlProfile("com.adhoco.controls.DangerousAppliance", "Appliance", HardwareType.BINARY_OUTPUT);

    public static final ControlProfile BLIND_LEVEL = new ControlProfile("com.adhoco.controls.BlindLevel", "Blind", HardwareType.ANALOG_OUTPUT);

    public static final ControlProfile TEXTILE_BLIND_LEVEL = new ControlProfile("com.adhoco.controls.TextileBlindLevel", "TextileBlind", HardwareType.ANALOG_OUTPUT);

    public static final ControlProfile VENETIAN_BLIND_LEVEL = new ControlProfile("com.adhoco.controls.VenetianBlindLevel", "VenetianLevel", HardwareType.ANALOG_OUTPUT);

    public static final ControlProfile VENETIAN_BLIND_SLAT = new ControlProfile("com.adhoco.controls.VenetianBlindSlat", "VenetianSlat", HardwareType.ANALOG_OUTPUT);

    public static final ControlProfile DOOR_CONTACT = new ControlProfile("com.adhoco.controls.DoorContact", "DoorContact", HardwareType.BINARY_INPUT);

    public static final ControlProfile FLOOR_HEATING = new ControlProfile("com.adhoco.controls.FloorHeating", "FloorHeating", HardwareType.BINARY_OUTPUT);

    public static final ControlProfile FLOOR_HEATING_SWITCH = new ControlProfile("com.adhoco.controls.FloorHeatingSwitch", "FloorHeatingSwitch", HardwareType.ANALOG_INPUT);

    public static final ControlProfile VALVE_HEATING = new ControlProfile("com.adhoco.controls.ValveHeating", "Heating", HardwareType.ANALOG_OUTPUT);

    public static final ControlProfile HUMIDITY = new ControlProfile("com.adhoco.controls.Humidity", "Humidity", HardwareType.ANALOG_INPUT);

    public static final ControlProfile ILLUMINANCE = new ControlProfile("com.adhoco.controls.Illuminance", "Illuminance", HardwareType.ANALOG_INPUT);

    public static final ControlProfile DIMMER_LAMP = new ControlProfile("com.adhoco.controls.DimmerLamp", "DimmerLamp", HardwareType.ANALOG_OUTPUT);

    public static final ControlProfile SWITCH_LAMP = new ControlProfile("com.adhoco.controls.SwitchLamp", "SwitchLamp", HardwareType.BINARY_OUTPUT);

    public static final ControlProfile OCCUPANCY = new ControlProfile("com.adhoco.controls.Occupancy", "Occupancy", HardwareType.BINARY_INPUT);

    public static final ControlProfile HUMIDITY_SELECTOR = new ControlProfile("com.adhoco.controls.HumiditySelector", "HumiSelector", HardwareType.ANALOG_OUTPUT);

    public static final ControlProfile TEMPERATURE_SELECTOR = new ControlProfile("com.adhoco.controls.TemperatureSelector", "TempSelector", HardwareType.ANALOG_OUTPUT);

    public static final ControlProfile THERMOMETER = new ControlProfile("com.adhoco.controls.Thermometer", "Thermometer", HardwareType.ANALOG_INPUT);

    public static final ControlProfile OUTDOOR_THERMOMETER = new ControlProfile("com.adhoco.controls.OutdoorThermometer", "OutdoorThermometer", HardwareType.ANALOG_INPUT);

    public static final ControlProfile OUTDOOR_ILLUMINANCE = new ControlProfile("com.adhoco.controls.OutdoorIlluminance", "OutdoorIlluminance", HardwareType.ANALOG_INPUT);

    public static final ControlProfile OUTDOOR_HUMIDITY = new ControlProfile("com.adhoco.controls.OutdoorHumidity", "OutdoorHumidity", HardwareType.ANALOG_INPUT);

    public static final ControlProfile WINDOW_CONTACT = new ControlProfile("com.adhoco.controls.WindowContact", "WindowContact", HardwareType.BINARY_INPUT);

    public static final ControlProfile WINDOW_ACTUATOR = new ControlProfile("com.adhoco.controls.WindowActuator", "WindowActuator", HardwareType.ANALOG_OUTPUT);

    public static final ControlProfile WIND_SPEED = new ControlProfile("com.adhoco.controls.WindSpeed", "WindSpeed", HardwareType.ANALOG_INPUT);

    public static final ControlProfile POWER_METER = new ControlProfile("com.adhoco.controls.PowerMeter", "PowerMeter", HardwareType.ANALOG_INPUT);

    public static final ControlProfile STANDBY_POWER_SWITCH = new ControlProfile("com.adhoco.controls.StandbyPowerSwitch", "StandbyPowerSwitch", HardwareType.BINARY_OUTPUT);

    public static final ControlProfile STANDBY_POWER_INFO = new ControlProfile("com.adhoco.controls.StandbyPowerInfo", "StandbyPowerInfo", HardwareType.ANALOG_INPUT);

    public static final ControlProfile ALARM = new ControlProfile("com.adhoco.controls.Alarm", "Alarm", HardwareType.BINARY_OUTPUT);

    public static final ControlProfile REMOTE_CONTROL_AUTOMATION = new ControlProfile("com.adhoco.controls.RemoteControlAutomation", "RemoteControl", HardwareType.ANALOG_OUTPUT);

    public static final ControlProfile VIRTUAL_HARDWARE = new ControlProfile("com.adhoco.controls.VirtualHardware", "VirtualHardware", HardwareType.BINARY_INPUT);

    public static final ControlProfile PHYSICAL_DEVICE = new ControlProfile("com.adhoco.other.PhysicalDevice", "PhysicalDevice", HardwareType.PHYSICAL_DEVICE);

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ControlProfile)) return false;
        if (((ControlProfile) obj).getProfileName().equals(profileName)) return true; else return false;
    }

    public HardwareType getHardwareType() {
        return type;
    }

    /**
	 * Returns the corresponding {@link ControlProfile} for a given profileName
	 * or null if no {@link ControlProfile} corresponds to this profileName.
	 * 
	 */
    public static ControlProfile byProfileName(String inProfileName) {
        return (ControlProfile) correspondanceMap.get(inProfileName);
    }

    /**
	 * Use this to get an int representation for switch statements
	 * 
	 * Note: Collisions (ie. different profileName but same hashCode) are
	 * possible but very unlikely. As the string doesn't change, this
	 * should not be a problem after an initial test.
	 */
    public int hashCode() {
        return profileName.hashCode();
    }

    /**
	 * Returns a {@link Set} of all control profiles.
	 * @return a Set of all {@link ControlProfile}s
	 */
    public static Set getAllControlProfiles() {
        Set out = new HashSet();
        out.addAll(correspondanceMap.values());
        return out;
    }

    public String getLocalizedResourceKey() {
        return resourceKey;
    }

    public String getLocalizedRepresentation() {
        ResourceBundle messages = ResourceBundle.getBundle("com.adhoco.system.SystemMessages", Settings.currentLocale);
        return messages.getString(resourceKey);
    }

    public String getProfileName() {
        return profileName;
    }

    public static void main(String[] args) {
        for (Iterator it = getAllControlProfiles().iterator(); it.hasNext(); ) {
            System.out.println(it.next());
        }
    }

    public static final String TAG_BASE = "n";

    public void serializeTo(Node root) {
        serializeTo(HardwareObjectMemento.getXmlHelper(), TAG_BASE, root);
    }

    public void serializeTo(XMLHelper helper, String baseTag, Node root) {
        helper.serializeTo(profileName, baseTag, root);
    }

    public static ControlProfile deserialize(Node base) {
        return deserialize(HardwareObjectMemento.getXmlHelper(), TAG_BASE, base);
    }

    public static ControlProfile deserialize(XMLHelper helper, String baseTag, Node base) {
        if (!helper.nodesAreEqual(base, baseTag)) {
            throw new IllegalArgumentException("Bad Element. Expected: " + helper.obfuscate(baseTag) + "  Got: " + base);
        }
        String name = helper.deserializeStringFrom(base);
        ControlProfile out = ControlProfile.byProfileName(name);
        return out;
    }
}
