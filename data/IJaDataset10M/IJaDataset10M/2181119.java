package com.exult.android;

import java.util.Vector;
import android.graphics.Point;

public abstract class Gump extends GameSingletons {

    protected int shapeNum;

    protected ShapeFrame shape;

    protected int x, y;

    protected Vector<GumpWidget> elems;

    protected boolean handlesKbd;

    protected void addElem(GumpWidget w) {
        elems.add(w);
    }

    protected void initShape(int shnum, ShapeFiles file) {
        if (file == null) file = ShapeFiles.GUMPS_VGA;
        shape = file.getShape(shnum, 0);
        shapeNum = shnum;
    }

    public Gump(int initx, int inity, int shnum, ShapeFiles file) {
        initShape(shnum, file);
        elems = new Vector<GumpWidget>();
        x = initx;
        y = inity;
        gumpman.addGump(this);
    }

    public Gump(int shnum) {
        initShape(shnum, ShapeFiles.GUMPS_VGA);
        elems = new Vector<GumpWidget>();
        setPos();
        gumpman.addGump(this);
    }

    public Gump(int shnum, ShapeFiles file) {
        initShape(shnum, file);
        elems = new Vector<GumpWidget>();
        setPos();
        gumpman.addGump(this);
    }

    protected Gump(ShapeFrame s) {
        shape = s;
        shapeNum = -1;
        elems = new Vector<GumpWidget>();
        setPos();
        gumpman.addGump(this);
    }

    public void close() {
        gumpman.closeGump(this);
    }

    public void updateGump() {
    }

