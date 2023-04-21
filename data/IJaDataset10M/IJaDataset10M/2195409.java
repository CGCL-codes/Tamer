package model;

import common.statistics.Series;

public abstract class ModelPart<T extends AbstractModel> implements ComputablePart<T> {

    public double[] x;

    public double[] y;

    public double[] yt;

    public Series xDist;
}
