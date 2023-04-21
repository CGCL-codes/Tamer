package com.kwoksys.action.admin;

import org.apache.struts.action.ActionForm;

/**
 * ActionForm for adding/editing user group.
 */
public class GroupForm extends ActionForm {

    private Integer groupId;

    private String groupName;

    private String groupDescription;

    private Integer[] availableMembers;

    private Integer[] selectedMembers;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public Integer[] getAvailableMembers() {
        return availableMembers;
    }

    public void setAvailableMembers(Integer[] availableMembers) {
        this.availableMembers = availableMembers;
    }

    public Integer[] getSelectedMembers() {
        return selectedMembers;
    }

    public void setSelectedMembers(Integer[] selectedMembers) {
        this.selectedMembers = selectedMembers;
    }
}
