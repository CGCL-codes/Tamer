package com.android.term;

import java.io.FileDescriptor;

/**
 * Utility methods for creating and managing a subprocess.
 * <p>
 * Note: The native methods access a package-private
 * java.io.FileDescriptor field to get and set the raw Linux
 * file descriptor. This might break if the implementation of
 * java.io.FileDescriptor is changed.
 */
public class Exec {

    static {
        System.loadLibrary("term");
    }

    /**
     * Create a subprocess. Differs from java.lang.ProcessBuilder in
     * that a pty is used to communicate with the subprocess.
     * <p>
     * Callers are responsible for calling Exec.close() on the returned
     * file descriptor.
     *
     * @param cmd The command to execute
     * @param arg0 The first argument to the command, may be null
     * @param arg1 the second argument to the command, may be null
     * @param processId A one-element array to which the process ID of the
     * started process will be written.
     * @return the file descriptor of the started process.
     *
     */
    public static native FileDescriptor createSubprocess(String cmd, String arg0, String arg1, int[] processId);

    /**
     * Set the widow size for a given pty. Allows programs
     * connected to the pty learn how large their screen is.
     */
    public static native void setPtyWindowSize(FileDescriptor fd, int row, int col, int xpixel, int ypixel);

    /**
     * Causes the calling thread to wait for the process associated with the
     * receiver to finish executing.
     *
     * @return The exit value of the Process being waited on
     *
     */
    public static native int waitFor(int processId);

    /**
     * Close a given file descriptor.
     */
    public static native void close(FileDescriptor fd);
}
