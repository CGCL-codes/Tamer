package com.cascadelayout.layout;

import java.util.ArrayList;
import java.util.List;

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
abstract class Segment<S> extends AbstractArea {

    private List<Area> areas;

    private S previous;

    private S next;

    public Segment(int id) {
        super(id);
        areas = new ArrayList<Area>();
    }

    public int indexOf(Area area) {
        return areas.indexOf(area);
    }

    public Area getAreaAt(int index) {
        return areas.get(index);
    }

    public void addArea(Area area) {
        if (areas.contains(area) == false) {
            areas.add(area);
        }
    }

    public int getAreaCount() {
        return areas.size();
    }

    public int getMaxHeight() {
        int height = 0;
        for (Area area : areas) {
            height = Math.max(height, area.getHeight());
        }
        return height;
    }

    public int getMaxWidth() {
        int width = 0;
        for (Area area : areas) {
            width = Math.max(width, area.getWidth());
        }
        return width;
    }

    @Override
    public int getHeight() {
        int height = 0;
        for (Area area : areas) {
            height = Math.max(height, area.getHeight());
        }
        return height;
    }

    @Override
    public int getWidth() {
        int width = 0;
        for (Area area : areas) {
            width = Math.max(width, area.getWidth());
        }
        return width;
    }

    @Override
    public int getX() {
        int x = 0;
        for (Area area : areas) {
            x = Math.min(x, area.getX());
        }
        return x;
    }

    @Override
    public int getY() {
        int y = 0;
        for (Area area : areas) {
            y = Math.min(y, area.getY());
        }
        return y;
    }

    public S getPrevious() {
        return previous;
    }

    public void setPrevious(S previous) {
        this.previous = previous;
    }

    public S getNext() {
        return next;
    }

    public void setNext(S next) {
        this.next = next;
    }

    public List<Area> getAreas() {
        return areas;
    }
}
