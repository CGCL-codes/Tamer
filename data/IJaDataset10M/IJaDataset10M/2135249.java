package net.sourceforge.insim4j.insim.interfaces;

import net.sourceforge.insim4j.insim.flags.FlagSet;

/**
 * Flag interface for all types of InSim flags.<br />
 * Flag is represented as non-negative power of 2.
 * Valid flag values must be 0, 1, 2, 4, 8, 16 etc. <br />
 * Valid flags can be combined into {@link FlagSet}s
 * 
 * @author Jiří Sotona
 * @version 1.0
 * @created 20-IX-2007 21:53:21
 */
public interface ILargeFlag extends IFlag {

    /**
   * Get value represented by this flag; see InSim.txt.
   * 
   * @return id of flag
   */
    public long getValue();
}
