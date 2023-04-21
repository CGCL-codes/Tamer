package net.sf.doolin.app.sc.client.renderer;

import net.sf.doolin.app.sc.client.bean.GameObject;

public abstract class AbstractGameRenderer<T extends GameObject> implements GameRenderer {

    private final GameObject gameObject;

    public AbstractGameRenderer(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return this.gameObject;
    }
}
