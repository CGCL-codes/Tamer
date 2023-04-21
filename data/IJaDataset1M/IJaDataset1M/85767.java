package org.modelversioning.operations.test;

import junit.framework.TestCase;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.modelversioning.core.conditions.FeatureCondition;
import org.modelversioning.core.impl.UUIDResourceFactoryImpl;
import org.modelversioning.operations.OperationSpecification;

public class OperationSpecificationReadTest extends TestCase {

    private String operationSpecificationURIString = "/org.modelversioning.operations.test/models/ecore/extract_super_class/extract_super_class.operation";

    private ResourceSet resourceSet;

    private Resource operationSpecificationResource;

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testReadOperationSpecification() {
        resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new UUIDResourceFactoryImpl());
        URI operationSpecificationUri = URI.createPlatformPluginURI(operationSpecificationURIString, true);
        operationSpecificationResource = resourceSet.getResource(operationSpecificationUri, true);
        OperationSpecification operationSpecification = (OperationSpecification) operationSpecificationResource.getContents().get(0);
        assertEquals("Extract Super Class", operationSpecification.getName());
        assertEquals("OCL", operationSpecification.getPreconditions().getLanguage());
        assertEquals(4, operationSpecification.getPreconditions().getRootTemplate().getSpecifications().size());
        assertEquals(" = null", ((FeatureCondition) operationSpecification.getPreconditions().getRootTemplate().getSpecifications().get(3)).getExpression());
    }
}
