package dde.colision;

import java.awt.Shape;

public interface DDCollidable {

    public Shape getModelBound();

    public void setModelBound(Shape model);

    public boolean isColiding(DDCollidable other);
}
