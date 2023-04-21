package com.jmbaai.bombsight.bomb;

import gov.nasa.worldwind.Disposable;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Renderable;
import gumbo.model.state.Animatable;
import gumbo.model.state.FrameTime;
import javax.media.opengl.GL;
import com.jmbaai.bombsight.tech.math.MathUtils;

/**
 * Dynamic model of an un-oriented ground burst. Burst position is
 * constant.
 * <P>
 * Unless otherwise noted, units are metric, distance is in meters, time is in
 * seconds.
 * 
 * @author Jon Barrilleaux of JMB and Associates, Inc.
 */
public interface XXXGroundBurst extends Animatable, Renderable, Disposable {

    public Angle getLatitude();

    public Angle getLongitude();

    public double getElevation();

    public Globe getGlobe();

    public boolean isDone();

    public class DefaultImpl implements XXXGroundBurst {

        /**
		 * Creates an instance.
		 * @param shape Never null.
		 * @param globe Never null.
		 * @param latitude Never null.
		 * @param longitude Never null.
		 */
        public DefaultImpl(BombBurstType shape, Globe globe, Angle latitude, Angle longitude) {
            if (shape == null) throw new IllegalArgumentException();
            if (globe == null) throw new IllegalArgumentException();
            if (latitude == null) throw new IllegalArgumentException();
            if (longitude == null) throw new IllegalArgumentException();
            _shape = shape;
            _globe = globe;
            _latitude = latitude;
            _longitude = longitude;
            _elevation = _globe.getElevationModel().getElevation(_latitude, _longitude);
            _isDone = false;
        }

        public final Angle getLatitude() {
            return _latitude;
        }

        public final Angle getLongitude() {
            return _longitude;
        }

        public final double getElevation() {
            return _elevation;
        }

        public final Globe getGlobe() {
            return _globe;
        }

        public final double getRadius() {
            return _radius;
        }

        public final boolean isDone() {
            return _isDone;
        }

        public void animateState(FrameTime frameTime) {
            if (_startTime < 0.0) _startTime = frameTime.getSimTime().getTime();
            double elapsedTime = frameTime.getSimTime().getTime() - _startTime;
            if (_isDone) return;
            _radius += elapsedTime * BURST_SPEED;
            _isDone = elapsedTime > BURST_DURATION;
        }

        public final void render(DrawContext dc) {
            if (_isDone) return;
            if (dc == null) throw new IllegalArgumentException();
            GL gl = dc.getGL();
            gl.glMatrixMode(GL.GL_MODELVIEW);
            gl.glPushMatrix();
            MathUtils.toGlobalRenderHPR(dc, _globe, _latitude, _longitude, _elevation, Angle.ZERO, Angle.ZERO, Angle.ZERO);
            _shape.render(dc, _radius);
            gl.glPopMatrix();
        }

        public void dispose() {
            _shape = null;
        }

        private double _startTime = -1;

        private double _tickTime = -1;

        private BombBurstType _shape;

        private Globe _globe;

        private Angle _latitude;

        private Angle _longitude;

        private double _elevation;

        private double _radius = 0.0;

        private boolean _isDone = false;

        public static final double BURST_SPEED = 100.0;

        public static final double BURST_DURATION = 10.0;
    }
}
