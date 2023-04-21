package it.newinstance.jrainbow.parser.java;

import it.newinstance.jrainbow.source.TaggedSource;
import it.newinstance.jrainbow.source.TaggedString;
import it.newinstance.jrainbow.source.TaggedStringIterator;
import it.newinstance.util.Configuration;
import java.util.List;

/**
 * @author Luigi R. Viggiano
 * @version $Id: KeywordMarker.java 159 2007-11-15 02:33:39Z luigi.viggiano $
 */
class KeywordMarker {

    private TaggedSource source;

    private static final Configuration CONF = Configuration.getConfiguration(KeywordMarker.class);

    private static final List<String> JAVA_PRIMITIVE_TYPES = CONF.getList("primitive.data.types");

    private static final List<String> JAVA_KEYWORDS = CONF.getList("java.keywords");

    private static final List<String> COMMENT_TASK_TAGS = CONF.getList("comment.task.tags");

    public KeywordMarker(TaggedSource source) {
        this.source = source;
    }

    public void mark() {
        for (TaggedString taggedString : new TaggedStringIterator(source)) {
            if (taggedString.getTag() == JavaTag.CODE) markJavaKeywords(source, taggedString);
            if (JavaTag.isCommentContext(taggedString.getTag())) {
                markTaskTags(source, taggedString);
                if (taggedString.getTag() == JavaTag.JAVADOC) markHtmlTags(source, taggedString);
            }
        }
    }

    private void markHtmlTags(TaggedSource source, TaggedString taggedString) {
        String string = taggedString.getString();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string.length(); ) {
            while (i < string.length() && string.charAt(i) != '<') i++;
            builder.setLength(0);
            char ch;
            int start = i;
            while (i < string.length() && (Character.isLetterOrDigit(ch = string.charAt(i)) || "</>".indexOf(ch) != -1)) {
                builder.append(ch);
                i++;
                if (ch == '>') break;
            }
            String candidateTag = builder.toString();
            boolean isHtml = candidateTag.lastIndexOf('<') == 0 && candidateTag.indexOf('>') == (candidateTag.length() - 1) && (Math.abs(candidateTag.lastIndexOf('/')) == 1);
            if (isHtml) source.mark(taggedString.getStartPosition() + start, candidateTag.length(), JavaTag.JAVADOC_HTML_TAG);
        }
    }

    /**
     * Interface to handle a java identifier.
     * 
     * @author Luigi R. Viggiano
     * @version $Id: KeywordMarker.java 159 2007-11-15 02:33:39Z luigi.viggiano $
     */
    private interface IdentifierHandler {

        void onIdentifier(String identifier, int position);
    }

    private void markJavaKeywords(final TaggedSource source, final TaggedString taggedString) {
        handleIdentifiers(taggedString, new IdentifierHandler() {

            public void onIdentifier(String identifier, int position) {
                if (JAVA_KEYWORDS.contains(identifier)) source.mark(taggedString.getStartPosition() + position, identifier.length(), JavaTag.JAVA_KEYWORD); else if (JAVA_PRIMITIVE_TYPES.contains(identifier)) source.mark(taggedString.getStartPosition() + position, identifier.length(), JavaTag.JAVA_PRIMITIVE_TYPE);
            }
        });
    }

    private void markTaskTags(final TaggedSource source, final TaggedString taggedString) {
        handleIdentifiers(taggedString, new IdentifierHandler() {

            public void onIdentifier(String identifier, int position) {
                if (COMMENT_TASK_TAGS.contains(identifier)) source.mark(taggedString.getStartPosition() + position, identifier.length(), JavaTag.COMMENT_TASK_TAG);
            }
        });
    }

    private void handleIdentifiers(TaggedString taggedString, IdentifierHandler handler) {
        String string = taggedString.getString();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < string.length(); ) {
            while (i < string.length() && !Character.isJavaIdentifierPart(string.charAt(i))) i++;
            builder.setLength(0);
            char ch;
            int start = i;
            while (i < string.length() && Character.isJavaIdentifierPart(ch = string.charAt(i))) {
                builder.append(ch);
                i++;
            }
            String identifier = builder.toString();
            handler.onIdentifier(identifier, start);
        }
    }
}
