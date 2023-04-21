package org.fao.fenix.web.modules.fpi.client.view.dataentry;

import org.fao.fenix.web.modules.core.client.Fenix;
import org.fao.fenix.web.modules.shared.window.client.view.FenixWindow;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;

public class FPIDataEntryWindow extends FenixWindow {

    protected FPIDataEntryParametersPanel fpiDataEntryParametersPanel;

    protected VerticalPanel parametersPanel;

    public void build() {
        buildToolbar();
        buildWestPanel();
        buildCenterPanel();
        format();
    }

    protected void format() {
        setSize(1000, 400);
        setTitle(Fenix.fpiLang.dataEntry());
        getWindow().setCollapsible(true);
        getWindow().setHeading(Fenix.fpiLang.fpiTool());
    }

    protected void buildToolbar() {
    }

    protected void buildCenterPanel() {
        FPIDataEntryTabPanel tabPanel = new FPIDataEntryTabPanel();
        fillCenterPart(tabPanel.build());
        getCenterData().setSize(500);
        getCenter().setHeading(Fenix.fpiLang.dataEntry());
    }

    protected void buildWestPanel() {
        fpiDataEntryParametersPanel = new FPIDataEntryParametersPanel();
        parametersPanel = fpiDataEntryParametersPanel.build();
        fillWestPart(parametersPanel);
        getWestData().setSize(350);
        getWest().setHeading(Fenix.fpiLang.parameters());
    }

    public void setParameterValue(String parameter, Object value) {
        fpiDataEntryParametersPanel.setParameterValue((HorizontalPanel) parametersPanel.getData(parameter), value.toString());
    }
}
