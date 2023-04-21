package org.wcb.gui.forms.report;

import org.wcb.model.bd.LogbookDelegate;
import org.wcb.model.vo.hibernate.Logbook;
import org.wcb.model.util.SpringUtil;
import org.wcb.model.service.IServicesConstants;
import org.wcb.resources.MessageResourceRegister;
import org.wcb.resources.MessageKey;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.JPanel;
import java.awt.Font;
import java.text.DecimalFormat;

/**
 * <small>
 * Copyright (c)  2006  wbogaardt.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * <p/>
 * $File:  $ <br>
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Apr 3, 2006 2:53:41 PM $ <br>
 * </small>
 *
 * @author wbogaardt
 * @version 1
 *          Date: Apr 3, 2006
 *          Time: 2:53:41 PM
 */
public class TotalTimeAircraftReportForm extends JPanel {

    private LogbookDelegate delegate;

    private String registration;

    private final DecimalFormat formatter = new DecimalFormat("###,##0.0");

    private Double totalTime = 0.0;

    /**
     * This report shows the total time in a selected aircraft.
     * @param reg The registration number of the aircraft.
     */
    public TotalTimeAircraftReportForm(String reg) {
        registration = reg;
        delegate = (LogbookDelegate) SpringUtil.getApplicationContext().getBean(IServicesConstants.LOGBOOK_DELEGATE);
        initComponents();
    }

    /**
     * Sets the registration number of the aircraft.
     * @param reg The registration number of the aircraft.
     */
    public void setRegistration(String reg) {
        this.registration = reg;
    }

    /**
     * Initialize components.
     */
    private void initComponents() {
        DefaultPieDataset dataset = groupByAircraft(this.registration);
        JFreeChart chart = ChartFactory.createPieChart("Total Time in " + registration + " - " + formatter.format(totalTime) + " hours", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionOutlinesVisible(false);
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage(MessageResourceRegister.getInstance().getValue(MessageKey.LABEL_NODATA));
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        add(new ChartPanel(chart));
    }

    /**
     * Calculate based on aircraft registration number. Create
     * a pie dataset.
     * @param nNumber Aircraft registration number.
     * @return Pie data set for jfree chart.
     */
    private DefaultPieDataset groupByAircraft(String nNumber) {
        java.util.List<Logbook> rows = delegate.findByRegistration(nNumber);
        Double pic = 0.0;
        Double sic = 0.0;
        Double xCountry = 0.0;
        Double instructing = 0.0;
        Double safety = 0.0;
        Double dual = 0.0;
        Double solo = 0.0;
        Double actualIMC = 0.0;
        Double simulatedIMC = 0.0;
        Double nightFlight = 0.0;
        totalTime = 0.0;
        for (Logbook item : rows) {
            totalTime = totalTime + item.getFlightDuration();
            pic = pic + item.getPic();
            sic = sic + item.getSic();
            xCountry = xCountry + item.getCrossCountry();
            instructing = instructing + item.getFlightInstructing();
            safety = safety + item.getSafetyPilot();
            dual = dual + item.getDualReceived();
            solo = solo + item.getSolo();
            nightFlight = nightFlight + item.getConditionNight();
            actualIMC = actualIMC + item.getConditionActualImc();
            simulatedIMC = simulatedIMC + item.getConditionSimulatedImc();
        }
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("PIC", pic);
        dataset.setValue("SIC", sic);
        dataset.setValue("X-country", xCountry);
        dataset.setValue("Instructing", instructing);
        dataset.setValue("Safety Pilot", safety);
        dataset.setValue("Dual", dual);
        dataset.setValue("Solo", solo);
        dataset.setValue("Actual IMC", actualIMC);
        dataset.setValue("Hood", simulatedIMC);
        dataset.setValue("Night", nightFlight);
        return dataset;
    }
}
