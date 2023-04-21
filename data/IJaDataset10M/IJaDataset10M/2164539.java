package org.jofc.attribute.chart.element;

import org.jofc.enums.LineDotType;
import net.sf.json.JSONObject;
import net.sf.json.JSONString;

/**
 * 这个对象用来记录线图中一条曲线的坐标点的样式
 * 
 * @author 张啸[xiao.zhang@opi-corp.com]
 */
public class LineDotStyle implements JSONString {

    private LineDotType dotType;

    private int dotSize;

    private int haloSize;

    private String colour;

    private String tip;

    /**
     * 默认的构造器，默认使用实心点的样式
     */
    public LineDotStyle() {
        dotType = LineDotType.SOLID_DOT;
        dotSize = 2;
        haloSize = 0;
        colour = "#3D5C56";
    }

    /**
     * 返回坐标点的基本样式，比如实心点，空心点等等
     * 
     * @return {@link LineDotType} 枚举类型
     */
    public LineDotType getDotType() {
        return dotType;
    }

    /**
     * 设置坐标点的基本样式，比如实心点，空心点等等
     * 
     * @param dotType {@link LineDotType} 枚举类型
     */
    public void setDotType(LineDotType dotType) {
        this.dotType = dotType;
    }

    /**
     * 返回坐标点的大小，单位是像素
     * 
     * @return int
     */
    public int getDotSize() {
        return dotSize;
    }

    /**
     * 设置坐标点的大小，单位是像素
     * 
     * @param dotSize
     */
    public void setDotSize(int dotSize) {
        this.dotSize = dotSize;
    }

    /**
     * 返回坐标点占用的面积的大小，单位是像素，如果Halo_size大于Dot_size，多出来的部分会补白(直译是光圈，不好理解， 自己试试就知道干什么用的了)
     * 
     * @return int
     */
    public int getHaloSize() {
        return haloSize;
    }

    /**
     * 设置 坐标点占用的面积的大小，单位是像素，如果Halo_size大于Dot_size，多出来的部分会补白(直译是光圈，不好理解， 自己试试就知道干什么用的了)
     * 
     * @param haloSize
     */
    public void setHaloSize(int haloSize) {
        this.haloSize = haloSize;
    }

    /**
     * 返回坐标点的颜色
     * 
     * @return String
     */
    public String getColour() {
        return colour;
    }

    /**
     * 设置坐标点的颜色
     * 
     * @param colour
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * 返回提示文字
     * 
     * @return String
     */
    public String getTip() {
        return tip;
    }

    /**
     * 设置提示文字，可用的变量有#x_label#，#val#，#x#，#y#<br>
     * 更多内容：http://teethgrinder.co.uk/open-flash-chart-2/x-axis-date.php
     * 
     * @param tip
     */
    public void setTip(String tip) {
        this.tip = tip;
    }

    /**
     * 返回json字符串
     * 
     * @see #toJSONString()
     */
    @Override
    public String toString() {
        return this.toJSONString();
    }

    /**
     * 返回json字符串
     */
    @Override
    public String toJSONString() {
        JSONObject jo = new JSONObject();
        jo.put("type", dotType.value);
        jo.put("dot-size", dotSize);
        jo.put("halo-size", haloSize);
        jo.put("colour", colour);
        jo.put("tip", tip);
        return jo.toString();
    }
}
