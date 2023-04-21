package sifter.translation;

import sifter.*;
import java.io.IOException;

/** 
    One condition for checking whether to send a file or not.  This is almost
    an abstract class: it always returns true.

    @author Fred Gylys-Colwell
    @version $Name:  $, $Revision: 1.1 $
*/
public class Condition {

    public boolean check(FilePair file, boolean nameOnly) throws IOException {
        return true;
    }

    public String toString() {
        return "(true)";
    }
}
