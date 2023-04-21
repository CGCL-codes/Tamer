package opengl.perspective;

import javax.media.opengl.GLAutoDrawable;
import util.Vector3d;

public interface Perspective {

    Vector3d getPosition();

    Vector3d getRotation();

    Vector3d getDirection();

    String getName();

    void setRotation(Vector3d r);

    void setPosition(Vector3d p);

    void setPerspective(GLAutoDrawable gld);

    void cleanUp();
}
