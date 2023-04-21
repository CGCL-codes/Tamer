package dynamator;

import java.io.File;
import java.io.IOException;

/**
    Input stream for reading an annotations file.
    In addition to processing performed by FakeoutSaxInputStream, this
    class adds location information to each element for better error
    messagess, and wraps the content of program elements in a CDATA
    block so that developers don't have to perform XML translation 
    (e.g. 'x &lt; 5' to 'x &amp;lt; 5').
    <p>
    The content of the following elements are wrapped in CDATA blocks:
    <ul>
      <li>after
      <li>after-content
      <li>after-extracts
      <li>before
      <li>before-content
      <li>before-extracts
      <li>content
      <li>epilog
      <li>extract
      <li>for
      <li>foreach
      <li>if
      <li>prolog
      <li>raw-content
    </ul>
**/
public abstract class AnnotationsFileInputStream extends FakeoutSaxInputStream {

    public static class Default extends AnnotationsFileInputStream {

        public Default(File file) throws IOException {
            super(file);
        }

        public Default(File file, String[] cdataContentTags) throws IOException {
            super(file, cdataContentTags);
        }
    }

    private static final String CDATA_START = "<![CDATA[";

    private static final String CDATA_END = "]]>";

    private String[] cdataContentTags_;

    private static final String[] defaultCdataContentTags_ = new String[] { "after", "after-content", "after-extracts", "before", "before-content", "before-extracts", "content", "epilog", "extract", "for", "foreach", "if", "prolog", "raw-attrs", "raw-content" };

    private static final String[] dynamatorCopyTags_ = new String[] { "after", "after-content", "before", "before-content", "content", "epilog", "prolog", "raw-content" };

    private boolean dynamatorTagEncountered_ = false;

    protected AnnotationsFileInputStream(File file) throws IOException {
        super(file);
        cdataContentTags_ = defaultCdataContentTags_;
    }

    protected AnnotationsFileInputStream(File file, String[] cdataContentTags) throws IOException {
        super(file);
        cdataContentTags_ = Utils.join(defaultCdataContentTags_, cdataContentTags);
        Utils.sort(cdataContentTags_);
    }

    /**
        Wraps the content of dynamator program-containing elements with 
        CDATA, and adds to all elements a location attribute for better
        error messages.
    **/
    protected StringBuffer handleTagStart() throws IOException {
        StringBuffer result = new StringBuffer(64);
        int c;
        while ((c = rawRead()) != -1 && !Character.isWhitespace((char) c) && c != '>' && c != '/') {
            result.append((char) c);
        }
        String tagname = result.toString().substring(1);
        if (tagname.length() == 0 && c == '/') {
            result.append((char) c);
            return result;
        }
        if (!dynamatorTagEncountered_) {
            dynamatorTagEncountered_ = tagname.equals("dynamator");
        }
        result.append(" _dyn_loc_=\"");
        result.append(fileName());
        result.append(':');
        result.append(lineNumber());
        result.append("\"");
        result.append((char) c);
        if (Utils.binarySearch(cdataContentTags_, tagname)) {
            int lineNumber = lineNumber();
            if (c != '>') {
                result.append(String.valueOf(accumulateTag()));
            }
            if (result.charAt(result.length() - 2) != '/') {
                result.append(CDATA_START);
                while ((c = rawRead()) != -1) {
                    if (c == '>') {
                        int p = result.length() - tagname.length();
                        if (result.charAt(p - 2) == '<' && result.charAt(p - 1) == '/' && tagname.equals(Utils.substring(result, p, result.length()))) {
                            result.insert(p - 2, CDATA_END);
                            result.append((char) c);
                            break;
                        } else {
                            result.append((char) c);
                        }
                    } else {
                        result.append((char) c);
                    }
                }
                if (c == -1) {
                    throw new FatalError(fileName() + '(' + lineNumber + "): " + "End tag not found for <" + tagname + ">");
                }
                if (Utils.binarySearch(dynamatorCopyTags_, tagname)) {
                    int offset = 0;
                    while (0 <= (offset = Utils.indexOf(result, "<dynamator:copy", offset))) {
                        result.insert(offset, CDATA_END);
                        offset = Utils.indexOf(result, '>', offset + CDATA_END.length());
                        if (offset < 0) {
                            throw new FatalError(fileName() + '(' + lineNumber + "): " + "End not found for <dynamator:copy");
                        }
                        ++offset;
                        result.insert(offset, CDATA_START);
                    }
                } else if ("foreach".equals(tagname)) {
                    int p = Utils.indexOf(result, "<if");
                    if (p >= 0) {
                        result.insert(p, CDATA_END);
                        p = Utils.indexOf(result, '>', p + CDATA_END.length());
                        if (p < 0) {
                            throw new FatalError(fileName() + '(' + lineNumber + "): " + "End not found for <if");
                        }
                        result.insert(p + 1, CDATA_START);
                        p = Utils.indexOf(result, "</if", p + CDATA_START.length() + 1);
                        if (p < 0) {
                            throw new FatalError(fileName() + '(' + lineNumber + "): " + "</if> not found");
                        }
                        result.insert(p, CDATA_END);
                        p = Utils.indexOf(result, '>', p + CDATA_END.length());
                        if (p < 0) {
                            throw new FatalError(fileName() + '(' + lineNumber + "): " + "End not found for <if");
                        }
                        result.insert(p + 1, CDATA_START);
                    }
                }
            }
        }
        return result;
    }

    protected boolean piShouldBeTreatedAsData(StringBuffer pi) {
        return dynamatorTagEncountered_;
    }

    /**
        Test driver.  Places output on System.out.
        <p>
        Usage: java dynamator.AnnotationsFileInputStream <i>filename</i>
    **/
    public static void main(String[] args) throws Exception {
        AnnotationsFileInputStream s = new AnnotationsFileInputStream.Default(new File(args[0]));
        int c;
        while ((c = s.read()) != -1) {
            System.out.print((char) c);
        }
    }
}
