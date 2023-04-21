package jcox.jplc.thread;

public interface Cancellable {

    /**
	 * Cancel a thread by interruption.  
	 * Allow the thread to clean up before interrupting itself.
	 * 
	 */
    public void cancel();
}
