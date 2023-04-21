package com.iver.cit.gvsig.fmap.rendering.styling.labeling;

import com.iver.utiles.IPersistence;

/**
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public interface IPlacementConstraints extends IPersistence, Cloneable {

    public static int DefaultDuplicateLabelsMode = IPlacementConstraints.ONE_LABEL_PER_FEATURE_PART;

    public static final int REMOVE_DUPLICATE_LABELS = 2;

    public static final int ONE_LABEL_PER_FEATURE = 3;

    public static final int ONE_LABEL_PER_FEATURE_PART = 4;

    public static final int OFFSET_HORIZONTALY_AROUND_THE_POINT = 5;

    public static final int ON_TOP_OF_THE_POINT = 6;

    public static final int AT_SPECIFIED_ANGLE = 7;

    public static final int AT_ANGLE_SPECIFIED_BY_A_FIELD = 8;

    public static final int HORIZONTAL = 9;

    public static final int PARALLEL = 10;

    public static final int FOLLOWING_LINE = 11;

    public static final int PERPENDICULAR = 12;

    public static final int AT_THE_END_OF_THE_LINE = 13;

    public static final int AT_THE_MIDDLE_OF_THE_LINE = 14;

    public static final int AT_THE_BEGINING_OF_THE_LINE = 15;

    public static final int AT_BEST_OF_LINE = 16;

    public abstract void setPlacementMode(int mode);

    public abstract boolean isBelowTheLine();

    public abstract void setBelowTheLine(boolean b);

    public abstract boolean isAboveTheLine();

    public abstract void setAboveTheLine(boolean b);

    public abstract boolean isOnTheLine();

    public abstract void setOnTheLine(boolean b);

    public abstract void setLocationAlongTheLine(int location);

    public abstract boolean isPageOriented();

    public abstract void setPageOriented(boolean b);

    public abstract void setDuplicateLabelsMode(int mode);

    public abstract int getDuplicateLabelsMode();

    public abstract boolean isParallel();

    public abstract boolean isFollowingLine();

    public abstract boolean isPerpendicular();

    public abstract boolean isHorizontal();

    public abstract boolean isAtTheBeginingOfLine();

    public abstract boolean isInTheMiddleOfLine();

    public abstract boolean isAtTheEndOfLine();

    public boolean isAtBestOfLine();

    public abstract boolean isOnTopOfThePoint();

    public abstract boolean isAroundThePoint();

    public boolean isFitInsidePolygon();

    public void setFitInsidePolygon(boolean b);
}
