package unibo.agent.dsl.components.parser;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.openarchitectureware.workflow.issues.Issues;
import org.openarchitectureware.xtext.parser.impl.AbstractParserComponent;
import org.openarchitectureware.xtext.resource.IXtextResource;
import unibo.agent.dsl.components.resource.dslComponentsResourceFactory;

public class ParserComponent extends AbstractParserComponent {

    static {
        dslComponentsResourceFactory.register();
    }

    protected String getFileExtension() {
        return "dsl";
    }
}
