package icapture.hibernate;

import icapture.com.Util;
import java.util.Date;

public final class ContainerType extends Persistent {

    private String columns;

    private String containerTypeID;

    private String description;

    private String rows;

    private String sortOrder;

    public String getContainerTypeID() {
        return containerTypeID;
    }

    public String getDescription() {
        return description;
    }

    public String getRows() {
        return rows;
    }

    public String getColumns() {
        return columns;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public String getVisibleName() {
        return description;
    }

    public String getPropertyValue(String propertyName) {
        if (propertyName.equals("visibleName")) {
            return description;
        } else {
            return super.getPropertyValue(propertyName);
        }
    }

    public Object getPropertyObject(String propertyName) {
        if (propertyName.equals("visibleName")) {
            return description;
        } else {
            return super.getPropertyObject(propertyName);
        }
    }

    public String[] getValueArray(byte[] indexArray, int viewSize) {
        return getValueArrayReadable(indexArray, viewSize);
    }

    public String[] getValueArrayReadable(byte[] indexArray, int viewSize) {
        String[] valueArray = super.getValueArray(indexArray, viewSize);
        if (indexArray[6] > 0) {
            valueArray[indexArray[6] - 1] = containerTypeID;
        }
        if (indexArray[7] > 0) {
            valueArray[indexArray[7] - 1] = description;
        }
        if (indexArray[8] > 0) {
            valueArray[indexArray[8] - 1] = rows;
        }
        if (indexArray[9] > 0) {
            valueArray[indexArray[9] - 1] = columns;
        }
        if (indexArray[10] > 0) {
            valueArray[indexArray[10] - 1] = sortOrder;
        }
        return valueArray;
    }

    public void setContainerTypeID(String input) {
        containerTypeID = input;
    }

    public void setDescription(String input) {
        description = input;
    }

    public void setRows(String input) {
        rows = input;
    }

    public void setColumns(String input) {
        columns = input;
    }

    public void setSortOrder(String input) {
        sortOrder = input;
    }
}
