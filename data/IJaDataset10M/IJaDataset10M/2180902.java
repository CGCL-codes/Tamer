package org.goodoldai.jeff.report.txt;

import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import java.io.PrintWriter;
import org.goodoldai.jeff.report.ReportChunkBuilder;

/**
 * A concrete builder for transforming text explanation chunks into pieces
 * of textual report
 *
 * @author Nemanja Jovanovic
 */
public class TXTTextChunkBuilder implements ReportChunkBuilder {

    /**
     * Initializes the builder
     */
    public TXTTextChunkBuilder() {
    }

    /**
     * This method transforms a text explanation chunk into a text report piece 
     * and writes this piece into the provided output stream which is, in this 
     * case, an instance of java.io.PrintWriter. The method first collects all 
     * general chunk data (context, rule, group, tags) and inserts them into 
     * the report, and then retrieves the chunk content. Since the content is, 
     * in this case, a string, it also gets inserted into the report.
     *
     * @param echunk text explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be
     * written as output (in this case java.io.PrintWriter)
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null, if the entered chunk is not a TextExplanationChunk instance or if 
     * the entered output stream type is not java.io.PrintWriter
     */
    public void buildReportChunk(ExplanationChunk echunk, Object stream, boolean insertHeaders) {
        if (echunk == null && stream == null) {
            throw new ExplanationException("All of the arguments are mandatory, so they can not be null");
        }
        if (echunk == null) {
            throw new ExplanationException("The argument 'echunk' is mandatory, so it can not be null");
        }
        if (stream == null) {
            throw new ExplanationException("The argument 'stream' is mandatory, so it can not be null");
        }
        if (!(echunk instanceof TextExplanationChunk)) {
            throw new ExplanationException("The ExplanationChunk must be the type of TextExplanationChunk");
        }
        if (!(stream instanceof PrintWriter)) {
            throw new ExplanationException("The argument 'stream' must be the type of java.io.PrintWriter");
        }
        PrintWriter writer = (PrintWriter) stream;
        if (insertHeaders) TXTChunkUtility.insertExplanationInfo(echunk, writer);
        TextExplanationChunk chunk = (TextExplanationChunk) echunk;
        insertContent(chunk, writer);
    }

    /**
     * This is a private method that is used to insert content into the document
     *
     * @param textExplenationChunk text explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an xml document (in this case java.io.PrintWriter)
     */
    private void insertContent(TextExplanationChunk chunk, PrintWriter writer) {
        String content = String.valueOf(chunk.getContent());
        writer.write(content + '\n' + '\n');
    }
}
