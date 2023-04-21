package com.ncs.crm.client.contract.queryValid;

import java.util.Date;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.ncs.crm.client.data.ConstantData;
import com.ncs.crm.client.entity.ClientConfig;
import com.ncs.crm.client.entity.User;
import com.ncs.crm.client.service.CustomerServiceRPC;
import com.ncs.crm.client.service.CustomerServiceRPCAsync;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.TextMatchStyle;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;

public class ContractInfoForm extends DynamicForm {

    private static String REQUIRED = "<font style='color:red'>*</font>";

    private final CustomerServiceRPCAsync service = GWT.create(CustomerServiceRPC.class);

    public ContractInfoForm(ClientConfig cc) {
        DataSource custDS = DataSource.get("servingCustomer");
        DataSource ownerDS = DataSource.get("owner");
        DataSource contractInfoDS = DataSource.get("contractInfo");
        setAutoHeight();
        setDataSource(contractInfoDS);
        TextItem contractNo = new TextItem("contractNo", "公司合同号");
        contractNo.setRequired(true);
        contractNo.setHint(REQUIRED);
        contractNo.setLength(30);
        TextItem custOrderNo = new TextItem("custOrderNo", "客户订单号");
        custOrderNo.setLength(30);
        DateItem signDate = new DateItem("signDate", "签约日期");
        signDate.setUseTextField(true);
        signDate.setRequired(true);
        signDate.setHint(REQUIRED);
        signDate.setDefaultValue(new Date());
        signDate.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATE);
        final DateItem startDate = new DateItem("startDate", "服务开始日期");
        startDate.setUseTextField(true);
        startDate.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATE);
        if (cc.getRelays().get(ConstantData.REQUIRE_STARTDATE) == 1) {
            startDate.setRequired(true);
            startDate.setHint(ConstantData.HINT_REQUIRED);
        }
        final DateItem endDate = new DateItem("endDate", "服务结束日期");
        endDate.setUseTextField(true);
        endDate.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATE);
        if (cc.getRelays().get(ConstantData.REQUIRE_ENDDATE) == 1) {
            endDate.setRequired(true);
            endDate.setHint(ConstantData.HINT_REQUIRED);
        }
        TextItem charge = new TextItem("charge", "合同金额");
        charge.setLength(20);
        charge.setMask("##########");
        charge.setRequired(true);
        charge.setHint(REQUIRED);
        final ComboBoxItem customerId = new ComboBoxItem("customerId", ConstantData.CUSTOMER_NAME);
        customerId.setWidth(250);
        customerId.setOptionDataSource(custDS);
        customerId.setDisplayField("name");
        customerId.setValueField("id");
        customerId.setAutoFetchData(true);
        customerId.setRequired(true);
        customerId.setHint(REQUIRED);
        ListGridField nameField = new ListGridField("name", ConstantData.CUSTOMER_NAME, 250);
        nameField.setShowHover(true);
        nameField.setAlign(Alignment.CENTER);
        ListGridField contactNameField = new ListGridField("contactName", "主联系人", 100);
        contactNameField.setAlign(Alignment.CENTER);
        customerId.setPickListFields(nameField, contactNameField);
        customerId.setShowPickListOnKeypress(true);
        customerId.setPickListWidth(375);
        customerId.setPickListCriteria(new Criteria("isDelete", "0"));
        customerId.setTextMatchStyle(TextMatchStyle.SUBSTRING);
        final SelectItem originalOwner = new SelectItem("originalOwner", "登记人");
        originalOwner.setOptionDataSource(ownerDS);
        originalOwner.setDisplayField("nickName");
        originalOwner.setValueField("id");
        originalOwner.setAutoFetchData(true);
        originalOwner.setRequired(true);
        originalOwner.setHint(REQUIRED);
        final SelectItem signer = new SelectItem("signer", "签约人");
        signer.setOptionDataSource(ownerDS);
        signer.setDisplayField("nickName");
        signer.setValueField("id");
        signer.setAutoFetchData(true);
        signer.setRequired(true);
        signer.setHint(REQUIRED);
        customerId.addChangedHandler(new ChangedHandler() {

            public void onChanged(ChangedEvent event) {
                System.out.println("customerId ChangedHandler...");
                String sCustomerId = getValueAsString("customerId");
                service.getOriginalOwnerByCustomerId(sCustomerId, new AsyncCallback<User>() {

                    public void onFailure(Throwable e) {
                        System.out.println("[ERROR]: " + e.toString());
                    }

                    public void onSuccess(User user) {
                        long userId = user.getId();
                        String nickName = user.getNickName();
                        getField("showOriginalOwner").setValue(nickName);
                        getField("originalOwner").setValue(userId);
                    }
                });
            }
        });
        HiddenItem submitter = new HiddenItem("submitter");
        HiddenItem contractType = new HiddenItem("contractType");
        HiddenItem createDate = new HiddenItem("createDate");
        createDate.setValue(new Date());
        setItems(contractNo, custOrderNo, customerId, signDate, charge, startDate, originalOwner, signer, endDate, contractType, createDate, submitter);
    }
}
