package org.earth.ui;

import org.earth.gl.MyGLUtils;
import org.earth.scene.Scene;
import android.view.MotionEvent;

public class SceneZoomer {

    /**
	 * @define {number} Number of levels to zoom on MouseWheel event.
	 */
    public static final float MOUSERZOOMER_ZOOMSTEP = 0.5f;

    /**
	 * @define {number} How to modify distance on MouseWheel event.
	 */
    public static final float MOUSERZOOMER_DISTANCEMODIFIER = 2.0f;

    /**
	 * @define {number} Duration of zooming animation in miliseconds.
	 */
    public static final int MOUSERZOOMER_DURATION = 120;

    private Animation animation_;

    private boolean fixedAltitude;

    private Scene scene_;

    private float targetX_;

    private float startX_;

    private boolean initMt;

    private float lastPinchSize;

    public SceneZoomer(Scene scene) {
        this.scene_ = scene;
        this.fixedAltitude = this.scene_.camera.fixedAltitude;
    }

    public void handleTouch(MotionEvent e) {
        if (e.getAction() != MotionEvent.ACTION_DOWN && e.getPointerCount() >= 2) {
            float x1 = e.getX(0);
            float y1 = e.getY(0);
            float x2 = e.getX(1);
            float y2 = e.getY(1);
            float x = Math.abs(x1 - x2);
            float y = Math.abs(y1 - y2);
            float pinchSize = (float) Math.sqrt(x * x + y * y);
            if (initMt) {
                float deltaZ = pinchSize - lastPinchSize;
                float altfactor = deltaZ / 50.0f;
                if (this.fixedAltitude) {
                    float currentAlt = this.scene_.camera.getAltitude();
                    currentAlt *= Math.pow(2.0f, -altfactor);
                    this.scene_.camera.setAltitude(currentAlt);
                } else {
                    float currentZoom = this.scene_.camera.getZoom();
                    final float finalZoom = (float) (currentZoom * Math.pow(2.0f, altfactor));
                    MyGLUtils.runOnGlThread(new Runnable() {

                        @Override
                        public void run() {
                            scene_.camera.setZoom(finalZoom);
                        }
                    });
                }
            } else {
                initMt = true;
            }
            lastPinchSize = pinchSize;
        } else {
            lastPinchSize = 0;
            initMt = false;
        }
    }

    /**
	 * Starts zooming in given direction or does nothing if zooming in that
	 * direction is already in progress.
	 * 
	 * @param {number} direction Direction of zooming +1 means in, -1 means out.
	 * @private
	 */
    public void zoom_(float direction) {
        int duration = MOUSERZOOMER_DURATION;
        if (this.animation_ != null && this.fixedAltitude != this.scene_.camera.fixedAltitude) {
            this.animation_.reset();
            this.animation_ = null;
        }
        this.fixedAltitude = this.scene_.camera.fixedAltitude;
        if (this.fixedAltitude) direction *= -1;
        if (this.animation_ != null) {
            if ((this.targetX_ > this.startX_) == (direction > 0)) {
                return;
            } else {
                this.animation_.reset();
                duration *= ((this.fixedAltitude ? this.scene_.camera.getAltitude() : this.scene_.camera.getZoom()) - this.startX_) / (this.targetX_ - this.startX_);
                float tempX = this.targetX_;
                this.targetX_ = this.startX_;
                this.startX_ = tempX;
            }
        } else {
            if (this.fixedAltitude) {
                this.startX_ = this.scene_.camera.getAltitude();
                this.targetX_ = (float) (this.startX_ * Math.pow(MOUSERZOOMER_DISTANCEMODIFIER, direction));
            } else {
                this.startX_ = this.scene_.camera.getZoom();
                this.targetX_ = this.startX_ + direction * MOUSERZOOMER_ZOOMSTEP;
            }
        }
        if (duration <= 0) return;
        this.animation_ = new Animation(this.fixedAltitude ? this.scene_.camera.getAltitude() : this.scene_.camera.getZoom(), this.targetX_, duration);
        this.animation_.play(false);
    }

    ;
}
