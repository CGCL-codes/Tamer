package com.google.testing.threadtester;

/**
 * A variant of Runnable whose main execution method is declared to throw an
 * Exception. Allows a test to throw an arbitrary Exception without having to
 * catch it and wrap it in a runtime exception.
 *
 * @see TestThread
 * @author alasdair.mackintosh@gmail.com (Alasdair Mackintosh)
 */
public interface ThrowingRunnable {

    /**
   * Executes the main task of this ThrowingRunnable. Unlike {@link Runnable#run},
   * this method can throw an Exception. Any thrown exception will indicate a
   * test failure.
   *
   * @throws Exception
   */
    void run() throws Exception;
}
