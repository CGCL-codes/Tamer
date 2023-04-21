package org.voidness.oje2d.particlesystems;

import org.voidness.oje2d.GLColor;
import org.voidness.oje2d.GLImage;

/** A simple snow particle system - EXPERIMENTAL */
public class GLParticleSystemSnow {

    private GLParticle[] particles = null;

    private int numberOfParticles;

    private int startX;

    private int startY;

    private int endX;

    private int endY;

    private float minSpeed;

    private float maxSpeed;

    private int minTimeToLive;

    private int maxTimeToLive;

    private float alphaStep;

    private boolean forever;

    private boolean isDead;

    private long startTime;

    public GLParticleSystemSnow() {
        isDead = true;
    }

    public void setArea(int mStartX, int mStartY, int mEndX, int mEndY) {
        startX = mStartX;
        startY = mStartY;
        endX = mEndX;
        endY = mEndY;
    }

    public void initParticles(String mFilename, int mNumber) {
        numberOfParticles = mNumber;
        particles = new GLParticle[numberOfParticles];
        GLImage image = new GLImage(mFilename, 2, 8, false);
        for (int i = 0; i < numberOfParticles; i++) particles[i] = new GLParticle(image);
    }

    public void setSpeed(float mMinSpeed, float mMaxSpeed) {
        minSpeed = mMinSpeed;
        maxSpeed = mMaxSpeed;
    }

    public void setTimeToLive(int mMinTtl, int mMaxTtl) {
        minTimeToLive = mMinTtl;
        maxTimeToLive = mMaxTtl;
    }

    public void setLoop(boolean mValue) {
        forever = mValue;
    }

    public void setAlphaStep(float mStep) {
        alphaStep = mStep;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        isDead = false;
        for (int i = 0; i < numberOfParticles; i++) {
            int xPos = (int) ((Math.random() * (endX - startX)) + startX);
            int yPos = (int) ((Math.random() * (endY - startY)) + startY);
            ;
            float speed = (float) ((Math.random() * (maxSpeed - minSpeed)) + minSpeed);
            int ttl = (int) ((Math.random() * (maxTimeToLive - minTimeToLive)) + minTimeToLive);
            particles[i].init(xPos, yPos, speed, ttl, GLColor.WHITE);
            particles[i].setAlpha(1.0f, alphaStep);
        }
    }

    public void stop() {
        isDead = true;
    }

    public void draw() {
        if (!isDead) {
            for (int i = 0; i < numberOfParticles; i++) if (!particles[i].isDead()) {
                particles[i].draw(0, 0);
                updateParticle(particles[i]);
            }
            if ((System.currentTimeMillis() - startTime) > maxTimeToLive && !forever) isDead = true;
        }
    }

    private void updateParticle(GLParticle mParticle) {
        mParticle.setCurrentTime(System.currentTimeMillis());
        if (mParticle.getCurrentTime() - mParticle.getLastTime() > (float) mParticle.getRadiusSpeed() / 1000) {
            if (Math.random() > 0.5) mParticle.setXPos((int) (mParticle.getXPos() + mParticle.getRadiusSpeed() / 5)); else mParticle.setXPos((int) (mParticle.getXPos() - mParticle.getRadiusSpeed() / 5));
            mParticle.setYPos((int) (mParticle.getRadius()));
            float radius = mParticle.getRadius() + mParticle.getRadiusSpeed();
            mParticle.setRadius(radius);
            mParticle.updateAlpha();
            if ((System.currentTimeMillis() - mParticle.getStartTime()) > mParticle.getTimeToLive()) mParticle.kill();
            if (mParticle.getXPos() < startX || mParticle.getXPos() > endX || mParticle.getYPos() < startY || mParticle.getYPos() > endY) mParticle.kill();
            if (forever && mParticle.isDead()) resurrect(mParticle);
            mParticle.setLastTime(mParticle.getCurrentTime());
        }
    }

    private void resurrect(GLParticle mParticle) {
        int xPos = (int) ((Math.random() * (endX - startX)) + startX);
        int yPos = startY;
        float speed = (float) ((Math.random() * (maxSpeed - minSpeed)) + minSpeed);
        int ttl = (int) ((Math.random() * (maxTimeToLive - minTimeToLive)) + minTimeToLive);
        mParticle.init(xPos, yPos, speed, ttl, GLColor.WHITE);
        mParticle.setAlpha(1.0f, alphaStep);
    }

    public boolean isDead() {
        return isDead;
    }

    public int getLiveParticleCount() {
        if (!isDead) {
            int value = 0;
            for (int i = 0; i < numberOfParticles; i++) if (!particles[i].isDead()) value++;
            return value;
        }
        return 0;
    }

    public int getParticleCount() {
        return numberOfParticles;
    }
}
