package org.illico.common.html;

public class SimpleAttributeAppender extends AbstractAttributeAppender<SimpleAttribute> {

    @Override
    protected void appendValue(Appendable buffer, SimpleAttribute attribute) throws Exception {
        buffer.append(attribute.getValue());
    }
}
