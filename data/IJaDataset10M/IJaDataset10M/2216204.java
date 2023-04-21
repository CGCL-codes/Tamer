package view;

/**
 * Defines the callback methods for the different states in the lifecycle of 
 * LegendTV screen.  The <code>ScreenManager</code> attempts to invoke these 
 * methods on the <code>JComponent</code> "screens" that are registered with it.
 * 
 * @author Eric Lutley
 */
public interface Screen {

    /**
	 * Indicates that the current screen is being replaced by a new screen, 
	 * but the current screen will continue to exist on the history stack.  
	 * This method is invoked just before the current screen is removed from 
	 * its container so that the screen object can save the UI state 
	 * information (current selections, etc) that will be lost when the 
	 * screen is removed.
	 */
    public void onPause();

    /**
	 * Indicates that this currently-paused screen is being displayed by 
	 * LegendTV.  This method is invoked just after the screen is re-added to 
	 * its container but before the container's <code>repaint</code> is 
	 * called, so that the previous UI state can be assumed before drawing 
	 * the new screen.
	 */
    public void onResume();

    public void onStart();

    public void onStop();
}
