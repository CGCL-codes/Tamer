package org.parosproxy.paros.core.spider;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Meta extends org.parosproxy.paros.core.spider.Tag {

    private static final ParserTag parser = new ParserTag("META", ParserTag.CLOSING_TAG_OPTIONAL);

    private static final ParserAttr parserAttrContent = new ParserAttr(Attr.CONTENT);

    private static final Pattern urlPattern = Pattern.compile("url\\s*=\\s*([^;]+)", Pattern.CASE_INSENSITIVE);

    private String url = "";

    /**
	 * Get an array of "A" tags.
	 * 
	 * @param doc
	 *            The html document to be parsed.
	 * @return array of "A"
	 */
    public static Meta[] getMetas(String doc) {
        Vector<Meta> metas = new Vector<Meta>();
        parser.parse(doc);
        while (parser.nextTag()) {
            String content = parser.getContent();
            String attrs = parser.getAttrs();
            Meta m = new Meta();
            m.buildAttrs(attrs);
            m.build(content);
            metas.addElement(m);
        }
        Meta[] result = new Meta[metas.size()];
        result = (Meta[]) metas.toArray(result);
        return result;
    }

    protected void buildAttrs(String attrs) {
        super.buildAttrs(attrs);
        try {
            String s = parserAttrContent.getValue(attrs);
            Matcher matcher = urlPattern.matcher(s);
            if (matcher.find()) {
                url = matcher.group(1);
            }
        } catch (Exception e) {
        }
    }

    /**
	 * @return Returns the href.
	 */
    public String getURL() {
        return url;
    }
}
