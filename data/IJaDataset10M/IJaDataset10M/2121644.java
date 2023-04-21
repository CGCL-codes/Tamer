package org.apache.poi.hssf.record.formula.udf;

import org.apache.poi.hssf.record.formula.functions.FreeRefFunction;

/**
 * Collects add-in libraries and VB macro functions together into one UDF finder
 *
 * @author PUdalau
 */
public final class AggregatingUDFFinder implements UDFFinder {

    private final UDFFinder[] _usedToolPacks;

    public AggregatingUDFFinder(UDFFinder... usedToolPacks) {
        _usedToolPacks = usedToolPacks.clone();
    }

    /**
	 * Returns executor by specified name. Returns <code>null</code> if
	 * function isn't contained by any registered tool pack.
	 *
	 * @param name Name of function.
	 * @return Function executor. <code>null</code> if not found
	 */
    public FreeRefFunction findFunction(String name) {
        FreeRefFunction evaluatorForFunction;
        for (UDFFinder pack : _usedToolPacks) {
            evaluatorForFunction = pack.findFunction(name);
            if (evaluatorForFunction != null) {
                return evaluatorForFunction;
            }
        }
        return null;
    }
}
