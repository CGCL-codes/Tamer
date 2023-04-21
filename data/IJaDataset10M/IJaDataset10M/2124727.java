package com.wonebiz.crm.client.contract.queryValid;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class FeeCheckWindow extends Window {

    private DataSource contractInfoDS = DataSource.get("contractInfo");

    public FeeCheckWindow(Record record, String userId) {
        setTitle("确认收款");
        setAutoCenter(true);
        setWidth(350);
        setHeight(230);
        setShowMinimizeButton(false);
        setCanDragReposition(false);
        setIsModal(true);
        final DynamicForm df = new DynamicForm();
        df.setDataSource(DataSource.get("contractFee"));
        df.editRecord(record);
        HiddenItem checkedBy = new HiddenItem("checkedBy");
        checkedBy.setValue(userId);
        HiddenItem isChecked = new HiddenItem("isChecked");
        DateItem checkDate = new DateItem("checkDate", "收款日期");
        TextItem feeValue = new TextItem("feeValue", "实收金额");
        feeValue.setRequired(true);
        feeValue.setMask("000000000000000");
        HiddenItem feeValueOld = new HiddenItem("feeValueOld");
        feeValueOld.setValue(record.getAttributeAsInt("feeValue"));
        HiddenItem contractInfoId = new HiddenItem("contractInfoId");
        TextAreaItem remark = new TextAreaItem("remark", "备注");
        remark.setWidth(200);
        df.setItems(checkedBy, checkDate, feeValue, feeValueOld, remark, isChecked, contractInfoId);
        addItem(df);
        IButton btn = new IButton("保存");
        btn.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (df.validate()) {
                    int newFee = Integer.parseInt(df.getValueAsString("feeValue"));
                    int oldFee = Integer.parseInt(df.getValueAsString("feeValueOld"));
                    String remark = df.getValueAsString("remark");
                    if (newFee == oldFee) {
                        System.out.println("金额一致，直接保存");
                        df.setValue("isChecked", 1);
                        df.saveData(new DSCallback() {

                            public void execute(DSResponse response, Object rawData, DSRequest request) {
                                destroy();
                            }
                        });
                    } else if (newFee != oldFee && (remark == null || "".equals(remark))) {
                        System.out.println("实收金额与计划金额不一致,备注为空");
                        SC.say("由于实收金额与计划金额不一致，请填写备注信息");
                        return;
                    } else {
                        System.out.println("实收金额与计划金额不一致,备注不为空");
                        final int feeOffset = newFee - oldFee;
                        System.out.println("差价为:" + feeOffset);
                        final String contractInfoId = df.getValueAsString("contractInfoId");
                        df.setValue("isChecked", 1);
                        df.saveData(new DSCallback() {

                            public void execute(DSResponse response, Object rawData, DSRequest request) {
                                System.out.println("状态码:" + response.getStatus());
                                if (response.getStatus() >= 0) {
                                    Criteria c = new Criteria("id", contractInfoId);
                                    contractInfoDS.fetchData(c, new DSCallback() {

                                        public void execute(DSResponse response, Object rawData, DSRequest request) {
                                            Record conRecord = response.getData()[0];
                                            int oldCharge = conRecord.getAttributeAsInt("charge");
                                            System.out.println("[Audit]:合同ID [" + contractInfoId + "]金额由" + oldCharge + ", 改为" + oldCharge + feeOffset);
                                            conRecord.setAttribute("charge", oldCharge + feeOffset);
                                            contractInfoDS.updateData(conRecord, new DSCallback() {

                                                public void execute(DSResponse response, Object rawData, DSRequest request) {
                                                    destroy();
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
        addItem(btn);
    }
}
