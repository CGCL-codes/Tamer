package org.andrewberman.ui.menu;

import processing.core.PApplet;

public class EmptyMenu extends Menu {

    public EmptyMenu(PApplet app) {
        super(app);
        open();
    }

    public void close() {
    }

    @Override
    public MenuItem add(MenuItem item) {
        return super.add(item);
    }

    @Override
    public MenuItem create(String label) {
        throw new RuntimeException("Can't create stuff on an Empty menu!");
    }

    @Override
    public void zSort() {
        super.zSort();
    }
}
