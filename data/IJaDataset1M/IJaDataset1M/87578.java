package uk.ac.bath.base;

import java.util.Vector;
import uk.ac.bath.util.Tweakable;

/**
 *
 *  Base class for environments that want to do animation
 *
 * @author pjl
 */
public abstract class BasicEnvironment implements EnvironmentIF {

    protected Vector<Tweakable> tweaks = new Vector<Tweakable>();

    GuiUpdaterIF guiUpdater;

    private long millisPerFrame;

    protected int nTick;

    private Thread runThread;

    private boolean dispose = false;

    private boolean doZap;

    protected Evaluator evaluator;

    boolean done = false;

    protected BasicEnvironment(Evaluator eval) {
        this.evaluator = eval;
    }

    public final int getTickCount() {
        return nTick;
    }

    public Vector<Tweakable> getTweaks() {
        return tweaks;
    }

    final void notifyMaybe() {
        if (guiUpdater != null) {
            guiUpdater.myUpdate();
        }
        try {
            Thread.sleep(millisPerFrame);
        } catch (InterruptedException e) {
        }
    }

    /**
     * start the simulation (usually on it's own thread)
     */
    public final void run() {
        run(-1);
    }

    private final void run(int nStep) {
        runThread = Thread.currentThread();
        init();
        while (nStep != 0) {
            if (dispose) {
                runThread = null;
                guiUpdater.dispose();
                guiUpdater = null;
                return;
            }
            tick();
            notifyMaybe();
            if (nStep > 0) {
                nStep--;
            }
            nTick++;
        }
    }

    /**
     *
     * request GUI update rate
     *
     * @param millisPerFrame
     */
    public void setFrameRate(long millisPerFrame) {
        this.millisPerFrame = millisPerFrame;
        if (runThread == null) {
            return;
        }
        runThread.interrupt();
    }

    /**
     * Set the object responsible for updating the model view.
     *
     * @param guiUpdater
     */
    public void setGuiUpdater(GuiUpdaterIF guiUpdater) {
        this.guiUpdater = guiUpdater;
    }

    protected abstract void init();

    protected void tick() {
        if (doZap) {
            evaluator.init();
            init();
            doZap = false;
        }
        Fitness fitness = evaluator.tick();
        if (fitness.decided()) {
            if (fitness.success()) return;
            nextEvaluation(fitness);
        }
    }

    public void dispose() {
        dispose = true;
    }

    /**
     *
     * End a trial with a given fitness and start a new one
     *
     * If fitness is null then no previous trial.
     *
     * @param fitness
     */
    public abstract void nextEvaluation(Fitness fitness);

    /**
     * Call this for a complete restart.
     *
     */
    public void zap() {
        doZap = true;
    }

    /**
     *
     * @return   string about the evaluator
     */
    public String reportSetup() {
        return evaluator.reportSetup();
    }
}
