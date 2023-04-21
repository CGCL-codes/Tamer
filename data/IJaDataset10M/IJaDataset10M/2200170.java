package org.jikesrvm.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jikesrvm.VM;
import org.jikesrvm.scheduler.VM_Processor;
import org.jikesrvm.scheduler.VM_ThreadEventConstants;
import org.jikesrvm.scheduler.VM_ThreadProcessWaitData;
import org.jikesrvm.scheduler.VM_Wait;
import org.vmmagic.pragma.Uninterruptible;

/**
 * Jikes RVM implementation of <code>java.lang.Process</code>.
 */
public class VM_Process extends java.lang.Process {

    static {
        System.loadLibrary("rvmexec");
    }

    VM_Processor creatingProcessor;

    private int pid;

    private int inputDescriptor;

    private int outputDescriptor;

    private int errorDescriptor;

    private OutputStream outputStream;

    private InputStream inputStream;

    private InputStream errorStream;

    private Object exitStatusLock = new Object();

    private boolean waiting = false;

    private boolean hasExited = false;

    private int exitStatus;

    /**
   * Constructor.
   * @param program name of program being executed
   * @param args array of program arguments
   * @param env array of environment variable bindings (optional)
   * @param dirPath name of directory to use as working directory (optional)
   */
    public VM_Process(String program, String[] args, String[] env, String dirPath) {
        pid = exec4(program, args, env, dirPath);
        creatingProcessor = VM_Processor.getCurrentProcessor();
        VM_FileSystem.onCreateFileDescriptor(inputDescriptor, false);
        VM_FileSystem.onCreateFileDescriptor(outputDescriptor, false);
        VM_FileSystem.onCreateFileDescriptor(errorDescriptor, false);
    }

    /**
   * Get the <code>VM_Processor</code> that the child process was
   * created from.
   */
    @Uninterruptible
    public VM_Processor getCreatingProcessor() {
        return creatingProcessor;
    }

    /**
   * Get the pid of this process.
   */
    public int getPid() {
        return pid;
    }

    /**
   * Get the exit value of the process.
   * @throws IllegalThreadStateException if the process hasn't exited yet
   */
    public int exitValue() {
        synchronized (exitStatusLock) {
            if (!hasExited) {
                if (waiting || !isDead()) {
                    throw new IllegalThreadStateException("I'm not dead yet!");
                }
                hasExited = true;
                exitStatusLock.notifyAll();
            }
            return exitStatus;
        }
    }

    /**
   * Wait for the process to exit.
   * @return the process's exit value
   */
    public int waitFor() throws InterruptedException {
        synchronized (exitStatusLock) {
            while (waiting) {
                exitStatusLock.wait();
            }
            if (hasExited) {
                return exitStatus;
            }
            waiting = true;
        }
        int code;
        try {
            code = waitForInternal();
        } catch (InterruptedException e) {
            synchronized (exitStatusLock) {
                waiting = false;
                exitStatusLock.notifyAll();
            }
            throw e;
        }
        synchronized (exitStatusLock) {
            waiting = false;
            hasExited = true;
            exitStatus = code;
            exitStatusLock.notifyAll();
        }
        return code;
    }

    /**
   * Kill the process.
   */
    public void destroy() {
        synchronized (exitStatusLock) {
            if (!hasExited) {
                destroyInternal();
            }
        }
    }

    /**
   * Get an <code>InputStream</code> to read process's stderr.
   */
    public InputStream getErrorStream() {
        if (errorStream == null) errorStream = getInputStream(errorDescriptor);
        return errorStream;
    }

    /**
   * Get an <code>InputStream</code> to read process's stdout.
   */
    public InputStream getInputStream() {
        if (inputStream == null) inputStream = getInputStream(outputDescriptor);
        return inputStream;
    }

    /**
   * Get an <code>OutputStream</code> to write process's stdin.
   */
    public OutputStream getOutputStream() {
        if (outputStream == null) outputStream = getOutputStream(inputDescriptor);
        return outputStream;
    }

    /**
   * Poll to see if process is dead.
   * <code>exitStatusLock</code> must be held.
   */
    private boolean isDead() {
        try {
            VM_ThreadProcessWaitData waitData = VM_Wait.processWait(this, 1.0d);
            boolean finished = waitData.finished;
            if (finished) {
                exitStatus = waitData.exitStatus;
            }
            return finished;
        } catch (InterruptedException e) {
            java.lang.Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
   * Blocking wait to see if process is dead.
   * Returns exit status.  Should be called with
   * <code>exitStatusLock</code> NOT held, but <code>waiting</code>
   * set to true so no other thread tries to wait
   * at the same time.  (Unix semantics require us to
   * do only a single <code>waitpid()</code> for the same process).
   */
    private int waitForInternal() throws InterruptedException {
        VM_ThreadProcessWaitData waitData = VM_Wait.processWait(this, VM_ThreadEventConstants.WAIT_INFINITE);
        if (VM.VerifyAssertions) VM._assert(waitData.finished);
        return waitData.exitStatus;
    }

    /**
   * Call fork() and execvp() to spawn the process.
   * @return the process id
   */
    private native int exec4(String program, String[] args, String[] env, String dirPath);

    /**
   * Send the process a kill signal.
   */
    private native void destroyInternal();

    private InputStream getInputStream(final int fd) {
        return new InputStream() {

            public int available() throws IOException {
                return VM_FileSystem.bytesAvailable(fd);
            }

            public void close() throws IOException {
            }

            public int read() throws IOException {
                return VM_FileSystem.readByte(fd);
            }

            public int read(byte[] buffer) throws IOException {
                return VM_FileSystem.readBytes(fd, buffer, 0, buffer.length);
            }

            public int read(byte[] buf, int off, int len) throws IOException {
                return VM_FileSystem.readBytes(fd, buf, off, len);
            }
        };
    }

    private OutputStream getOutputStream(final int fd) {
        return new OutputStream() {

            public void write(int b) throws IOException {
                VM_FileSystem.writeByte(fd, b);
            }

            public void write(byte[] b) throws IOException {
                VM_FileSystem.writeBytes(fd, b, 0, b.length);
            }

            public void write(byte[] b, int off, int len) throws IOException {
                VM_FileSystem.writeBytes(fd, b, off, len);
            }

            public void flush() throws IOException {
                VM_FileSystem.sync(fd);
            }

            public void close() throws IOException {
            }
        };
    }
}
