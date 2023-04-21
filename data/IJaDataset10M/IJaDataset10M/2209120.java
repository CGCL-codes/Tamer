package com.dukesoftware.ongakumusou.canvasdraw;

import java.awt.Graphics2D;
import com.dukesoftware.ongakumusou.data.element.Element;

/**
 * 
 * 
 * @author 
 * @since 2007/11/26
 * @version last update 2007/11/26
 */
public interface Drawer {

    void drawElementsNormal(Graphics2D g);

    void updateAndDrawElementsForClick(Graphics2D g);

    void updateAndDrawElements(Graphics2D g);

    int getWidth();

    int getHeight();

    void setSelect(Element param, double size);

    boolean isSelected(Element param);
}
