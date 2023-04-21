package com.tomgibara.android.veecheck;

/**
 * Provides settings for a {@link VeecheckReceiver} instance.
 * 
 * @author Tom Gibara
 */
public interface VeecheckSettings {

    /**
	 * Whether checking should be performed.
	 *  
	 * @return true iff checking should be performed
	 */
    boolean isEnabled();

    /**
	 * The time-interval, in milliseconds, at which the {@link VeecheckReceiver}
	 * will consider checking for new application versions.
	 *   
	 * @return a strictly positive number
	 */
    long getPeriod();

    /**
	 * The URI from which the versions document is downloaded. Currently, URIs
	 * are restricted to the http scheme. The URI may be subject to substitution
	 * with tokens.   
	 * 
	 * @return the URI from which the versions document will be downloaded
	 */
    String getCheckUri();

    /**
	 * The length of time, in milliseconds, for which the application version
	 * will not be checked by the {@link VeecheckReceiver}
	 * 
	 * @return a strictly positive number
	 */
    long getCheckInterval();
}
