package javacream.scene;

import javacream.util.AbstractTree;

/**
 * Renderable
 *
 * @author Glenn Powell
 *
 */
public abstract class Renderable extends AbstractTree<Renderable> {

    public Renderable() {
    }

    public AbstractTree<Renderable> createTree(Renderable data) {
        return data;
    }

    public Renderable getData() {
        return this;
    }

    public abstract void setTime(float time);

    public abstract void update(float dt);

    public abstract void prePaint(GraphicsContext gc);

    public abstract void paint(GraphicsContext gc);

    public abstract void postPaint(GraphicsContext gc);
}
