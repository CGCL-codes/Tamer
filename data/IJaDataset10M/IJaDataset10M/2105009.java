package candy.core.entity.country;

import candy.core.entity.IEntity;

public class Directory_Country_Region_Name implements IEntity {

    private long region_Name_ID;

    private String locale;

    private long region_ID;

    private String name;

    public long getRegion_Name_ID() {
        return region_Name_ID;
    }

    public void setRegion_Name_ID(long region_Name_ID) {
        this.region_Name_ID = region_Name_ID;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public long getRegion_ID() {
        return region_ID;
    }

    public void setRegion_ID(long region_ID) {
        this.region_ID = region_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setKeyValue(long value) {
        this.region_Name_ID = value;
    }

    @Override
    public long getKeyValue() {
        return this.region_Name_ID;
    }
}
