package joeq.Compiler.Analysis.IPSSA.Utils;

import java.util.HashSet;
import joeq.Compiler.Analysis.IPSSA.SSAIterator;

/**
 * @author V.Benjamin Livshits
 * @version $Id: DefinitionSet.java 2250 2005-04-29 07:41:11Z joewhaley $
 * 
 * Strongly typed definition set.
 */
public class DefinitionSet extends HashSet {

    /**
     * Version ID for serialization.
     */
    private static final long serialVersionUID = 3258130267196831288L;

    public DefinitionSet() {
        super();
    }

    public SSAIterator.DefinitionIterator getDefinitionIterator() {
        return new SSAIterator.DefinitionIterator(iterator());
    }
}
