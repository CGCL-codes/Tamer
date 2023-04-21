package oldjuie;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.SystemColor;

class NDialog extends Dialog {

    public static int Startx, Starty, maxx = 0, maxy = 0, minx = 0, miny = 0;

    public static Point a1;

    public Component hasfocus;

    public int nupanel = 0, nuapplet = 0, nuscrollpane = 0, nubutton = 0, nulabel = 0, nutextarea = 0;

    public int nutextfield = 0, nucheckbox = 0, nuchoice = 0, nuhscrollbar = 0, nuvscrollbar = 0;

    public int nulist = 0, nucanvas = 0;

    public String variable = "";

    public String classname = "Dialog";

    public String sourcename = "";

    public boolean defcolor = true;

    public NDialog(Frame fr) {
        super(fr);
    }

    public NDialog(Frame fr, String title) {
        super(fr, title);
    }

    public void doLayout() {
        super.doLayout();
        if ((NFrame.lastfocused == this) && (this.hasfocus == null)) Ed.edit.prop.updateProperties(this);
    }

    public boolean handleEvent(Event evt) {
        if (evt.id == Event.WINDOW_DESTROY) {
            this.setVisible(false);
            if (NFrame.lastfocused == this) {
                NFrame.lastfocused = null;
                Ed.edit.prop.bound();
            }
            Ed.edit.updatemenus();
        } else if (evt.id == Event.GOT_FOCUS) {
            NFrame.lastfocused = this;
            Ed.edit.updatemenus();
            Ed.edit.prop.bound();
        } else if ((NFrame.lastfocused == this) && (this.hasfocus == null) && (evt.id == Event.WINDOW_MOVED)) Ed.edit.prop.updateProperties(this); else if (evt.id == Event.WINDOW_DEICONIFY) {
            this.setSize(this.getSize().width + 1, this.getSize().height);
            this.setSize(this.getSize().width - 1, this.getSize().height);
            this.doLayout();
        }
        if ((this.hasfocus != null) && (evt.id == Event.KEY_ACTION)) {
            boolean flag1 = true;
            Point a2 = this.hasfocus.getLocation();
            Dimension a3 = this.hasfocus.getSize();
            switch(evt.key) {
                case Event.UP:
                    if ((evt.modifiers & Event.SHIFT_MASK) != 0) {
                        if (!(this.hasfocus instanceof NChoice)) a3.height = a3.height - 1;
                    } else a2.translate(0, -1);
                    break;
                case Event.DOWN:
                    if ((evt.modifiers & Event.SHIFT_MASK) != 0) {
                        if (!(this.hasfocus instanceof NChoice)) a3.height = a3.height + 1;
                    } else a2.translate(0, 1);
                    break;
                case Event.RIGHT:
                    if ((evt.modifiers & Event.SHIFT_MASK) != 0) a3.width = a3.width + 1; else a2.translate(1, 0);
                    break;
                case Event.LEFT:
                    if ((evt.modifiers & Event.SHIFT_MASK) != 0) a3.width = a3.width - 1; else a2.translate(-1, 0);
                    break;
                default:
                    flag1 = false;
            }
            if (flag1) {
                this.hasfocus.setBounds(a2.x, a2.y, a3.width, a3.height);
                Ed.edit.prop.updateProperties(this.hasfocus);
            }
            return true;
        } else if ((this.hasfocus != null) && (evt.key == Event.DELETE)) {
            this.delete(this.hasfocus);
            this.hasfocus = null;
            Ed.edit.prop.bound();
            return true;
        }
        if ((evt.target instanceof Container) && (Ed.press >= 3)) {
            switch(evt.id) {
                case Event.MOUSE_DOWN:
                    a1 = sumParentsXY((Component) evt.target);
                    Startx = evt.x - a1.x;
                    Starty = evt.y - a1.y;
                    break;
                case Event.MOUSE_DRAG:
                    {
                        Graphics g = ((Container) evt.target).getGraphics();
                        g.clearRect(minx, miny, maxx - minx + 1, maxy - miny + 1);
                        int ax = Math.max(Startx, evt.x - a1.x);
                        int ay = Math.max(Starty, evt.y - a1.y);
                        int mx = Math.min(Startx, evt.x - a1.x);
                        int my = Math.min(Starty, evt.y - a1.y);
                        maxx = ax;
                        maxy = ay;
                        minx = mx;
                        miny = my;
                        g.drawRect(mx, my, ax - mx, ay - my);
                        break;
                    }
                case Event.MOUSE_UP:
                    Graphics g = ((Container) evt.target).getGraphics();
                    g.clearRect(minx, miny, maxx - minx + 1, maxy - miny + 1);
                    if ((maxx - minx != 0) || (maxy - miny != 0)) {
                        Component tem3 = new Canvas();
                        switch(Ed.press) {
                            case 3:
                                nupanel++;
                                tem3 = new NPanel("Panel" + String.valueOf(nupanel));
                                break;
                            case 4:
                                nuapplet++;
                                tem3 = new NApplet("Applet" + String.valueOf(nuapplet));
                                break;
                            case 5:
                                nuscrollpane++;
                                tem3 = new NScrollPane();
                                break;
                            case 6:
                                nubutton++;
                                tem3 = new NButton("Button" + String.valueOf(nubutton));
                                break;
                            case 7:
                                nulabel++;
                                tem3 = new NCanvas("Label" + String.valueOf(nulabel));
                                ((NCanvas) tem3).islabel = true;
                                break;
                            case 8:
                                nutextarea++;
                                tem3 = new NTextArea("TextArea" + String.valueOf(nutextarea));
                                break;
                            case 9:
                                nutextfield++;
                                tem3 = new NTextField("TextField" + String.valueOf(nutextfield));
                                break;
                            case 10:
                                nucheckbox++;
                                tem3 = new NCheckbox("Checkbox" + String.valueOf(nucheckbox));
                                break;
                            case 11:
                                nuchoice++;
                                tem3 = new NChoice();
                                ((NChoice) tem3).addItem("Choice" + String.valueOf(nuchoice));
                                break;
                            case 12:
                                nuhscrollbar++;
                                tem3 = new NScrollbar(Scrollbar.HORIZONTAL);
                                break;
                            case 13:
                                nuvscrollbar++;
                                tem3 = new NScrollbar(Scrollbar.VERTICAL);
                                break;
                            case 14:
                                nulist++;
                                tem3 = new NList("List" + String.valueOf(nulist));
                                break;
                            case 15:
                                nucanvas++;
                                tem3 = new NCanvas("Canvas" + String.valueOf(nucanvas));
                                break;
                        }
                        ;
                        if (evt.target instanceof ScrollPane) try {
                            Component com = ((ScrollPane) evt.target).getComponent(0);
                            this.delete(com);
                        } catch (ArrayIndexOutOfBoundsException e) {
                        }
                        if (tem3 instanceof Button) {
                            tem3.setBackground(SystemColor.control);
                            tem3.setForeground(SystemColor.controlText);
                        } else if (tem3 instanceof Scrollbar) {
                            tem3.setBackground(SystemColor.scrollbar);
                            tem3.setForeground(SystemColor.controlText);
                        }
                        ((Container) evt.target).add(tem3);
                        String s = "." + tem3.getClass().getSuperclass().getName();
                        s = s.substring(s.lastIndexOf(".") + 1);
                        if (tem3 instanceof NCanvas) if (((NCanvas) tem3).islabel) s = "Label";
                        try {
                            tem3.getClass().getDeclaredField("classname").set(tem3, s);
                        } catch (Exception e) {
                        }
                        if (this.hasfocus != null) {
                            this.hasfocus.hide();
                            this.hasfocus.show();
                        }
                        ;
                        if (tem3 instanceof NChoice) tem3.reshape(minx, miny, maxx - minx + 1, 21); else tem3.reshape(minx, miny, maxx - minx + 1, maxy - miny + 1);
                        this.hasfocus = (Component) tem3;
                        Ed.edit.prop.bound();
                    }
                    maxx = 0;
                    maxy = 0;
                    minx = 0;
                    miny = 0;
                    break;
            }
            return true;
        } else if (!(evt.target instanceof NDialog)) {
            switch(evt.id) {
                case Event.MOUSE_DOWN:
                case 1001:
                case Event.GOT_FOCUS:
                case Event.SCROLL_PAGE_DOWN:
                case Event.SCROLL_PAGE_UP:
                case Event.SCROLL_LINE_UP:
                case Event.SCROLL_LINE_DOWN:
                case Event.SCROLL_ABSOLUTE:
                    if ((evt.id == Event.GOT_FOCUS) && ((evt.target instanceof NPanel) || (evt.target instanceof NApplet) || (evt.target instanceof NCanvas))) break;
                    if ((evt.target instanceof NPanel) || (evt.target instanceof NApplet) || (evt.target instanceof NCanvas) || (evt.target instanceof NScrollPane)) {
                        a1 = sumParentsXY((Component) evt.target);
                        Point a2 = sumParentsXY(((Component) evt.target).getParent());
                        Startx = evt.x + a2.x - a1.x;
                        Starty = evt.y + a2.y - a1.y;
                    }
                    if (this.hasfocus != evt.target) {
                        if (this.hasfocus != null) {
                            this.hasfocus.hide();
                            this.hasfocus.show();
                        }
                        ;
                        this.hasfocus = (Component) evt.target;
                        Ed.edit.prop.bound();
                        NFrame.doborder((Component) evt.target);
                    } else NFrame.doborder((Component) evt.target);
                    break;
                case Event.MOUSE_DRAG:
                    ((Component) evt.target).setLocation(new Point(evt.x - Startx, evt.y - Starty));
                    break;
                case Event.MOUSE_UP:
                    Ed.edit.prop.updateProperties((Component) evt.target);
                    break;
            }
            return true;
        } else {
            if (evt.id == Event.MOUSE_DOWN) {
                if (this.hasfocus != null) {
                    this.hasfocus.hide();
                    this.hasfocus.show();
                    this.hasfocus = null;
                }
                Ed.edit.prop.bound();
                return true;
            }
        }
        return false;
    }

    public Point sumParentsXY(Component com) {
        Point a1 = new Point(0, 0);
        Point a2;
        Component comp = com;
        while (comp != this) {
            a2 = ((Component) comp).getLocation();
            a1.translate(a2.x, a2.y);
            comp = ((Component) comp).getParent();
        }
        return a1;
    }

    public void delete(Component comp) {
        if (comp != null) {
            countComponents(comp, -1);
            comp.getParent().remove(comp);
        }
    }

    public void countComponents(Component com, int incdec) {
        String s;
        if (com instanceof NScrollbar) {
            if (((NScrollbar) com).getOrientation() == Scrollbar.HORIZONTAL) s = "nuhscrollbar"; else s = "nuvscrollbar";
        } else s = "nu" + com.getClass().getName().substring(1).toLowerCase();
        if (com instanceof NCanvas) if (((NCanvas) com).islabel) s = "nulabel";
        try {
            this.getClass().getDeclaredField(s).setInt(this, this.getClass().getDeclaredField(s).getInt(this) + incdec);
        } catch (Exception e) {
        }
        if (com instanceof Container) {
            for (int i = 0; i < ((Container) com).getComponentCount(); i++) countComponents(((Container) com).getComponent(i), incdec);
        }
    }
}
