package net.teqlo.components.standard.xsltV0_01;

import javax.xml.transform.URIResolver;
import net.teqlo.db.ExecutorLookup;
import net.teqlo.xml.Generator;

public class XsltExecutorNoCache extends AbstractXsltExecutor {

    public XsltExecutorNoCache(XsltV0_01 component, ExecutorLookup el, URIResolver resolver) {
        super(component, el, resolver);
    }

    @Override
    protected Generator getGenerator() {
        return new Generator();
    }
}
