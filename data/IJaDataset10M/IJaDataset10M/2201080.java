package org.prevayler.implementation;

import java.io.*;
import org.prevayler.*;

/** Provides an easy API for reading commands and snapshots.
*/
class CommandInputStream {

    public CommandInputStream(String directory) throws IOException {
        fileFinder = new NumberFileFinder(directory);
    }

    public PrevalentSystem readLastSnapshot() throws IOException, ClassNotFoundException {
        File snapshotFile = fileFinder.lastSnapshot();
        if (snapshotFile == null) return null;
        out(snapshotFile);
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(snapshotFile));
        try {
            return (PrevalentSystem) ois.readObject();
        } finally {
            ois.close();
        }
    }

    public Command readCommand() throws IOException, ClassNotFoundException {
        if (currentLogStream == null) currentLogStream = newLogStream();
        try {
            return (Command) currentLogStream.readObject();
        } catch (EOFException eof) {
        } catch (ObjectStreamException osx) {
            logStreamExceptionMessage(osx);
        } catch (RuntimeException rx) {
            logStreamExceptionMessage(rx);
        }
        currentLogStream.close();
        currentLogStream = null;
        return readCommand();
    }

    public CommandOutputStream commandOutputStream(boolean shouldReset, boolean shouldClean, int aBufferSize) {
        return new CommandOutputStream(fileFinder.fileCreator(), shouldReset, shouldClean, aBufferSize);
    }

    private ObjectInputStream newLogStream() throws IOException {
        File logFile = fileFinder.nextPendingLog();
        out(logFile);
        return new ObjectInputStream(new BufferedInputStream(new FileInputStream(logFile)));
    }

    private void logStreamExceptionMessage(Exception exception) {
        out("   " + exception);
        out("   Some commands might have been lost. Looking for the next file...");
    }

    private static void out(File file) {
        out("Reading: " + file + "...");
    }

    private static void out(Object obj) {
        System.out.println(obj);
    }

    private NumberFileFinder fileFinder;

    private ObjectInputStream currentLogStream;
}
