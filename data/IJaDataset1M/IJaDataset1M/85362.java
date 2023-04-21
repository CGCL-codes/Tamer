package com.employee.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.jrefinery.chart.ChartFactory;
import com.jrefinery.chart.ChartUtilities;
import com.jrefinery.chart.JFreeChart;
import com.jrefinery.data.CategoryDataset;
import com.jrefinery.data.DefaultCategoryDataset;
import com.jrefinery.data.DefaultPieDataset;

public class CreateSampleChartAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("JavaWorld", new Integer(75));
        pieDataset.setValue("Other", new Integer(25));
        String[] seriesNames = new String[] { "2001", "2002" };
        String[] categoryNames = new String[] { "First Quater", "Second Quater" };
        Number[][] categoryData = new Integer[][] { { new Integer(20), new Integer(35) }, { new Integer(40), new Integer(60) } };
        CategoryDataset categoryDataset = new DefaultCategoryDataset(seriesNames, categoryNames, categoryData);
        JFreeChart chart = ChartFactory.createVerticalBarChart3D("Sample Category Chart", "Quarters", "Sales", categoryDataset, true);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            BufferedImage image = chart.createBufferedImage(500, 300);
            ChartUtilities.writeBufferedImageAsPNG(out, image);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
        return mapping.findForward("showChart");
    }
}
