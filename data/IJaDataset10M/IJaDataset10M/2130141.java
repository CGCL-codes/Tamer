package org.jdesktop.animation.transitions;

import org.junit.*;
import mockit.*;
import org.jdesktop.animation.timing.*;

public final class ScreenTransitionTest {

    @Mocked(methods = { "setAnimator", "start" }, inverse = true)
    private ScreenTransition transition;

    @Mocked
    private Animator animator;

    @Test
    public void setAnimator() {
        new Expectations() {

            {
                animator.isRunning();
                returns(false);
                animator.addTarget(null);
            }
        };
        transition.setAnimator(animator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setAnimatorToNull() {
        transition.setAnimator(null);
    }

    @Test(expected = IllegalStateException.class)
    public void setAnimatorAlreadyRunning() {
        new Expectations() {

            {
                animator.isRunning();
                returns(true);
            }
        };
        transition.setAnimator(animator);
    }

    @Test
    public void startWithNonRunningAnimator() {
        new Expectations() {

            {
                setField(transition, animator);
                animator.isRunning();
                returns(false);
                animator.start();
            }
        };
        transition.start();
    }

    @Test
    public void startWithRunningAnimator() {
        new Expectations() {

            {
                setField(transition, animator);
                animator.isRunning();
                returns(true);
                animator.stop();
                animator.start();
            }
        };
        transition.start();
    }
}
