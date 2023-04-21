package be.lassi.lanbox.commands.layer;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import be.lassi.support.ObjectBuilder;
import be.lassi.support.ObjectTest;

/**
 * Tests class <code>LayerResume</code>.
 */
public class LayerResumeTCL {

    @Test
    public void encodeDecode() {
        LayerResume command1 = new LayerResume(7);
        LayerResume command2 = new LayerResume(command1.getRequest());
        assertEquals(command1, command2);
    }

    @Test
    public void object() {
        ObjectBuilder b = new ObjectBuilder() {

            public Object getObject1() {
                return new LayerResume(1);
            }

            public Object getObject2() {
                return new LayerResume(2);
            }
        };
        ObjectTest.test(b);
    }
}
