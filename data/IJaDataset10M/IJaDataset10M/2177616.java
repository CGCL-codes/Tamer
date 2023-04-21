package net.sf.gamine.common;

import javax.microedition.khronos.opengles.*;
import java.util.*;

/**
 * A Sequence object represents an animated sequence displayed in a {@link SequenceView}.  It defines a series of
 * {@link Stage}s that are executed repeatedly.  Each iteration of the Sequence executes each of its Stages
 * in order, with the result of producing a single frame of animation.
 * <p>
 * Although it is possible to use a Sequence object directly, for most purposes you will use its subclass
 * {@link DefaultSequence}, which defines a set of Stages appropriate for most games.
 */
public class Sequence {

    private final List<Stage> stages;

    private SequenceView view;

    private float time;

    private float maxTimePerIteration;

    private long lastFrameTime;

    /**
   * Construct a new Sequence.
   *
   * @param stages  the list of Stages to execute for each iteration of the Sequence
   */
    public Sequence(Stage stages[]) {
        this.stages = Collections.unmodifiableList(Arrays.asList(stages));
        for (Stage stage : stages) stage.setSequence(this);
        time = 0.0f;
        maxTimePerIteration = 1.0f;
    }

    /**
   * This is called by SequenceView when this Sequence begins running.
   *
   * @param view    the SequenceView in which this Sequence will be displayed
   */
    protected void onStart(SequenceView view) {
        lastFrameTime = 0;
        this.view = view;
        for (Stage stage : stages) stage.onStart();
    }

    /**
   * This is called by SequenceView when this Sequence stops running.
   */
    protected void onStop() {
        view = null;
        for (Stage stage : stages) stage.onStop();
    }

    /**
   * Execute one iteration of the Sequence.  This is called by SequenceView.
   */
    protected void executeIteration(GL11 gl) {
        long t = System.currentTimeMillis();
        float dt = (lastFrameTime == 0 ? 0.0f : 0.001f * (t - lastFrameTime));
        if (dt > maxTimePerIteration) dt = maxTimePerIteration;
        time += dt;
        lastFrameTime = t;
        List<Stage> localStages = stages;
        int numStages = localStages.size();
        for (int i = 0; i < numStages; i++) localStages.get(i).execute(time, dt, gl);
    }

    /**
   * Get the SequenceView in which this Sequence is being displayed.  If this Sequence is not currently running,
   * this will return null.
   */
    public SequenceView getSequenceView() {
        return view;
    }

    /**
   * Get the list of Stages that are executed for each iteration of the Sequence
   */
    public List<Stage> getStages() {
        return stages;
    }

    /**
   * Get the maximum amount of game time that should ever elapse during a single iteration, regardless of the
   * amount of real time that passes (measured in seconds).
   */
    public float getMaxTimePerIteration() {
        return maxTimePerIteration;
    }

    /**
   * Set the maximum amount of game time that should ever elapse during a single iteration, regardless of the
   * amount of real time that passes (measured in seconds).
   */
    public void setMaxTimePerIteration(float maxTimePerIteration) {
        this.maxTimePerIteration = maxTimePerIteration;
    }
}
