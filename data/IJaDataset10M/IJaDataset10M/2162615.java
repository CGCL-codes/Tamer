package de.creatronix.artist3k.controller.action;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import com.keypoint.PngEncoder;
import de.creatronix.artist3k.controller.form.StatsForm;
import de.creatronix.artist3k.model.ApplicationManager;
import de.creatronix.artist3k.model.EventManager;
import de.creatronix.artist3k.model.LocationManager;
import de.creatronix.artist3k.model.ModelController;
import de.creatronix.artist3k.model.StageActManager;

public class StatsAction extends Action {

    private static PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Clubs / Kneipen", new Double(43.2));
        dataset.setValue("Open Air", new Double(10.0));
        dataset.setValue("Juz", new Double(27.5));
        dataset.setValue("ISP", new Double(17.5));
        return dataset;
    }

    private static JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart("Booking Statistiken", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionOutlinesVisible(false);
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));
        plot.setNoDataMessage("No data available");
        plot.setSimpleLabels(true);
        return chart;
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        StatsForm statsForm = (StatsForm) form;
        EventManager eventManager = ModelController.getInstance().getEventManager();
        ApplicationManager appManager = ModelController.getInstance().getApplicationManager();
        LocationManager locManager = ModelController.getInstance().getLocationManager();
        StageActManager stageActManager = ModelController.getInstance().getStageActManager();
        statsForm.setNumberOfEvents(eventManager.getNumberOfEvents());
        statsForm.setNumberOfApplications(appManager.getNumberOfApplications());
        statsForm.setNumberOfLocations(locManager.getNumberOfLocations());
        statsForm.setNumberOfStageActs(stageActManager.getNumberOfStageActs());
        return mapping.findForward("stats");
    }
}
