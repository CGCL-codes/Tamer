package org.jofc.facade;

import org.jofc.attribute.chart.element.PieElement;
import org.jofc.attribute.chart.element.value.PieValue;
import org.jofc.builder.JOFCBaseBuilder;
import org.jofc.builder.JOFCFactory;
import org.jofc.builder.PieBuilder;
import org.jofc.enums.Chart;
import org.jofc.enums.PieAnimates;
import org.jofc.itf.JOFCElement;
import org.jofc.itf.JOFCValue;
import java.util.ArrayList;
import java.util.List;

public class JPieChart extends JChart {

    private PieBuilder pieBuilder;

    public JPieChart() {
        this.pieBuilder = (PieBuilder) JOFCFactory.getBuilder(Chart.PIE);
    }

    public JPieChart addPieValue(double value, String label, String colour, int fontSize) {
        pieBuilder.addPieValue(value, label, colour, fontSize);
        return this;
    }

    /**
     * 设置饼图数据
     * 
     * @param values
     */
    public JPieChart setValues(Number[] values) {
        pieBuilder.setValues(values);
        return this;
    }

    /**
     * 设置饼图数据
     * 
     * @param values
     */
    public JPieChart setValues(int[] values) {
        pieBuilder.setValues(values);
        return this;
    }

    /**
     * 设置饼图数据
     * 
     * @param values
     */
    public JPieChart setValues(short[] values) {
        pieBuilder.setValues(values);
        return this;
    }

    /**
     * 设置饼图数据
     * 
     * @param values
     */
    public JPieChart setValues(long[] values) {
        pieBuilder.setValues(values);
        return this;
    }

    /**
     * 设置饼图数据
     * 
     * @param values
     */
    public JPieChart setValues(float[] values) {
        pieBuilder.setValues(values);
        return this;
    }

    /**
     * 设置饼图数据
     * 
     * @param values
     */
    public JPieChart setValues(double[] values) {
        pieBuilder.setValues(values);
        return this;
    }

    /**
     * 设置饼图数据
     * 
     * @param values
     */
    public JPieChart setValues(PieValue[] values) {
        pieBuilder.setValues(values);
        return this;
    }

    /**
     * 设置饼图数据
     * 
     * @param values
     */
    public JPieChart setValues(List<PieValue> values) {
        pieBuilder.setValues(values);
        return this;
    }

    /**
     * 添加动画效果
     * 
     * @param pieAnimates
     * @return JPieChart
     */
    public JPieChart addAnimate(PieAnimates pieAnimates) {
        pieBuilder.addAnimate(pieAnimates);
        return this;
    }

    /**
     * 设置图表上的色块使用的颜色
     * 
     * @param colours
     */
    public JPieChart setColours(List<String> colours) {
        pieBuilder.setColours(colours);
        return this;
    }

    /**
     * 设置图表打开时，开始展开的角度
     * 
     * @param startAngle
     */
    public JPieChart setStartAngle(int startAngle) {
        pieBuilder.setStartAngle(startAngle);
        return this;
    }

    /**
     * 设置透明度，取值范围为[0,1]
     * 
     * @param alpha
     */
    public JPieChart setAlpha(double alpha) {
        pieBuilder.setAlpha(alpha);
        return this;
    }

    /**
     * 设置是否使用颜色渐变效果，默认使用
     * 
     * @param isGradientFill
     */
    public JPieChart setGradientFill(boolean isGradientFill) {
        pieBuilder.setGradientFill(isGradientFill);
        return this;
    }

    /**
     * 设置鼠标移至热区时显示的提示文本
     * 
     * @param tip
     */
    public JPieChart setTip(String tip) {
        pieBuilder.setTip(tip);
        return this;
    }

    /**
     * 设置是否显示hint提示，默认显示
     * 
     * @param noLabels
     */
    public JPieChart setNoLabels(boolean noLabels) {
        pieBuilder.setNoLabels(noLabels);
        return this;
    }

    /**
     * 设置鼠标点击时触发的事件，可以是网址(比如http://www.google.com/)，也可以是JavaScript代码中的某个函数
     * 
     * @param onClick
     */
    public JPieChart setOnClick(String onClick) {
        pieBuilder.setOnClick(onClick);
        return this;
    }

    /**
     * 设置饼图的半径，单位是像素
     * 
     * @param radius
     */
    public JPieChart setRadius(int radius) {
        pieBuilder.setRadius(radius);
        return this;
    }

    /**
     * 返回{@link PieBuilder}对象
     * 
     * @return PieBuilder
     */
    public PieBuilder getPieBuilder() {
        return pieBuilder;
    }

    @Override
    public int getCountElement() {
        return 1;
    }

    @Override
    public double getTopValue() {
        double top = 0;
        List<Double> values = this.getValueNumbers(0);
        for (Double value : values) {
            top = top > value ? top : value;
        }
        return top;
    }

    @Override
    public List<Double> getValueNumbers(int index) {
        return getValueNumbers();
    }

    @Override
    public List<Double> getValueNumbers() {
        List<PieValue> values = getValueObjects();
        List<Double> result = new ArrayList<Double>(values.size());
        for (PieValue value : values) {
            result.add(value.getValue());
        }
        return result;
    }

    /**
     * 返回数据对象的列表
     * 
     * @return List
     */
    public List<PieValue> getValueObjects() {
        return this.getPieElements().get(0).getValues();
    }

    /**
     * 返回{@link PieElement}对象的列表，一个饼图中只有一个{@link PieElement}对象
     * 
     * @return List
     */
    public List<PieElement> getPieElements() {
        return pieBuilder.getElements();
    }

    @Override
    public JOFCBaseBuilder<? extends JOFCElement<? extends JOFCValue>, ? extends JOFCValue> getBuilder() {
        return pieBuilder;
    }
}
