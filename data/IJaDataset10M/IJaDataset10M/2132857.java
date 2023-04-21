package com.ncs.crm.client.report;

import com.ncs.crm.client.ProtoPage;
import com.ncs.crm.client.data.PanelFactory;
import com.ncs.crm.client.entity.ClientConfig;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class TrackReport extends ProtoPage {

    private ListGrid trackReportListGrid;

    private Label operationLabel;

    private static final String DESCRIPTION = "查询统计员工的拜访量";

    public static class Factory implements PanelFactory {

        private String id;

        public Canvas create(ClientConfig config) {
            TrackReport tr = new TrackReport(config);
            id = tr.getID();
            return tr;
        }

        public String getDescription() {
            return DESCRIPTION;
        }

        public String getID() {
            return id;
        }
    }

    public TrackReport() {
    }

    public TrackReport(ClientConfig config) {
        setHeight100();
        setWidth100();
        SectionStack mainStack = new SectionStack();
        mainStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        mainStack.setAnimateSections(true);
        trackReportListGrid = new TrackReportListGrid();
        operationLabel = new Label();
        operationLabel.setHeight(20);
        SectionStackSection top_section = new SectionStackSection("查询选项");
        top_section.setItems(new TrackSearchForm(config, trackReportListGrid, operationLabel));
        top_section.setExpanded(true);
        top_section.setCanCollapse(false);
        SectionStackSection mid_section = new SectionStackSection("员工列表");
        mid_section.setItems(trackReportListGrid);
        mid_section.setExpanded(true);
        mid_section.setCanCollapse(false);
        final IButton btnPrint = new IButton("打印预览");
        btnPrint.setWidth(80);
        btnPrint.setIcon("bugList/printer.png");
        btnPrint.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                Canvas.showPrintPreview(trackReportListGrid);
            }
        });
        mid_section.setControls(btnPrint);
        mainStack.setSections(top_section, mid_section);
        addMember(mainStack);
        show();
    }
}
