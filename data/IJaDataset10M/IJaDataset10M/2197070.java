package de.maramuse.soundcomp.process;

/**
 * Every mixed-language class must fulfill this interface
 */
public interface NativeStub {

    /**
	 * Returns a java representation of the pointer to the C++ object
	 * 
	 * @return a java representation of the pointer to the C++ object
	 */
    public long getNativeSpace();
}
