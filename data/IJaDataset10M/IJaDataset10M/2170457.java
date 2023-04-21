package de.andreavicentini.magicphoto.batch;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.MessageFormat;

public interface IExecutor {

    class ExecutionException extends Exception {

        private final Exception delegate;

        public ExecutionException(Exception e) {
            this.delegate = e == null ? new NullPointerException() : e;
        }

        public boolean equals(Object obj) {
            return delegate.equals(obj);
        }

        public Throwable fillInStackTrace() {
            return delegate.fillInStackTrace();
        }

        public String getLocalizedMessage() {
            return delegate.getLocalizedMessage();
        }

        public String getMessage() {
            return delegate.getMessage();
        }

        public int hashCode() {
            return delegate.hashCode();
        }

        public void printStackTrace() {
            delegate.printStackTrace();
        }

        public void printStackTrace(PrintStream s) {
            delegate.printStackTrace(s);
        }

        public void printStackTrace(PrintWriter s) {
            delegate.printStackTrace(s);
        }

        public String toString() {
            return delegate.toString();
        }
    }

    class FailureException extends Exception {

        public final String command;

        public final int status;

        public FailureException(String command, int status) {
            this.command = command;
            this.status = status;
        }

        public String getLocalizedMessage() {
            return MessageFormat.format("Failure executing command={0}. Exit status={1}", new Object[] { command, new Integer(status) });
        }
    }

    interface Pipe {

        void addNewLine(String line);

        void stop();
    }

    Pipe NIL = new Pipe() {

        public void addNewLine(String arg0) {
        }

        public void stop() {
        }
    };

    void execute(String command, Pipe output) throws ExecutionException, FailureException;
}
