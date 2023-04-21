package org.zmpp.vm;

/**
 * The Output interface.
 * @author Wei-ju Wu
 * @version 1.5
 */
public interface Output {

    /** The output stream number for the screen. */
    static final int OUTPUTSTREAM_SCREEN = 1;

    /** The output stream number for the transcript. */
    static final int OUTPUTSTREAM_TRANSCRIPT = 2;

    /** The output stream number for the memory stream. */
    static final int OUTPUTSTREAM_MEMORY = 3;

    /**
   * Selects/unselects the specified output stream. If the streamnumber
   * is negative, |streamnumber| is deselected, if positive, it is selected.
   * Stream 3 (the memory stream) can not be selected by this function,
   * but can be deselected here.
   * @param streamnumber the output stream number
   * @param flag true to enable, false to disable
   */
    void selectOutputStream(int streamnumber, boolean flag);

    /**
   * Selects the output stream 3 which writes to memory.
   * @param tableAddress the table address to write to
   * @param tableWidth the table width
   */
    void selectOutputStream3(int tableAddress, int tableWidth);

    /**
   * Prints the ZSCII string at the specified address to the active
   * output streams.
   * @param stringAddress the address of an ZSCII string
   */
    void printZString(int stringAddress);

    /**
   * Prints the specified string to the active output streams.
   * @param str the string to print, encoding is ZSCII
   */
    void print(String str);

    /**
   * Prints a newline to the active output streams.
   */
    void newline();

    /**
   * Prints the specified ZSCII character.
   * @param zchar the ZSCII character to print
   */
    void printZsciiChar(char zchar);

    /**
   * Prints the specified signed number.
   * @param num the number to print?
   */
    void printNumber(short num);

    /**
   * Flushes the active output streams.
   */
    void flushOutput();

    /**
   * Resets the output streams.
   */
    void reset();
}
