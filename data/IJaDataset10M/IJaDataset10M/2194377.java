package com.allesblinkt.leicasdream;

import werkzeug.interpolation.InterpolateLinear;
import werkzeug.interpolation.Interpolator;
import mathematik.Vector3f;
import gestalt.candidates.JoglGLUTBitmapFont;
import gestalt.impl.jogl.extension.quadline.JoglQuadLine;
import gestalt.shape.Color;
import gestalt.shape.Plane;

public class SputnikTag implements Constants {

    public Plane drawable;

    public String oid;

    private Vector3f _position;

    private Vector3f _previousPosition;

    private Vector3f _labelPosition;

    public JoglGLUTBitmapFont label;

    private boolean inDecay = false;

    private int decay = 0;

    private boolean _inited = false;

    public long lastSeen = 0;

    public boolean isAlive = false;

    public boolean isMyOwn = false;

    private int _sightings = 0;

    private Vector3f[] _points;

    private PositionHistory _positionHistory;

    public JoglQuadLine quadLine;

    public SputnikTag(String theOid) {
        this.oid = theOid;
        _position = new Vector3f(0, 0, 0);
        _previousPosition = new Vector3f(0, 0, 0);
        _labelPosition = new Vector3f(0, 0, 0);
        _positionHistory = new PositionHistory(10);
        quadLine = new JoglQuadLine();
        quadLine.material().transparent = true;
        quadLine.setLineWidthInterpolator(new Interpolator(0, 10, new InterpolateLinear()));
    }

    public void activate() {
        drawable.material().color.set(0, 0.7f, 1f, 1f);
        drawable.material().depthtest = false;
        decay = 80;
        inDecay = true;
    }

    public void setPosition(Vector3f p) {
        isAlive = true;
        _inited = true;
        _sightings++;
        lastSeen = System.currentTimeMillis();
        float x = FLOOR_TEXTURE_SIZE * 0.5f * p.x;
        float z = -FLOOR_TEXTURE_SIZE * 0.5f * p.y;
        float y = p.z * FLOOR_HEIGHT - 2 * FLOOR_HEIGHT + 10;
        _previousPosition.set(_position);
        _position.set(x, y, z);
        _labelPosition.set(x, y + 15, z);
        drawable.position().set(_previousPosition);
        label.position.set(_labelPosition);
    }

    public void animate(float blend, boolean isActive, boolean isTheChose) {
        if (_inited) {
            if (isMyOwn == true) {
                drawable.material().color.set(1f, 0, 0, 1f);
                inDecay = false;
            }
            Vector3f smoothPosition = new Vector3f(_position);
            smoothPosition.interpolate(blend, _previousPosition);
            if (_position.y < 0) drawable.material().color.set(FLOOR_A_COLOR);
            if (_position.y > 0 && _position.y < FLOOR_HEIGHT) drawable.material().color.set(FLOOR_B_COLOR);
            if (_position.y > FLOOR_HEIGHT) drawable.material().color.set(FLOOR_C_COLOR);
            if (System.currentTimeMillis() - lastSeen > LIFESPAN && isAlive) {
                isAlive = false;
            }
            if (!isAlive) {
                drawable.material().color.a = 0.1f;
            }
            drawable.position().set(smoothPosition);
            smoothPosition.add(0, 40, 0);
            label.color.set(drawable.material().color);
            label.position.set(smoothPosition);
            if (decay > 0) {
                decay--;
                drawable.material().color.set(1f, 0, 0, 0.6f);
            }
            if (decay == 0 && inDecay) {
            }
            drawable.setPlaneSizeToTextureSize();
            drawable.scale().scale(0.3f);
            if (isActive) {
                label.active = true;
                if (isTheChose) {
                    drawable.setPlaneSizeToTextureSize();
                    drawable.scale().scale(0.6f);
                }
            } else {
                drawable.material().color.a = 0.1f;
                label.active = false;
            }
        }
    }
}
