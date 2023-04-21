package org.jalgo.module.heapsort.anim;

/**
 * <p>This time entity controls an animation. The animation is being run
 * for local times in the real interval [0..1]. The time transformation
 * is set up in the constructor according to the absolute <code>start</code>
 * time desired for the animation as well as the duration it reports.</p>
 * 
 * <p>This class does only support at most one listener for two reasons:
 * a) this should suffice in most cases, and otherwise, the listener
 * could act as subject to other listeners; b) it's the easiest way of
 * getting around the multiple inheritance problem.</p>
 * 
 * @author mbue
 */
public final class AnimationTimeEntity extends TimeEntity implements Animation {

    private Animation anim;

    private AnimationListener listener;

    private State state = new State1();

    public AnimationTimeEntity(Animation anim, double start) {
        super();
        this.anim = anim;
        this.offset = start;
        this.scale = anim.getDuration();
    }

    public void setListener(AnimationListener l) {
        listener = l;
    }

    protected void doUpdate(double tloc) {
        state.doUpdate(tloc);
    }

    private interface State {

        void doUpdate(double tloc);
    }

    /**
	 * pre-init state
	 * 
	 * @author mbue
	 */
    private class State1 implements State {

        public void doUpdate(double tloc) {
            if (tloc >= 1.0) {
                state = new State3();
                anim.update(1.0);
                if (listener != null) listener.animationComplete(anim);
            } else if (tloc >= 0.0) {
                state = new State2();
                anim.update(0.0);
            }
        }
    }

    /**
	 * running animation
	 * 
	 * @author mbue
	 */
    private class State2 implements State {

        public void doUpdate(double tloc) {
            if (tloc < 1.0) {
                anim.update(tloc);
            } else {
                state = new State3();
                anim.update(1.0);
                if (listener != null) listener.animationComplete(anim);
            }
        }
    }

    /**
	 * post-done state
	 * 
	 * @author mbue
	 */
    private class State3 implements State {

        public void doUpdate(double tloc) {
        }
    }

    public void done() {
        anim.done();
    }

    public double getDuration() {
        return anim.getDuration();
    }

    public void init() {
        anim.init();
    }
}
