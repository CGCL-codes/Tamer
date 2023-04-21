package us.wthr.jdem846.model;

import us.wthr.jdem846.model.annotations.ProcessOption;
import us.wthr.jdem846.model.processing.coloring.ColorTintsListModel;

public class TestingOptionModel implements OptionModel {

    private String string1;

    private String string2;

    private double double1;

    private double double2;

    private int int1;

    private int int2;

    public TestingOptionModel() {
    }

    @ProcessOption(id = "us.wthr.jdem846.model.TestingOptionModel.string1", label = "String 1", tooltip = "String 1 Tooltip", enabled = true)
    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    @ProcessOption(id = "us.wthr.jdem846.model.TestingOptionModel.string2", label = "String 2", tooltip = "String 2 Tooltip", enabled = true)
    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }

    @ProcessOption(id = "us.wthr.jdem846.model.TestingOptionModel.double1", label = "Double 1", tooltip = "Double 1 Tooltip", enabled = true)
    public double getDouble1() {
        return double1;
    }

    public void setDouble1(double double1) {
        this.double1 = double1;
    }

    @ProcessOption(id = "us.wthr.jdem846.model.TestingOptionModel.double2", label = "Double 2", tooltip = "Double 2 Tooltip", enabled = true)
    public double getDouble2() {
        return double2;
    }

    public void setDouble2(double double2) {
        this.double2 = double2;
    }

    @ProcessOption(id = "us.wthr.jdem846.model.TestingOptionModel.int1", label = "Integer 1", tooltip = "Integer 1 Tooltip", enabled = true)
    public int getInt1() {
        return int1;
    }

    public void setInt1(int int1) {
        this.int1 = int1;
    }

    @ProcessOption(id = "us.wthr.jdem846.model.TestingOptionModel.int2", label = "Integer 2", tooltip = "Integer 2 Tooltip", enabled = true)
    public int getInt2() {
        return int2;
    }

    public void setInt2(int int2) {
        this.int2 = int2;
    }

    public TestingOptionModel copy() {
        TestingOptionModel copy = new TestingOptionModel();
        copy.string1 = this.string1;
        copy.string2 = this.string2;
        copy.double1 = this.double1;
        copy.double2 = this.double2;
        copy.int1 = this.int1;
        copy.int2 = this.int2;
        return copy;
    }
}
