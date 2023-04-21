package net.sourceforge.nrl.parser.ast.action.impl;

import net.sourceforge.nrl.parser.NRLParserTestCase;
import net.sourceforge.nrl.parser.ast.IModelReference;
import net.sourceforge.nrl.parser.ast.constraints.IArithmeticExpression;
import net.sourceforge.nrl.parser.ast.constraints.ILiteralString;
import net.sourceforge.nrl.parser.ast.impl.NRLActionParser;

/**
 * Test action macro application parsing.
 * 
 * @author Christian Nentwich
 */
public class ActionFragmentApplicationActionImplTest extends NRLParserTestCase {

    /**
	 * Test parsing.
	 * 
	 * @throws Exception
	 */
    public void testParse() throws Exception {
        NRLActionParser parser = getParserFor("{foo} 'x'");
        ActionFragmentApplicationActionImpl action = (ActionFragmentApplicationActionImpl) parser.simpleAction().getTree();
        assertEquals("foo", action.getActionFragmentId());
        assertNull(action.getFragment());
        assertEquals(1, action.getParameters().size());
        assertTrue(action.getParameters().get(0) instanceof ILiteralString);
        parser = getParserFor("{foo} target and 2+3");
        action = (ActionFragmentApplicationActionImpl) parser.simpleAction().getTree();
        assertEquals("foo", action.getActionFragmentId());
        assertNull(action.getFragment());
        assertTrue(action.getParameters().get(0) instanceof IModelReference);
        assertTrue(action.getParameters().get(1) instanceof IArithmeticExpression);
    }
}
