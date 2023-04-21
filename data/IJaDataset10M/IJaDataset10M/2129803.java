package nevo.test;

import java.util.*;
import nevo.core.*;
import nevo.pkgs.expr.RealFunction;
import org.junit.*;
import org.lsmp.djep.vectorJep.values.*;

public class RealFunctionTest {

    @Test
    public void test1() throws Exception {
        RealFunction f = new RealFunction();
        f.setExpression("x+1");
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("x", 2.0);
        Object result = f.evaluate(input);
        Assert.assertTrue(result instanceof Double);
        Assert.assertTrue((Double) result == 3.0);
    }

    @Test
    public void test2() throws Exception {
        RealFunction f = new RealFunction();
        f.setInputDimension(3);
        f.setOutputDimension(3);
        f.setExpression("x+[1,1,1]");
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("x", new Double[] { 2.0, 3.0, 4.0 });
        Object result = f.evaluate(input);
        Assert.assertTrue(result instanceof MVector);
        MVector v = (MVector) result;
        Assert.assertTrue((Double) v.getEle(0) == 3.0);
        Assert.assertTrue((Double) v.getEle(1) == 4.0);
        Assert.assertTrue((Double) v.getEle(2) == 5.0);
    }
}
