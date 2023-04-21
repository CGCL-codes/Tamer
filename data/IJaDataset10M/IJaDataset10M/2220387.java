package net.sf.f2s.util.transformer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author rogiel
 * 
 */
public class TransformerFactoryTest {

    @Test
    public void testBooleanTransformer() throws TransformationException {
        assertTrue("true", TransformerFactory.getTransformer(Boolean.class).transform("true"));
        assertFalse("false", TransformerFactory.getTransformer(Boolean.class).transform("false"));
        assertTrue("true", TransformerFactory.getTransformer(Boolean.TYPE).transform("true"));
        assertFalse("false", TransformerFactory.getTransformer(Boolean.TYPE).transform("false"));
    }
}
