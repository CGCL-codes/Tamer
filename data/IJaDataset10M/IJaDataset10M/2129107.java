package info.bliki.htmlcleaner;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Simple XML serializer - creates resulting XML without indenting lines.</p>
 *
 * Created by: Vladimir Nikic<br/>
 * Date: November, 2006.
 */
public class SimpleXmlSerializer extends XmlSerializer {

    protected SimpleXmlSerializer(Writer writer, HtmlCleaner htmlCleaner) {
        super(writer, htmlCleaner);
    }

    private void serialize(List nodes, TagNode tagNode) throws IOException {
        if (nodes != null && !nodes.isEmpty()) {
            Iterator childrenIt = nodes.iterator();
            while (childrenIt.hasNext()) {
                Object item = childrenIt.next();
                if (item != null) {
                    if (item instanceof List) {
                        serialize((List) item, tagNode);
                    } else if (item instanceof ContentToken) {
                        ContentToken contentToken = (ContentToken) item;
                        String content = contentToken.getContent();
                        if (!dontEscape(tagNode)) {
                            content = escapeXml(content);
                        } else {
                            content = content.replaceAll("]]>", "]]&amp;");
                        }
                        writer.write(content);
                    } else {
                        ((BaseToken) item).serialize(this);
                    }
                }
            }
        }
    }

    @Override
    protected void serialize(TagNode tagNode) throws IOException {
        serializeOpenTag(tagNode);
        List tagChildren = tagNode.getChildren();
        if (!tagChildren.isEmpty()) {
            serialize(tagChildren, tagNode);
            serializeEndTag(tagNode);
        }
    }
}
