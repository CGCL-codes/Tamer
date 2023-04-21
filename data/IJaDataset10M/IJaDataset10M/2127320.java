package com.niyue.sandbox.uclock.clockr;

public interface StatusSubscriber {

    /**
	 * update the photo loading status.
	 * @param photosLoaded the number of photos that have been loaded 
	 */
    public void update(int photosLoaded);
}
