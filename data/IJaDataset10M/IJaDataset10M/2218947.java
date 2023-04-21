package org.gzigzag.transform;

import java.util.*;
import org.gzigzag.*;

/** ZZspace version changer - from version 1 to version 2.
 */
public class Version1 implements VersionChanger {

    String rcsid = "$Id: Version1.java,v 1.3 2000/09/19 10:32:01 ajk Exp $";

    public static final void pa(String s) {
        System.out.println(s);
    }

    public static final void pan(String s) {
        System.out.print(s);
    }

    /** Change the space from version 1 to version 2 to make it compatible.
 *  Change to 3-dimension cursing system, allowing every cell to be accursed:
 *  <ol>
 *  <li> fold d.cursor's headcells onto TMPDIM
 *  <li> change d.cursor to d.cursor-list
 *  <li> change TMPDIM to d.cursor
 *  </ol>
 *  @param s	The space to convert
 */
    public int changeVersion(ZZSpace s) {
        pa("Converting from version 1 to version 2:");
        pa("Change to 3-dimension cursing system.");
        pan("Folding d.cursor's headcells to TMPDIM... ");
        (new FoldDim("d.cursor", "TMPDIM", s)).transform();
        pa("done.");
        pan("Renaming to d.cursor and d.cursor-list... ");
        (new ChangeDim("d.cursor", "d.cursor-list", s)).transform();
        pan("d.cursor-list done... ");
        (new ChangeDim("TMPDIM", "d.cursor", s)).transform();
        pa(" d.cursor done.");
        pa("Converted to version 2.");
        return 2;
    }
}
