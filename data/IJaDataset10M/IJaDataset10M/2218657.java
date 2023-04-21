package cn.andy.xmall.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import cn.andy.xmall.ui.XmallFrame;
import cn.andy.xmall.util.Helper;

/**
 * A controller holding the data of charts and controlling charts display.
 * 
 * @author Andy Luo
 * @version 0.2.3
 */
public class ChartController {

    private XmallFrame frame = XmallFrame.getDefaultXmallFrame();

    private DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    private String name;

    private int type;

    private String xlabel;

    private String ylabel;

    private JFreeChart chart;

    private boolean isModified = false;

    private static ChartController instance = null;

    public static ChartController getDefaultChartController() {
        if (instance == null) {
            instance = new ChartController();
        }
        return instance;
    }

    /**
     * ����һ�������ļ�����ʾ������ͱ����
     * 
     * @param file
     *            Ҫ�������ļ���
     */
    public void analyse(String file) throws Exception {
        isModified = true;
        SAXReader reader = new SAXReader();
        Document doc;
        if (file.equals("test.xml")) {
            doc = reader.read(Helper.class.getResourceAsStream("res/test.xml"), "gb2312");
        } else {
            doc = reader.read(new File(file));
        }
        List list = doc.selectNodes("/xmallchart");
        Iterator i1 = list.iterator();
        if (i1.hasNext()) {
            Element e = (Element) i1.next();
            Iterator iAttributes = e.attributeIterator();
            name = ((Attribute) iAttributes.next()).getValue();
            type = Integer.parseInt(((Attribute) iAttributes.next()).getValue());
            xlabel = ((Attribute) iAttributes.next()).getValue();
            ylabel = ((Attribute) iAttributes.next()).getValue();
            Iterator iElements = e.elementIterator("class");
            int cIndex = 1, eIndex = 1;
            while (iElements.hasNext()) {
                Element e1 = (Element) iElements.next();
                String cName = ((Attribute) e1.attributeIterator().next()).getValue();
                frame.setTableValueAt(cName, cIndex, 0);
                Iterator iElements1 = e1.elementIterator();
                eIndex = 1;
                while (iElements1.hasNext()) {
                    Element e2 = (Element) iElements1.next();
                    String eName = ((Attribute) e2.attributeIterator().next()).getValue();
                    int eValue = Integer.parseInt(e2.getText());
                    dataset.addValue(eValue, cName, eName);
                    if (cIndex == 1) {
                        frame.setTableValueAt(eName, 0, eIndex);
                    }
                    frame.setTableValueAt(new Integer(eValue).toString(), cIndex, eIndex);
                    eIndex++;
                }
                cIndex++;
            }
        }
        drawChart(type);
    }

    /**
     * �����Ӧͼ��
     * 
     * @param type
     *            ͼ������
     */
    public void drawChart(int type) {
        chart = Drawer.createChart(type, name, xlabel, ylabel, dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        frame.setChart(chartPanel);
    }

    /**
     * �任ͼ������
     * @param type Ŀ������
     */
    public void changeType(int type) {
        this.type = type;
        drawChart(type);
    }

    /**
     * ��ָ���ļ���淽��
     * 
     * @param fileName
     *            �ļ����
     */
    public void save(String fileName) throws Exception {
        String tName = chart.getTitle().getText();
        String xLabel = (this.type != 4) ? ((CategoryPlot) chart.getPlot()).getDomainAxis(0).getLabel() : this.xlabel;
        String yLabel = (this.type != 4) ? ((CategoryPlot) chart.getPlot()).getRangeAxis(0).getLabel() : this.ylabel;
        Document doc = DocumentHelper.createDocument();
        Element xmallchart = doc.addElement("xmallchart").addAttribute("name", tName).addAttribute("type", Integer.toString(type)).addAttribute("xlabel", xLabel).addAttribute("ylabel", yLabel);
        for (int cIndex = 1; frame.getTableValue(cIndex, 0) != null; cIndex++) {
            Element cClass = xmallchart.addElement("class").addAttribute("name", frame.getTableValue(cIndex, 0).toString());
            for (int eIndex = 1; frame.getTableValue(0, eIndex) != null; eIndex++) {
                String eName = frame.getTableValue(0, eIndex).toString();
                String eValue = frame.getTableValue(cIndex, eIndex).toString();
                cClass.addElement("element").addAttribute("name", eName).addText(eValue);
            }
        }
        OutputFormat of = OutputFormat.createPrettyPrint();
        of.setEncoding("gb2312");
        XMLWriter writer = new XMLWriter(new FileWriter(fileName), of);
        writer.write(doc);
        writer.close();
    }

    /**
     * ��ǰ�ĵ��Ƿ���δ������޸�
     * 
     * @return �Ƿ��޸�
     */
    public boolean isModified() {
        return isModified;
    }

    /**
     * �½�����
     * 
     * @param type
     *            ͼ������
     */
    public void newChart(int type) throws Exception {
        this.type = type;
        isModified = true;
        analyse("test.xml");
    }

    /**
     * ����޸ĺ����ͼ��
     */
    public void update() throws Exception {
        dataset.clear();
        for (int cIndex = 1; ; cIndex++) {
            if (frame.getModelValue(cIndex, 0) == null) {
                break;
            }
            String cName = frame.getModelValue(cIndex, 0).toString();
            if (cName.equals("")) {
                break;
            }
            for (int eIndex = 1; ; eIndex++) {
                if (frame.getModelValue(0, eIndex) == null) {
                    break;
                }
                String eName = frame.getModelValue(0, eIndex).toString();
                if (eName.equals("")) {
                    break;
                }
                int eValue = Integer.parseInt(frame.getModelValue(cIndex, eIndex).toString());
                dataset.addValue(eValue, cName, eName);
            }
            frame.repaintChartPanel();
        }
    }
}
