package org.fao.fenix.web.modules.birt.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.io.FileUtils;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.emf.common.util.EList;
import org.fao.fenix.domain.exception.FenixException;
import org.fao.fenix.domain.label.CommodityLabel;
import org.fao.fenix.domain.label.GaulLabel;
import org.fao.fenix.domain.perspective.ChartView;
import org.fao.fenix.domain.perspective.DataView;
import org.fao.fenix.domain.perspective.ReportView;
import org.fao.fenix.domain.perspective.SelectedField;
import org.fao.fenix.domain.perspective.SelectedValue;
import org.fao.fenix.domain.perspective.TableView;
import org.fao.fenix.persistence.info.dataset.DatasetDao;
import org.fao.fenix.persistence.perspective.DataViewDao;
import org.fao.fenix.persistence.perspective.MapDao;
import org.fao.fenix.persistence.perspective.TableDao;
import org.fao.fenix.web.modules.birt.client.control.services.BirtService;
import org.fao.fenix.web.modules.birt.client.view.vo.BeanLegend;
import org.fao.fenix.web.modules.birt.client.view.vo.BeanParameter;
import org.fao.fenix.web.modules.birt.client.view.vo.WizardVo;
import org.fao.fenix.web.modules.birt.server.utils.BirtUtil;
import org.fao.fenix.web.modules.birt.server.utils.GraphEngine;
import org.fao.fenix.web.modules.birt.server.utils.RptdesignToString;
import org.fao.fenix.web.modules.birt.server.utils.TableReport;
import org.fao.fenix.web.modules.birt.server.utils.report.AddChartToReport;
import org.fao.fenix.web.modules.birt.server.utils.report.AddMapToReport;
import org.fao.fenix.web.modules.birt.server.utils.report.AddTableToReport;
import org.fao.fenix.web.modules.birt.server.utils.report.AddTextToReport;
import org.fao.fenix.web.modules.core.client.view.vo.FenixResourceMetadataVo;
import org.fao.fenix.web.modules.core.client.view.vo.FenixResourceVo;
import org.fao.fenix.web.modules.core.server.utils.FenixResourceBuilder;
import org.fao.fenix.web.modules.core.server.utils.Setting;
import org.fao.fenix.web.modules.re.server.REServiceImpl;
import org.fao.fenix.web.modules.text.server.TextServiceImpl;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ibm.icu.util.ULocale;

public class BirtServiceImpl extends RemoteServiceServlet implements BirtService {

    DataViewDao dataviewDao;

    REServiceImpl reServiceImpl;

    TextServiceImpl textServiceImpl;

    MapDao mapDao;

    DatasetDao datasetDao;

    TableDao tableDao;

    public String getBirtApplName() {
        return Setting.getBirtApplName();
    }

