package com.googlecode.legendtv.test.drivers.faults;

/**
 * Native class that provides methods to simulate crashes in native code.
 *
 * @author Guy Paddock
 */
public class LibCrash implements LC {

    static {
        System.load("/home/pvr/LTV/LegendTV-JNI/test/drivers/faults/libCrash.so");
    }

    /**
	 * Method that simulates a normal JNI method that is encapsulated by fault
	 * tolerance.
	 * 
	 * This method is nothing special -- it simply prints "No crashies!!!" to
	 * console, to demonstrate that JNI fault tolerance has no effect on
	 * the normal execution of a method.
	 */
    public native void noCrash();

    /**
	 * Method that simulates a crash that occurs in a thread outside of a
	 * fault scope.
	 */
    public native void asyncCrash();

    /**
	 * Method that simulates one of three different types of crashes:
	 * <ul>
	 * 	<li>Out-of-bounds memory access.</li>
	 * 	<li>Illegal instruction.</li>
	 * 	<li>Floating-point exception.</li>
	 * </ul>
	 * 
	 * If JNI fault tolerance is functioning properly, the crash should be
	 * intercepted and thrown as the Java exception FatalSignalError.
	 * 
	 * This method never returns normally.
	 * 
	 * @throws FatalSignalError	As a result of the simulated crash.
	 */
    public native int crash();
}
