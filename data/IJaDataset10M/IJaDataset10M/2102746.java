package org.norecess.nolatte.primitives;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertSame;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.IText;
import org.norecess.nolatte.ast.Identifier;
import org.norecess.nolatte.ast.visitors.DatumVisitor;
import org.norecess.nolatte.environments.IEnvironment;
import org.norecess.nolatte.interpreters.IInterpreter;
import org.norecess.nolatte.system.IDatumVisitorReflection;
import org.norecess.nolatte.types.IDataTypeFilter;

public class ApplyDatumProcessorPrimitiveTest {

    private static final int LINE_NO = 1234;

    private IDatumVisitorReflection myReflection;

    private IInterpreter myInterpreter;

    private IDataTypeFilter myFilter;

    private IEnvironment myEnvironment;

    private Datum myRawProcessor;

    private IText myProcessorName;

    private DatumVisitor<Datum> myVisitor;

    private Datum myTree;

    private Datum myAnswer;

    private ApplyDatumProcessorPrimitive myPrimitive;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        myReflection = createMock(IDatumVisitorReflection.class);
        myInterpreter = createMock(IInterpreter.class);
        myFilter = createMock(IDataTypeFilter.class);
        myEnvironment = createMock(IEnvironment.class);
        expectFreeAccessToInterpretersAccessors();
        myRawProcessor = createMock(Datum.class);
        myProcessorName = createMock(IText.class);
        myVisitor = createMock(DatumVisitor.class);
        myTree = createMock(Datum.class);
        myAnswer = createMock(Datum.class);
        myPrimitive = new ApplyDatumProcessorPrimitive(myReflection);
    }

    public void replay() {
        EasyMock.replay(myReflection, myInterpreter, myFilter, myEnvironment, myRawProcessor, myProcessorName, myVisitor, myTree, myAnswer);
    }

    public void verify() {
        EasyMock.verify(myReflection, myInterpreter, myFilter, myEnvironment, myRawProcessor, myProcessorName, myVisitor, myTree, myAnswer);
    }

    @Test
    public void should_apply_datum_processor() {
        expect(myEnvironment.get(new Identifier(LINE_NO, "processor"))).andReturn(myRawProcessor);
        expect(myFilter.getText(myRawProcessor)).andReturn(myProcessorName);
        expect(myReflection.construct(myProcessorName)).andReturn(myVisitor);
        expect(myEnvironment.get(new Identifier(LINE_NO, "tree"))).andReturn(myTree);
        expect(myTree.accept(myVisitor)).andReturn(myAnswer);
        replay();
        assertSame(myAnswer, myPrimitive.apply(myInterpreter));
        verify();
    }

    private void expectFreeAccessToInterpretersAccessors() {
        expect(myInterpreter.getDataTypeFilter()).andReturn(myFilter).anyTimes();
        expect(myInterpreter.getEnvironment()).andReturn(myEnvironment).anyTimes();
    }
}
