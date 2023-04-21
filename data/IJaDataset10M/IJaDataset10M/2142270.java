package system.dto;

import java.util.ArrayList;
import java.util.List;
import system.value.ManageRecordValue;

public class ManageRecordDto {

    private List outList = new ArrayList();

    private ManageRecordValue recordValue = new ManageRecordValue();

    public List getOutList() {
        return outList;
    }

    public void setOutList(List outList) {
        this.outList = outList;
    }

    public ManageRecordValue getRecordValue() {
        return recordValue;
    }

    public void setRecordValue(ManageRecordValue recordValue) {
        this.recordValue = recordValue;
    }
}
