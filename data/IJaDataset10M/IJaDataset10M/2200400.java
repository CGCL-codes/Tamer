package net.cyan.activex.word;

import net.cyan.activex.ActiveXObject;
import java.awt.*;

/**
 * <p>Title: border����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author ccs
 * @version 1.0
 */
public abstract class Border implements ActiveXObject {

    public abstract int getArtStyle();

    public abstract void setArtStyle(int artStyle);

    public abstract int getArtWidth();

    public abstract void setArtWidth(int artWidth);

    public abstract Color getColor();

    public abstract void setColor(Color color);

    public abstract boolean isInside();

    public abstract int getLineStyle();

    public abstract void setLineStyle(int lineStyle);

    public abstract int getLineWidth();

    public abstract void setLineWidth(int lineWidth);

    public abstract boolean isVisible();

    public abstract void setVisible(boolean visible);
}
