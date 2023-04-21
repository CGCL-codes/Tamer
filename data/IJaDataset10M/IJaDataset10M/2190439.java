package net.cyan.activex.word;

import net.cyan.activex.*;
import java.awt.*;

/**
 * <p>Title: borders����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author ccs
 * @version 1.0
 */
public abstract class Borders implements ActiveXCollection {

    public abstract Border item(int index);

    public abstract boolean isEnable();

    public abstract void setEnable(boolean enable);

    public abstract int getDistanceFrom();

    public abstract void setDistanceFrom(int distanceFrom);

    public abstract boolean isAlwaysInFront();

    public abstract void setAlwaysInFront(boolean alwaysInFront);

    public abstract int getDistanceFromBottom();

    public abstract void setDistanceFromBottom(int distanceFromBottom);

    public abstract int getDistanceFromLeft();

    public abstract void setDistanceFromLeftm(int distanceFromLeft);

    public abstract int getDistanceFromTop();

    public abstract void setDistanceFromTop(int distanceFromTop);

    public abstract int getDistanceFromRight();

    public abstract void setDistanceFromRight(int distanceFromRight);

    public abstract boolean isEnableFirstPageInSection();

    public abstract void setEnableFirstPageInSection(boolean enableFirstPageInSection);

    public abstract boolean isEnableOtherPagesInSection();

    public abstract void setEnableOtherPagesInSection(boolean enableOtherPagesInSection);

    public abstract boolean isHasHorizontal();

    public abstract void setHasHorizontal(boolean hasHorizontal);

    public abstract boolean isHasVertical();

    public abstract void setHasVertical(boolean hasVertical);

    public abstract Color getInsideColor();

    public abstract void setInsideColor(Color color);

    public abstract int getInsideLineStyle();

    public abstract void setInsideLineStyle(int lineStyle);

    public abstract int getInsideLineWidth();

    public abstract void setInsideLineWidth(int lineWidth);

    public abstract Color getOutColor();

    public abstract void setOutColor(Color color);

    public abstract int getOutLineStyle();

    public abstract void setOutLineStyle(int lineStyle);

    public abstract int getOutLineWidth();

    public abstract void setOutLineWidth(int lineWidth);

    public abstract boolean isJoinBorders();

    public abstract void setJoinBorders(boolean joinBorders);

    public abstract boolean isShadow();

    public abstract void setShadow(boolean shadow);

    public abstract boolean isSurroundFooter();

    public abstract void setSurroundFooter(boolean surroundFooter);

    public abstract boolean isSurroundHeader();

    public abstract void setSurroundHeader(boolean surroundHeader);
}
