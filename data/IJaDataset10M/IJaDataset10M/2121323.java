package edu.washington.assist.publish;

import java.io.IOException;

public interface TimeLineSynchronizer {

    /**
	 * Return the time that the given MSB started.  The time is in real/GPS time.
	 * 
	 * @param reportID
	 * @return
	 * @throws IOException
	 */
    public abstract long getMsbStartTime(long reportID) throws IOException;

    /**
	 * Return the time the whistle blew in real/GPS time.  Assumed to be 
	 * the same for all reports.
	 * 
	 * @return
	 * @throws IOException
	 */
    public abstract long getWhistleTime() throws IOException;

    public abstract long getAudioRecorderStartTime(long reportID);
}
