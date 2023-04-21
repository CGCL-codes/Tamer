package com.luciddreamingapp.beta.util.audio;

import android.os.Bundle;

/**
 * An instrument which measures some quantity, or accesses or produces some
 * data, which can be displayed on one or more {@link Gauge} objects.
 */
public class Instrument {

    /**
     * The application is starting.  Perform any initial set-up prior to
     * starting the application.
     */
    public void appStart() {
    }

    /**
     * We are starting the main run; start measurements.
     */
    public void measureStart() {
    }

    /**
     * We are stopping / pausing the run; stop measurements.
     */
    public void measureStop() {
    }

    /**
     * The application is closing down.  Clean up any resources.
     */
    public void appStop() {
    }

    /**
     * Update the state of the instrument for the current frame.
     * 
     * <p>Instruments may override this, and can use it to read the
     * current input state.  This method is invoked in the main animation
     * loop -- i.e. frequently.
     * 
     * @param   now         Nominal time of the current frame in ms.
     */
    protected void doUpdate(long now) {
    }

    /**
     * Save the state of the game in the provided Bundle.
     * 
     * @param   icicle      The Bundle in which we should save our state.
     */
    protected void saveState(Bundle icicle) {
    }

    /**
     * Restore the game state from the given Bundle.
     * 
     * @param   icicle      The Bundle containing the saved state.
     */
    protected void restoreState(Bundle icicle) {
    }

    @SuppressWarnings("unused")
    private static final String TAG = "instrument";
}
