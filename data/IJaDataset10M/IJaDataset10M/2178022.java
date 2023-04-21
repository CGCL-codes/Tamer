package com.ibm.wala.classLoader;

/**
 *
 * An interface for something that carries a location in bytecode
 * 
 * @author sfink
 */
public interface IProgramCounter {

    /**
   * A constant indicating no source line number information is available.
   */
    public static final int NO_SOURCE_LINE_NUMBER = -1;

    /**
   * Return the program counter (index into the method's bytecode) 
   * for this call site.
   * @return the program counter (index into the method's bytecode) 
   * for this call site.
   *
   */
    public abstract int getProgramCounter();
}
