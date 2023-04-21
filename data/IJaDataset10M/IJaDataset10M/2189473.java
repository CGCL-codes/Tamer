package net.homeip.mleclerc.omnilink.enumeration;

@SuppressWarnings("serial")
public class AlarmTypeEnum extends Enum {

    public static final EnumInfo metaInfo = new EnumInfo();

    public static final AlarmTypeEnum BURGLARY = new AlarmTypeEnum("Burglary", 1);

    public static final AlarmTypeEnum FIRE = new AlarmTypeEnum("Fire", 2);

    public static final AlarmTypeEnum GAS = new AlarmTypeEnum("Gas", 3);

    public static final AlarmTypeEnum AUXILIARY = new AlarmTypeEnum("Auxiliary", 4);

    public static final AlarmTypeEnum FREEZE = new AlarmTypeEnum("Freeze", 5);

    public static final AlarmTypeEnum WATER = new AlarmTypeEnum("Water", 6);

    public static final AlarmTypeEnum DURESS = new AlarmTypeEnum("Duress", 7);

    public static final AlarmTypeEnum TEMPERATURE = new AlarmTypeEnum("Temperature", 8);

    public AlarmTypeEnum(String userLabel, int value) {
        super(userLabel, value);
        metaInfo.add(this);
    }
}
