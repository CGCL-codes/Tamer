package org.iwidget.model.javascript;

import org.iwidget.desktop.model.*;
import org.mozilla.javascript.*;

/**
 *
 * @author Muhammad Hakim A
 */
public class ScrollElementJS extends ScriptableObject implements IwidgetScroller {

    public String getClassName() {
        if (elementData != null) return elementData.getName(); else return "Scroller";
    }

    public ScrollElementJS() {
    }

    public ScrollElementJS(WidgetElement widget) {
        elementData = new ElementScroller();
        elementData.setName("Scroller");
        elementData.widget = widget;
    }

    public ScrollElementJS(WidgetElement widget, String name) {
        elementData = new ElementScroller();
        elementData.setName(name);
        elementData.widget = widget;
        setDefaults();
    }

    public ElementScroller getElementData() {
        return elementData;
    }

    public void setElementData(ElementScroller d) {
        elementData = d;
    }

    private void setDefaults() {
        try {
            defineProperty("bgColor", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("bgOpacity", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("cursor", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("height", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("hScroller", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("name", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("onKeyDown", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("onKeyUp", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("onMouseUp", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("onMouseDown", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("onMouseEnter", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("onMouseExit", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("scrolling", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("scroll", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("tooltip", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("visible", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("vHeight", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("vWidth", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("vScroller", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("width", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("x", org.iwidget.model.javascript.ScrollElementJS.class, 0);
            defineProperty("y", org.iwidget.model.javascript.ScrollElementJS.class, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void jsConstructor() {
        if (elementData.widget == null) {
            Scriptable scope = ScriptableObject.getTopLevelScope(this);
            Object obj = scope.get("widget", scope);
            elementData.widget = (WidgetElement) Context.toType(obj, org.iwidget.desktop.model.WidgetElement.class);
        }
        setDefaults();
        elementData.widget.uiObjects.add(elementData);
        if (elementData.widget.getUIComponent() != null) elementData.widget.getUIComponent().insertScrollerJS(elementData);
    }

    public String getBgColor() {
        return elementData.getBgColor();
    }

    public void setBgColor(String s) {
        elementData.setBgColor(s);
    }

    public int getBgOpacity() {
        return elementData.getBgOpacity();
    }

    public void setBgOpacity(int i) {
        elementData.setBgOpacity(i);
    }

    public String getCursor() {
        return elementData.getCursor();
    }

    public void setCursor(String string) {
        elementData.setCursor(string);
    }

    public int getHeight() {
        return elementData.getHeight();
    }

    public void setHeight(int i) {
        elementData.setHeight(i);
    }

    public int getHScroller() {
        return elementData.getHScroller();
    }

    public void setHScroller(int i) {
        elementData.setHScroller(i);
    }

    public String getName() {
        return elementData.getName();
    }

    public void setName(String s) {
        elementData.setName(s);
    }

    public int getScrollSpeed() {
        return elementData.getScrollSpeed();
    }

    public void setScrollSpeed(int i) {
        elementData.setScrollSpeed(i);
    }

    public String getScrolling() {
        return elementData.getScrolling();
    }

    public void setScrolling(String dir) {
        elementData.setScrolling(dir);
    }

    public String getScroll() {
        return "";
    }

    public void setScroll(int amount) {
        elementData.setScroll(amount);
    }

    public String getTooltip() {
        return elementData.getTooltip();
    }

    public void setTooltip(String s) {
        elementData.setTooltip(s);
    }

    public boolean getVisible() {
        return elementData.getVisible();
    }

    public void setVisible(boolean b) {
        elementData.setVisible(b);
    }

    public int getVScroller() {
        return elementData.getVScroller();
    }

    public void setVScroller(int i) {
        elementData.setVScroller(i);
    }

    public int getVHeight() {
        return elementData.getVHeight();
    }

    public void setVHeight(int i) {
        elementData.setVHeight(i);
    }

    public int getVWidth() {
        return elementData.getVWidth();
    }

    public void setVWidth(int i) {
        elementData.setVWidth(i);
    }

    public int getWidth() {
        return elementData.getWidth();
    }

    public void setWidth(int i) {
        elementData.setWidth(i);
    }

    public int getX() {
        return elementData.getX();
    }

    public void setX(int i) {
        elementData.setX(i);
    }

    public int getY() {
        return elementData.getY();
    }

    public void setY(int i) {
        elementData.setY(i);
    }

    public void setOnMouseUp(String string) {
        elementData.setAction("onMouseUp", string);
    }

    public String getOnMouseUp() {
        return null;
    }

    public void setOnMouseDown(String string) {
        elementData.setAction("onMouseDown", string);
    }

    public String getOnMouseDown() {
        return null;
    }

    public void setOnMouseEnter(String string) {
        elementData.setAction("onMouseExit", string);
    }

    public String getOnMouseEnter() {
        return null;
    }

    public void setOnMouseExit(String string) {
        elementData.setAction("onMouseEnter", string);
    }

    public String getOnMouseExit() {
        return null;
    }

    public void setOnKeyDown(String string) {
        elementData.setAction("onKeyDown", string);
    }

    public String getOnKeyDown() {
        return null;
    }

    public void setOnKeyUp(String string) {
        elementData.setAction("onKeyUp", string);
    }

    public String getOnKeyUp() {
        return null;
    }

    public void setOnKeyPress(String string) {
        elementData.setAction("onKeyPress", string);
    }

    public String getOnKeyPress() {
        return null;
    }

    private static final long serialVersionUID = 0x3632313736303833L;

    ElementScroller elementData;
}
