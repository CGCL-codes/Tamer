package com.openospc.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import com.openospc.db.Db;
import com.openospc.db.DbData;
import com.openospc.department.Machine;
import com.openospc.product.process.ControlChart;

public class XBarLineChart extends OpenospcChart {

    protected final Log logger = LogFactory.getLog(getClass());

    public JFreeChart getChart(ControlChart cc, String mcid, com.openospc.product.Process process) {
        JFreeChart chart = ChartFactory.createLineChart(cc.getName(), cc.getXLabel(), cc.getYLabel(), getXBarCategoryDataSet(cc, mcid, process), PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(cc.getLowestLimit(), cc.getHighestLimit());
        Machine m;
        try {
            m = getMachine(mcid);
            chart.addSubtitle(new TextTitle(m.getMachineName()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 0, 205));
        renderer.setSeriesShapesVisible(0, true);
        plot.getRenderer().setSeriesStroke(0, new BasicStroke(2.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_ROUND, 0.0f, new float[] { 2.0f, 0.0f }, 0.0f));
        renderer.setSeriesPaint(1, new Color(255, 0, 0));
        renderer.setSeriesShapesVisible(1, false);
        plot.getRenderer().setSeriesStroke(1, new BasicStroke(2.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_ROUND, 0.0f, new float[] { 2.0f, 0.0f }, 0.0f));
        renderer.setSeriesPaint(2, new Color(255, 0, 0));
        renderer.setSeriesShapesVisible(2, false);
        plot.getRenderer().setSeriesStroke(2, new BasicStroke(2.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_ROUND, 0.0f, new float[] { 2.0f, 0.0f }, 0.0f));
        renderer.setSeriesPaint(3, new Color(118, 238, 0));
        renderer.setSeriesShapesVisible(3, false);
        plot.getRenderer().setSeriesStroke(3, new BasicStroke(2.0f, BasicStroke.JOIN_ROUND, BasicStroke.JOIN_ROUND, 0.0f, new float[] { 2.0f, 0.0f }, 0.0f));
        renderer.setSeriesPaint(4, new Color(255, 0, 0));
        renderer.setSeriesShapesVisible(4, false);
        plot.getRenderer().setSeriesStroke(4, new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[] { 5.0f, 6.0f }, 0.0f));
        renderer.setSeriesPaint(5, new Color(118, 238, 0));
        renderer.setSeriesShapesVisible(5, false);
        plot.getRenderer().setSeriesStroke(5, new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[] { 5.0f, 6.0f }, 0.0f));
        renderer.setSeriesPaint(6, new Color(255, 0, 0));
        renderer.setSeriesShapesVisible(6, false);
        plot.getRenderer().setSeriesStroke(6, new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[] { 5.0f, 6.0f }, 0.0f));
        return chart;
    }

    public Machine getMachine(String mcid) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Machine m = new Machine();
        String sql = "SELECT * from Departments d,  Machines m where MachineId = '" + mcid + "' " + "AND d.DepartmentId = m.DepartmentId " + "order by d.Name, m.Name ";
        Db db = new Db();
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            m.setCode(rs.getString("d.Code"));
            m.setId(rs.getString("d.DepartmentId"));
            m.setMachineId(rs.getString("m.MachineId"));
            m.setMachineName(rs.getString("m.Name"));
            m.setMachineNumber(rs.getString("m.Number"));
            m.setName(rs.getString("d.Name"));
        }
        db.close();
        return m;
    }

    public DefaultCategoryDataset getXBarCategoryDataSet(ControlChart cc, String mcid, com.openospc.product.Process process) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            String sql = "SELECT " + cc.getInitialSummaryField() + ", d.BatchNumber FROM Data" + process.getProcessId() + " d, " + "Summary" + process.getProcessId() + " s " + "where s.Id = d.Id AND MachineId = '" + mcid + "'   AND isactive = 'Y' AND status='CO'   order by BatchNumber ";
            logger.info("TERA ::" + sql);
            DbData dbdata = new DbData();
            Statement stmt = dbdata.getStatement();
            ResultSet rs;
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                dataset.addValue(rs.getFloat(cc.getInitialSummaryField()), "X Bar", rs.getString("BatchNumber"));
                logger.info("USL " + cc.getUSL());
                dataset.addValue(cc.getUSL(), "USL", rs.getString("BatchNumber"));
                dataset.addValue(cc.getLSL(), "LSL", rs.getString("BatchNumber"));
                dataset.addValue(cc.getSL(), "SL", rs.getString("BatchNumber"));
                dataset.addValue(cc.getUCL(), "UCL", rs.getString("BatchNumber"));
                dataset.addValue(cc.getCL(), "CL", rs.getString("BatchNumber"));
                dataset.addValue(cc.getLCL(), "LCL", rs.getString("BatchNumber"));
            }
            rs.close();
            dbdata.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return dataset;
    }
}
