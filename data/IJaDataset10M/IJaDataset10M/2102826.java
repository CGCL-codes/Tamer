package org.easybi.chart.impl.chartdir.piechart;

import org.easybi.chart.impl.chartdir.TitleFormat;
import org.easybi.chart.impl.chartdir.piechart.util.LabelLayout;
import org.easybi.chart.impl.chartdir.piechart.util.RingSize;
import org.easybi.chart.impl.chartdir.util.TextBoxFormat;
import ChartDirector.Chart;

/**
 * ����pie��˵��û��series�ĸ��һ��layer�ͱ�ʾһ��������߶�donut���ö��layer
 * ����ʹ��getSection�������һ������趨�������������ǵ����Դ����֣����Ժ���
 * @author steve
 *
 */
public class LayerFormat {

    RingSize size = null;

    String threeDDeep = "0";

    int threeDAngle = -1;

    String explodeName = "";

    int lineColor = Chart.SameAsMainColor;

    int joinLineColor = Chart.SameAsMainColor;

    TextBoxFormat labelStyle = null;

    String labelFormat = "";

    LabelLayout labelLayout = null;

    TitleFormat subTitle = null;

    int width = -1;

    int height = -1;

    int positionX = -1;

    int positionY = -1;

    public String getExplodeName() {
        return explodeName;
    }

    public void setExplodeName(String explodeName) {
        this.explodeName = explodeName;
    }

    public int getJoinLineColor() {
        return joinLineColor;
    }

    public void setJoinLineColor(int joinLineColor) {
        this.joinLineColor = joinLineColor;
    }

    public String getLabelFormat() {
        return labelFormat;
    }

    public void setLabelFormat(String labelFormat) {
        this.labelFormat = labelFormat;
    }

    public LabelLayout getLabelLayout() {
        return labelLayout;
    }

    public void setLabelLayout(LabelLayout labelLayout) {
        this.labelLayout = labelLayout;
    }

    public TextBoxFormat getLabelStyle() {
        return labelStyle;
    }

    public void setLabelStyle(TextBoxFormat labelStyle) {
        this.labelStyle = labelStyle;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public RingSize getSize() {
        return size;
    }

    public void setSize(RingSize size) {
        this.size = size;
    }

    public int getThreeDAngle() {
        return threeDAngle;
    }

    public void setThreeDAngle(int threeDAngle) {
        this.threeDAngle = threeDAngle;
    }

    public String getThreeDDeep() {
        return threeDDeep;
    }

    public void setThreeDDeep(String threeDDeep) {
        this.threeDDeep = threeDDeep;
    }

    public TitleFormat getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(TitleFormat subTitle) {
        this.subTitle = subTitle;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}
