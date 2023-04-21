package boldinventions.ansi_console.serial;

/**
     * A struct which links a Setting name with it's value.
     * @author Kevin
     *
     */
public final class SettingNameAndValue {

    public String name;

    public int value;

    public SettingNameAndValue(String aName, int aValue) {
        name = aName;
        value = aValue;
    }
}
