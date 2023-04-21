package org.peaseplate.templateengine.template.chunk;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.peaseplate.templateengine.build.BuildContext;
import org.peaseplate.templateengine.designator.Block;
import org.peaseplate.templateengine.designator.Chunk;
import org.peaseplate.templateengine.designator.RenderException;
import org.peaseplate.templateengine.template.internal.IndentTemplateElement;
import org.peaseplate.templateengine.template.internal.LineSeparatorTemplateElement;

/**
 * A block is a list of chunks, which form the template. A chunk itself may contain a block
 * 
 * @author Manfred HANTSCHEL
 */
public class DefaultBlock implements Block {

    private final List<Chunk> chunks;

    public DefaultBlock() {
        super();
        chunks = new ArrayList<Chunk>();
    }

    /**
	 * Adds a chunk to the block
	 * 
	 * @param chunk the chunk
	 * @throws IllegalArgumentException if the chunk is an alternation but the previous chunk is not conditional
	 * @throws IllegalStateException if the chunk already belongs to another parent block
	 */
    public void add(final Chunk chunk) throws IllegalArgumentException, IllegalStateException {
        if ((chunk.isAlternation()) && (chunks.size() > 0) && (!chunks.get(chunks.size() - 1).isAlternatable())) {
            throw new IllegalArgumentException("A chunk that supports alternation may only be added after a chunk which is conditional");
        }
        chunk.setParent(this);
        chunks.add(chunk);
    }

    /**
	 * Returns the list containing all chunks
	 * 
	 * @return the list containing all chunks
	 */
    public List<Chunk> getChunks() {
        return chunks;
    }

    /**
	 * Renders the block
	 * 
	 * @param context the context
	 * @param writer the writer
	 * @throws RenderException on occasion
	 * @throws IOException on occasion
	 */
    public void render(final BuildContext context, final Writer writer) throws RenderException, IOException {
        final String indent = context.getIndent();
        boolean renderNext = true;
        boolean newLine = false;
        for (final Chunk chunk : chunks) {
            if ((!renderNext) && (!chunk.isAlternation())) {
                renderNext = true;
            }
            if ((newLine) && (indent != null)) {
                writer.write(indent);
            }
            if (renderNext) {
                renderNext = !chunk.render(context, writer, block);
            }
            if ((!renderNext) && (!chunk.isAlternatable())) {
                renderNext = true;
            }
            if (chunk instanceof IndentTemplateElement) {
                context.setIndent((indent != null) ? indent + ((IndentTemplateElement) chunk).getIndent() : ((IndentTemplateElement) chunk).getIndent());
            } else {
                context.setIndent(indent);
            }
            newLine = chunk instanceof LineSeparatorTemplateElement;
        }
        context.setIndent(indent);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chunks.size(); i += 1) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(chunks.get(i));
        }
        return builder.toString();
    }
}
