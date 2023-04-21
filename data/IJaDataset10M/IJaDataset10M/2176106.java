package org.mtmi.tuio;

public class CursorPoint {

    protected int sessionId;

    protected float cx, cy;

    protected float cXSpeed = 0, cYSpeed = 0;

    protected float cMotionSpeed = 0, cMotionAccel = 0;

    protected int timestamp = -1;

    protected States cState;

    public enum States {

        ADDED, UPDATED, REMOVED
    }

    public CursorPoint(CursorPoint cp) {
        sessionId = cp.sessionId;
        cx = cp.cx;
        cy = cp.cy;
        cXSpeed = cp.cXSpeed;
        cYSpeed = cp.cXSpeed;
        cMotionSpeed = cp.cMotionSpeed;
        cMotionAccel = cp.cMotionAccel;
        timestamp = cp.timestamp;
        cState = cp.cState;
    }

    public CursorPoint(int mSessionId, float mX, float mY, States mState) {
        super();
        sessionId = mSessionId;
        cx = mX;
        cy = mY;
        cState = mState;
    }

    public CursorPoint(int mSessionId, float mX, float mY, float mXSpeed, float mYSpeed, float mMotionSpeed, float mMotionAccel, int mTimestamp, States mState) {
        super();
        sessionId = mSessionId;
        cx = mX;
        cy = mY;
        cXSpeed = mXSpeed;
        cYSpeed = mYSpeed;
        cMotionSpeed = mMotionSpeed;
        cMotionAccel = mMotionAccel;
        timestamp = mTimestamp;
        cState = mState;
    }

    public float SquareDistance(CursorPoint cursor) {
        float dx = cx - cursor.cx;
        float dy = cy - cursor.cy;
        return dx * dx + dy * dy;
    }

    public float SquareDistance(float x, float y) {
        float dx = cx - x;
        float dy = cy - y;
        return dx * dx + dy * dy;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int mSessionId) {
        sessionId = mSessionId;
    }

    public float getX() {
        return cx;
    }

    public void setX(float mX) {
        cx = mX;
    }

    public float getY() {
        return cy;
    }

    public void setY(float mY) {
        cy = mY;
    }

    public float xSpeed() {
        return cXSpeed;
    }

    public void setXSpeed(float mXSpeed) {
        cXSpeed = mXSpeed;
    }

    public float ySpeed() {
        return cYSpeed;
    }

    public void setYSpeed(float mYSpeed) {
        cYSpeed = mYSpeed;
    }

    public float motionSpeed() {
        return cMotionSpeed;
    }

    public void setMotionSpeed(float mMotionSpeed) {
        cMotionSpeed = mMotionSpeed;
    }

    public float motionAccel() {
        return cMotionAccel;
    }

    public void setMotionAccel(float mMotionAccel) {
        cMotionAccel = mMotionAccel;
    }

    public int timestamp() {
        return timestamp;
    }

    public void setTimestamp(int mTimestamp) {
        timestamp = mTimestamp;
    }

    public States getM_state() {
        return cState;
    }

    public void setM_state(States mState) {
        cState = mState;
    }

    ;

    public CursorPoint clone() {
        return new CursorPoint(this);
    }
}
