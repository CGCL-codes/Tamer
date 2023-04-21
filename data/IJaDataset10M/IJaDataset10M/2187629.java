package org.peaseplate.templateengine;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.peaseplate.templateengine.designator.Block;
import org.peaseplate.templateengine.designator.RenderException;
import org.peaseplate.templateengine.template.RenderDirective;

public class ExpectBlockChunk extends AbstractBlockChunk {

    public ExpectBlockChunk(final int line, final int column) {
        super(line, column);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean isAlternatable() {
        return true;
    }

    /**
	 * {@inheritDoc}
	 */
    public RenderDirective render(final RenderContext context, final Writer writer, final Block block) throws RenderException, IOException {
        final StringWriter backup = new StringWriter();
        try {
            context.pushScope();
            getBlock().render(context, backup, block);
            context.popScope();
        } finally {
            backup.close();
        }
        context.getScope().defineVariable("#expected", backup.toString());
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("${expect}");
        builder.append(super.toString());
        return builder.toString();
    }
}
