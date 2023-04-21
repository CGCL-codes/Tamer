package com.androidtowerwars.model;

import com.androidtowerwars.util.Vector2d;

public interface IProjectile {

    public float getX();

    public float getY();

    public void setPosition(float x, float y);

    public float getSpeed();

    public void setSpeed(float speed);

    public ISoldier getTarget();

    public void setTarget(ISoldier target);

    public ITower getParent();

    public void setParent(ITower parent);

    public Vector2d getDirection();
}