    public String nameFileById(Long dataviewId) {
        DataView dataView = dataviewDao.findById(dataviewId);
        String nameFile = BirtUtil.randomNameFile();
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + nameFile);
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));
            out.write(dataView.getRptdesign());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FenixException("Unable to create rptdesign file for dataview " + dataView.getResourceId());
        }
        return nameFile;
    }

    public String openReport(Long id, String servletType) {
        return "<iframe src='/" + Setting.getBirtApplName() + "/FenixBirtServlet?dataViewId=" + id + "&servletType=" + servletType + "' width='100%' height='100%' />";
    }

    public String openReport(String rptdesign, String servletType) {
        return "<iframe src='/" + Setting.getBirtApplName() + "/FenixBirtServletByFile?report=" + rptdesign + ".rptdesign&servletType=" + servletType + "' width='100%' height='100%' />";
    }

    public String getTemplate(String templateType) {
        String template = Setting.systemPathBirt + "/" + getBirtApplName() + "/template/" + templateType + ".rptdesign";
        String nameRptdesign = BirtUtil.randomNameFile();
        String renameTemplate = System.getProperty("java.io.tmpdir") + File.separator + nameRptdesign;
        try {
            FileUtils.copyFile(new File(template), new File(renameTemplate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(template);
        System.out.println(renameTemplate);
        return nameRptdesign;
    }

    public List getAllReport() {
        List retList = new ArrayList();
        for (DataView view : dataviewDao.findAll()) {
            if (view.getRptdesign() != null) {
                retList.add(view.getResourceId());
            }
        }
        return retList;
    }

    public Long saveChartLayout(String rptDesign, long dataviewId, String Title) {
        DataView r;
        String rep = new RptdesignToString(rptDesign).getRptdesignLikeString();
        r = dataviewDao.findById(dataviewId);
        r.setRptdesign(rep);
        if (!Title.equals("")) {
            r.setTitle(Title);
        }
        dataviewDao.save(r);
        return r.getResourceId();
    }

    public Long saveChart(String rptDesign, long dataviewId, String Title, WizardVo bean) {
        DataView r = dataviewDao.findById(dataviewId);
        String rep = new RptdesignToString(rptDesign).getRptdesignLikeString();
        List<SelectedField> selectedFieldList = new ArrayList<SelectedField>();
        SelectedField selectedFieldX = new SelectedField();
        selectedFieldX.setFieldName(bean.getDimensionX());
        for (int i = 0; i < bean.getDimensionValuesX().size(); i++) {
            SelectedValue value = new SelectedValue();
            value.setSelectedValue((String) ((List) ((List) bean.getDimensionValuesX()).get(i)).get(0));
            selectedFieldX.getSelectedValues().add(value);
        }
        selectedFieldList.add(selectedFieldX);
        SelectedField selectedFieldY = new SelectedField();
        selectedFieldY.setFieldName(bean.getDimensionY());
        for (int i = 0; i < bean.getDimensionValuesY().size(); i++) {
            SelectedValue value = new SelectedValue();
            value.setSelectedValue((String) ((List) ((List) bean.getDimensionValuesY()).get(i)).get(0));
            selectedFieldY.getSelectedValues().add(value);
        }
        selectedFieldList.add(selectedFieldY);
        SelectedField selectedFieldY2 = new SelectedField();
        selectedFieldY2.setFieldName(bean.getDimensionY());
        for (int i = 0; i < bean.getDimensionValuesYForLine().size(); i++) {
            SelectedValue value = new SelectedValue();
            value.setSelectedValue((String) ((List) ((List) bean.getDimensionValuesYForLine()).get(i)).get(0));
            selectedFieldY2.getSelectedValues().add(value);
        }
        selectedFieldList.add(selectedFieldY2);
        for (int j = 0; j < bean.getOtherDimension().size(); j++) {
            SelectedField selectedFieldOther = new SelectedField();
            selectedFieldOther.setFieldName((String) ((List) bean.getOtherDimension().get(j)).get(0));
            SelectedValue value = new SelectedValue();
            value.setSelectedValue((String) ((List) bean.getOtherDimension().get(j)).get(1));
            selectedFieldOther.getSelectedValues().add(value);
            selectedFieldList.add(selectedFieldOther);
        }
        r.setSelectedFields(selectedFieldList);
        r.setRptdesign(rep);
        r.setDatasetId((Long) bean.getDatasetId().get(0));
        ((ChartView) r).setChartType(bean.getChartType());
        ((ChartView) r).setFlip(bean.isFlip());
        StringTokenizer optionChart = new StringTokenizer(bean.getDim2_3D(), "_");
        List<String> optChart = new ArrayList<String>();
        while (optionChart.hasMoreTokens()) {
            optChart.add(optionChart.nextToken());
        }
        if (optChart.get(0).equals("2D")) {
            ((ChartView) r).setDepth(false);
        } else if (optChart.get(0).equals("2D with depth")) {
            ((ChartView) r).setDepth(true);
        }
        ((ChartView) r).setPosition(optChart.get(1));
        if (!Title.equals("")) {
            r.setTitle(Title);
        }
        dataviewDao.save(r);
        return r.getResourceId();
    }

    public Long saveChartAs(String rptDesign, long dataviewId, String Title, WizardVo bean) {
        DataView r = new ChartView();
        ;
        String rep = new RptdesignToString(rptDesign).getRptdesignLikeString();
        List<SelectedField> selectedFieldList = new ArrayList<SelectedField>();
        SelectedField selectedFieldX = new SelectedField();
        selectedFieldX.setFieldName(bean.getDimensionX());
        for (int i = 0; i < bean.getDimensionValuesX().size(); i++) {
            SelectedValue value = new SelectedValue();
            value.setSelectedValue((String) ((List) ((List) bean.getDimensionValuesX()).get(i)).get(0));
            selectedFieldX.getSelectedValues().add(value);
        }
        selectedFieldList.add(selectedFieldX);
        SelectedField selectedFieldY = new SelectedField();
        selectedFieldY.setFieldName(bean.getDimensionY());
        for (int i = 0; i < bean.getDimensionValuesY().size(); i++) {
            SelectedValue value = new SelectedValue();
            value.setSelectedValue((String) ((List) ((List) bean.getDimensionValuesY()).get(i)).get(0));
            selectedFieldY.getSelectedValues().add(value);
        }
        selectedFieldList.add(selectedFieldY);
        SelectedField selectedFieldY2 = new SelectedField();
        selectedFieldY2.setFieldName(bean.getDimensionY());
        for (int i = 0; i < bean.getDimensionValuesYForLine().size(); i++) {
            SelectedValue value = new SelectedValue();
            value.setSelectedValue((String) ((List) ((List) bean.getDimensionValuesYForLine()).get(i)).get(0));
            selectedFieldY2.getSelectedValues().add(value);
        }
        selectedFieldList.add(selectedFieldY2);
        for (int j = 0; j < bean.getOtherDimension().size(); j++) {
            SelectedField selectedFieldOther = new SelectedField();
            selectedFieldOther.setFieldName((String) ((List) bean.getOtherDimension().get(j)).get(0));
            SelectedValue value = new SelectedValue();
            value.setSelectedValue((String) ((List) bean.getOtherDimension().get(j)).get(1));
            selectedFieldOther.getSelectedValues().add(value);
            selectedFieldList.add(selectedFieldOther);
        }
        r.setSelectedFields(selectedFieldList);
        r.setRptdesign(rep);
        r.setDatasetId((Long) bean.getDatasetId().get(0));
        ((ChartView) r).setChartType(bean.getChartType());
        ((ChartView) r).setFlip(bean.isFlip());
        StringTokenizer optionChart = new StringTokenizer(bean.getDim2_3D(), "_");
        List<String> optChart = new ArrayList<String>();
        while (optionChart.hasMoreTokens()) {
            optChart.add(optionChart.nextToken());
        }
        if (optChart.get(0).equals("2D")) {
            ((ChartView) r).setDepth(false);
        } else if (optChart.get(0).equals("2D with depth")) {
            ((ChartView) r).setDepth(true);
        }
        ((ChartView) r).setPosition(optChart.get(1));
        if (!Title.equals("")) {
            r.setTitle(Title);
        }
        dataviewDao.save(r);
        return r.getResourceId();
    }

    public Long saveReportAs(String rptDesign, long dataviewId, String title) {
        DataView r = new ReportView();
        String rep = new RptdesignToString(rptDesign).getRptdesignLikeString();
        r.setRptdesign(rep);
        r.setTitle(title);
        dataviewDao.save(r);
        return r.getResourceId();
    }

    public Long saveReport(String rptDesign, long dataviewId, String title) {
        DataView r = dataviewDao.findById(dataviewId);
        String rep = new RptdesignToString(rptDesign).getRptdesignLikeString();
        r.setRptdesign(rep);
        r.setTitle(title);
        dataviewDao.save(r);
        return r.getResourceId();
    }

    public WizardVo getChart(long dataviewId, WizardVo bean) {
        ChartView chart = (ChartView) dataviewDao.findById(dataviewId);
        List dsetID = new ArrayList();
        dsetID.add(chart.getDatasetId());
        bean.setDatasetId(dsetID);
        bean.setTitle(chart.getTitle());
        bean.setDimensionX(chart.getSelectedFields().get(0).getFieldName());
        bean.setDimensionY(chart.getSelectedFields().get(1).getFieldName());
        List dimensionValuesX = new ArrayList();
        for (int i = 0; i < chart.getSelectedFields().get(0).getSelectedValues().size(); i++) {
            dimensionValuesX.add(chart.getSelectedFields().get(0).getSelectedValues().get(i).getSelectedValue());
        }
        bean.setDimensionValuesX(dimensionValuesX);
        List dimensionValuesY = new ArrayList();
        for (int i = 0; i < chart.getSelectedFields().get(1).getSelectedValues().size(); i++) {
            dimensionValuesY.add(chart.getSelectedFields().get(1).getSelectedValues().get(i).getSelectedValue());
        }
        bean.setDimensionValuesY(dimensionValuesY);
        List dimensionValuesYForLine = new ArrayList();
        for (int i = 0; i < chart.getSelectedFields().get(2).getSelectedValues().size(); i++) {
            dimensionValuesYForLine.add(chart.getSelectedFields().get(2).getSelectedValues().get(i).getSelectedValue());
        }
        bean.setDimensionValuesYForLine(dimensionValuesYForLine);
        List otherDimension = new ArrayList();
        for (int i = 3; i < chart.getSelectedFields().size(); i++) {
            List element = new ArrayList();
            element.add(chart.getSelectedFields().get(i).getFieldName());
            element.add(chart.getSelectedFields().get(i).getSelectedValues().get(0).getSelectedValue());
            otherDimension.add(element);
        }
        bean.setOtherDimension(otherDimension);
        bean.setChartType(chart.getChartType());
        bean.setFlip(chart.isFlip());
        String dim2_3D = null;
        if (chart.isDepth()) {
            dim2_3D = "2D with depth";
        } else {
            dim2_3D = "2D";
        }
        dim2_3D += "_" + chart.getPosition();
        bean.setDim2_3D(dim2_3D);
        return bean;
    }

    public List updatePreview(WizardVo bean, String servletType) {
        List<Object> result = new ArrayList<Object>();
        String message;
        GraphEngine newChart = new GraphEngine(bean, reServiceImpl);
        String rep = newChart.createReport();
        message = "<iframe src='/" + Setting.getBirtApplName() + "/FenixBirtServletByFile?report=" + rep + "&servletType=" + servletType + "' width='100%' height='100%' />";
        result.add(rep);
        result.add(message);
        return result;
    }

    public BeanLegend legendChart(String rptDesign, String method, BeanLegend bean) {
        DesignConfig dConfig = new DesignConfig();
        dConfig.setBIRTHome(Setting.getReportEngine());
        DesignEngine dEngine = new DesignEngine(dConfig);
        SessionHandle session = dEngine.newSessionHandle(ULocale.ENGLISH);
        String name = System.getProperty("java.io.tmpdir") + File.separator + rptDesign;
        ReportDesignHandle design = null;
        try {
            design = session.openDesign(name);
            DesignElementHandle ex = design.findElement("NewChart");
            ExtendedItemHandle eHandle = (ExtendedItemHandle) ex;
            Chart chart = (Chart) eHandle.getReportItem().getProperty("chart.instance");
            if (method.equals("get")) {
                bean.setVisible(chart.getLegend().isVisible());
                bean.setSizeLabel((int) chart.getLegend().getText().getFont().getSize());
                Position pos = chart.getLegend().getPosition();
                if (pos.getName().equals("Right")) {
                    bean.setPosition("3");
                } else if (pos.getName().equals("Left")) {
                    bean.setPosition("2");
                } else if (pos.getName().equals("Above")) {
                    bean.setPosition("0");
                } else if (pos.getName().equals("Below")) {
                    bean.setPosition("1");
                }
                if (!chart.getType().equals("Pie")) {
                    bean.getColor().clear();
                    int tmp = ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getSeriesDefinitions().size();
                    if (((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getAssociatedAxes().size() > 1) {
                        tmp += ((Axis) ((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getAssociatedAxes().get(1)).getSeriesDefinitions().size();
                    }
                    List listPar;
                    int y1 = 0;
                    int y2 = 0;
                    for (int i = 0; i < tmp; i++) {
                        listPar = new ArrayList();
                        SeriesDefinition s;
                        if (i < ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getSeriesDefinitions().size()) {
                            s = (SeriesDefinition) ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getSeriesDefinitions().get(y1);
                            y1++;
                        } else {
                            s = (SeriesDefinition) ((Axis) ((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getAssociatedAxes().get(1)).getSeriesDefinitions().get(y2);
                            y2++;
                        }
                        listPar.add(((Series) s.getSeries().get(0)).getSeriesIdentifier());
                        EList list = s.getSeriesPalette().getEntries();
                        String color = "#";
                        if (chart.getType().equals("Line")) {
                            for (int z = 0; z < s.getSeries().size(); z++) {
                                String r = Integer.toHexString(((LineSeries) s.getSeries().get(z)).getLineAttributes().getColor().getRed());
                                if (r.length() == 1) r = "0" + r;
                                String g = Integer.toHexString(((LineSeries) s.getSeries().get(z)).getLineAttributes().getColor().getGreen());
                                if (g.length() == 1) g = "0" + g;
                                String b = Integer.toHexString(((LineSeries) s.getSeries().get(z)).getLineAttributes().getColor().getBlue());
                                if (b.length() == 1) b = "0" + b;
                                color += r + g + b;
                            }
                        } else if (chart.getType().equals("Bar/Line")) {
                            String packClass = s.getSeries().get(0).getClass().toString();
                            int index = (packClass.length()) - 1;
                            while (packClass.charAt(index) != '.') {
                                index--;
                            }
                            index++;
                            packClass = packClass.substring(index, packClass.length());
                            if (packClass.equals("LineSeriesImpl")) {
                                for (int z = 0; z < s.getSeries().size(); z++) {
                                    String r = Integer.toHexString(((LineSeries) s.getSeries().get(z)).getLineAttributes().getColor().getRed());
                                    if (r.length() == 1) r = "0" + r;
                                    String g = Integer.toHexString(((LineSeries) s.getSeries().get(z)).getLineAttributes().getColor().getGreen());
                                    if (g.length() == 1) g = "0" + g;
                                    String b = Integer.toHexString(((LineSeries) s.getSeries().get(z)).getLineAttributes().getColor().getBlue());
                                    if (b.length() == 1) b = "0" + b;
                                    color += r + g + b;
                                }
                            } else {
                                for (int j = 0; j < list.size(); j++) {
                                    String r = Integer.toHexString(((ColorDefinition) list.get(j)).getRed());
                                    if (r.length() == 1) r = "0" + r;
                                    String g = Integer.toHexString(((ColorDefinition) list.get(j)).getGreen());
                                    if (g.length() == 1) g = "0" + g;
                                    String b = Integer.toHexString(((ColorDefinition) list.get(j)).getBlue());
                                    if (b.length() == 1) b = "0" + b;
                                    color += r + g + b;
                                }
                            }
                        } else {
                            for (int j = 0; j < list.size(); j++) {
                                String r = Integer.toHexString(((ColorDefinition) list.get(j)).getRed());
                                if (r.length() == 1) r = "0" + r;
                                String g = Integer.toHexString(((ColorDefinition) list.get(j)).getGreen());
                                if (g.length() == 1) g = "0" + g;
                                String b = Integer.toHexString(((ColorDefinition) list.get(j)).getBlue());
                                if (b.length() == 1) b = "0" + b;
                                color += r + g + b;
                            }
                        }
                        listPar.add(color);
                        bean.setColor(listPar);
                    }
                } else {
                    bean.getColor().clear();
                    int tmp = ((ChartWithoutAxes) chart).getSeriesDefinitions().size();
                    List listPar;
                    for (int i = 0; i < tmp; i++) {
                        listPar = new ArrayList();
                        SeriesDefinition s = (SeriesDefinition) ((ChartWithoutAxes) chart).getSeriesDefinitions().get(i);
                        listPar.add(((Series) s.getSeries().get(0)).getSeriesIdentifier());
                        String color = "#";
                        EList list = s.getSeriesPalette().getEntries();
                        for (int j = 0; j < list.size(); j++) {
                            String r = Integer.toHexString(((ColorDefinition) list.get(j)).getRed());
                            if (r.length() == 1) r = "0" + r;
                            String g = Integer.toHexString(((ColorDefinition) list.get(j)).getGreen());
                            if (g.length() == 1) g = "0" + g;
                            String b = Integer.toHexString(((ColorDefinition) list.get(j)).getBlue());
                            if (b.length() == 1) b = "0" + b;
                            color += r + g + b;
                        }
                        listPar.add(color);
                        bean.setColor(listPar);
                    }
                }
                return bean;
            } else if (method.equals("set")) {
                chart.getLegend().setVisible(bean.isVisible());
                chart.getLegend().getText().getFont().setSize(bean.getSizeLabel());
                if (bean.getPosition().equals("Right")) {
                    chart.getLegend().setPosition(Position.RIGHT_LITERAL);
                } else if (bean.getPosition().equals("Left")) {
                    chart.getLegend().setPosition(Position.LEFT_LITERAL);
                } else if (bean.getPosition().equals("Above")) {
                    chart.getLegend().setPosition(Position.ABOVE_LITERAL);
                } else if (bean.getPosition().equals("Below")) {
                    chart.getLegend().setPosition(Position.BELOW_LITERAL);
                }
                if (!chart.getType().equals("Pie")) {
                    int tmp = ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getSeriesDefinitions().size();
                    if (((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getAssociatedAxes().size() > 1) {
                        tmp += ((Axis) ((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getAssociatedAxes().get(1)).getSeriesDefinitions().size();
                    }
                    int y1 = 0;
                    int y2 = 0;
                    for (int i = 0; i < tmp; i++) {
                        SeriesDefinition s;
                        if (i < ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getSeriesDefinitions().size()) {
                            s = (SeriesDefinition) ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getSeriesDefinitions().get(y1);
                            y1++;
                        } else {
                            s = (SeriesDefinition) ((Axis) ((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getAssociatedAxes().get(1)).getSeriesDefinitions().get(y2);
                            y2++;
                        }
                        String col = (String) bean.getColorFromClientToServer().get(i);
                        StringTokenizer colorComp = new StringTokenizer(col, "_");
                        int r = 0;
                        int g = 0;
                        int b = 0;
                        int cont = 0;
                        while (colorComp.hasMoreTokens()) {
                            if (cont == 0) {
                                r = Integer.valueOf(colorComp.nextToken()).intValue();
                            } else if (cont == 1) {
                                g = Integer.valueOf(colorComp.nextToken()).intValue();
                            } else if (cont == 2) {
                                b = Integer.valueOf(colorComp.nextToken()).intValue();
                            }
                            cont++;
                        }
                        if (chart.getType().equals("Line")) {
                            ((LineSeries) s.getSeries().get(0)).getLineAttributes().setColor(ColorDefinitionImpl.create(r, g, b));
                        } else if (chart.getType().equals("Bar/Line")) {
                            String packClass = s.getSeries().get(0).getClass().toString();
                            int index = (packClass.length()) - 1;
                            while (packClass.charAt(index) != '.') {
                                index--;
                            }
                            index++;
                            packClass = packClass.substring(index, packClass.length());
                            if (packClass.equals("LineSeriesImpl")) {
                                ((LineSeries) s.getSeries().get(0)).getLineAttributes().setColor(ColorDefinitionImpl.create(r, g, b));
                            } else {
                                s.getSeriesPalette().getEntries().clear();
                                final Fill[] fiaBase = { ColorDefinitionImpl.create(r, g, b) };
                                for (int z = 0; z < fiaBase.length; z++) {
                                    s.getSeriesPalette().getEntries().add(fiaBase[z]);
                                }
                            }
                        } else {
                            s.getSeriesPalette().getEntries().clear();
                            final Fill[] fiaBase = { ColorDefinitionImpl.create(r, g, b) };
                            for (int z = 0; z < fiaBase.length; z++) {
                                s.getSeriesPalette().getEntries().add(fiaBase[z]);
                            }
                        }
                    }
                }
            }
            design.saveAs(System.getProperty("java.io.tmpdir") + File.separator + rptDesign);
        } catch (Exception e) {
            System.err.println("Report " + name + " not opened!\nReason is " + e.toString());
        }
        return null;
    }

    public BeanParameter labelChart(String rptDesign, String method, BeanParameter bean) {
        DesignConfig dConfig = new DesignConfig();
        dConfig.setBIRTHome(Setting.getReportEngine());
        DesignEngine dEngine = new DesignEngine(dConfig);
        SessionHandle session = dEngine.newSessionHandle(ULocale.ENGLISH);
        String name = System.getProperty("java.io.tmpdir") + File.separator + rptDesign;
        ReportDesignHandle design = null;
        List<List<String>> parameters = new ArrayList<List<String>>();
        try {
            design = session.openDesign(name);
            DesignElementHandle handle = (DesignElementHandle) design.findElement("NewChart");
            ExtendedItemHandle eHandle = (ExtendedItemHandle) handle;
            Chart chart = (Chart) eHandle.getReportItem().getProperty("chart.instance");
            if (method.equals("get")) {
                bean.setTitle(chart.getTitle().getLabel().getCaption().getValue());
                bean.setTitleVisible(chart.getTitle().isVisible());
                bean.setSizeTitle((int) chart.getTitle().getLabel().getCaption().getFont().getSize());
                if (!chart.getType().equals("Pie")) {
                    bean.setXAxisTitle(((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getTitle().getCaption().getValue());
                    bean.setXAxisVisible(((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getTitle().isVisible());
                    bean.setSizeXAxis((int) ((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getTitle().getCaption().getFont().getSize());
                    bean.setSizeXAxisLabel((int) ((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getLabel().getCaption().getFont().getSize());
                    bean.setXAxisLabelVisible(((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getLabel().isVisible());
                    bean.setYAxisTitle(((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getTitle().getCaption().getValue());
                    bean.setYAxisVisible(((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getTitle().isVisible());
                    bean.setSizeYAxis((int) ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getTitle().getCaption().getFont().getSize());
                    bean.setYAxisLabelVisible(((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getLabel().isVisible());
                    bean.setSizeYAxisLabel((int) ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getLabel().getCaption().getFont().getSize());
                    SeriesDefinition s = (SeriesDefinition) ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getSeriesDefinitions().get(0);
                    Series ob;
                    ob = (Series) s.getSeries().get(0);
                    bean.setChartValue(ob.getLabel().isVisible());
                    bean.setSizeChartValue((int) ob.getLabel().getCaption().getFont().getSize());
                } else {
                    SeriesDefinition s = (SeriesDefinition) ((ChartWithoutAxes) chart).getSeriesDefinitions().get(0);
                    Series ob;
                    ob = (Series) s.getSeries().get(0);
                    bean.setChartValue(ob.getLabel().isVisible());
                    bean.setSizeChartValue((int) ob.getLabel().getCaption().getFont().getSize());
                }
                return bean;
            } else if (method.equals("set")) {
                chart.getTitle().getLabel().getCaption().setValue(bean.getTitle());
                chart.getTitle().setVisible(bean.isTitleVisible());
                chart.getTitle().getLabel().getCaption().getFont().setSize(bean.getSizeTitle());
                if (!chart.getType().equals("Pie")) {
                    ((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getTitle().getCaption().setValue(bean.getXAxisTitle());
                    ((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getTitle().setVisible(bean.isXAxisVisible());
                    ((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getTitle().getCaption().getFont().setSize(bean.getSizeXAxis());
                    ((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getLabel().getCaption().getFont().setSize(30);
                    ((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getLabel().getCaption().getFont().setSize(bean.getSizeXAxisLabel());
                    ((ChartWithAxes) chart).getPrimaryBaseAxes()[0].getLabel().setVisible(bean.isXAxisLabelVisible());
                    ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getTitle().getCaption().setValue(bean.getYAxisTitle());
                    ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getTitle().setVisible(bean.isYAxisVisible());
                    ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getTitle().getCaption().getFont().setSize(bean.getSizeYAxis());
                    ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getLabel().setVisible(bean.isYAxisLabelVisible());
                    ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getLabel().getCaption().getFont().setSize(bean.getSizeYAxisLabel());
                    int numDimY = ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getSeriesDefinitions().size();
                    for (int i = 0; i < numDimY; i++) {
                        SeriesDefinition s = (SeriesDefinition) ((ChartWithAxes) chart).getPrimaryOrthogonalAxis(((ChartWithAxes) chart).getPrimaryBaseAxes()[0]).getSeriesDefinitions().get(i);
                        Series ob;
                        for (int j = 0; j < s.getSeries().size(); j++) {
                            ob = (Series) s.getSeries().get(j);
                            ob.getLabel().setVisible(bean.isChartValue());
                            ob.getLabel().getCaption().getFont().setSize(bean.getSizeChartValue());
                        }
                    }
                } else {
                    int tmp = ((ChartWithoutAxes) chart).getSeriesDefinitions().size();
                    for (int i = 0; i < tmp; i++) {
                        SeriesDefinition s = (SeriesDefinition) ((ChartWithoutAxes) chart).getSeriesDefinitions().get(i);
                        Series ob;
                        for (int j = 0; j < s.getSeries().size(); j++) {
                            ob = (Series) s.getSeries().get(j);
                            ob.getLabel().setVisible(bean.isChartValue());
                            ob.getLabel().getCaption().getFont().setSize(bean.getSizeChartValue());
                        }
                    }
                }
            }
            design.saveAs(System.getProperty("java.io.tmpdir") + File.separator + rptDesign);
        } catch (Exception e) {
            System.err.println("Report " + name + " not opened!\nReason is " + e.toString());
        }
        return null;
    }

    public String resizeChart(String rptDesign, String op) {
        DesignConfig dConfig = new DesignConfig();
        dConfig.setBIRTHome(Setting.getReportEngine());
        DesignEngine dEngine = new DesignEngine(dConfig);
        SessionHandle session = dEngine.newSessionHandle(ULocale.ENGLISH);
        String name = System.getProperty("java.io.tmpdir") + File.separator + rptDesign;
        ReportDesignHandle design = null;
        try {
            design = session.openDesign(name);
            DesignElementHandle handle = (DesignElementHandle) design.findElement("NewChart");
            ExtendedItemHandle eHandle = (ExtendedItemHandle) handle;
            Chart chart = (Chart) eHandle.getReportItem().getProperty("chart.instance");
            double oldWidth = chart.getBlock().getBounds().getWidth();
            double oldHeight = chart.getBlock().getBounds().getHeight();
            if (op.equals("in")) {
                double tmp = eHandle.getWidth().getMeasure() + 50;
                eHandle.setWidth(String.valueOf(tmp) + "pt");
                tmp = eHandle.getHeight().getMeasure() + 50;
                eHandle.setHeight(String.valueOf(tmp) + "pt");
                chart.getBlock().getBounds().setWidth(oldWidth + 50);
                chart.getBlock().getBounds().setHeight(oldHeight + 50);
            } else {
                if ((oldWidth - 50) > 0 && (oldHeight - 50) > 0) {
                    double tmp = eHandle.getWidth().getMeasure() - 50;
                    eHandle.setWidth(String.valueOf(tmp) + "pt");
                    tmp = eHandle.getHeight().getMeasure() - 50;
                    eHandle.setHeight(String.valueOf(tmp) + "pt");
                    chart.getBlock().getBounds().setWidth(oldWidth - 50);
                    chart.getBlock().getBounds().setHeight(oldHeight - 50);
                }
            }
            design.saveAs(System.getProperty("java.io.tmpdir") + File.separator + rptDesign);
        } catch (Exception e) {
            System.err.println("Report " + name + " not opened!\nReason is " + e.toString());
        }
        return "<iframe src='/" + Setting.getBirtApplName() + "/FenixBirtServletByFile?report=" + rptDesign + "&servletType=run' width='100%' height='100%' />";
    }

    public List addTextToReport(String rptDesign, String content) {
        List<String> reportObject = new ArrayList<String>();
        DesignConfig dConfig = new DesignConfig();
        dConfig.setBIRTHome(Setting.getReportEngine());
        DesignEngine dEngine = new DesignEngine(dConfig);
        SessionHandle session = dEngine.newSessionHandle(ULocale.ENGLISH);
        if (rptDesign.equals("")) {
            String nameRptdesign = BirtUtil.randomNameFile();
            TextItemHandle text = null;
            try {
                SessionHandle newSession = BirtUtil.getDesignSessionHandle();
                String rep = null;
                ReportDesignHandle designHandle = null;
                IDesignEngine designEngine = null;
                ElementFactory designFactory = null;
                designHandle = session.createDesign();
                designFactory = designHandle.getElementFactory();
                DesignElementHandle simpleMasterPage = designFactory.newSimpleMasterPage("Master Page");
                designHandle.getMasterPages().add(simpleMasterPage);
                ElementFactory elementFactory = designHandle.getElementFactory();
                text = designHandle.getElementFactory().newTextItem("Note");
                text.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
                text.setContent(content);
                designHandle.getBody().add(text);
                System.out.println("path = " + System.getProperty("java.io.tmpdir") + File.separator + nameRptdesign);
                designHandle.saveAs(System.getProperty("java.io.tmpdir") + File.separator + nameRptdesign);
            } catch (BirtException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            reportObject.add(nameRptdesign);
            reportObject.add(text.getName());
        } else {
            DesignConfig dConfigReport = new DesignConfig();
            dConfigReport.setBIRTHome(Setting.getReportEngine());
            DesignEngine dEngineReport = new DesignEngine(dConfigReport);
            SessionHandle sessionReport = dEngine.newSessionHandle(ULocale.ENGLISH);
            String nameReport = System.getProperty("java.io.tmpdir") + File.separator + rptDesign;
            ReportDesignHandle designReport = null;
            TextItemHandle text = null;
            try {
                designReport = session.openDesign(nameReport);
                text = designReport.getElementFactory().newTextItem("Note");
                text.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
                text.setContent(content);
                designReport.getBody().add(text);
            } catch (Exception e) {
                System.err.println("Report  not opened!\nReason is " + e.toString());
            }
            try {
                designReport.saveAs(nameReport);
            } catch (Exception e) {
                e.printStackTrace();
            }
            reportObject.add(rptDesign);
            reportObject.add(text.getName());
        }
        return reportObject;
    }

    public List createReport(List id, List titleObject, String rptDesign, String typeView, int width, int height) {
        List<String> reportObject = new ArrayList<String>();
        for (int i = 0; i < id.size(); i++) {
            if (typeView.equals("chart")) {
                DataView r;
                r = dataviewDao.findById((Long) id.get(i));
                if (reportObject.size() == 0) {
                    AddChartToReport.getChart(rptDesign, r, reportObject, (String) titleObject.get(i));
                } else {
                    AddChartToReport.getChart(reportObject.get(0), r, reportObject, (String) titleObject.get(i));
                }
            } else if (typeView.equals("table")) {
                if (reportObject.size() == 0) {
                    AddTableToReport.getTable(rptDesign, (Long) id.get(i), reServiceImpl, reportObject, (String) titleObject.get(i));
                } else {
                    AddTableToReport.getTable(reportObject.get(0), (Long) id.get(i), reServiceImpl, reportObject, (String) titleObject.get(i));
                }
            } else if (typeView.equals("map")) {
                if (reportObject.size() == 0) {
                    AddMapToReport.getMap(rptDesign, (Long) id.get(i), mapDao, reportObject, width, height, (String) titleObject.get(i));
                } else {
                    AddMapToReport.getMap(reportObject.get(0), (Long) id.get(i), mapDao, reportObject, width, height, (String) titleObject.get(i));
                }
            } else if (typeView.equals("text")) {
                if (reportObject.size() == 0) {
                    AddTextToReport.getText(rptDesign, (Long) id.get(i), textServiceImpl, reportObject, (String) titleObject.get(i));
                } else {
                    AddTextToReport.getText(reportObject.get(0), (Long) id.get(i), textServiceImpl, reportObject, (String) titleObject.get(i));
                }
            }
        }
        return reportObject;
    }

    public void removeObjectByReport(String rptDesign, String birtObject) {
        DesignConfig dConfigReport = new DesignConfig();
        dConfigReport.setBIRTHome(Setting.getReportEngine());
        DesignEngine dEngine = new DesignEngine(dConfigReport);
        SessionHandle session = dEngine.newSessionHandle(ULocale.ENGLISH);
        String nameReport = System.getProperty("java.io.tmpdir") + File.separator + rptDesign;
        ReportDesignHandle designReport = null;
        try {
            designReport = session.openDesign(nameReport);
            designReport.getBody().drop(designReport.findElement(birtObject));
            designReport.saveAs(System.getProperty("java.io.tmpdir") + File.separator + rptDesign);
        } catch (Exception e) {
            System.err.println("Report not opened!\nReason is " + e.toString());
        }
    }

    public void moveObjectByReport(String rptDesign, String upDown, String birtObject) {
        DesignConfig dConfigReport = new DesignConfig();
        dConfigReport.setBIRTHome(Setting.getReportEngine());
        DesignEngine dEngine = new DesignEngine(dConfigReport);
        SessionHandle session = dEngine.newSessionHandle(ULocale.ENGLISH);
        String nameReport = System.getProperty("java.io.tmpdir") + File.separator + rptDesign;
        ReportDesignHandle designReport = null;
        try {
            designReport = session.openDesign(nameReport);
            int pos = designReport.getBody().findPosn(designReport.findElement(birtObject));
            if (upDown.equals("up")) {
                if (pos != 0) {
                    designReport.getBody().shift(designReport.findElement(birtObject), (pos - 1));
                    designReport.saveAs(System.getProperty("java.io.tmpdir") + File.separator + rptDesign);
                }
            } else if (upDown.equals("down")) {
                if (pos != designReport.getBody().getCount()) {
                    designReport.getBody().shift(designReport.findElement(birtObject), (pos + 1));
                    designReport.saveAs(System.getProperty("java.io.tmpdir") + File.separator + rptDesign);
                }
            }
        } catch (Exception e) {
            System.err.println("Report not opened!\nReason is " + e.toString());
        }
    }

    public String separatorObjectByReport(String rptDesign, String HTMLObj, String birtObject) {
        String result = "";
        DesignConfig dConfigReport = new DesignConfig();
        dConfigReport.setBIRTHome(Setting.getReportEngine());
        DesignEngine dEngine = new DesignEngine(dConfigReport);
        SessionHandle session = dEngine.newSessionHandle(ULocale.ENGLISH);
        String nameReport = System.getProperty("java.io.tmpdir") + File.separator + rptDesign;
        ReportDesignHandle designReport = null;
        try {
            designReport = session.openDesign(nameReport);
            int pos = designReport.getBody().findPosn(designReport.findElement(birtObject));
            TextItemHandle text = null;
            ElementFactory elementFactory = designReport.getElementFactory();
            if (HTMLObj.equals("br")) {
                text = designReport.getElementFactory().newTextItem("Space");
                text.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
                text.setContent("<br>");
                designReport.getBody().add(text, (pos + 1));
                designReport.saveAs(System.getProperty("java.io.tmpdir") + File.separator + rptDesign);
            } else if (HTMLObj.equals("hr")) {
                text = designReport.getElementFactory().newTextItem("HorizontalBar");
                text.setContentType(DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML);
                text.setContent("<br><div style='width: 100%; height:1px; background-color:gray;'>&nbsp;</div><br>");
                designReport.getBody().add(text, (pos + 1));
                designReport.saveAs(System.getProperty("java.io.tmpdir") + File.separator + rptDesign);
            }
            result = text.getName();
        } catch (Exception e) {
            System.err.println("Report not opened!\nReason is " + e.toString());
        }
        return result;
    }

    public List reportObject(String rptDesign) {
        List listObject = new ArrayList();
        DesignConfig dConfigReport = new DesignConfig();
        dConfigReport.setBIRTHome(Setting.getReportEngine());
        DesignEngine dEngine = new DesignEngine(dConfigReport);
        SessionHandle session = dEngine.newSessionHandle(ULocale.ENGLISH);
        String nameReport = System.getProperty("java.io.tmpdir") + File.separator + rptDesign;
        ReportDesignHandle designReport = null;
        try {
            designReport = session.openDesign(nameReport);
            int numObject = designReport.getBody().getCount();
            for (int i = 0; i < numObject; i++) {
                List element = new ArrayList();
                element.add(((ReportItemHandle) designReport.getBody().get(i)).getDisplayName());
                element.add(designReport.getBody().get(i).getName());
                listObject.add(element);
            }
        } catch (Exception e) {
            System.err.println("Report not opened!\nReason is " + e.toString());
        }
        return listObject;
    }

    public String exportTable(List data, String typeExport) {
        TableReport table = new TableReport(data);
        String nameFile = table.createReport();
        String exportFile = BirtUtil.randomFileExport() + "." + typeExport;
        IReportEngine reportEngine = BirtUtil.getReportEngine();
        try {
            IReportRunnable design = reportEngine.openReportDesign(nameFile);
            IRunTask task1 = reportEngine.createRunTask(design);
            task1.run(nameFile + ".rptdocument");
            IReportDocument document = reportEngine.openReportDocument(nameFile + ".rptdocument");
            IRenderOption options = new RenderOption();
            options.setOutputFormat(typeExport);
            options.setOutputFileName(Setting.getSystemPath() + "/exportObject/" + exportFile);
            IRenderTask task = reportEngine.createRenderTask(document);
            task.setRenderOption(options);
            task.render();
            task.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exportFile;
    }

    public String exportChart(String rptdesign, String typeExport) {
        String exportFile = BirtUtil.randomFileExport() + "." + typeExport;
        IReportEngine reportEngine = BirtUtil.getReportEngine();
        try {
            IReportRunnable design = reportEngine.openReportDesign(System.getProperty("java.io.tmpdir") + File.separator + rptdesign);
            IRunTask task1 = reportEngine.createRunTask(design);
            task1.run(System.getProperty("java.io.tmpdir") + File.separator + rptdesign + ".rptdocument");
            IReportDocument document = reportEngine.openReportDocument(System.getProperty("java.io.tmpdir") + File.separator + rptdesign + ".rptdocument");
            IRenderOption options = new RenderOption();
            options.setOutputFormat(typeExport);
            options.setOutputFileName(Setting.getSystemPath() + "/exportObject/" + exportFile);
            IRenderTask task = reportEngine.createRenderTask(document);
            task.setRenderOption(options);
            task.render();
            task.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exportFile;
    }

    public String exportText(String content, String typeExport) {
        List textReport = addTextToReport("", content);
        String exportFile = BirtUtil.randomFileExport() + "." + typeExport;
        String fileName = (String) textReport.get(0);
        IReportEngine reportEngine = BirtUtil.getReportEngine();
        try {
            IReportRunnable design = reportEngine.openReportDesign(System.getProperty("java.io.tmpdir") + File.separator + fileName);
            IRunTask task1 = reportEngine.createRunTask(design);
            task1.run(System.getProperty("java.io.tmpdir") + File.separator + fileName + ".rptdocument");
            IReportDocument document = reportEngine.openReportDocument(System.getProperty("java.io.tmpdir") + File.separator + fileName + ".rptdocument");
            IRenderOption options = new RenderOption();
            options.setOutputFormat(typeExport);
            options.setOutputFileName(Setting.getSystemPath() + "/exportObject/" + exportFile);
            IRenderTask task = reportEngine.createRenderTask(document);
            task.setRenderOption(options);
            task.render();
            task.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exportFile;
    }

    public void setMapDao(MapDao mapDao) {
        this.mapDao = mapDao;
    }

    public void setDataviewDao(DataViewDao dataviewDao) {
        this.dataviewDao = dataviewDao;
    }

    public void setTextServiceImpl(TextServiceImpl textServiceImpl) {
        this.textServiceImpl = textServiceImpl;
    }

    public void setReServiceImpl(REServiceImpl reServiceImpl) {
        this.reServiceImpl = reServiceImpl;
    }

    public void setDatasetDao(DatasetDao datasetDao) {
        this.datasetDao = datasetDao;
    }

    public void setTableDao(TableDao tableDao) {
        this.tableDao = tableDao;
    }

    public FenixResourceMetadataVo getNewChartMetadata(FenixResourceMetadataVo metadata, String rptDesign, long dataviewId, WizardVo bean) {
        final Long id = saveChartAs(rptDesign, dataviewId, metadata.getTitle(), bean);
        metadata.setResourceId(id);
        return metadata;
    }

    public FenixResourceVo createFenixResource(Long id) {
        DataView view = dataviewDao.findById(id);
        FenixResourceVo fenixResource = null;
        if (view != null) {
            fenixResource = FenixResourceBuilder.build(dataviewDao.findById(id));
        }
        return fenixResource;
    }

    public FenixResourceMetadataVo getNewReportMetadata(FenixResourceMetadataVo metadata, String rptDesign, long dataviewId) {
        final Long id = saveReportAs(rptDesign, dataviewId, metadata.getTitle());
        metadata.setResourceId(id);
        return metadata;
    }

    @SuppressWarnings("unchecked")
    public List<List<String>> getRecords(long datasetId, List<String> columnNames) {
        List originalRowList = datasetDao.getRowValues(datasetId, columnNames);
        List<List<String>> rowList = new ArrayList<List<String>>();
        for (Object[] originalRow : (List<Object[]>) originalRowList) {
            List<String> row = new ArrayList<String>(originalRow.length);
            for (Object field : originalRow) {
                row.add(field.toString());
            }
            rowList.add(row);
        }
        return rowList;
    }

    @SuppressWarnings("unchecked")
    public List getTableDimensions(Long id) {
        List dimensions = new ArrayList();
        TableView table = tableDao.findById(id);
        dimensions.add(table.getDatasetId());
        dimensions.add(table.getTitle());
        List fields = table.getSelectedFields();
        for (int i = 0; i < fields.size(); i++) dimensions.add(((SelectedField) fields.get(i)).getFieldName());
        return dimensions;
    }

    public List<List<String>> getRecordsWithLabel(Long datasetId, List<String> columnNames) {
        List originalRowList = datasetDao.getRowValues(datasetId, columnNames);
        List<List<String>> rowList = new ArrayList<List<String>>();
        for (Object[] originalRow : (List<Object[]>) originalRowList) {
            List<String> row = new ArrayList<String>(originalRow.length);
            for (Object field : originalRow) {
                row.add(field.toString());
            }
            rowList.add(row);
        }
        for (int i = 0; i < rowList.size(); i++) {
            for (int j = 0; j < columnNames.size(); j++) {
                if (columnNames.get(j).equals("commodityCode")) {
                    rowList.get(i).set(j, CommodityLabel.getLable(rowList.get(i).get(j)));
                } else if (columnNames.get(j).equals("featureCode")) {
                    rowList.get(i).set(j, GaulLabel.getLable(rowList.get(i).get(j)));
                }
            }
        }
        return rowList;
    }
}
