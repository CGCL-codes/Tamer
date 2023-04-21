package icescrum2.presentation.app.chart;

import icescrum2.presentation.app.releasebrowser.ReleaseBrowserSession;
import icescrum2.presentation.model.ProductImpl;
import icescrum2.presentation.model.ReleaseImpl;
import icescrum2.presentation.model.SprintImpl;
import icescrum2.presentation.scrumos.LocaleBean;
import icescrum2.presentation.scrumos.ScrumOScontroller;
import icescrum2.presentation.scrumos.ScrumOSutil;
import icescrum2.presentation.scrumos.model.Widgetable;
import icescrum2.presentation.scrumos.model.Windowable;
import icescrum2.service.chart.BurndownChartProduct;
import icescrum2.service.chart.BurndownChartRelease;
import icescrum2.service.chart.BurndownChartSprint;
import icescrum2.service.chart.BurnupChartProduct;
import icescrum2.service.chart.ChartIS2;
import icescrum2.service.chart.CollaborationRadarChart;
import icescrum2.service.chart.ParkingLotThemeReleaseChart;
import icescrum2.service.chart.VelocityChartSprint;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import com.icesoft.faces.component.outputchart.OutputChart;
import icescrum2.presentation.app.releasebrowser.ReleaseBrowserUI;
import icescrum2.service.chart.BurnupChartSprint;
import icescrum2.service.chart.DiffVelocity;
import icescrum2.service.chart.GlobalChartTest;
import icescrum2.service.chart.PointBurnupChartProduct;

public class ChartUI implements Windowable {

    private String windowTitle = LocaleBean.get("is2_chart");

    private String windowIdTitle = "is2_chart";

    private String windowPath = "/WEB-INF/classes/icescrum2/presentation/app/chart/ChartUI.jspx";

    private String id = "chartUI";

    private ScrumOScontroller scrumOS;

    private String clickedValue = "";

    private ChartIS2 currentChart;

    private boolean chartRendered = true;

    public boolean isChartRendered() {
        return chartRendered;
    }

    public void setChartRendered(boolean chartRendered) {
        this.chartRendered = chartRendered;
    }

    public ChartUI() {
        scrumOS = (ScrumOScontroller) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{scrumOS}").getValue(FacesContext.getCurrentInstance());
        if (currentChart == null && scrumOS.getMainWindow() != null && scrumOS.getMainWindow().getWindowId().equalsIgnoreCase(this.windowIdTitle)) {
            this.chartRendered = false;
            scrumOS.closeWindow();
        }
    }

