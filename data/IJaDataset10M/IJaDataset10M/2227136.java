package de.jassda.modules.trace.examples.simple;

/**
 * @author eagle
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExceptionTest1 {

    public static void main(String[] args) {
        m1();
    }

    /**
	 * 
	 */
    private static void m1() {
        try {
            ExceptionTest2.n();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
