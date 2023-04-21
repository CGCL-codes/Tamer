package com.wonebiz.crm.client.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.FormLayoutType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.wonebiz.crm.client.BlankContextMenu;
import com.wonebiz.crm.client.customer.OwnerChooseWindow;
import com.wonebiz.crm.client.entity.ClientConfig;

public class TrackSearchForm extends VLayout {

    private TextItem ownerItem;

    private HiddenItem hiddenOwner;

    private ListGrid trackGird;

    private DynamicForm searchFrom;

    private DateItem dateFromItem;

    private DateItem dateToItem;

    private DynamicForm dateRangeForm;

    private ClientConfig config;

    private DataSource userDS = DataSource.get("user");

    private DataSource mannerDS = DataSource.get("visitManner");

    private CheckboxItem isWorkDays;

    private TextItem mannerText;

    private HiddenItem hiddenManner;

    @SuppressWarnings("deprecation")
    public TrackSearchForm(ClientConfig cc, ListGrid grid, Label lab) {
        this.trackGird = grid;
        this.config = cc;
        setAutoHeight();
        searchFrom = new DynamicForm();
        searchFrom.setAutoHeight();
        searchFrom.setAutoWidth();
        searchFrom.setLayoutAlign(VerticalAlignment.TOP);
        searchFrom.setNumCols(4);
        ownerItem = new TextItem("");
        ownerItem.setWidth(450);
        ownerItem.setValue("");
        ownerItem.setDisabled(true);
        hiddenOwner = new HiddenItem("trueOwner");
        hiddenOwner.setValue("");
        LinkItem linkItem = new LinkItem("link");
        linkItem.setWidth(0);
        linkItem.setTitle("");
        linkItem.setLinkTitle("选择客户专员:");
        linkItem.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                ownerItem.clearValue();
                hiddenOwner.clearValue();
                Criteria c = new Criteria();
                c.addCriteria("id", config.getCoveredUsers());
                userDS.fetchData(c, new DSCallback() {

                    public void execute(DSResponse response, Object rawData, DSRequest request) {
                        new OwnerChooseWindow(response.getData(), ownerItem, hiddenOwner).show();
                    }
                });
            }
        });
        dateRangeForm = new DynamicForm();
        dateRangeForm.setNumCols(10);
        dateRangeForm.setAutoWidth();
        dateRangeForm.setAutoHeight();
        Label lable = new Label("lable");
        lable.setWidth(80);
        lable.setHeight(25);
        lable.setContents("查询日期:");
        dateFromItem = new DateItem("from");
        int i = new Date().getMonth();
        int ii = new Date().getYear();
        Date d2 = new Date();
        if (i > 0) {
            System.out.println("i>0");
            d2.setMonth(i - 1);
        }
        if (i == 0) {
            System.out.println("i==0");
            d2.setMonth(11);
            d2.setYear(ii - 1);
        }
        dateFromItem.setDefaultValue(d2);
        dateFromItem.setUseTextField(true);
        dateFromItem.setWidth(100);
        dateFromItem.setTitle("从");
        dateFromItem.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATE);
        dateToItem = new DateItem("createTime2");
        dateToItem.setDefaultValue(new Date());
        dateToItem.setUseTextField(true);
        dateToItem.setWidth(100);
        dateToItem.setTitle("到");
        dateToItem.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATE);
        isWorkDays = new CheckboxItem("isWork", "以工作日计算");
        isWorkDays.setDefaultValue(true);
        dateRangeForm.setItems(dateFromItem, dateToItem, isWorkDays);
        LinkItem mannerItem = new LinkItem("manner");
        mannerItem.setWidth(0);
        mannerItem.setTitle("");
        mannerItem.setLinkTitle("选择拜访方式:");
        mannerItem.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                mannerText.clearValue();
                hiddenManner.clearValue();
                mannerDS.fetchData(new Criteria(), new DSCallback() {

                    public void execute(DSResponse response, Object rawData, DSRequest request) {
                        new MannerChooseWindow(response.getData(), mannerText, hiddenManner).show();
                    }
                });
            }
        });
        mannerText = new TextItem("");
        mannerText.setWidth(450);
        mannerText.setValue("上门拜访,");
        mannerText.setDisabled(true);
        hiddenManner = new HiddenItem("trueManner");
        hiddenManner.setValue("1,");
        DynamicForm mannerForm = new DynamicForm();
        mannerForm.setAutoHeight();
        mannerForm.setAutoWidth();
        mannerForm.setLayoutAlign(VerticalAlignment.TOP);
        mannerForm.setNumCols(4);
        mannerForm.setFields(mannerItem, mannerText, hiddenManner);
        HLayout dateLay = new HLayout();
        dateLay.setWidth100();
        HLayout blankLay = new HLayout();
        blankLay.setWidth(20);
        dateLay.addMember(blankLay);
        dateLay.addMember(lable);
        dateLay.addMember(dateRangeForm);
        searchFrom.setItems(linkItem, ownerItem, hiddenOwner);
        DynamicForm btnFrom = new DynamicForm();
        btnFrom.setLayoutAlign(VerticalAlignment.TOP);
        btnFrom.setItemLayout(FormLayoutType.ABSOLUTE);
        ButtonItem btnSave = new ButtonItem("save");
        btnSave.setTitle("查询");
        btnSave.setIcon("silk/find.png");
        btnSave.setTextAlign(Alignment.CENTER);
        btnSave.setWidth(100);
        btnSave.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                query();
            }
        });
        ButtonItem btnReset = new ButtonItem("reset");
        btnReset.setTitle("清除");
        btnReset.setTextAlign(Alignment.CENTER);
        btnReset.setWidth(100);
        btnReset.setLeft(110);
        btnReset.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                resetSearchForm();
            }
        });
        btnFrom.setItems(btnSave, btnReset);
        addMember(searchFrom);
        addMember(mannerForm);
        addMember(dateLay);
        addMember(btnFrom);
        addShowContextMenuHandler(new BlankContextMenu());
    }

    private void resetSearchForm() {
        searchFrom.clearValues();
    }

    private void query() {
        System.out.println("预备查询...");
        trackGird.invalidateCache();
        final Criteria criteria = new Criteria();
        Date from = (Date) dateFromItem.getValue();
        Date to = (Date) dateToItem.getValue();
        if (to.getTime() < from.getTime()) {
            SC.warn("起始日期不能小于结束日期");
            return;
        }
        criteria.addCriteria("from", from);
        criteria.addCriteria("to", to);
        if (isWorkDays.getValueAsBoolean()) {
            System.out.println("工作日");
            criteria.addCriteria("isWork", "1");
        } else {
            System.out.println("自然日");
            criteria.addCriteria("isWork", "0");
        }
        String owner = hiddenOwner.getValue().toString();
        if (owner == null || "".equals(owner)) {
            System.out.println("缺省多个客户专员");
            criteria.addCriteria("owner", config.getCoveredUsers());
        } else {
            System.out.println("数组,查多个人");
            String o = owner;
            int i = o.indexOf(",");
            List<String> list = new ArrayList<String>();
            while (i >= 0) {
                String sun = o.substring(0, i);
                list.add(sun);
                o = o.substring(i + 1, o.length());
                i = o.indexOf(",");
            }
            System.out.println(list.size());
            String[] data = new String[list.size()];
            for (int j = 0; j < list.size(); j++) {
                data[j] = list.get(j);
            }
            System.out.println("data.length : " + data.length);
            criteria.addCriteria("owner", data);
        }
        String manner = hiddenManner.getValue().toString();
        System.out.println("测试输出  manner : " + manner);
        if (manner == null || "".equals(manner)) {
            System.out.println("缺省拜访方式 :" + manner);
            criteria.addCriteria("manner", "ALL");
        } else {
            System.out.println("有选择拜访方式");
            String o = manner;
            int i = o.indexOf(",");
            List<String> list = new ArrayList<String>();
            while (i >= 0) {
                String sun = o.substring(0, i);
                list.add(sun);
                o = o.substring(i + 1, o.length());
                i = o.indexOf(",");
            }
            System.out.println(list.size());
            String[] data = new String[list.size()];
            for (int j = 0; j < list.size(); j++) {
                data[j] = list.get(j);
            }
            System.out.println("data.length : " + data.length);
            criteria.addCriteria("manner", data);
        }
        trackGird.fetchData(criteria);
    }
}
