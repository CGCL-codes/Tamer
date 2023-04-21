package org.peaseplate.domain.chunk;

import java.io.IOException;
import java.io.Writer;
import org.peaseplate.BlockExpansion;
import org.peaseplate.BuildContext;
import org.peaseplate.ExpandableBlock;
import org.peaseplate.TemplateException;
import org.peaseplate.TemplateLocator;
import org.peaseplate.TemplateScannerException;
import org.peaseplate.chunk.AbstractBlock;
import org.peaseplate.domain.lang.command.ICommand;

public class WithBlock extends AbstractBlock implements ExpandableBlock {

    private final ICommand command;

    private BlockExpansion elseBlock = null;

    public WithBlock(TemplateLocator locator, int line, int column, ICommand command) {
        super(locator, line, column);
        this.command = command;
    }

    public ICommand getCommand() {
        return command;
    }

    /**
	 * @see org.peaseplate.ExpandableBlock#addExpansion(org.peaseplate.BlockExpansion)
	 */
    public void addExpansion(BlockExpansion chunk) throws TemplateException {
        if (chunk instanceof ElseBlock) elseBlock = chunk; else throw new TemplateScannerException(chunk.getLocator(), chunk.getLine(), chunk.getColumn(), "\"with\" may only be expanded by \"else\"");
    }

    /**
	 * @see org.peaseplate.Chunk#isVisible()
	 */
    public boolean isVisible() {
        return false;
    }

    /**
	 * @see org.peaseplate.Chunk#isEssential()
	 */
    public boolean isEssential() {
        return true;
    }

    /**
	 * @see org.peaseplate.Chunk#render(BuildContext, java.io.Writer)
	 */
    public void render(BuildContext context, Writer writer) throws TemplateException, IOException {
        Object object = getCommand().call(context);
        if (object != null) {
            context.pushWorkingObject(object);
            renderBlock(context, writer);
            context.popWorkingObject();
        } else if (elseBlock != null) {
            elseBlock.render(context, writer);
        }
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("WithBlock(");
        builder.append(getLine()).append(":").append(getColumn());
        builder.append(") [").append(getCommand().toString()).append("] ");
        builder.append(super.toString());
        if (elseBlock != null) builder.append("\n").append(elseBlock);
        return builder.toString();
    }
}
