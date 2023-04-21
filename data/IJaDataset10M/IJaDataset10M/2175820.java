package net.iskandar.murano_example.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.iskandar.murano_example.EmployeeManagement;
import net.iskandar.murano_example.dto.EmployeeUpdateObject;
import net.iskandar.murano_example.dto.EmployeeListObject;
import net.iskandar.murano_example.dto.EmployeeOrderSettings;
import net.iskandar.murano_example.dto.SelectOption;
import net.iskandar.murano_example.dto.StatusCase;
import net.iskandar.murano_example.domain.Status;

public abstract class EmployeeManagementAbstractImpl implements EmployeeManagement {

    public List<SelectOption> getStatuses(StatusCase statusCase) {
        List<SelectOption> statuses = new ArrayList<SelectOption>();
        for (Status status : Status.values()) {
            if (statusCase == StatusCase.MODIFY_EMPLOYEE && status == Status.ALL) continue;
            statuses.add(new SelectOption(status.ordinal(), status.getLabel()));
        }
        return statuses;
    }

    public Integer getDefaultStatusId() {
        return Status.ACTIVE.ordinal();
    }

    public EmployeeOrderSettings getDefaultOrderSettings() {
        return new EmployeeOrderSettings(EmployeeOrderSettings.FIRST_NAME_FIELD, EmployeeOrderSettings.ASC);
    }
}
