package cn.ekuma.odbo.client.query;

public class GWTQBFParameterLong extends GWTQBFParameter<Long> {

    public static GWTQBFParameter ALL = new GWTQBFParameterLong("", GWTQBFCompareEnum.COMP_ALL, null);

    public GWTQBFParameterLong(String fieldName, GWTQBFCompareEnum qbf, Long object) {
        super(fieldName, qbf, object);
    }

    public GWTQBFParameterLong() {
    }
}
