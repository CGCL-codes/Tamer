package net.sf.doolin.oxml.action;

import java.io.IOException;
import net.sf.doolin.oxml.OXMLContext;
import net.sf.doolin.util.xml.DOMUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

/**
 * Root OXML action.
 * <p>
 * Parameters for this action are:
 * <ul>
 * <li><code>package</code> - (optional) Defines a common package for all
 * <code>create</code> or <code>createDef</code> <code>class</code> attributes.
 * </ul>
 * 
 * @author Damien Coraboeuf (x74639)
 * 
 */
public class RootOXMLAction extends AbstractSequenceOXMLAction {

    private String packageName;

    @Override
    public void parse(Element e) throws IOException {
        super.parse(e);
        this.packageName = DOMUtils.getAttribute(e, "package", false, null);
    }

    @Override
    public void process(OXMLContext context) {
        if (StringUtils.isNotBlank(this.packageName)) {
            context.setPackageName(this.packageName);
        }
        super.process(context);
    }
}
