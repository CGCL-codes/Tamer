package au.com.uptick.serendipity.client.data;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ContextAreaRecord extends ListGridRecord {

    private static final String COLUMN1_ICON = "column1Icon";

    private static final String COLUMN1_DESCRIPTION = "column1Description";

    private static final String COLUMN2_ICON = "column2Icon";

    private static final String COLUMN2_DESCRIPTION = "column2Description";

    public ContextAreaRecord() {
    }

    public ContextAreaRecord(String column1Icon, String column1Description, String column2Icon, String column2Description) {
        setColumn1Icon(column1Icon);
        setColumn1Description(column1Description);
        setColumn2Icon(column2Icon);
        setColumn2Description(column2Description);
    }

    public void setColumn1Icon(String icon) {
        setAttribute(COLUMN1_ICON, icon);
    }

    public void setColumn1Description(String description) {
        setAttribute(COLUMN1_DESCRIPTION, description);
    }

    public void setColumn2Icon(String icon) {
        setAttribute(COLUMN2_ICON, icon);
    }

    public void setColumn2Description(String description) {
        setAttribute(COLUMN2_DESCRIPTION, description);
    }

    public String getColumn1Icon() {
        return getAttributeAsString(COLUMN1_ICON);
    }

    public String getColumn1Description() {
        return getAttributeAsString(COLUMN1_DESCRIPTION);
    }

    public String getColumn2Icon() {
        return getAttributeAsString(COLUMN2_ICON);
    }

    public String getColumn2Description() {
        return getAttributeAsString(COLUMN2_DESCRIPTION);
    }
}
