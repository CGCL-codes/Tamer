package org.peaseplate.domain.designator;

import org.peaseplate.Chunk;
import org.peaseplate.Designator;
import org.peaseplate.TemplateEngine;
import org.peaseplate.TemplateException;
import org.peaseplate.domain.chunk.WithBlock;
import org.peaseplate.domain.lang.Parser;
import org.peaseplate.domain.lang.command.ICommand;
import org.peaseplate.domain.model.CompileContext;
import org.peaseplate.domain.parser.Token;

/**
 * The designator for the "with" keyword. Expects the code chunk to contain
 * a expression that returns an object or null.
 * 
 * @author Manfred HANTSCHEL
 */
public class WithDesignator implements Designator {

    public static final String NAME = "with";

    /**
	 * The keyword of this designator is "with".
	 * 
	 * @see org.peaseplate.Designator#getKeyword()
	 */
    public String getKeyword() {
        return NAME;
    }

    /**
	 * The designator is not empty.
	 * 
	 * @see org.peaseplate.Designator#isEmpty()
	 */
    public boolean isEmpty() {
        return false;
    }

    /**
	 * @see org.peaseplate.Designator#isBlockHead()
	 */
    public boolean isBlockHead() {
        return true;
    }

    /**
	 * @see org.peaseplate.Designator#isBlockTail()
	 */
    public boolean isBlockTail() {
        return false;
    }

    /**
	 * @see org.peaseplate.Designator#isExpandableBlock()
	 */
    public boolean isExpandableBlock() {
        return true;
    }

    /**
	 * @see org.peaseplate.Designator#isBlockExpansion()
	 */
    public boolean isBlockExpansion() {
        return false;
    }

    /**
	 * @see org.peaseplate.Designator#isVisible()
	 */
    public boolean isVisible() {
        return false;
    }

    /**
	 * @see org.peaseplate.Designator#compile(org.peaseplate.TemplateEngine, org.peaseplate.domain.model.CompileContext, org.peaseplate.domain.parser.Token)
	 */
    public Chunk compile(TemplateEngine engine, CompileContext context, Token token) throws TemplateException {
        Parser parser = new Parser(engine, context, token.getLine(), token.getColumn(), token.getSource(), token.getOffset(), token.getLength());
        ICommand command = parser.readDefaultReference();
        return new WithBlock(context.getLocator(), token.getLine(), token.getColumn(), command);
    }
}
