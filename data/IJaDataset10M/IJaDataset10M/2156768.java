package sts.framework;

import kellinwood.framework.plugin.Plugin;
import sts.hibernate.*;
import java.util.*;
import javax.swing.*;
import net.sf.hibernate.*;

/**
 *
 * @author ken
 */
public interface CorrectionFactory extends Plugin {

    /** Returns the correction code coresponding to the correction
     *  created by this factory instance.  Correction code syntax is
     *  DDD-ZZ-ID, where DDD is the three character disposition code
     *  (e.g., RDG, ZFP, SCP).  ZZ is the "zone" in which the
     *  correction is applied -- CP (corrected place), RS (race
     *  scoring), SS (series scoring), or FP (finish place).  ZFP and
     *  SCP are applied in the corrected place zone (affecting the
     *  corrected place of the result).  The RS and SS zones are for
     *  ajusting the score via Redress using average scores from other
     *  races. The CP zone is also used for Redress in which the place
     *  of a boat is adjusted without affecting the place of other
     *  boats.  Finally, to force a boat into a given place, Redress
     *  in the FP zone can be used.  In this case it also changes the
     *  finish place of all boats at/after the assigned place.  The ID
     *  token is a non-zero length string which uniquely identifies
     *  the exact correction algorithm.
     */
    public String getCorrectionCode();

    /** Returns true if an argument is required. */
    public boolean requiresArgument();

    /** Validates the data format of the argument conained in the argument 
     * string. */
    public void validateArgument(String argument) throws Exception;

    /** The argument string usually contains a number, such as penalty
     *  percentage for ZFP and SCP, corrected place for RDG-CP's,
     *  score or scoring adjustment parameter for RDG-SC's, and
     *  absolute place for RDG-FP's.
     */
    public Correction createCorrection(String id, String argument);
}
