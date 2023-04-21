package org.ashkelon;

import org.ashkelon.util.StringUtils;
import org.ashkelon.util.JDocUtil;
import org.ashkelon.util.HtmlUtils;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.Tag;

/**
 * @author Eitan Suez
 */
public class InlineTagResolver {

    public InlineTagResolver() {
    }

    /**
    * resolves all inline tags in a description into html links
    * @param tags text represented as an array of tags, as returned by doc.inlineTags()
    * @return resolved text
    */
    public String resolveDescription(DocInfo sourcedoc, Tag[] tags) {
        StringBuffer text = new StringBuffer("");
        for (int i = 0; i < tags.length; i++) {
            if ("@return".equals(tags[i].kind()) && (tags[i].inlineTags() != null && tags[i].inlineTags().length > 0)) {
                return resolveDescription(sourcedoc, tags[i].inlineTags());
            }
            if ("@see".equals(tags[i].kind())) {
                Reference ref = new Reference(sourcedoc, (SeeTag) tags[i]);
                String cmd = ref.getRefDocTypePrefix();
                if (ref.getRefDocType() == DocInfo.MEMBER_TYPE || ref.getRefDocType() == DocInfo.EXECMEMBER_TYPE) {
                    String name = JDocUtil.stripParensFromMethodName(ref.getRefDocName());
                    ref.setRefDocName(name);
                }
                String name = ref.getRefDocName();
                String caption = (StringUtils.isBlank(ref.getLabel())) ? name : ref.getLabel();
                text.append(HtmlUtils.link(href(cmd, name), caption));
            } else {
                text.append(tags[i].text());
            }
            text.append(" ");
        }
        return text.toString().trim();
    }

    private String href(String cmd, String name) {
        return cmd + ".main.do?name=" + name;
    }
}
