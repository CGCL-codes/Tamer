package tests.gui;

import javax.media.opengl.GLCanvas;
import gov.nasa.worldwind.layers.AbstractLayer;
import gov.nasa.worldwind.render.DrawContext;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.Label;
import org.fenggui.composites.Window;
import org.fenggui.layout.Alignment;
import org.fenggui.render.jogl.EventBinding;
import org.fenggui.render.jogl.JOGLBinding;
import org.fenggui.theme.DefaultTheme;
import org.fenggui.theme.ITheme;
import org.fenggui.theme.XMLTheme;

public class WWJHelloWorld extends AbstractLayer {

    private GLCanvas canvas = null;

    private Display display = null;

    private final String THEME_PATH = "data/themes/QtCurve/QtCurve.xml";

    public WWJHelloWorld(GLCanvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Load theme
     */
    private void loadTheme() {
        ITheme theme;
        try {
            theme = new XMLTheme(THEME_PATH);
        } catch (Exception e) {
            System.err.println(e);
            theme = new DefaultTheme();
        }
        FengGUI.setTheme(theme);
    }

    /**
     * Build the GUI.
     */
    public void buildGUI() {
        System.out.println("build GUI");
        display = FengGUI.createDisplay(new JOGLBinding(canvas));
        new EventBinding(canvas, display);
        loadTheme();
        Window w = new Window(true, false, false, true);
        Label l = new Label("Hello World!!");
        l.getAppearance().setAlignment(Alignment.MIDDLE);
        w.getContentContainer().addWidget(l);
        w.setXY(50, 50);
        w.setSize(200, 100);
        w.layout();
        display.addWidget(w);
        display.setDepthTestEnabled(true);
    }

    protected void doRender(DrawContext dc) {
        if (display == null) {
            buildGUI();
        }
        display.display();
    }
}
