package com.wonebiz.crm.client.welcome;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class PicSettingWindow extends Window implements EntryPoint {

    public PicSettingWindow() {
        setTitle("设置图片(修改后重新登录生效)");
        setWidth(680);
        setHeight(420);
        setAutoCenter(true);
        setCanDrag(false);
        setCanDragResize(false);
        setCanDragReposition(false);
        setIsModal(true);
        setShowCloseButton(true);
        setShowMinimizeButton(false);
        setShowModalMask(true);
        SectionStack stack = new SectionStack();
        stack.setVisibilityMode(VisibilityMode.MULTIPLE);
        stack.setAnimateSections(true);
        SectionStackSection section1 = new SectionStackSection("图片列表");
        final ListGrid grid = new PicListGrid();
        section1.setItems(grid);
        section1.setExpanded(true);
        section1.setCanCollapse(false);
        IButton btnUpload = new IButton("上传图片");
        btnUpload.setIcon("demoApp/upload.png");
        btnUpload.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                new PicUploadWindow().show();
            }
        });
        IButton btnFresh = new IButton("刷新");
        btnFresh.setIcon("demoApp/refresh.png");
        btnFresh.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                grid.invalidateCache();
                grid.fetchData();
            }
        });
        IButton btnView = new IButton("预览");
        btnView.setIcon("demoApp/view.png");
        btnView.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Window w = new Window();
                w.setTitle("预览");
                w.setAutoCenter(true);
                w.setHeight(240);
                w.setWidth(440);
                w.setPadding(0);
                w.setIsModal(true);
                w.setShowMinimizeButton(false);
                w.addItem(new NewsPicPane());
                w.show();
            }
        });
        IButton btnDel = new IButton("删除图片");
        btnDel.setIcon("demoApp/icon_delete.png");
        btnDel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (grid.getSelectedRecord() == null) {
                    SC.say("请选择图片");
                    return;
                } else {
                    SC.confirm("确认要删除吗?", new BooleanCallback() {

                        public void execute(Boolean value) {
                            if (value) {
                                grid.removeSelectedData();
                            }
                        }
                    });
                }
            }
        });
        section1.setControls(btnUpload, btnFresh, btnView, btnDel);
        stack.setSections(section1);
        VLayout vLayout = new VLayout(20);
        HLayout hLayout = new HLayout(10);
        hLayout.addMember(stack);
        vLayout.addMember(hLayout);
        addItem(vLayout);
    }

    public void onModuleLoad() {
        new PicSettingWindow().show();
    }
}
