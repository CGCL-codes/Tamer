package org.goodoldai.jeff.report.html;

import org.dom4j.Document;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.TextExplanationChunk;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.dom4j.Element;
import org.goodoldai.jeff.explanation.ExplanationException;

/**
 *
 * @author Ivan Milenkovic
 */
public class HTMLTextChunkBuilder implements ReportChunkBuilder {

    public HTMLTextChunkBuilder() {
    }

    /**
     * This method transforms a text explanation chunk into an HTML report piece
     * and writes this piece into the provided xml document which is, in this
     * case, an instance of org.dom4j.Document. The method first collects all
     * general chunk data (context, rule, group, tags) and inserts them into
     * the report, and then retrieves the chunk content. Since the content is,
     * in this case, a string, it also gets inserted into the report.
     *
     * @param echunk text explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be
     * written in as an xml document (in this case org.dom4j.Document)
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null, if the entered chunk is not a TextExplanationChunk instance or if
     * the entered output stream type is not org.dom4j.Document
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
        if (!(stream instanceof Document)) {
            throw new ExplanationException("The stream must be the type of org.dom4j.Document");
        }
        Document document = (Document) stream;
        Element bodyElement = document.getRootElement().element("body");
        Element textParagraphElement = bodyElement.addElement("p");
        if (insertHeaders) {
            HTMLChunkUtility.insertExplanationInfo(echunk, textParagraphElement);
        }
        TextExplanationChunk textExplenationChunk = (TextExplanationChunk) echunk;
        insertContent(textExplenationChunk, textParagraphElement);
    }

    private void insertContent(TextExplanationChunk textExplanationChunk, Element element) {
        String content = String.valueOf(textExplanationChunk.getContent());
        element.setText(content);
    }
}
