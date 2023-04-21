package org.monet.modelling.ide.builders.stages.semantic;

import java.util.Collection;
import javax.xml.xpath.XPathExpressionException;
import org.eclipse.core.resources.IResource;
import org.monet.modelling.ide.builders.Module;
import org.monet.modelling.ide.problems.Problem;
import org.w3c.dom.NodeList;

public abstract class SemanticRule {

    private IResource resource;

    private Module module;

    public void setResource(IResource resource) {
        this.resource = resource;
    }

    public IResource getResource() {
        return resource;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return this.module;
    }

    public abstract String getXPathRule();

    public abstract Collection<Problem> checkNodes(NodeList nodes) throws XPathExpressionException;
}
