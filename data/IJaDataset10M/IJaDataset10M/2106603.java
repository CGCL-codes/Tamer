package com.das.sampler.logic;

import com.das.core.logic.BizModel;

public interface Sampler extends BizModel {

    public static final String FLD_ID = "id";

    public static final String FLD_X = "x";

    public static final String FLD_Y = "y";

    public static final String FLD_WIDTH = "width";

    public static final String FLD_HEIGHT = "height";

    public Long getId();

    public void setId(Object id);

    public Integer getX();

    public void setX(Object x);

    public Integer getY();

    public void setY(Object y);

    public Integer getWidth();

    public void setWidth(Object width);

    public Integer getHeight();

    public void setHeight(Object height);
}
