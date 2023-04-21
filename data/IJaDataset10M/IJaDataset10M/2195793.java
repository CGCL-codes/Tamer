package net.homeip.mleclerc.omnilink.enumeration;

@SuppressWarnings("serial")
public class BasicUnitControlEnum extends Enum {

    public static final EnumInfo metaInfo = new EnumInfo();

    public static final BasicUnitControlEnum OFF = new BasicUnitControlEnum("Off", 0);

    public static final BasicUnitControlEnum ON = new BasicUnitControlEnum("On", 1);

    public BasicUnitControlEnum(String userLabel, int value) {
        super(userLabel, value);
        metaInfo.add(this);
    }
}
