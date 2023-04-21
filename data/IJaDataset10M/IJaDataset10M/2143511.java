package ces.sf.oa.util;

import java.sql.Date;

public abstract class PojoInterface {

    private String useName;

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public abstract String getFileName();

    public abstract String getDepartName();

    public abstract Date getCreateDate();

    public abstract String getCreateName();
}