    public void renderBurndownChartSprint(ActionEvent ae) {
        this.setClickedValue("");
        SprintImpl sprint = (SprintImpl) ScrumOSutil.getObjectFromRepeatTag(ae.getComponent());
        ((ReleaseBrowserSession) scrumOS.getManagedValue("#{releaseBrowserSession}")).setCurrentSprint(sprint);
        SprintImpl s = ((ReleaseBrowserSession) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{releaseBrowserSession}").getValue(FacesContext.getCurrentInstance())).getCurrentSprint();
        currentChart = new BurndownChartSprint(s.getEntity());
        if (currentChart.isRendered()) {
            scrumOS.getAppLauncher().goChart();
            this.chartRendered = true;
        } else ScrumOSutil.displayWarning("is2_chart_empty");
    }

    public void renderParkingLotThemeRelease(ActionEvent ae) {
        this.setClickedValue("");
        ((ReleaseBrowserSession) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{releaseBrowserSession}").getValue(FacesContext.getCurrentInstance())).setCurrentRelease((ReleaseImpl) ScrumOSutil.getObjectFromRepeatTag(ae.getComponent()));
        ProductImpl p = ((ProductImpl) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{scrumOS.product}").getValue(FacesContext.getCurrentInstance()));
        ReleaseImpl r = (ReleaseImpl) ((ReleaseBrowserUI) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{releaseBrowserUI}").getValue(FacesContext.getCurrentInstance())).getCurrentRelease();
        currentChart = new ParkingLotThemeReleaseChart(p.getEntity(), r.getEntity());
        if (currentChart.isRendered()) {
            scrumOS.getAppLauncher().goChart();
            this.chartRendered = true;
        } else ScrumOSutil.displayWarning("is2_chart_empty");
    }

    public void renderParkingLotThemeProduct(ActionEvent ae) {
        this.setClickedValue("");
        ((ReleaseBrowserSession) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{releaseBrowserSession}").getValue(FacesContext.getCurrentInstance())).setCurrentRelease((ReleaseImpl) ScrumOSutil.getObjectFromRepeatTag(ae.getComponent()));
        ProductImpl p = ((ProductImpl) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{scrumOS.product}").getValue(FacesContext.getCurrentInstance()));
        currentChart = new ParkingLotThemeReleaseChart(p.getEntity(), null);
        if (currentChart.isRendered()) {
            scrumOS.getAppLauncher().goChart();
            this.chartRendered = true;
        } else ScrumOSutil.displayWarning("is2_chart_empty");
    }

    public void renderBurndownChartProduct(ActionEvent ae) {
        this.setClickedValue("");
        ProductImpl p = ((ProductImpl) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{scrumOS.product}").getValue(FacesContext.getCurrentInstance()));
        currentChart = new BurndownChartProduct(p.getEntity(), scrumOS.getUser());
        if (currentChart.isRendered()) {
            scrumOS.getAppLauncher().goChart();
            this.chartRendered = true;
        } else ScrumOSutil.displayWarning("is2_chart_empty");
    }

    public void renderRealVelocity(ActionEvent ae) {
        this.setClickedValue("");
        ProductImpl p = ((ProductImpl) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{scrumOS.product}").getValue(FacesContext.getCurrentInstance()));
        currentChart = new DiffVelocity(p.getEntity(), scrumOS.getUser());
        if (currentChart.isRendered()) {
            scrumOS.getAppLauncher().goChart();
            this.chartRendered = true;
        } else ScrumOSutil.displayWarning("is2_chart_empty");
    }

    public void renderGlobalTest(ActionEvent ae) {
        this.setClickedValue("");
        ProductImpl p = ((ProductImpl) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{scrumOS.product}").getValue(FacesContext.getCurrentInstance()));
        currentChart = new GlobalChartTest(p.getEntity());
        if (currentChart.isRendered()) {
            scrumOS.getAppLauncher().goChart();
            this.chartRendered = true;
        } else ScrumOSutil.displayWarning("is2_chart_empty");
    }

    public void goToPreviousWindow(ActionEvent ae) {
        scrumOS.openWindow(scrumOS.getLastWindow(), Boolean.TRUE);
    }

    public void renderBurnupChartProduct(ActionEvent ae) {
        this.setClickedValue("");
        ProductImpl p = ((ProductImpl) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{scrumOS.product}").getValue(FacesContext.getCurrentInstance()));
        currentChart = new BurnupChartProduct(p.getEntity());
        if (currentChart.isRendered()) {
            scrumOS.getAppLauncher().goChart();
            this.chartRendered = true;
        } else ScrumOSutil.displayWarning("is2_chart_empty");
    }

    public void renderBurnupChartSprint(ActionEvent ae) {
        this.setClickedValue("");
        SprintImpl sprint = (SprintImpl) ScrumOSutil.getObjectFromRepeatTag(ae.getComponent());
        ((ReleaseBrowserSession) scrumOS.getManagedValue("#{releaseBrowserSession}")).setCurrentSprint(sprint);
        SprintImpl s = ((ReleaseBrowserSession) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{releaseBrowserSession}").getValue(FacesContext.getCurrentInstance())).getCurrentSprint();
        currentChart = new BurnupChartSprint(s.getEntity());
        if (currentChart.isRendered()) {
            scrumOS.getAppLauncher().goChart();
            this.chartRendered = true;
        } else ScrumOSutil.displayWarning("is2_chart_empty");
    }

    public void renderPointBurnupChartProduct(ActionEvent ae) {
        this.setClickedValue("");
        ProductImpl p = ((ProductImpl) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{scrumOS.product}").getValue(FacesContext.getCurrentInstance()));
        currentChart = new PointBurnupChartProduct(p.getEntity());
        if (currentChart.isRendered()) {
            scrumOS.getAppLauncher().goChart();
            this.chartRendered = true;
        } else ScrumOSutil.displayWarning("is2_chart_empty");
    }

    public void renderBurndownChartRelease(ActionEvent ae) {
        this.setClickedValue("");
        ReleaseImpl r = (ReleaseImpl) ((ReleaseBrowserUI) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{releaseBrowserUI}").getValue(FacesContext.getCurrentInstance())).getCurrentRelease();
        currentChart = new BurndownChartRelease(r.getEntity());
        if (currentChart.isRendered()) {
            scrumOS.getAppLauncher().goChart();
            this.chartRendered = true;
        } else ScrumOSutil.displayWarning("is2_chart_empty");
    }

    public void renderCollaborationChartProduct(ActionEvent ae) {
        this.setClickedValue("");
        currentChart = new CollaborationRadarChart();
        if (currentChart.isRendered()) {
            scrumOS.getAppLauncher().goChart();
            this.chartRendered = true;
        } else ScrumOSutil.displayWarning("is2_chart_empty");
    }

    public void renderVelocityChartSprint(ActionEvent ae) {
        this.setClickedValue("");
        ProductImpl p = ((ProductImpl) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{scrumOS.product}").getValue(FacesContext.getCurrentInstance()));
        currentChart = new VelocityChartSprint(p.getEntity());
        if (currentChart.isRendered()) {
            scrumOS.getAppLauncher().goChart();
            this.chartRendered = true;
        } else ScrumOSutil.displayWarning("is2_chart_empty");
    }

    public boolean render(OutputChart c) {
        if (currentChart != null) {
            c.setChart(currentChart.getChart());
        }
        return true;
    }

    public void action(ActionEvent event) {
        if (event.getSource() instanceof OutputChart) {
            OutputChart chart = (OutputChart) event.getSource();
            if (chart.getClickedImageMapArea().getXAxisLabel() != null) {
                setClickedValue("[ " + chart.getClickedImageMapArea().getXAxisLabel() + " ]  :  " + chart.getClickedImageMapArea().getValue());
            }
        }
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public void closeWindow() {
    }

    public boolean processContent() {
        return true;
    }

    public String getWindowId() {
        return windowIdTitle;
    }

    public Boolean getWidgetable() {
        return this instanceof Widgetable;
    }

    public void openWindow() {
    }

    public ChartIS2 getCurrentChart() {
        return currentChart;
    }

    public void setCurrentChart(ChartIS2 chart) {
        this.currentChart = chart;
    }

    public String getWindowPath() {
        return windowPath;
    }

    public boolean prepareWindow() {
        return true;
    }

    public String getClickedValue() {
        return clickedValue;
    }

    public void setClickedValue(String clickedValue) {
        this.clickedValue = clickedValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMainWindow() {
        return this.scrumOS.getMainWindow().getId();
    }
}
