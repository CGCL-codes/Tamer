package com.google.gwt.maeglin89273.game.mengine.layer;

import com.google.gwt.maeglin89273.game.mengine.game.HasGameLoop;

/**
 * @author Maeglin Liao
 *
 */
public abstract class Layer implements HasGameLoop {

    private int index = -1;

    private GroupLayer parent;

    void setParentLayer(GroupLayer p) {
        this.parent = p;
    }

    public GroupLayer getParentLayer() {
        return parent;
    }

    void setIndex(int i) {
        this.index = i;
    }

    public int getIndex() {
        return index;
    }
}
