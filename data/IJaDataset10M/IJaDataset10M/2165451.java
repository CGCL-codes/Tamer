package net.sf.profiler4j.test;

import net.sf.profiler4j.agent.ThreadProfiler;

/**
 * @author Antonio S. R. Gomes
 */
public class TestSessionChange {

    public static void main(String[] args) {
        while (true) {
            foo();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }
    }

    private static int n = 0;

    public static void foo() {
        System.err.println("inside TestSessionChange.foo()  count=" + n + "  session=" + ThreadProfiler.getSessionId());
        n++;
    }
}
