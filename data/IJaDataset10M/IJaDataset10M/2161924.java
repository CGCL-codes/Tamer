package kfschmidt.joglrenderer;

import kfschmidt.rendering.*;
import kfschmidt.geom3d.*;
import java.awt.Container;

public class JOGLRenderer implements OnscreenRenderer {

    RenderListener mConsumer;

    public void setDisplay(Container container) {
    }

    public void render(Scene scene, RenderListener consumer, RenderingHints3D hints) throws InterruptedException {
    }

    public void printStats() {
        System.out.println("Render Stats not implemented.");
    }
}
