package gmof;

import java.util.Iterator;

/**
 * Any element that implements that interface can be used as a plot hook.
 * 
 * @author max
 * @version 0.0
 * 
 * TODO Make this work!
 */
public interface PlotHookable {

    /**
     * Adds a PlotHook
     * 
     * @param toAdd
     *            The PlotHook to add.
     */
    public void addPlotHook(PlotHook toAdd);

    /**
     * Removes a PlotHook
     * 
     * @param toRemove
     *            The PlotHook to remove.
     */
    public void removePlotHook(PlotHook toRemove);

    /**
     * @return Returns whether the PlotHookable currently has PlotHooks
     *         attached.
     */
    public boolean hasPlotHooks();

    /**
     * @return Returns an Iterator over all the PlotHookable's PlotHooks.
     */
    public Iterator plotHooks();
}
