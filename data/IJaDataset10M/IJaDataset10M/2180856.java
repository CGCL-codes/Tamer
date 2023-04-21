package com.ncs.crm.client.welcome;

import java.util.Date;
import com.ncs.crm.client.entity.User;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class SystemNoticeSection extends SectionStackSection {

    private ListGrid grid;

    private User loginUser;

    public SystemNoticeSection(User user) {
        this.loginUser = user;
        setTitle("公告信息");
        grid = new SystemNoticeListGrid();
        setItems(grid);
        setResizeable(false);
        setCanCollapse(false);
        setExpanded(true);
        ImgButton addButton = new ImgButton();
        addButton.setTitle("新增");
        addButton.setPrompt("新增公司信息");
        addButton.setSrc("demoApp/add.png");
        addButton.setSize(16);
        addButton.setShowFocused(false);
        addButton.setShowRollOver(false);
        addButton.setShowDown(false);
        addButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                addDeptFunction();
            }
        });
        ImgButton editButton = new ImgButton();
        editButton.setTitle("修改");
        editButton.setPrompt("修改公司信息");
        editButton.setSrc("demoApp/edit.png");
        editButton.setSize(16);
        editButton.setShowFocused(false);
        editButton.setShowRollOver(false);
        editButton.setShowDown(false);
        editButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                editDeptFunction();
            }
        });
        ImgButton removeButton = new ImgButton();
        removeButton.setTitle("删除");
        removeButton.setPrompt("删除公司信息");
        removeButton.setSrc("demoApp/remove.png");
        removeButton.setSize(16);
        removeButton.setShowFocused(false);
        removeButton.setShowRollOver(false);
        removeButton.setShowDown(false);
        removeButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                delDeptFunction();
            }
        });
        ImgButton viewButton = new ImgButton();
        viewButton.setTitle("查看");
        viewButton.setPrompt("查看公司信息");
        viewButton.setSrc("demoApp/view.png");
        viewButton.setSize(16);
        viewButton.setShowFocused(false);
        viewButton.setShowRollOver(false);
        viewButton.setShowDown(false);
        viewButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                viewDeptFunction();
            }
        });
        if (user.getRoleId().indexOf("1") >= 0) {
            setControls(addButton, editButton, removeButton);
        }
    }

    private void addDeptFunction() {
        final SystemNoticeEditWindow w = new SystemNoticeEditWindow();
        w.setTitle("新建公司信息");
        final SystemNoticeEditForm form = new SystemNoticeEditForm();
        w.addItem(form);
        IButton btn = new IButton("保存");
        w.addItem(btn);
        btn.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (form.validate()) {
                    form.getField("addedBy").setValue(loginUser.getId());
                    form.getField("createTime").setValue(new Date());
                    form.getField("updateTime").setValue(new Date());
                    form.saveData(new DSCallback() {

                        public void execute(DSResponse response, Object rawData, DSRequest request) {
                            if (response.getStatus() >= 0) {
                                w.destroy();
                            }
                        }
                    });
                }
            }
        });
        w.show();
    }

    private void editDeptFunction() {
        if (grid.getSelectedRecord() == null) {
            SC.say("请选择一条记录!");
            return;
        }
        final SystemNoticeEditWindow w = new SystemNoticeEditWindow();
        w.setTitle("修改合同类型");
        final SystemNoticeEditForm form = new SystemNoticeEditForm();
        Record r = grid.getSelectedRecord();
        form.editRecord(r);
        w.addItem(form);
        IButton btn = new IButton("保存");
        w.addItem(btn);
        btn.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (form.validate()) {
                    form.getField("updateTime").setValue(new Date());
                    form.saveData(new DSCallback() {

                        public void execute(DSResponse response, Object rawData, DSRequest request) {
                            if (response.getStatus() >= 0) {
                                w.destroy();
                            }
                        }
                    });
                }
            }
        });
        w.show();
    }

    private void delDeptFunction() {
        if (grid.getSelectedRecord() == null) {
            SC.say("请选择一条记录!");
            return;
        }
        SC.confirm("注意", "确认要删除吗？", new BooleanCallback() {

            public void execute(Boolean value) {
                if (value) {
                    Record r = grid.getSelectedRecord();
                    r.setAttribute("isDelete", "1");
                    grid.updateData(r);
                }
            }
        });
    }

    private void viewDeptFunction() {
    }
}
