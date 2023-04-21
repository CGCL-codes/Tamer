package uk.ac.lkl.migen.system.expresser.model.shape.block.test;

import org.junit.Test;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModelImpl;
import uk.ac.lkl.migen.system.expresser.model.ModelColor;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BasicShape;
import uk.ac.lkl.migen.system.expresser.model.shape.block.PatternShape;
import uk.ac.lkl.migen.system.expresser.test.StandaloneExpresserTest;

public class TestBasic extends StandaloneExpresserTest {

    /**
     * This (in combination with code in finalize() method is used to test
     * whether there is a memory leak for basic shapes.
     * 
     */
    @Test
    public void testMemoryLeak() {
        ExpresserModel model = new ExpresserModelImpl();
        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
            BasicShape block = new BasicShape(ModelColor.BLUE);
            PatternShape pattern = new PatternShape();
            pattern.setShape(block);
            pattern.setXIncrement(new IntegerValue(1));
            pattern.setIterations(10);
            model.addObject(pattern);
            model.removeObject(pattern);
        }
    }
}
