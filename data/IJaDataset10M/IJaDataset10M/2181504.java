package net.iskandar.murano_example.gwt.ui.client.widget;

import java.util.List;
import com.extjs.gxt.ui.client.data.BaseModelData;
import net.iskandar.murano_example.dto.SelectOption;
import net.iskandar.murano_example.dto.EmployeeUpdateObject;

public class EmployeeEditData {

    private List<BaseModelData> statuses;

    private List<BaseModelData> positions;

    private EmployeeUpdateObject employeeEditObject;

    public List<BaseModelData> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<BaseModelData> statuses) {
        this.statuses = statuses;
    }

    public List<BaseModelData> getPositions() {
        return positions;
    }

    public void setPositions(List<BaseModelData> positions) {
        this.positions = positions;
    }

    public EmployeeUpdateObject getEditEmployeeObject() {
        return employeeEditObject;
    }

    public void setEditEmployeeObject(EmployeeUpdateObject editEmployeeObject) {
        this.employeeEditObject = editEmployeeObject;
    }
}
