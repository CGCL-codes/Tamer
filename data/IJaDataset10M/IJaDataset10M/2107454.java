package com.ncs.crm.client.customer;

import java.util.Date;
import com.google.gwt.user.client.ui.Hidden;
import com.ncs.crm.client.BlankContextMenu;
import com.ncs.crm.client.Constant;
import com.ncs.crm.client.entity.User;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.FormLayoutType;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon.Picker;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.HLayout;

public class VisitNewWindow extends Window {

    private static final String REQUIRED = "<font style='color:red'>*</font>";

    private DynamicForm visitEditForm;

    private HiddenItem customerId;

    private HiddenItem isFirstTime;

    private DataSource visitDS = DataSource.get("visit");

    private int custPK;

    public VisitNewWindow(final Record record, User user) {
        custPK = record.getAttributeAsInt("id");
        setTitle("客户拜访信息");
        setAutoCenter(true);
        setIsModal(false);
        setWidth(500);
        setHeight(400);
        setAlign(Alignment.CENTER);
        setPadding(20);
        setShowMinimizeButton(false);
        setIsModal(true);
        setCanDragReposition(false);
        setShowModalMask(true);
        HLayout layout1 = new HLayout();
        layout1.setAlign(Alignment.CENTER);
        visitEditForm = new DynamicForm();
        visitEditForm.setDataSource(visitDS);
        visitEditForm.setWidth100();
        visitEditForm.setAlign(Alignment.CENTER);
        HiddenItem date = new HiddenItem("date");
        date.setValue(new Date());
        SelectItem manner = new SelectItem("manner", "拜访方式");
        manner.setRequired(true);
        manner.setHint(REQUIRED);
        manner.setIconWidth(23);
        PickerIcon p1 = new PickerIcon(new Picker("myaction/add.png"), new FormItemClickHandler() {

            public void onFormItemClick(FormItemIconClickEvent event) {
                new DefineMannerWindow().animateShow(AnimationEffect.WIPE);
            }
        });
        p1.setWidth(23);
        manner.setIcons(p1);
        manner.setOptionDataSource(DataSource.get("visitManner"));
        manner.setAutoFetchData(true);
        manner.setDisplayField("mannerName");
        manner.setValueField("id");
        manner.setAddUnknownValues(false);
        customerId = new HiddenItem("customerId");
        customerId.setValue(custPK);
        isFirstTime = new HiddenItem("isFirstTime");
        final SelectItem contact = new SelectItem("contact", "客户联系人") {

            protected Criteria getPickListFilterCriteria() {
                Criteria criteria = new Criteria("customerId", String.valueOf(custPK));
                return criteria;
            }
        };
        contact.setRequired(true);
        contact.setHint(REQUIRED);
        contact.setOptionDataSource(DataSource.get(Constant.DS_CONTACT));
        contact.setAutoFetchData(true);
        contact.setDisplayField("contactName");
        contact.setValueField("id");
        TextAreaItem content = new TextAreaItem("content", "拜访内容");
        content.setHeight(150);
        content.setWidth(350);
        content.setRequired(true);
        content.setHint(REQUIRED);
        SelectItem maturity = new SelectItem("maturity", "成熟度");
        maturity.setRequired(true);
        maturity.setHint(REQUIRED);
        maturity.setAutoFetchData(true);
        maturity.setOptionDataSource(DataSource.get("visitMaturity"));
        maturity.setDisplayField("maturityName");
        maturity.setValueField("id");
        PickerIcon p2 = new PickerIcon(new Picker("myaction/add.png"), new FormItemClickHandler() {

            public void onFormItemClick(FormItemIconClickEvent event) {
                new DefineMaturityWindow().animateShow(AnimationEffect.WIPE);
            }
        });
        p2.setWidth(23);
        maturity.setIcons(p2);
        HiddenItem byWhom = new HiddenItem("byWhom");
        byWhom.setValue(user.getId());
        HiddenItem isDelete = new HiddenItem("isDelete");
        isDelete.setValue(0);
        visitEditForm.setItems(date, manner, contact, maturity, content, customerId, isFirstTime, byWhom, isDelete);
        addShowContextMenuHandler(new BlankContextMenu());
        layout1.addMember(visitEditForm);
        HLayout lay = new HLayout();
        lay.setWidth100();
        lay.setHeight(30);
        addItem(lay);
        addItem(layout1);
        DynamicForm df = new DynamicForm();
        df.setWidth100();
        df.setNumCols(6);
        df.setAlign(Alignment.CENTER);
        df.setItemLayout(FormLayoutType.ABSOLUTE);
        ButtonItem btnNew = new ButtonItem("保存");
        btnNew.setAlign(Alignment.CENTER);
        btnNew.setWidth(100);
        btnNew.setLeft(200);
        btnNew.setIcon("myaction/ok.png");
        btnNew.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (visitEditForm.validate()) {
                    Criteria c = new Criteria();
                    c.addCriteria("customerId", custPK);
                    visitDS.fetchData(c, new DSCallback() {

                        public void execute(DSResponse response, Object rawData, DSRequest request) {
                            int i = response.getData().length;
                            if (i == 0) {
                                System.out.println("is a first-time visit.");
                                isFirstTime.setValue(1);
                            } else {
                                System.out.println("isn't a first-time visit.");
                                isFirstTime.setValue(0);
                            }
                            visitEditForm.saveData(new DSCallback() {

                                public void execute(DSResponse response, Object rawData, DSRequest request) {
                                    if (response.getStatus() >= 0) {
                                        String lastVisitId = response.getData()[0].getAttributeAsString("id");
                                        System.err.println(lastVisitId);
                                        record.setAttribute("lastVisitId", lastVisitId);
                                        DataSource.get("customers").updateData(record, new DSCallback() {

                                            public void execute(DSResponse response, Object rawData, DSRequest request) {
                                                destroy();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
        df.setItems(btnNew);
        addItem(df);
        HLayout lay2 = new HLayout();
        lay2.setHeight(30);
        addItem(lay2);
        show();
    }

    public void myEditRecord(Record record) {
        visitEditForm.editRecord(record);
    }
}