    public final int getShapenum() {
        return shapeNum;
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final void setPos(int newx, int newy) {
        x = newx;
        y = newy;
    }

    public final void setPos() {
        x = (gwin.getWidth() - shape.getWidth()) / 2 + shape.getXLeft();
        y = (gwin.getHeight() - shape.getHeight()) / 2 + shape.getYAbove();
    }

    public void getDirty(Rectangle rect) {
        if (shape == null) rect.set(0, 0, 0, 0); else rect.set(x - shape.getXLeft(), y - shape.getYAbove(), shape.getWidth(), shape.getHeight());
    }

    public void paint() {
        shape.paint(gwin.getWin(), x, y);
        gwin.setPainted();
        paintElems();
    }

    public final void paintElems() {
        if (elems != null) {
            int cnt = elems.size();
            for (int i = 0; i < cnt; ++i) elems.elementAt(i).paint();
        }
    }

    public final boolean canHandleKbd() {
        return handlesKbd;
    }

    public ContainerGameObject getContainer() {
        return null;
    }

    public ContainerGameObject findActor(int mx, int my) {
        return null;
    }

    public final ContainerGameObject getContOrActor(int mx, int my) {
        ContainerGameObject ret = findActor(mx, my);
        if (ret != null) return ret;
        return getContainer();
    }

    public void getShapeRect(Rectangle r, GameObject obj) {
        r.set(0, 0, 0, 0);
    }

    public void getShapeLocation(Point loc, GameObject obj) {
        loc.set(0, 0);
    }

    public GameObject findObject(int mx, int my) {
        return null;
    }

    public boolean isPersistent() {
        return false;
    }

    public boolean isModal() {
        return false;
    }

    public final int getShapeNum() {
        return shapeNum;
    }

    public boolean hasPoint(int sx, int sy) {
        return shape != null && shape.hasPoint(sx - x, sy - y);
    }

    public GumpWidget.Button onButton(int mx, int my) {
        int cnt = elems != null ? elems.size() : 0;
        for (int i = 0; i < cnt; ++i) {
            GumpWidget w = elems.elementAt(i);
            GumpWidget.Button b = w.onButton(mx, my);
            if (b != null) return b;
        }
        return null;
    }

    public boolean add(GameObject obj, int mx, int my, int sx, int sy, boolean dont_check, boolean combine) {
        return false;
    }

    public void remove(GameObject obj) {
    }

    public boolean isDraggable() {
        return true;
    }

    protected void addCheckMark(int checkx, int checky) {
        checkx += 16;
        checky -= 12;
        elems.add(new GumpWidget.Checkmark(this, checkx, checky));
    }

    public static class Container extends Gump {

        protected ContainerGameObject container;

        protected Rectangle objectArea = new Rectangle();

        private Rectangle paintBox = new Rectangle();

        private void initialize(int shnum) {
            if (shnum == game.getShape("gumps/box")) {
                setObjectArea(46, 28, 74, 32, 8, 56);
            } else if (shnum == game.getShape("gumps/crate")) {
                setObjectArea(50, 20, 80, 24, 8, 64);
            } else if (shnum == game.getShape("gumps/barrel")) {
                setObjectArea(32, 32, 40, 40, 12, 124);
            } else if (shnum == game.getShape("gumps/bag")) {
                setObjectArea(48, 20, 66, 44, 8, 66);
            } else if (shnum == game.getShape("gumps/backpack")) {
                setObjectArea(36, 36, 85, 40, 8, 62);
            } else if (shnum == game.getShape("gumps/basket")) {
                setObjectArea(42, 32, 70, 26, 8, 56);
            } else if (shnum == game.getShape("gumps/chest")) {
                setObjectArea(40, 18, 60, 37, 8, 46);
            } else if (shnum == game.getShape("gumps/shipshold")) {
                setObjectArea(38, 10, 82, 80, 8, 92);
            } else if (shnum == game.getShape("gumps/drawer")) {
                setObjectArea(36, 12, 70, 26, 8, 46);
            } else if (shnum == game.getShape("gumps/tree")) {
                setObjectArea(62, 22, 36, 44, 9, 100);
            } else if (shnum == game.getShape("gumps/body")) {
                setObjectArea(36, 46, 84, 40, 8, 70);
            } else setObjectArea(52, 22, 60, 40, 8, 64);
        }

        protected Container(Actor a, int initx, int inity, int shnum) {
            super(initx, inity, shnum, ShapeFiles.GUMPS_VGA);
            container = a;
        }

        public Container(ContainerGameObject cont, int initx, int inity, int shnum) {
            super(initx, inity, shnum, ShapeFiles.GUMPS_VGA);
            container = cont;
            initialize(shnum);
        }

        public Container(ContainerGameObject cont, int initx, int inity, int shnum, ShapeFiles file) {
            super(initx, inity, shnum, file);
            container = cont;
            initialize(shnum);
        }

        public Container(ContainerGameObject cont, int shnum) {
            super(shnum);
            container = cont;
            initialize(shnum);
        }

        public Container(int shnum, ShapeFiles file) {
            super(shnum, file);
            initialize(shnum);
        }

        protected final void setObjectArea(int x, int y, int w, int h, int checkx, int checky) {
            objectArea.set(x, y, w, h);
            addCheckMark(checkx, checky);
        }

        protected final void setObjectArea(int x, int y, int w, int h) {
            objectArea.set(x, y, w, h);
        }

        public ContainerGameObject getContainer() {
            return container;
        }

        public void getShapeRect(Rectangle r, GameObject obj) {
            ShapeFrame s = obj.getShape();
            if (s == null) r.set(0, 0, 0, 0);
            r.set(x + objectArea.x + obj.getTx() - s.getXLeft(), y + objectArea.y + obj.getTy() - s.getYAbove(), s.getWidth(), s.getHeight());
        }

        public void getShapeLocation(Point loc, GameObject obj) {
            loc.x = x + objectArea.x + obj.getTx();
            loc.y = y + objectArea.y + obj.getTy();
        }

        public GameObject findObject(int mx, int my) {
            if (container == null) return null;
            ObjectList.ObjectIterator iter = container.getIterator();
            GameObject obj, found = null;
            Rectangle box = new Rectangle();
            while ((obj = iter.next()) != null) {
                getShapeRect(box, obj);
                if (box.hasPoint(mx, my)) {
                    ShapeFrame s = obj.getShape();
                    int ox = x + objectArea.x + obj.getTx(), oy = y + objectArea.y + obj.getTy();
                    if (s.hasPoint(mx - ox, my - oy)) found = obj;
                }
            }
            return found;
        }

        public boolean add(GameObject obj, int mx, int my, int sx, int sy, boolean dont_check, boolean combine) {
            if (container == null || (!cheat.inHackMover() && !dont_check && !container.hasRoom(obj))) return false;
            GameObject onobj = findObject(mx, my);
            if (onobj != null && onobj != obj && onobj.drop(obj)) return true;
            if (!container.add(obj, dont_check)) return false;
            if (sx == -1 && sy == -1 && mx == -1 && my == -1) obj.setShapePos(255, 255); else if (sx != -2 && sy != -2 && mx != -2 && my != -2) {
                sx -= x + objectArea.x;
                sy -= y + objectArea.y;
                ShapeFrame shape = obj.getShape();
                if (sx - shape.getXLeft() < 0) sx = shape.getXLeft(); else if (sx + shape.getXRight() > objectArea.w) sx = objectArea.w - shape.getXRight();
                if (sy - shape.getYAbove() < 0) sy = shape.getYAbove(); else if (sy + shape.getYBelow() > objectArea.h) sy = objectArea.h - shape.getYBelow();
                obj.setShapePos(sx, sy);
            }
            return true;
        }

        public void remove(GameObject obj) {
            container.remove(obj);
            gwin.setAllDirty();
        }

        public void paint() {
            super.paint();
            if (container == null) return;
            paintBox.set(objectArea.x + x, objectArea.y + y, objectArea.w, objectArea.h);
            ObjectList.ObjectIterator iter = container.getIterator();
            int cury = 0, curx = 0;
            int endy = paintBox.h, endx = paintBox.w;
            int loop = 0;
            GameObject obj;
            while ((obj = iter.next()) != null) {
                ShapeFrame shape = obj.getShape();
                if (shape == null) continue;
                int objx = obj.getTx() - shape.getXLeft() + 1 + objectArea.x;
                int objy = obj.getTy() - shape.getYAbove() + 1 + objectArea.y;
                if (!objectArea.hasPoint(objx, objy) || !objectArea.hasPoint(objx + shape.getXRight() - 1, objy + shape.getYBelow() - 1)) {
                    int px = curx + shape.getWidth(), py = cury + shape.getHeight();
                    if (px > endx) px = endx;
                    if (py > endy) py = endy;
                    obj.setShapePos(px - shape.getXRight(), py - shape.getYBelow());
                    curx += shape.getWidth() - 1;
                    if (curx >= endx) {
                        cury += 8;
                        curx = 0;
                        if (cury >= endy) cury = 2 * (++loop);
                    }
                }
                obj.paintShape(paintBox.x + obj.getTx(), paintBox.y + obj.getTy());
                if (obj == GameWindow.targetObj) obj.paintOutline(paintBox.x + obj.getTx(), paintBox.y + obj.getTy(), ShapeID.HIT_PIXEL); else if (obj == GameWindow.onObj) obj.paintOutline(paintBox.x + obj.getTx(), paintBox.y + obj.getTy(), ShapeID.POISON_PIXEL);
            }
        }
    }

    public abstract static class Modal extends Gump implements ExultActivity.ClickTracker {

        protected boolean done;

        protected GumpWidget.Button pushed;

        public Modal(int initx, int inity, int shnum, ShapeFiles file) {
            super(initx, inity, shnum, file);
        }

        public Modal(int shnum, ShapeFiles file) {
            super(shnum, file);
        }

        public Modal(ShapeFrame s) {
            super(s);
        }

        public Modal(int shnum) {
            super(shnum);
        }

        public void close() {
            done = true;
            System.out.println("Gump: close");
            super.close();
        }

        public final boolean isDone() {
            return done;
        }

        public void onUp(int mx, int my) {
            close();
        }

        public void keyDown(int chr) {
        }

        public void textInput(int chr, int unicode) {
        }

        @Override
        public void onMotion(int x, int y) {
        }

        public void onDown(int x, int y) {
        }

        public boolean isModal() {
            return true;
        }

        private static class GumpThread extends Thread {

            private Gump.Modal gump;

            private Point p = new Point();

            private int mouseShape;

            public GumpThread(Gump.Modal g, int mouse) {
                gump = g;
                mouseShape = mouse;
            }

            @Override
            public void run() {
                System.out.println("GumpThread: run");
                mouse.setLocation(gwin.getWidth() / 2, gwin.getHeight() / 2);
                while (!gump.isDone()) {
                    ExultActivity.getClick(p, gump, mouseShape);
                    gump.onUp(p.x, p.y);
                }
            }
        }

        public Thread track(int mouseShape) {
            GumpThread t = new GumpThread(this, mouseShape);
            t.start();
            return t;
        }
    }
}
