package net.homeip.mleclerc.omnilink.enumeration;

@SuppressWarnings("serial")
public class FeatureTypeEnum extends Enum {

    public static final EnumInfo metaInfo = new EnumInfo();

    public static final FeatureTypeEnum NUVO_CONCERTO = new FeatureTypeEnum("NuVo Concerto", 1);

    public static final FeatureTypeEnum NUVO_ESSENTIA = new FeatureTypeEnum("NuVo Essentia/Simplese", 2);

    public static final FeatureTypeEnum NUVO_GRAND_CONCERTO = new FeatureTypeEnum("NuVo Grand Concerto", 3);

    public static final FeatureTypeEnum RUSSOUND = new FeatureTypeEnum("Russound", 4);

    public static final FeatureTypeEnum HAI_HIFI = new FeatureTypeEnum("HAI Hi-Fi", 5);

    public static final FeatureTypeEnum XANTECH = new FeatureTypeEnum("Xantech", 6);

    public static final FeatureTypeEnum SPEAKERCRAFT = new FeatureTypeEnum("Speakercraft", 7);

    public static final FeatureTypeEnum PROFICIENT = new FeatureTypeEnum("Duress", 8);

    public static final FeatureTypeEnum DHC_SECURITY = new FeatureTypeEnum("DHC Security", 9);

    public FeatureTypeEnum(String userLabel, int value) {
        super(userLabel, value);
        metaInfo.add(this);
    }
}
