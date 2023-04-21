package com.ncs.crm.client.customer2;

import com.ncs.crm.client.ProtoPage;
import com.ncs.crm.client.data.PanelFactory;
import com.ncs.crm.client.data.ServerCountLabel;
import com.ncs.crm.client.entity.ClientConfig;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class VisitQuery extends ProtoPage {

    private static final String DESCRIPTION = "查看自己或下属业务员的拜访记录。";

    private ListGrid visitListGrid;

    private VisitTabSet visitTabSet;

    private ClientConfig config;

    private ServerCountLabel label;

    public static class Factory implements PanelFactory {

        private String id;

        public Canvas create(ClientConfig cc) {
            VisitQuery vq = new VisitQuery(cc);
            id = vq.getID();
            return vq;
        }

        public String getDescription() {
            return DESCRIPTION;
        }

        public String getID() {
            return id;
        }
    }

    public VisitQuery() {
    }

    public VisitQuery(ClientConfig cc) {
        visitListGrid = new VisitListGrid();
        this.config = cc;
        SectionStack stack_main = new SectionStack();
        stack_main.setVisibilityMode(VisibilityMode.MULTIPLE);
        stack_main.setAnimateSections(true);
        label = new ServerCountLabel();
        SectionStackSection section_top = new SectionStackSection("拜访查询");
        section_top.setItems(new VisitSearchForm(config, visitListGrid, label));
        section_top.setExpanded(true);
        section_top.setResizeable(true);
        SectionStackSection section_mid = new SectionStackSection("客户列表");
        section_mid.setResizeable(true);
        visitListGrid.addDataArrivedHandler(new DataArrivedHandler() {

            public void onDataArrived(DataArrivedEvent event) {
                label.incrementAndUpdate2(event.getStartRow(), event.getEndRow());
            }
        });
        DataSource customerDS = DataSource.get("servingCustomer");
        visitTabSet = new VisitTabSet(visitListGrid, customerDS);
        visitListGrid.addCellClickHandler(new CellClickHandler() {

            public void onCellClick(CellClickEvent event) {
                visitTabSet.viewInfo();
            }
        });
        section_mid.setItems(visitListGrid, label);
        section_mid.setExpanded(true);
        SectionStackSection section_detail = new SectionStackSection("详细信息");
        section_detail.setItems(visitTabSet);
        section_detail.setExpanded(true);
        stack_main.setSections(section_top, section_mid, section_detail);
        addMember(stack_main);
    }
}
