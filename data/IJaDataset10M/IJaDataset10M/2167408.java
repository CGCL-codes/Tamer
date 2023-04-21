package uk.ac.lkl.migen.system.expresser.model;

import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.TiedNumberExpression;

/**
 * When walking models and canvases this is called whenever
 * a tiedNumber is encountered
 * 
 * @author Ken Kahn
 *
 */
public abstract class Walker {

    /**
     * @param tiedNumber -- tiedNumber found
     * @param shape -- shape it was found in (or null if found on the canvas)
     * @param handle -- attribute handle where the tied number or 
     * the expression it part of was found (or null if found on the canvas)
     * @param expresserModel -- model containing the shape (or its proxy)
     * 
     * @return true if the walk should continue
     */
    public abstract boolean tiedNumberFound(TiedNumberExpression<?> tiedNumber, BlockShape shape, AttributeHandle<IntegerValue> handle, ExpresserModel expresserModel);
}
