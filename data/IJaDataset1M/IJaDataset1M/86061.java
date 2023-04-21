package com.googlecode.boringengine;

public class PauseMenu extends Menu<MenuItem> {

    private PauseMenu() {
        super(200, 0, 400, 0, 40);
    }

    @Override
    public void draw() {
        drawScreen();
        Graphics.drawRect(200, menuY, 400, menuHeight, 9);
        super.draw();
    }

    @Override
    protected void menuItemClicked(MenuItem item) {
        item.r.run();
        if (item.close) isClosed = true;
    }

    @Override
    protected void draw(MenuItem item, int x, int y, boolean isSelected) {
        Render.moveOverWithoutScale(x, y);
        item.anim.setPaused(!isSelected);
        Render.addScale(2);
        item.anim.draw(10, 4);
        DefaultFont.getDefaultFont().draw(50, 4, item.desc);
        Render.addScale(.5);
        if (isSelected) {
            Graphics.drawRect(1, 1, 398, 2, 0);
            Graphics.drawRect(1, 37, 398, 2, 0);
            Graphics.drawRect(1, 1, 2, 36, 0);
            Graphics.drawRect(397, 1, 2, 36, 0);
        }
        Render.moveOverWithoutScale(-x, -y);
    }

    private static PauseMenu menu;

    public static PauseMenu getInstance() {
        if (menu == null) menu = new PauseMenu();
        return menu;
    }

    public static void add(String desc, Animator anim, Runnable r, boolean close) {
        add(desc, anim, r, close, getInstance().numberOfItems() - 1);
    }

    public static void addEngine(String desc, Animator anim, Runnable r, boolean close) {
        add(desc, anim, r, close, getInstance().numberOfItems());
    }

    public static void clear() {
        PauseMenu me = getInstance();
        while (me.numberOfItems() > 0) me.removeItem(0);
    }

    private static void add(String desc, Animator anim, Runnable r, boolean close, int index) {
        MenuItem tmp = new MenuItem();
        tmp.anim = anim;
        tmp.desc = desc;
        tmp.r = r;
        tmp.close = close;
        PauseMenu me = getInstance();
        me.addItem(tmp, index);
        me.menuHeight = me.numberOfItems() * me.menuItemHeight;
        me.menuY = 300 - me.menuHeight / 2;
    }

    @Override
    public void reset() {
        super.reset();
        clearScreen();
    }
}

class MenuItem {

    Animator anim;

    String desc;

    Runnable r;

    boolean close;
}
