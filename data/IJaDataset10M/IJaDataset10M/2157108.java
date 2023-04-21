package com.jme3.voxel.field.operators;

import com.jme3.voxel.field.Operator;
import com.jme3.voxel.field.ScalarField;

/**
 * TODO doc
 * 
 * @author Marius Dransfeld
 * 
 */
public class Negate extends Operator {

    /**
	 * TODO doc
	 */
    public Negate() {
        super(1);
    }

    /**
	 * TODO doc
	 * 
	 * @param field
	 */
    public Negate(ScalarField field) {
        super(1);
        operands[0] = field;
    }

    @Override
    public float evaluate(float x, float y, float z) {
        return -operands[0].evaluate(x, y, z);
    }
}
