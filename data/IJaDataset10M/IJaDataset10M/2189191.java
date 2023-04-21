package org.stellarium;

import org.stellarium.projector.DefaultProjector;
import org.stellarium.projector.Projector;
import static org.stellarium.ui.SglAccess.*;
import org.stellarium.ui.render.STexture;
import javax.media.opengl.GL;
import javax.vecmath.Point3d;
import java.awt.*;
import static java.lang.StrictMath.sin;

/**
 * A wrapper to a StelObjectBase subtype.
 * <p/>
 * See the <a href="http://cvs.sourceforge.net/viewcvs.py/stellarium/stellarium/src/stel_object.cpp?view=markup">C++ version of this file</a>
 * and <a href="http://cvs.sourceforge.net/viewcvs.py/stellarium/stellarium/src/stel_object.h?view=markup">its header</a>.
 *
 * @author <a href="mailto:javarome@javarome.net"/>Jerome Beau</a>
 * @version Java
 */
public abstract class StelObject {

    public abstract double getParentSatellitesFOV(NavigatorIfc nav);

    public abstract STexture getPointer();

    public void drawPointer(Navigator nav, DefaultProjector prj, long localTime) {
        Point3d pos = getEarthEquPos(nav);
        Point3d screenPos = new Point3d();
        if (prj.projectEarthEqu(pos, screenPos)) {
            prj.setOrthographicProjection();
            try {
                drawPointerTexture(nav, prj, localTime, screenPos);
            } finally {
                prj.resetPerspectiveProjection();
            }
        }
    }

    protected void drawPointerTexture(Navigator nav, DefaultProjector prj, long localTime, Point3d screenPos) {
    }

    protected void drawPointerTexture1(Navigator nav, DefaultProjector prj, long localTime, Point3d screenPos) {
        double size = getOnScreenSize(prj, nav);
        size += 20d;
        size += 10d * sin(0.002d * localTime);
        STexture texture = getPointer();
        glBindTexture(GL.GL_TEXTURE_2D, texture.getID());
        glEnable(GL.GL_TEXTURE_2D);
        glEnable(GL.GL_BLEND);
        glTranslated(screenPos.x, screenPos.y, 0);
        if (getType() == TYPE.PLANET) {
            glRotatef(localTime / 100, 0, 0, -1);
        }
        glTranslated(-size / 2, -size / 2, 0.0d);
        glRotatef(90, 0, 0, 1);
        texture.displayTexture(-10, -10, 20, 20);
        glRotatef(-90, 0, 0, 1);
        glTranslated(0, size, 0);
        texture.displayTexture(-10, -10, 20, 20);
        glRotatef(-90, 0, 0, 1);
        glTranslated(0, size, 0.0f);
        texture.displayTexture(-10, -10, 20, 20);
        glRotatef(-90, 0, 0, 1);
        glTranslated(0, size, 0);
        texture.displayTexture(-10, -10, 20, 20);
    }

    static class StelObjectUninitialized extends StelObject {

        public STexture getPointer() {
            return null;
        }

        public String getInfoString(NavigatorIfc nav) {
            return "";
        }

        public String getShortInfoString(NavigatorIfc nav) {
            return "";
        }

        public TYPE getType() {
            return TYPE.UNINITIALIZED;
        }

        public String getEnglishName() {
            return "";
        }

        public String getNameI18n() {
            return "";
        }

        public Point3d getEarthEquPos(NavigatorIfc nav) {
            return new Point3d(1, 0, 0);
        }

        public Point3d getObsJ2000Pos(NavigatorIfc nav) {
            return new Point3d(1, 0, 0);
        }

        public float getMag(NavigatorIfc nav) {
            return -10;
        }

        public Color getRGB() {
            return null;
        }

        public double getParentSatellitesFOV(NavigatorIfc nav) {
            return 0;
        }
    }

    static final StelObjectUninitialized uninitialized_object = new StelObjectUninitialized();

    public static StelObjectUninitialized getUninitializedObject() {
        return uninitialized_object;
    }

    public enum TYPE {

        UNINITIALIZED, STAR, PLANET, NEBULA, CONSTELLATION, TELESCOPE
    }

    public void update(long deltaTime) {
    }

    /**
     * Write I18n information about the object in wstring.
     */
    public abstract String getInfoString(NavigatorIfc nav);

    /**
     * The returned wstring can typically be used for object labeling in the sky
     */
    public abstract String getShortInfoString(NavigatorIfc nav);

    public abstract TYPE getType();

    public abstract String getEnglishName();

    public abstract String getNameI18n();

    public abstract Point3d getEarthEquPos(NavigatorIfc nav);

    public abstract Point3d getObsJ2000Pos(NavigatorIfc nav);

    public abstract float getMag(NavigatorIfc nav);

    public abstract Color getRGB();

    public double getCloseFOV(NavigatorIfc nav) {
        return 10;
    }

    public double getSatellitesFOV(NavigatorIfc nav) {
        return -1;
    }

    public double getOnScreenSize(Projector prj, NavigatorIfc nav) {
        return 0;
    }
}
