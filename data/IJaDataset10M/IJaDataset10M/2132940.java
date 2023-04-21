package com.factoria2.absolute.widgets.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.factoria2.absolute.widgets.core.AbsWidget;
import com.factoria2.absolute.widgets.core.geom.Rectangle;
import com.factoria2.absolute.widgets.core.geom.Size;

public class ATower<T extends AbsWidget> extends AbsWidget {

    private boolean vertical = true;

    private List<T> children = new ArrayList<T>();

    private List<T> roChildren = Collections.unmodifiableList(children);

    public ATower() {
    }

    public ATower(final boolean vertical) {
        setVertical(vertical);
    }

    public ATower(final boolean vertical, final T... children) {
        setVertical(vertical);
        for (T child : children) {
            addWidget(child);
        }
    }

    public void addWidget(final T child) {
        children.add(child);
        addChild(child);
    }

    public List<T> getChildren() {
        return roChildren;
    }

    @Override
    public Size getPreferredSize() {
        Size size = Size.EMPTYNESS;
        if (vertical) {
            for (AbsWidget child : children) {
                Size pref = child.getPreferredSize();
                if (size.getWidth() < pref.getWidth()) {
                    size = new Size(pref.getWidth(), size.getHeight() + pref.getHeight());
                } else {
                    size = new Size(size.getWidth(), size.getHeight() + pref.getHeight());
                }
            }
        } else {
            for (AbsWidget child : children) {
                Size pref = child.getPreferredSize();
                if (size.getHeight() < pref.getHeight()) {
                    size = new Size(size.getWidth() + pref.getWidth(), pref.getHeight());
                } else {
                    size = new Size(size.getWidth() + pref.getWidth(), size.getHeight());
                }
            }
        }
        return size;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(final boolean vertical) {
        this.vertical = vertical;
        relayout();
    }

    @Override
    protected void layoutChildren(final Rectangle clientBounds) {
        if (vertical) {
            int y = 0;
            for (AbsWidget child : children) {
                Size pref = child.getPreferredSize();
                child.setBounds(0, y, pref);
                y += pref.getHeight();
            }
        } else {
            int x = 0;
            for (AbsWidget child : children) {
                Size pref = child.getPreferredSize();
                child.setBounds(x, 0, pref);
                x += pref.getWidth();
            }
        }
    }
}
