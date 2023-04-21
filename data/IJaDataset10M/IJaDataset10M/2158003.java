package org.peaseplate.domain.parser;

import java.util.ArrayList;
import java.util.List;
import org.peaseplate.BlockExpansion;
import org.peaseplate.BlockHead;
import org.peaseplate.Chunk;
import org.peaseplate.ExpandableBlock;
import org.peaseplate.Template;
import org.peaseplate.TemplateDefinitionException;
import org.peaseplate.TemplateEngine;
import org.peaseplate.TemplateException;
import org.peaseplate.TemplateLocator;
import org.peaseplate.TemplateParserException;
import org.peaseplate.domain.model.CompileContext;
import org.peaseplate.domain.template.ChunkBasedTemplate;

/**
 * code = {codeOfChunk [begin code (end | expansionBlock)]}
 * expansionBlock = codeOfExpansionChunkHead begin code (end | expansionBlock)  
 * 
 * @author Manfred HANTSCHEL
 */
public class TemplateParser {

    private final TemplateEngine engine;

    private final TemplateScanner scanner;

    public TemplateParser(TemplateEngine engine, TemplateLocator locator) throws TemplateException {
        super();
        this.engine = engine;
        scanner = new TemplateScanner(engine, locator);
    }

    public TemplateEngine getEngine() {
        return engine;
    }

    public TemplateScanner getScanner() {
        return scanner;
    }

    /**
	 * code = {codeOfChunk [block]}
	 * block = begin code (end | expansionBlock)
	 * expansionBlock = codeOfExpansionChunkHead block
	 *
  	 * code = {codeOfChunk [block]
  	 * block = chunk.isBlockHead code (chunk.isBlockTail | chunk.isExpansionBlock)]}
	 * expansionBlock = codeOfExpansionChunkHead block
	 */
    public Template parse() throws TemplateException {
        getScanner().next(0);
        CompileContext context = new CompileContext(scanner.getLocator());
        Chunk[] chunks = parse(context, 0);
        return new ChunkBasedTemplate(scanner.getLocator(), chunks, context.getImportedResourceKeys(), context.getMacroBlocks());
    }

    /**
	 * code = {codeOfChunk [block]}
	 * block = begin code (end | expansionBlock)
	 * expansionBlock = codeOfExpansionChunkHead block
	 *
  	 * code = {codeOfChunk [block]
  	 * block = chunk.isBlockHead code (chunk.isBlockTail | chunk.isExpansionBlock)]}
	 * expansionBlock = codeOfExpansionChunkHead block
	 */
    protected Chunk[] parse(CompileContext context, int level) throws TemplateException {
        List<Chunk> result = new ArrayList<Chunk>();
        Token token = getScanner().current();
        while ((!token.isBlockTail()) && (token.getType() != TokenType.END_OF_SOURCE)) {
            Chunk chunk = token.compile(engine, context);
            Chunk[] block = null;
            Chunk expansion = null;
            if (token.isBlockHead()) {
                Token content = getScanner().next(level + 1);
                if ((content == null) || (content.getType() == TokenType.END_OF_SOURCE)) throw new TemplateParserException(context.getLocator(), content.getLine(), content.getColumn(), "Unexpected end of source");
                block = parse(context, level + 1);
                Token tail = getScanner().current();
                if ((tail == null) || (tail.getType() == TokenType.END_OF_SOURCE)) throw new TemplateParserException(context.getLocator(), tail.getLine(), tail.getColumn(), "Unexpected end of source");
                if (tail.isBlockExpansion()) expansion = parseExpansion(context, level); else if (!tail.isBlockTail()) throw new TemplateParserException(context.getLocator(), tail.getLine(), tail.getColumn(), "Expected block expansion or end but found " + tail);
            }
            if (block != null) {
                if (!(chunk instanceof BlockHead)) throw new TemplateDefinitionException("Chunk " + chunk.getClass() + " should be a BlockHead as specified in it's designator");
                ((BlockHead) chunk).setBlock(block);
            }
            if (expansion != null) {
                if (!(chunk instanceof ExpandableBlock)) throw new TemplateDefinitionException("Chunk " + chunk.getClass() + " does not allow expansion");
                ((ExpandableBlock) chunk).addExpansion((BlockExpansion) expansion);
            }
            if (chunk != null) result.add(chunk);
            token = getScanner().next(level);
        }
        return result.toArray(new Chunk[result.size()]);
    }

    protected Chunk parseExpansion(CompileContext context, int level) throws TemplateException {
        Token token = getScanner().current();
        if (!token.isBlockHead()) throw new TemplateParserException(context.getLocator(), token.getLine(), token.getColumn(), "Expected block head, but found " + token);
        Chunk chunk = token.compile(engine, context);
        Token content = getScanner().next(level + 1);
        if ((content == null) || (content.getType() == TokenType.END_OF_SOURCE)) throw new TemplateParserException(context.getLocator(), content.getLine(), content.getColumn(), "Unexpected end of source");
        Chunk[] block = parse(context, level + 1);
        if (block != null) {
            if (!(chunk instanceof BlockHead)) throw new TemplateDefinitionException("Chunk " + chunk.getClass() + " should be a BlockHead as specified in it's designator");
            ((BlockHead) chunk).setBlock(block);
        }
        Chunk expansion = null;
        Token tail = getScanner().current();
        if ((tail == null) || (tail.getType() == TokenType.END_OF_SOURCE)) throw new TemplateParserException(context.getLocator(), tail.getLine(), tail.getColumn(), "Unexpected end of source");
        if (tail.isBlockExpansion()) expansion = parseExpansion(context, level); else if (!tail.isBlockTail()) throw new TemplateParserException(context.getLocator(), tail.getLine(), tail.getColumn(), "Expected block expansion or end but found " + tail);
        if (expansion != null) {
            if (!(chunk instanceof ExpandableBlock)) throw new TemplateDefinitionException("Chunk " + chunk.getClass() + " does not allow expansion");
            ((ExpandableBlock) chunk).addExpansion((BlockExpansion) expansion);
        }
        return chunk;
    }
}
