package org.itunesdbparser.domain.db.itunes.specification;

import org.itunesdbparser.domain.db.itunes.dataitems.DataItem;

/**
 * In this case the actual length of the DataItem is only known by inspecting
 * another DataItem.
 */
public class VariableLengthDataItemSpec implements DataItemSpec {

    private String name;

    private DataItem lengthDataItem;

    private int length;

    private String description;

    private String dataType;

    /**
     * This data item's length is specfied by the value of another DataItem. This contractor
     * takes the other DataItem object and all the necessary parameters to make this DataItem
     * and gets the value from the lengthDataItem setting this length of this DataItem.
     * @param name
     * @param lengthDataItem
     * @param description
     * @param dataType
     */
    public VariableLengthDataItemSpec(String name, DataItem lengthDataItem, String description, String dataType) {
        this.name = name;
        this.lengthDataItem = lengthDataItem;
        this.description = description;
        this.dataType = dataType;
        setLength(getLength());
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLength() {
        return ((Integer) lengthDataItem.getHeader().get("string-length")).intValue();
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
