package com.ncs.crm.client.contract.addContract;

import com.google.gwt.i18n.client.NumberFormat;
import com.ncs.crm.client.Constant;
import com.ncs.crm.client.data.FeeXMLDS;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SummaryFunctionType;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class FakeFeeGrid extends ListGrid {

    public FakeFeeGrid() {
        DataSource ds = FeeXMLDS.getInstance();
        setDataSource(ds);
        setCanRemoveRecords(true);
        setHeight(130);
        setEmptyMessage("");
        setWidth(515);
        setCanSort(false);
        setCanFreezeFields(false);
        setShowGridSummary(true);
        ListGridField feeType = new ListGridField("feeType", "费用名称", 100);
        feeType.setAlign(Alignment.CENTER);
        feeType.setRequired(true);
        feeType.setOptionDataSource(DataSource.get(Constant.DS_CONTRACT_FEE_TYPE));
        feeType.setDisplayField("typeName");
        feeType.setValueField("id");
        ListGridField feeValue = new ListGridField("feeValue", "金额", 100);
        feeValue.setAlign(Alignment.CENTER);
        feeValue.setRequired(true);
        feeValue.setSummaryFunction(SummaryFunctionType.SUM);
        feeValue.setShowGridSummary(true);
        feeValue.setCellFormatter(new CellFormatter() {

            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                if (value == null) {
                    return null;
                }
                try {
                    NumberFormat nf = NumberFormat.getFormat("##0.00");
                    String s = "¥" + nf.format((Integer.parseInt((String) value)));
                    return s;
                } catch (Exception e) {
                    return value.toString();
                }
            }
        });
        ListGridField dueDate = new ListGridField("dueDate", "计划到款日期", 100);
        dueDate.setAlign(Alignment.CENTER);
        dueDate.setEditorType(new DateItem());
        dueDate.setRequired(true);
        ListGridField remark = new ListGridField("remark", "备注", 190);
        remark.setAlign(Alignment.LEFT);
        setFields(feeType, feeValue, dueDate, remark);
    }
}
