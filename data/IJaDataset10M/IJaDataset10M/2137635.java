package com.physicalneuro.neuro;

import com.jme.math.Vector2f;

public interface NeuroMovementInterface {

    public void setNeuroMovement(Object movement, Object heading);

    public Object getMovement();

    public void setMovement(Object movement);

    public Object updateMovement();

    public void setDesiredPosition(Object position);

    public Object getDesiredPosition();
}
