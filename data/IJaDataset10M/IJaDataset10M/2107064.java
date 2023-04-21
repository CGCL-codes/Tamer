package net.cyan.activex.word;

import net.cyan.activex.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author ccs
 * @version 1.0
 */
public abstract class TextColumns implements ActiveXCollection {

    public abstract boolean isEvenlySpaced();

    public abstract void setEvenlySpaced(boolean evenlySpaced);

    public abstract int getFlowDirection();

    public abstract void setFlowDirection(int flowDirection);

    public abstract boolean isLineBetween();

    public abstract void setLineBetween(boolean lineBetween);

    public abstract int getSpacing();

    public abstract void setSpacing(int spacing);

    public abstract int getWidth();

    public abstract void setWidth(int width);

    abstract TextColumn item(int index);

    abstract TextColumn add(int widht, String spacing, boolean evenlySpaced);
}
