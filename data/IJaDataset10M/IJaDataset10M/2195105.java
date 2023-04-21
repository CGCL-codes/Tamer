package com.entelience.objects.vrt;

import java.io.Serializable;

public class VulnGroupReportInfoLine extends VulnReportInfoLine implements Serializable {

    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
