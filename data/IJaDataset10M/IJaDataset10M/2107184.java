package gov.nasa.jpf.jvm.undo;

import gov.nasa.jpf.jvm.TestJPF;
import org.junit.Test;
import org.junit.runner.JUnitCore;

/**
 * JPF-level tests for the Undo State extension.
 * 
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 */
public class TestUndoJPF extends TestJPF {

    static final String TEST_CLASS = TestUndo.class.getName();

    public static void main(String[] args) {
        JUnitCore.main(TestUndoJPF.class.getName());
    }

    /**************************** tests **********************************/
    @Test
    public void testPrimitiveField() {
        String[] args = { TEST_CLASS, "+vm.backtracker.class = gov.nasa.jpf.jvm.undo.UndoBacktracker", "+vm.fields_factory.class = gov.nasa.jpf.jvm.undo.UndoFieldsFactory", "testPrimitiveField" };
        runJPFnoException(args);
    }

    @Test
    public void testReferenceField() {
        String[] args = { TEST_CLASS, "+vm.backtracker.class = gov.nasa.jpf.jvm.undo.UndoBacktracker", "+vm.fields_factory.class = gov.nasa.jpf.jvm.undo.UndoFieldsFactory", "testReferenceField" };
        runJPFnoException(args);
    }
}
