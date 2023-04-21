package org.jdesktop.swingx.multislider;

/**
 *
 * @author jm158417
 */
public class Thumb<E> {

    private float position;

    private E object;

    private MultiThumbModel model;

    /** Creates a new instance of Thumb */
    public Thumb(MultiThumbModel model) {
        this.model = model;
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
        model.thumbPositionChanged(this);
    }

    public E getObject() {
        return object;
    }

    public void setObject(E object) {
        this.object = object;
        model.thumbValueChanged(this);
    }
}
