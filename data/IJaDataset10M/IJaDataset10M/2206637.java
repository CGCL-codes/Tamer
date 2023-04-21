package base;

import java.applet.Applet;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;
import org.w3c.dom.html.HTMLDocument;

public class CApplet extends Applet implements Runnable, MouseListener, CTitleSetter {

    public void paintComponent(Graphics G) {
        CGraphics.Draw(G);
    }

    @Override
    public void init() {
        addMouseListener(this);
    }

    public void run() {
        CGraphics.Init();
        CGraphics.Component = this;
        CGraphics.TitleSetter = this;
        CBase.Run();
    }

    @Override
    public boolean keyDown(Event e, int key) {
        if (key == Event.ESCAPE) CProject.Action = CProject.MainMenu;
        return true;
    }

    @Override
    public boolean mouseMove(Event evt, int x, int y) {
        CGraphics.MouseX = x;
        CGraphics.MouseY = y;
        repaint();
        return true;
    }

    public void mouseClicked(MouseEvent e) {
        if (CGUI.GadgetUnderMouse != null) {
            CProject.SelectedGadget = CGUI.GadgetUnderMouse;
            CProject.Action = CProject.MenuAction;
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void SetTitle(String Title) {
        try {
            Class c = Class.forName("com.sun.java.browser.plugin2.DOM");
            Method m = c.getMethod("getDocument", new Class[] { java.applet.Applet.class });
            HTMLDocument doc = (HTMLDocument) m.invoke(null, new Object[] { this });
            doc.setTitle(Title);
        } catch (Exception e) {
            System.out.println("New Java Plug-In not available");
        }
    }
}
