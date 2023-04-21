package org.miase.jlibsedml.ui.resources;

import static org.junit.Assert.*;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sedml.Libsedml;
import org.sedml.SEDMLDocument;
import org.sedml.SedMLError;

@RunWith(JMock.class)
public class SEDMLResourceChangeListenerTest {

    Mockery mockery = new JUnit4Mockery();

    final IResourceDelta delta = mockery.mock(IResourceDelta.class);

    final IResource folder = mockery.mock(IFolder.class);

    final IResource file = mockery.mock(IFile.class);

    SEDMLValidatorVisitorTSS tss;

    class SEDMLValidatorVisitorTSS extends SEDMLValidatorVisitor {

        SEDMLValidatorVisitorTSS() {
            super();
        }

        boolean added = false;

        boolean isSEDML = true;

        void addMarkers(IFile changedFile, List<SedMLError> errs) {
            added = true;
        }

        boolean isSedml(IFile file) {
            return isSEDML;
        }

        SEDMLDocument getDocument(IFile changedFile) {
            return new SEDMLDocument();
        }
    }

    @Before
    public void setUp() throws Exception {
        tss = new SEDMLValidatorVisitorTSS();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testVisitTrueForFolder() throws CoreException {
        mockery.checking(new Expectations() {

            {
                atLeast(1).of(delta).getResource();
                will(returnValue(folder));
            }

            {
                atLeast(1).of(folder).getType();
                will(returnValue(IResource.FOLDER));
            }
        });
        assertTrue(tss.visit(delta));
    }

    @Test
    public final void testMarkersAddedIfIsSEDMLFile() throws CoreException {
        mockery.checking(new Expectations() {

            {
                atLeast(1).of(delta).getResource();
                will(returnValue(file));
            }

            {
                atLeast(1).of(file).getType();
                will(returnValue(IResource.FILE));
            }
        });
        assertFalse(tss.visit(delta));
        assertTrue(tss.added);
    }

    @Test
    public final void testVisitFalseIfNonSEDMLFile() throws CoreException {
        mockery.checking(new Expectations() {

            {
                atLeast(1).of(delta).getResource();
                will(returnValue(file));
            }

            {
                atLeast(1).of(file).getType();
                will(returnValue(IResource.FILE));
            }
        });
        tss.isSEDML = false;
        assertFalse(tss.visit(delta));
    }
}
