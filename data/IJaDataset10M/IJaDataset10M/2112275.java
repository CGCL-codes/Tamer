package org.wicketopia.util;

import org.wicketopia.builder.feature.annotation.metadata.Order;
import org.wicketopia.builder.feature.annotation.required.Required;
import org.wicketopia.builder.feature.annotation.validator.Length;
import org.wicketopia.builder.feature.annotation.visible.Visible;
import org.wicketopia.context.Context;
import java.io.Serializable;

/**
 * @author James Carman
 */
public class EditableBean implements Serializable {

    private String stringProperty;

    private int intProperty;

    private double doubleProperty;

    private Gender gender;

    private boolean bool;

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    @Order(2)
    @Visible({ Context.CREATE, Context.VIEW })
    public double getDoubleProperty() {
        return doubleProperty;
    }

    public void setDoubleProperty(double doubleProperty) {
        this.doubleProperty = doubleProperty;
    }

    @Order(3)
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Order(1)
    public int getIntProperty() {
        return intProperty;
    }

    public void setIntProperty(int intProperty) {
        this.intProperty = intProperty;
    }

    @Order(0)
    @Length(min = 5)
    @Required(Context.CREATE)
    public String getStringProperty() {
        return stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }
}
