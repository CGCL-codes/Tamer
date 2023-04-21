package com.android1.amarena2d.nodes.behavior.delegates;

import com.android1.amarena2d.commons.Callback;
import com.android1.amarena2d.nodes.behavior.ActionTarget;

public interface DynamicsDelegate {

    public MoveOptions to(float x, float y, float duration);

    public MoveOptions by(float x, float y, float duration);

    public MoveWidthOptions with(float velocityX, float velocityY);

    public void stop();

    public static interface MoveWidthOptions extends MoveOptions {

        public MoveOptions accelerate(float accelerateX, float accelerateY, float duration);

        public MoveOptions accelerate(float accelerateX, float accelerateY, float terminalVelocityX, float terminalVelocityY);
    }

    public static interface MoveOptions {

        public void setOnFinished(Callback<ActionTarget> callback);
    }
}
