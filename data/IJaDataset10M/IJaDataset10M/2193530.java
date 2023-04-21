package uk.ac.gla.cmt.animatics.apps.aniplay;

public class SimpleRenderingManager implements AbstractRenderingManager, Runnable {

    private DrawSurface m_draw_surface = null;

    private DrawManager m_draw_manager = null;

    private Thread m_scene_render_thread = null;

    public SimpleRenderingManager(DrawSurface draw_surface, DrawManager draw_manager) {
        this.m_draw_surface = draw_surface;
        this.m_draw_manager = draw_manager;
    }

    public void start() {
        if (this.m_scene_render_thread == null) {
            this.m_scene_render_thread = new Thread(this);
            this.m_scene_render_thread.setDaemon(true);
            this.m_scene_render_thread.start();
        }
    }

    public void stop() {
        if (this.m_scene_render_thread != null) {
            Thread t = this.m_scene_render_thread;
            this.m_scene_render_thread = null;
            t.interrupt();
        }
    }

    public void run() {
        Thread current_thread = Thread.currentThread();
        while (this.m_scene_render_thread == current_thread) {
            if (this.m_draw_manager.requiresRepaint(false)) {
                if (this.m_draw_manager.renderScene()) {
                    this.m_draw_surface.repaint();
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        }
    }
}
