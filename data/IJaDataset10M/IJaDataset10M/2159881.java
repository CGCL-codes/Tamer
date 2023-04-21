package org.renjin.cli;

import java.io.PrintStream;
import java.io.Reader;

/**
	The capabilities of a minimal console for R.
	Stream I/O and optimized print for output.

 @author  @author Patrick Niemeyer (pat@pat.net)

*/
public interface Console {

    public Reader getIn();

    public PrintStream getOut();

    public PrintStream getErr();

    public void println(Object o);

    public void print(Object o);

    public void error(Object o);

    public int getCharactersPerLine();
}
