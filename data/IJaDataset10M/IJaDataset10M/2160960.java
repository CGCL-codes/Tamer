package ro.gateway.aida.charts;

import java.io.IOException;
import java.text.NumberFormat;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.BarRenderer;
import org.jfree.chart.renderer.BarRenderer3D;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.DefaultCategoryDataset;
import ro.xblue.translator.LanguageBean;
import ro.xblue.translator.Module_DB;
import ro.xblue.translator.TranslatorConstants;
import ro.xblue.translator.TranslatorUtils;
import ro.xblue.translator.Translator_DB;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Jun 20, 2004
 * Time: 12:10:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class BarChartRenderer {

    public static void make_barchart(HttpServletRequest request, ServletContext application, ChartWizzard$ActionForm form) throws IOException, ChartException {
        HttpSession session = request.getSession();
        LanguageBean lang_bean = TranslatorUtils.getCurrentLanguage(request, application);
        Translator_DB translator_db = (Translator_DB) application.getAttribute(TranslatorConstants.DB_TRANSLATOR);
        Module_DB tr_module = translator_db.getModule("charts");
        String x_axe = "";
        if (form.x_axe_group_criteria != null) {
            x_axe = tr_module != null ? tr_module.getMessage(lang_bean.language, form.x_axe_group_criteria) : form.x_axe_group_criteria;
            if (x_axe == null) {
                x_axe = form.x_axe_group_criteria;
            }
        } else {
            x_axe = tr_module != null ? tr_module.getMessage(lang_bean.language, form.x_axe_criteria) : form.x_axe_criteria;
            if (x_axe == null) {
                x_axe = form.x_axe_criteria;
            }
        }
        String y_axe = "";
        if (form.y_axe_criteria != null) {
            y_axe = tr_module != null ? tr_module.getMessage(lang_bean.language, form.y_axe_criteria) : form.y_axe_criteria;
            if (y_axe == null) {
                y_axe = form.y_axe_criteria;
            }
        }
        System.out.println("y axe(" + lang_bean.language + "):" + y_axe);
        DefaultCategoryDataset category_dataset = new DefaultCategoryDataset();
        BarChartDataMan.populate_category_dataset(category_dataset, lang_bean.language, y_axe, form);
        if (category_dataset.getColumnCount() + category_dataset.getRowCount() < 1) {
            throw new ChartException("No data to display");
        }
        AbstractCategoryItemRenderer renderer = null;
        if (form.chart_type.endsWith("3d")) {
            renderer = new BarRenderer3D();
        } else {
            renderer = new BarRenderer();
        }
        renderer.setToolTipGenerator(new StandardCategoryItemLabelGenerator(NumberFormat.getInstance()));
        renderer.setLabelGenerator(new StandardCategoryItemLabelGenerator(NumberFormat.getInstance()));
        CategoryAxis cat_axis = new CategoryAxis(x_axe);
        ValueAxis val_axis = new NumberAxis(y_axe);
        CategoryPlot plot = new CategoryPlot(category_dataset, cat_axis, val_axis, renderer);
        if (category_dataset.getColumnCount() > 7) {
            plot.setOrientation(PlotOrientation.HORIZONTAL);
        }
        JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, form.x_axe_group_criteria != null);
        chart.setBackgroundPaint(java.awt.Color.white);
        chart.setAntiAlias(true);
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
        form.file_name = ServletUtilities.saveChartAsPNG(chart, form.chartwidth, form.chartheight, info, session);
        form.tooltipmap = ChartUtilities.getImageMap(form.file_name, info);
    }
}
