package org.peaseplate.templateengine.template.designator;

import org.peaseplate.queryengine.QueryParser;
import org.peaseplate.queryengine.internal.ParserException;
import org.peaseplate.queryengine.internal.PPQueryParser;
import org.peaseplate.templateengine.Template;
import org.peaseplate.templateengine.TemplateEngine;
import org.peaseplate.templateengine.TemplateException;
import org.peaseplate.templateengine.designator.Chunk;
import org.peaseplate.templateengine.designator.Designator;
import org.peaseplate.templateengine.template.chunk.internal.ImportChunk;
import org.peaseplate.templateengine.template.parser.TemplateParserException;
import org.peaseplate.templateengine.util.CompileContext;

/**
 * The implementation of the import keyword. The import designator loads all macros of a specified template, but does
 * not include any content like the {@link IncludeDesignator}. Usage: ${import: templateName}
 * 
 * @author Manfred HANTSCHEL
 */
public class ImportDesignator implements Designator {

    /**
	 * The name of the designator
	 */
    public static final String NAME = "import";

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String getKeyword() {
        return NAME;
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean isEmpty() {
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean isBlockHead() {
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean isBlockTail() {
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean isExpandableBlock() {
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    public boolean isBlockExpansion() {
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    public Chunk compile(final TemplateEngine engine, final CompileContext context, final Token token) throws TemplateException {
        final QueryParser parser = new PPQueryParser(token.getLine(), token.getColumn(), token.getSource(), token.getOffset(), token.getLength());
        String name;
        try {
            name = parser.readKey();
        } catch (final ParserException e) {
            throw new TemplateParserException(context.getLocator(), e.getLine(), e.getColumn(), "Could not parse code", e);
        }
        final Template template = context.getLocator().resolve(engine, name, context.getLocator().getKey().getLocale(), context.getLocator().getKey().getEncoding());
        if (template == null) {
            throw new TemplateParserException(context.getLocator(), token.getLine(), token.getColumn(), "Unresolved import \"" + name + "\"");
        }
        context.addImportedResourceKey(template.getLocator().getKey());
        return new ImportChunk(context.getLocator(), token.getLine(), token.getColumn(), template.getLocator().getKey());
    }
}
