package org.jofc.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jofc.itf.JOFCAttribute;
import org.jofc.util.JOFCUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONString;

/**
 * X坐标轴的抽象，可以设置自己定义的文字作为坐标值，也可以设置颜色等样式。
 * 
 * @author 张啸[zzxx513@163.com]
 * 
 */
public class XAxis implements JOFCAttribute {

    private int tickHeight;

    private int stroke;

    private String colour;

    private String gridColour;

    private boolean offset;

    private int steps;

    private Labels labels = new Labels();

    private int width3D;

    public XAxis() {
        steps = 1;
        colour = "#000000";
        tickHeight = 3;
        stroke = 2;
        offset = true;
        gridColour = "#e0e0e0";
    }

    public int get3Dwidth() {
        return width3D;
    }

    public void set3Dwidth(int width3D) {
        this.width3D = width3D;
    }

    /**
     * 返回显示X轴的坐标值时，间隔的单位数量
     * 
     * @return int
     */
    public int getLabelSteps() {
        return labels.steps;
    }

    /**
     * 显示X轴坐标值时，每steps个单位显示一个坐标值，如果为1，表示每个坐标值都显示
     * 
     * @param labelSteps
     */
    public void setLabelSteps(int labelSteps) {
        labels.steps = labelSteps;
    }

    /**
     * 返回X轴文字逆时针旋转的角度
     * 
     * @return int
     */
    public int getLabelRotate() {
        return labels.rotate;
    }

    /**
     * 设置X轴文字顺时针旋转的角度
     * 
     * @param labelRotate
     */
    public void setLabelRotate(int labelRotate) {
        labels.rotate = labelRotate;
    }

    /**
     * 返回X轴文字的颜色
     * 
     * @return String
     */
    public String getLabelColour() {
        return labels.colour;
    }

    /**
     * 设置X轴文字的颜色
     * 
     * @param labelColour
     */
    public void setLabelColour(String labelColour) {
        labels.colour = labelColour;
    }

    /**
     * 返回X轴文字的字号
     * 
     * @return int
     */
    public int getLabelSize() {
        return labels.size;
    }

    /**
     * 设置X轴文字的字号
     * 
     * @param labelSize
     */
    public void setLabelSize(int labelSize) {
        labels.size = labelSize;
    }

    /**
     * 返回X轴文字的对象列表，合法的对象类型包括String对象或者{@link XAxisLabel}对象
     * 
     * @return List<Object>
     */
    public List<Object> getLabelObjectList() {
        return labels.labels;
    }

    /**
     * 返回X轴文字的字符串列表
     * 
     * @return List<String>
     */
    public List<String> getLabelList() {
        List<String> result = new ArrayList<String>(20);
        for (Object label : labels.labels) {
            if (label == null) {
                result.add("null");
            } else if (label instanceof String) {
                result.add((String) label);
            } else if (label instanceof XAxisLabel) {
                result.add(((XAxisLabel) label).getText());
            } else {
                result.add("undefined");
            }
        }
        return result;
    }

    /**
     * 设置X坐标轴的标签的值，List中可以放置String类型或者{@link XAxisLabel}
     * 对象，其他类型按照"undefined"字符串处理
     * 
     * @param labelList X坐标轴的标签值
     * @see XAxisLabel
     */
    public void setLabelList(List<Object> labelList) {
        labels.labels = labelList;
    }

    /**
     * 设置X坐标轴的标签的值，List中可以放置String类型或者{@link XAxisLabel}
     * 对象，其他类型按照"undefined"字符串处理
     * 
     * @param labelList X坐标轴的标签值
     * @see XAxisLabel
     */
    public void setLabelList(Object[] labelList) {
        labels.labels = Arrays.asList(labelList);
    }

    /**
     * 返回X坐标轴上突起的刻度的长度，单位是像素
     * 
     * @return int
     */
    public int getTickHeight() {
        return tickHeight;
    }

    /**
     * 设置X坐标轴上突起的刻度的长度，单位是像素
     * 
     * @param tickHeight
     */
    public void setTickHeight(int tickHeight) {
        this.tickHeight = tickHeight;
    }

    /**
     * 返回X坐标轴的厚度，单位是像素
     * 
     * @return int
     */
    public int getStroke() {
        return stroke;
    }

    /**
     * 设置X坐标轴的厚度，单位是像素
     * 
     * @param stroke
     */
    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    /**
     * 返回X坐标轴是否以坐标原点为起点，false表示向原点右侧偏移一些
     * 
     * @return boolean
     */
    public boolean isOffset() {
        return offset;
    }

    /**
     * 设置X坐标轴是否以坐标原点为起点，false表示向原点右侧偏移一些
     * 
     * @param offset
     */
    public void setOffset(boolean offset) {
        this.offset = offset;
    }

    /**
     * 返回X坐标轴的颜色，比如“#343434”
     * 
     * @return String
     */
    public String getColour() {
        return colour;
    }

    /**
     * 设置X坐标轴的颜色，比如“#343434”
     * 
     * @param colour
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * 返回图表中竖线的颜色，比如“#343434”
     * 
     * @return String
     */
    public String getGridColour() {
        return gridColour;
    }

    /**
     * 设置图表中竖线的颜色，比如“#343434”
     * 
     * @param gridColour
     */
    public void setGridColour(String gridColour) {
        this.gridColour = gridColour;
    }

    /**
     * 返回显示X轴的竖线时，间隔的单位数量
     * 
     * @return int
     */
    public int getSteps() {
        return steps;
    }

    /**
     * 显示X轴的竖线时，每steps个单位显示一条竖线，如果为1，表示每条竖线都显示
     */
    public void setSteps(int steps) {
        this.steps = steps;
    }

    @Override
    public String getAttributeName() {
        return "x_axis";
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        JSONObject jo = new JSONObject();
        JOFCUtils.appendJson(jo, "colour", colour);
        JOFCUtils.appendJson(jo, "grid-colour", gridColour);
        JOFCUtils.appendJson(jo, "tick-height", tickHeight);
        JOFCUtils.appendJson(jo, "stroke", stroke);
        JOFCUtils.appendJson(jo, "offset", offset);
        JOFCUtils.appendJson(jo, "steps", steps);
        if (labels.labels.size() > 0) {
            JOFCUtils.appendJson(jo, "labels", labels);
        }
        return jo.toString();
    }

    private static class Labels implements JSONString {

        private List<Object> labels = new ArrayList<Object>();

        private int steps;

        private int rotate;

        private String colour;

        private int size;

        public Labels() {
            steps = 1;
            rotate = 0;
            colour = "#000000";
            size = 12;
        }

        @Override
        public String toJSONString() {
            JSONObject jo = new JSONObject();
            JSONArray ja = new JSONArray();
            for (Object label : labels) {
                if (label == null) {
                    ja.add("null");
                } else if (label instanceof String || label instanceof XAxisLabel) {
                    ja.add(label);
                } else {
                    ja.add("undefined");
                }
            }
            JOFCUtils.appendJson(jo, "steps", steps);
            JOFCUtils.appendJson(jo, "rotate", rotate);
            JOFCUtils.appendJson(jo, "colour", colour);
            JOFCUtils.appendJson(jo, "size", size);
            JOFCUtils.appendJson(jo, "labels", ja);
            return jo.toString();
        }
    }

    @Override
    public Object getAttributeValue() {
        return true;
    }
}
