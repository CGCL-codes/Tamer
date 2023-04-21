package de.jardas.ictk.boardgame.ui.cli;

import java.io.PrintWriter;
import java.io.Writer;
import de.jardas.ictk.boardgame.Board;
import de.jardas.ictk.boardgame.ui.BoardDisplay;

/** This is a command line visual representation of a board.
 */
public interface CLIBoardDisplay extends BoardDisplay {

    /** sets the display to assume the foreground is darker than the background.
    *  This might be beneficial when using the display on a Black on White
    *  screen.  By default all CLI Displays assume White on Black.
    */
    public void setInverse(boolean t);

    /** checks to see if the display is assuming the foreground is darker than
    *  the background.  For example, on a Black on White display.
    *
    *  @return true if the display assumes the foreground is darker.
    */
    public boolean isInverse();

    /** sets the stream where the display will be sent.  Currently a
    *  PrintWriter must be specified.
    *  <br>
    *  Example:
    *  <code>
    *     display.setWriter(new PrintWriter(System.out, true));
    *  </code>
    *  <br>
    *  Note: if you're expecting output and not seeing it, make sure you
    *  set auto-flush on for the PrintWriter.
    */
    public void setWriter(PrintWriter out);

    /** returns the Writer currently being used.  This is always a PrintWriter
    *  with other Streams or Writers nestled inside.
    */
    public Writer getWriter();

    /** prints the current board onto the Writer.
    */
    public void print();

    /** prints the current specified board onto the Writer.
    */
    public void print(Board board);
}
