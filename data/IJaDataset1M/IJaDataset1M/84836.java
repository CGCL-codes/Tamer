package uk.ac.gla.cmt.animatics.apps.aniplay;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DrawSurfaceListener extends WindowAdapter implements ComponentListener {

    private DrawManager m_draw_manager = null;

    private AbstractRenderingManager m_rendering_manager = null;

    public DrawSurfaceListener(DrawManager draw_manager, AbstractRenderingManager rendering_manager) {
        this.m_draw_manager = draw_manager;
        this.m_rendering_manager = rendering_manager;
    }

    public void componentResized(ComponentEvent e) {
        this.m_draw_manager.setDrawAreaSize(e.getComponent().getSize());
        e.getComponent().repaint();
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
        this.m_rendering_manager.start();
    }

    public void componentHidden(ComponentEvent e) {
        this.m_rendering_manager.stop();
    }

    public void windowIconified(WindowEvent e) {
        this.m_rendering_manager.stop();
    }

    public void windowDeiconified(WindowEvent e) {
        this.m_rendering_manager.start();
    }
}
