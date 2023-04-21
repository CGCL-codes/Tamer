package org.deltaj.constraints.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.deltaj.DeltaJStandaloneSetup;
import org.deltaj.constraints.DeltaJConstraintsStandaloneSetup;
import org.deltaj.constraints.constraints.DeltaJConstraints;
import org.deltaj.constraints.tests.input.TestInputs;
import org.deltaj.deltaj.Program;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.junit.AbstractXtextTests;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import com.google.inject.Injector;

/**
 * @author bettini
 * 
 */
public class DeltaJConstraintsAbstractTests extends AbstractXtextTests {

    protected Injector deltaJInjector = new DeltaJStandaloneSetup().createInjectorAndDoEMFRegistration();

    protected TestInputs testInputs = new TestInputs();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        with(DeltaJConstraintsStandaloneSetup.class);
    }

    protected DeltaJConstraints getConstraints(String program) throws Exception {
        return (DeltaJConstraints) getModel(program);
    }

    protected XtextResourceSet createResourseSetForDeltaJ() {
        XtextResourceSet resourceSet = deltaJInjector.getInstance(XtextResourceSet.class);
        resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
        return resourceSet;
    }

    protected Program loadDeltaJProgram(CharSequence deltaJProgram) throws Exception {
        return loadDeltaJProgram(deltaJProgram.toString());
    }

    protected Program loadDeltaJProgram(String deltaJProgram) throws Exception {
        XtextResourceSet resourceSet = createResourseSetForDeltaJ();
        Resource resource = resourceSet.createResource(URI.createURI("dummy:/example.deltaj"));
        InputStream in = new ByteArrayInputStream(deltaJProgram.getBytes());
        resource.load(in, resourceSet.getLoadOptions());
        EList<Diagnostic> errors = resource.getErrors();
        if (errors.size() > 0) {
            System.err.println("unexpected errors: " + errors);
        }
        assertEquals(errors.toString(), 0, errors.size());
        Program program = (Program) resource.getContents().get(0);
        return program;
    }

    protected DeltaJConstraints loadDeltaJConstraints(ResourceSet resourceSet, CharSequence deltaJConstraints) throws IOException {
        return loadDeltaJConstraints(resourceSet, deltaJConstraints.toString(), 0);
    }

    protected DeltaJConstraints loadDeltaJConstraints(ResourceSet resourceSet, String deltaJConstraints) throws IOException {
        return loadDeltaJConstraints(resourceSet, deltaJConstraints, 0);
    }

    protected DeltaJConstraints loadDeltaJConstraints(ResourceSet resourceSet, String deltaJConstraints, int expectedErrors) throws IOException {
        XtextResourceSet resourceSet2 = getInjector().getInstance(XtextResourceSet.class);
        Resource resource2 = resourceSet2.createResource(URI.createURI("dummy:/example.deltajconstraints"));
        resourceSet.getResources().add(resource2);
        InputStream in = new ByteArrayInputStream(deltaJConstraints.getBytes());
        resource2.load(in, resourceSet.getLoadOptions());
        EList<Diagnostic> errors = resource2.getErrors();
        if (errors.size() > 0) {
            System.err.println("unexpected errors: " + errors);
        }
        assertEquals(errors.toString(), expectedErrors, errors.size());
        DeltaJConstraints constraints = (DeltaJConstraints) resource2.getContents().get(0);
        return constraints;
    }

    protected DeltaJConstraints loadConstraints(CharSequence deltajProgram, CharSequence constraintProgram) throws Exception {
        return loadConstraints(deltajProgram.toString(), constraintProgram.toString());
    }

    protected DeltaJConstraints loadConstraints(String deltajProgram, String constraintsProgram) throws Exception {
        Program program = loadDeltaJProgram(deltajProgram);
        return loadDeltaJConstraints(program.eResource().getResourceSet(), constraintsProgram);
    }

    protected void myAssertArrayToString(Iterable<? extends Object> objects, String... strings) {
        int i = 0;
        for (Object object : objects) {
            assertEquals(strings[i++], object.toString());
        }
    }
}
