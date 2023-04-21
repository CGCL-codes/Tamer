package jme3clogic;

/**
 * A reaction is something that happens when the condition of a trigger hold.
 * @author pgi
 */
public abstract class Reaction implements Cloneable {

    /**
     * A default reaction that does nothing.
     */
    public static final Reaction NOTHING = new Reaction() {

        /**
	 * Does nothing.
	 */
        @Override
        public void act(float timePerFrame) {
        }
    };

    /** disposes this reaction */
    public void dispose() {
    }

    /** executes this task */
    public abstract void act(float timePerFrame);

    /** defaults to true, check if this reaction has ended its task */
    public boolean finished() {
        return true;
    }

    public void reset() {
    }

    ;

    /** Returns a new reaction that executes this and that simultaneously */
    public Reaction and(Reaction that) {
        return new ReactionAnd(this, that);
    }

    /** Returns a new reaction that executes this until is finished, then that */
    public Reaction then(Reaction that) {
        return new ReactionThen(this, that);
    }

    private static class ReactionAnd extends Reaction {

        private final Reaction a, b;

        public ReactionAnd(Reaction a, Reaction b) {
            this.a = a;
            this.b = b;
        }

        public void act(float tpf) {
            a.act(tpf);
            b.act(tpf);
        }

        public boolean finished() {
            return a.finished() & b.finished();
        }
    }

    private static class ReactionThen extends Reaction {

        private final Reaction a, b;

        private boolean firstDone;

        public ReactionThen(Reaction a, Reaction b) {
            this.a = a;
            this.b = b;
        }

        public void act(float tpf) {
            if (firstDone) {
                b.act(tpf);
            } else {
                a.act(tpf);
                firstDone = a.finished();
            }
        }

        public boolean finished() {
            return a.finished() & b.finished();
        }
    }

    public Reaction clone() {
        return this;
    }
}
