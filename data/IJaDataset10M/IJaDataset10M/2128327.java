package com.sunstar.sos.pojo;

import com.sunstar.sos.pojo.annotation.ColumnAnnotation;
import com.sunstar.sos.pojo.annotation.TableAnnotation;

@TableAnnotation(table = "ss_sys_sequences", primaryKey = "table_name", primaryType = "string", remark = "")
public class SysSequences implements java.io.Serializable {

    private static final long serialVersionUID = -5064552155363091300L;

    @ColumnAnnotation(colName = "table_name", remark = "����")
    private String tableName;

    @ColumnAnnotation(colName = "next_value", remark = "��һ����������ֵ")
    private long nextValue;

    @ColumnAnnotation(colName = "step_value", remark = "�������в���")
    private long stepValue;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getNextValue() {
        return nextValue;
    }

    public void setNextValue(long nextValue) {
        this.nextValue = nextValue;
    }

    public long getStepValue() {
        return stepValue;
    }

    public void setStepValue(long stepValue) {
        this.stepValue = stepValue;
    }
}
