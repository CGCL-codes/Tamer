package com.javector.ser.adaptive.list;

import com.javector.ser.adaptive.po.MyItem;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: Kishore G
 * Date: Nov 23, 2005
 * Time: 1:17:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class MyListBean {

    private double[] num;

    private String[] city;

    private Calendar date;

    private MyItem item;

    private int[] someIntegers;

    public double[] getNum() {
        return num;
    }

    public void setNum(double[] num) {
        this.num = num;
    }

    public String[] getCity() {
        return city;
    }

    public void setCity(String[] city) {
        this.city = city;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public MyItem getItem() {
        return item;
    }

    public void setItem(MyItem item) {
        this.item = item;
    }

    public int[] getSomeIntegers() {
        return someIntegers;
    }

    public void setSomeIntegers(int[] someIntegers) {
        this.someIntegers = someIntegers;
    }
}
