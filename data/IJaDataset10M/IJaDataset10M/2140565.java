package de.bielefeld.uni.cebitec.cav.utils;

/**
 * A simple implementation of an {@link AbstractProgressReporter}.
 * Writes all progress to STDOUT
 *
 * This can be used if progress should be displayed without gui.
 * 
 * @author phuseman
 */
public class SimpleProgressReporter implements AbstractProgressReporter {

    /**
	 * Just print the message (if present) and the percent done (if nonnegative)
	 * 
	 * @param percentDone
	 *            how far is the algorithm: 0.0 is just started, 1.0 is
	 *            finished. any negative value means that only the comment is
	 *            important.
	 * @param comment
	 *            the comment for this status change
	 */
    public void reportProgress(double percentDone, String comment) {
        System.out.println((percentDone > 0 ? ((int) (percentDone * 100) + "% ") : "") + comment);
    }
}
