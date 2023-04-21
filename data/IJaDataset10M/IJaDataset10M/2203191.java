package nevo.test;

import java.util.*;
import nevo.core.*;
import nevo.pkgs.expr.RealFunctionModel;
import org.junit.*;
import org.lsmp.djep.vectorJep.values.*;

public class RealFunctionModelTest {

    @Test
    public void test1() throws Exception {
        RealFunctionModel m = new RealFunctionModel();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("x", 1);
        m.setExpression("x+1");
        Map<String, Object> output = m.run(params);
        Assert.assertTrue(output.containsKey("output"));
        Object out = output.get("output");
        Assert.assertTrue(out instanceof Double);
        double val = (Double) out;
        Assert.assertTrue(val == 2.0);
    }

    @Test
    public void test2() throws Exception {
        RealFunctionModel m = new RealFunctionModel();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("x", new Integer[] { 1, 2, 3 });
        m.setInputDimension(3);
        m.setOutputDimension(3);
        m.setOutputName("y");
        m.setExpression("x+[1,1,1]");
        Map<String, Object> output = m.run(params);
        Assert.assertTrue(output.containsKey("y"));
        Object out = output.get("y");
        Assert.assertTrue(out instanceof MVector);
        MVector v = (MVector) out;
        Assert.assertTrue((Double) v.getEle(0) == 2.0);
        Assert.assertTrue((Double) v.getEle(1) == 3.0);
        Assert.assertTrue((Double) v.getEle(2) == 4.0);
    }
}
