package org.norecess.citkit.types;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.norecess.citkit.tir.expressions.NilETIR;
import org.norecess.citkit.types.NilType;
import org.norecess.citkit.types.TypeExpression;

public class TypeExpressionTest {

    private TypeExpression myTypeExpression;

    @Before
    public void setUp() {
        myTypeExpression = new TypeExpression(NilType.NIL_TYPE, new NilETIR());
    }

    @Test
    public void testToString() {
        assertEquals("<type:nil, exp:nil>", myTypeExpression.toString());
    }
}
