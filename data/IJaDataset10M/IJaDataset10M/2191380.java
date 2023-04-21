package bitWave.visualization;

/**
 * Host interface for implementation on UI components that are the output target
 * for a view.
 * @author fw
 */
public interface ViewHost {

    /** 
     * Signals the host that the view has been invalidated and needs to be refreshed.
     */
    void viewInvalidated();

    /**
     * Sets the view to be displayed in the host.
     * @param view The view.
     */
    public void setView(View view);
}
