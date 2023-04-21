package org.scribble.monitor.expression.jaxen;

import org.scribble.monitor.*;
import org.scribble.monitor.expression.jaxen.JaxenDOMExpression;
import org.scribble.monitor.impl.Scope;
import org.scribble.monitor.model.ConversationContext;
import junit.framework.TestCase;

public class JaxenDOMExpressionTest extends TestCase {

    public void testEvaluateMessage() {
        try {
            JaxenDOMExpression expr = new JaxenDOMExpression("$doc/sub/value/text() = 'hello'");
            org.w3c.dom.Document doc = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            org.w3c.dom.Element sub = doc.createElement("sub");
            doc.appendChild(sub);
            org.w3c.dom.Element value = doc.createElement("value");
            sub.appendChild(value);
            org.w3c.dom.Text text = doc.createTextNode("hello");
            value.appendChild(text);
            Message mesg = new Message();
            mesg.setContent(doc);
            ConversationContext context = new Scope() {
            };
            context.getVariables().put("doc", doc);
            boolean result = expr.evaluate(context);
            if (result == false) {
                fail("Expecting 'true' result");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to evaluate: " + e);
        }
    }
}
