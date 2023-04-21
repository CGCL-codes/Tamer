package net.leibman.sprinko.config;

import java.io.Serializable;

/**
 * Defines constants for standard properties names and values used to create
 * SprinkoEntries...
 * @since 1.3 (Berlin 2K meeting)
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public interface SprinkoEntryConst extends Serializable {

    public static final String LOGICAL_NAME_DEFAULT_PROP_VALUE = "UnknownDevice";

    public static final String SI_FACTORY_CLASS_DEFAULT_PROP_VALUE = "UnknownServiceInstanceFactoryClass";

    public static final String SERVICE_CLASS_DEFAULT_PROP_VALUE = "UnknownServiceClass";

    public static final String DEVICE_CATEGORY_DEFAULT_PROP_VALUE = "CashDrawer";

    public static final String VERSION_DEFAULT_PROP_VALUE = "1.5";

    public static final String VENDOR_NAME_DEFAULT_PROP_VALUE = "Unknown Vendor Name";

    public static final String VENDOR_URL_DEFAULT_PROP_VALUE = "http://www.UnknownVerdorURL.com";

    public static final String PRODUCT_NAME_DEFAULT_PROP_VALUE = "Unknown Product Name";

    public static final String PRODUCT_URL_DEFAULT_PROP_VALUE = "http://www.UnknownProductURL.com";

    public static final String PRODUCT_DESCRIPTION_DEFAULT_PROP_VALUE = "Unknown Product Description";

    /** @return an array of all compatible JavaPOS version values */
    public static final String[] VERSION_PROPS = { "1.2", "1.3", "1.4", "1.5", "1.6", "1.7" };

    /** Required property indicating the BUS used for this SprinkoEntry */
    public static final String DEVICE_BUS_PROP_NAME = "deviceBus";

    /** Property value for deviceBus for RS232 */
    public static final String RS232_DEVICE_BUS = "RS232";

    /** Property value for deviceBus for standard Parallel ports */
    public static final String PARALLEL_DEVICE_BUS = "Parallel";

    /** Property value for deviceBus for USB */
    public static final String USB_DEVICE_BUS = "USB";

    /** Property value for deviceBus for RS485 (or SIO) */
    public static final String RS485_DEVICE_BUS = "RS485";

    /** Property value for deviceBus for HID (or Human Inferface Device) */
    public static final String HID_DEVICE_BUS = "HID";

    /** Property value for deviceBus for proprietary buses */
    public static final String PROPRIETARY_DEVICE_BUS = "Proprietary";

    /** Property value for deviceBus for other "Unknown" buses */
    public static final String UNKNOWN_DEVICE_BUS = "Unknown";

    /** Array of all the deviceBus property values */
    public static final String[] DEVICE_BUS_VALUES = { RS232_DEVICE_BUS, PARALLEL_DEVICE_BUS, USB_DEVICE_BUS, RS485_DEVICE_BUS, HID_DEVICE_BUS, PROPRIETARY_DEVICE_BUS, UNKNOWN_DEVICE_BUS };

    /** The default SprinkoEntry property type */
    public static final Class DEFAULT_PROP_TYPE = String.class;

    /** Array of all the property types allowed for a SprinkoEntry property */
    public static final Class[] PROP_TYPES = { String.class, Boolean.class, Byte.class, Character.class, Double.class, Float.class, Integer.class, Long.class, Short.class };

    /** Array of all the property types allowed for a SprinkoEntry property */
    public static final String[] PROP_TYPES_SHORT_NAMES = { "String", "Boolean", "Byte", "Character", "Double", "Float", "Integer", "Long", "Short" };
}
