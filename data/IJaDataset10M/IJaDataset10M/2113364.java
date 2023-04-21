package org.omegahat.Simulation.MCMC.Listeners;

import org.omegahat.Simulation.MCMC.*;
import java.util.Vector;
import org.omegahat.Graphics.Plots.*;
import org.omegahat.Graphics.Devices.*;

/**
 * Displays contents and counter of <code>RecordingStepEvent<code>s when one is recieved.
 */
public class ExpandingPlottingListener extends PlottingListener {

    int maxsize;

    int maxsize() {
        return maxsize;
    }

    int maxsize(int newval) {
        return maxsize = newval;
    }

    public ExpandingPlottingListener(String[] varNames) {
        this(varNames, 1000, 100000);
    }

    public ExpandingPlottingListener(String[] varNames, int minsize, int maxsize) {
        super(varNames, minsize);
        this.maxsize = maxsize;
    }

    public void notify(MCMCEvent e) {
        if (index >= (int) (0.95 * (double) time.length) && time.length < maxsize) {
            int oldsize = time.length;
            int size = Math.min(oldsize * 2, maxsize);
            this.time = new double[size];
            double[][] data = new double[varNames.length][size];
            for (int j = 0; j < varNames.length; j++) for (int i = 0; i < size; i++) {
                if (j == 0) this.time[i] = (double) i;
                if (i < oldsize) {
                    data[j][i] = this.data[j][i];
                } else {
                    data[j][i] = Double.NaN;
                }
            }
            this.data = data;
            for (int j = 0; j < varNames.length; j++) {
                plots[j] = new Scatterplot(this.time, this.data[j], Scatterplot.LINES);
                plotWindows[j].plot(plots[j]);
            }
        }
        super.notify(e);
    }
}
