package rjws.utils.xml;

public class XMLFilter {

    /**
	 * Creates a new XML-Filter
	 * @param document
	 */
    public XMLFilter(String document) {
        this.document = document;
    }

    /**
	 * The Document
	 */
    private String document;

    /**
	 * Start of Tag
	 */
    private static final String TAG_START = "<";

    /**
	 * End of Tag
	 */
    private static final String TAG_END = ">";

    /**
	 * Replaces a part of the Document
	 * @param tag
	 * @param replacer
	 * @param substring
	 */
    private String getReplacementText(String tag, XMLReplacer replacer, String substring) {
        final String STOP_STRING = TAG_START + "/" + tag + TAG_END;
        final String END_OF_ATTRIBUTES = TAG_END;
        int stopTagIndex = substring.indexOf(STOP_STRING);
        int endOfAttributes = substring.indexOf(END_OF_ATTRIBUTES);
        if (stopTagIndex < 0) throw new XMLFilterException("End of Tag (" + STOP_STRING + ") not found: " + substring.substring(128));
        if (endOfAttributes < 0) throw new XMLFilterException("End of attributes (/) not found!");
        String currentContext = substring.substring(0, endOfAttributes);
        XMLReplacementContext context = new XMLReplacementContext();
        context.content = currentContext;
        String replacementText = replacer.replace(tag, context);
        return replacementText;
    }

    public class XMLReplacementContext {

        private String content;

        public String getAttribute(String key) {
            return getAttributeFromContent(content, key);
        }
    }

    /**
	 * Extracts the Attribute from the String
	 * @param content
	 * @param key
	 * @return
	 */
    private String getAttributeFromContent(String content, String key) {
        int index = content.indexOf(key);
        if (index < 0) throw new XMLFilterException("Attribute <" + key + "> not found in: " + content);
        int endIndex = content.indexOf(" ", index);
        int assignmentIndex = content.indexOf("=", index);
        if (endIndex < 0 && content.length() > assignmentIndex) endIndex = content.length();
        String value = content.substring(assignmentIndex + 1, endIndex);
        return strip(value);
    }

    /**
	 * Strips a String
	 * @param str
	 * @return
	 */
    private String strip(String str) {
        str = str.trim();
        if (str.startsWith("'") || str.startsWith("\"")) str = str.substring(1);
        if (str.endsWith("'") || str.endsWith("\"")) str = str.substring(0, str.length() - 1);
        return str.trim();
    }

    /**
	 * Replaces the Tag in the Document
	 * @param tag
	 * @param replacer
	 * @return
	 */
    public int replace(String tag, XMLReplacer replacer) {
        final String STOP_STRING = TAG_START + "/" + tag + TAG_END;
        final String START_STRING = TAG_START + tag;
        int replacedCount = 0;
        String substr = document;
        int startIndex;
        do {
            startIndex = substr.indexOf(START_STRING);
            if (startIndex > 0) {
                String replacementText = getReplacementText(tag, replacer, document.substring(startIndex));
                int stopIndex = substr.indexOf(STOP_STRING) + STOP_STRING.length();
                doReplace(startIndex, stopIndex, replacementText);
                replacedCount++;
                substr = substr.substring(startIndex + START_STRING.length());
            }
        } while (startIndex > 0);
        return replacedCount;
    }

    private void doReplace(int startIndex, int stopIndex, String text) {
        synchronized (this) {
            StringBuilder builder = new StringBuilder(document.length());
            String firstHalf = document.substring(0, startIndex);
            String secondHalf = document.substring(stopIndex, document.length());
            builder.append(firstHalf);
            builder.append(text);
            builder.append(secondHalf);
            document = builder.toString();
        }
    }

    /**
	 * Returns the current Document
	 * @return
	 */
    public String getDocument() {
        return document;
    }
}
