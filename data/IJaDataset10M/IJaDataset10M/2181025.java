package org.exist.xslt.constructors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.exist.xquery.AnalyzeContextInfo;
import org.exist.xquery.PathExpr;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xslt.XSLContext;
import org.exist.xslt.expression.XSLPathExpr;
import org.exist.xslt.pattern.Pattern;

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
public class AttributeConstructor extends org.exist.xquery.AttributeConstructor {

    public AttributeConstructor(XQueryContext context, String name) {
        super(context, name);
    }

    public void analyze(AnalyzeContextInfo contextInfo) throws XPathException {
        newDocumentContext = (contextInfo.getFlags() & IN_NODE_CONSTRUCTOR) == 0;
        List<Object> newContents = new ArrayList<Object>(5);
        for (Iterator<Object> i = contents.iterator(); i.hasNext(); ) {
            Object obj = i.next();
            if (obj instanceof String) {
                String value = (String) obj;
                if (value.startsWith("{") && value.startsWith("}")) {
                    XSLPathExpr expr = new XSLPathExpr((XSLContext) getContext());
                    Pattern.parse(contextInfo.getContext(), value.substring(1, value.length() - 1), expr);
                    newContents.add(expr);
                    continue;
                }
            }
            newContents.add(obj);
        }
        contents.clear();
        contents.add(newContents);
    }
}
