package com.cascadelayout.layout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

/**
 * Copyright (C) 2008, 2009 Carlos Eduardo Leite de Andrade
 * 
 * This library is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 * 
 * For more information, contact: www.cascadelayout.com
 * 
 * @author Carlos Eduardo Leite de Andrade
 */
class Area extends AbstractArea {

    private Component component;

    private int x;

    private int y;

    private int componentHeight;

    private int componentWidth;

    private int horizontalLayer;

    private int verticalLayer;

    private Fill fill;

    private Insets margin;

    private boolean leftEmpytFill;

    private boolean rightEmpytFill;

    private boolean topEmpytFill;

    private boolean bottomEmpytFill;

    private boolean reservedArea;

    private boolean computed;

    public Area(int id, int horizontalLayer, int verticalLayer, Component component) {
        super(id);
        this.horizontalLayer = horizontalLayer;
        this.verticalLayer = verticalLayer;
        this.component = component;
        margin = new Insets(0, 0, 0, 0);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getHeight() {
        return componentHeight + margin.bottom + margin.top;
    }

    @Override
    public int getWidth() {
        return componentWidth + margin.left + margin.right;
    }

    public Fill getFill() {
        return fill;
    }

    public void setFill(Fill fill) {
        if (fill != null) {
            this.fill = fill;
        }
    }

    protected boolean hasVerticalFill() {
        if (getFill().equals(Fill.VERTICAL) || getFill().equals(Fill.BOTH)) {
            return true;
        }
        return false;
    }

    protected boolean hasHorizontalFill() {
        if (getFill().equals(Fill.HORIZONTAL) || getFill().equals(Fill.BOTH)) {
            return true;
        }
        return false;
    }

    public Insets getMargin() {
        return margin;
    }

    public void setMargin(Insets margin) {
        if (margin == null) {
            margin = new Insets(0, 0, 0, 0);
        } else {
            margin.bottom = Math.max(margin.bottom, 0);
            margin.top = Math.max(margin.top, 0);
            margin.left = Math.max(margin.left, 0);
            margin.right = Math.max(margin.right, 0);
        }
        this.margin = (Insets) margin.clone();
    }

    public Component getComponent() {
        return component;
    }

    public void apply() {
        int ax = x + margin.left;
        int ay = y + margin.top;
        component.setBounds(ax, ay, componentWidth, componentHeight);
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void reset() {
        super.reset();
        computed = false;
        setShifted(false);
        x = 0;
        y = 0;
        Dimension dim = component.getPreferredSize();
        componentWidth = dim.width;
        componentHeight = dim.height;
    }

    public int getHorizontalLayer() {
        return horizontalLayer;
    }

    public int getVerticalLayer() {
        return verticalLayer;
    }

    public int getComponentHeight() {
        return componentHeight;
    }

    public void setComponentHeight(int componentHeight) {
        this.componentHeight = componentHeight;
    }

    public int getComponentWidth() {
        return componentWidth;
    }

    public void setComponentWidth(int componentWidth) {
        this.componentWidth = componentWidth;
    }

    @Override
    public void applyHorizontalShift(int pixels) {
        if (isComputed() && isEnabled() && isShifted() == false) {
            setShifted(true);
            x += pixels;
            shiftHorizontalLinks(pixels);
        }
    }

    @Override
    public void applyVerticalShift(int pixels) {
        if (isComputed() && isEnabled() && isShifted() == false) {
            setShifted(true);
            y += pixels;
            shiftVerticalLinks(pixels);
        }
    }

    public boolean isEnabled() {
        return (reservedArea || component.isVisible());
    }

    public boolean isComputed() {
        return computed;
    }

    public void setComputed(boolean computed) {
        this.computed = computed;
    }

    public void setReservedArea(boolean reservedArea) {
        this.reservedArea = reservedArea;
    }

    public boolean hasLeftEmpytFill() {
        return leftEmpytFill;
    }

    public void setLeftEmpytFill(boolean leftEmpytFill) {
        this.leftEmpytFill = leftEmpytFill;
    }

    public boolean hasRightEmpytFill() {
        return rightEmpytFill;
    }

    public void setRightEmpytFill(boolean rightEmpytFill) {
        this.rightEmpytFill = rightEmpytFill;
    }

    public boolean hasTopEmpytFill() {
        return topEmpytFill;
    }

    public void setTopEmpytFill(boolean topEmpytFill) {
        this.topEmpytFill = topEmpytFill;
    }

    public boolean hasBottomEmpytFill() {
        return bottomEmpytFill;
    }

    public void setBottomEmpytFill(boolean bottomEmpytFill) {
        this.bottomEmpytFill = bottomEmpytFill;
    }
}
