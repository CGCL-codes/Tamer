package org.nakedobjects.nos.client.xat.html;

import org.nakedobjects.nos.client.xat.TestValueDecorator;
import org.nakedobjects.nos.client.xat.TestValueImpl;
import org.nakedobjects.xat.documentor.html.HtmlDocumentor;

public class HtmlTestValue extends TestValueDecorator {

    public HtmlTestValue(final TestValueImpl wrappedObject, final HtmlDocumentor documentor) {
        super(wrappedObject);
    }
}
