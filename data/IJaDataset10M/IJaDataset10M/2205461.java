package org.norecess.nolatte.primitives.system;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertSame;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.IText;
import org.norecess.nolatte.environments.IEnvironment;
import org.norecess.nolatte.interpreters.IInterpreter;
import org.norecess.nolatte.system.IProcess;
import org.norecess.nolatte.system.IReader;
import org.norecess.nolatte.system.ISystem;
import org.norecess.nolatte.system.NoLatteVariables;
import org.norecess.nolatte.types.IDataTypeFilter;

public class ProcessOutputPrimitiveTest {

    private ISystem mySystem;

    private IInterpreter myInterpreter;

    private IDataTypeFilter myDataTypeFilter;

    private IEnvironment myEnvironment;

    private Datum myRawDatum;

    private IText myCommand;

    private IProcess myProcess;

    private IReader myInputReader;

    private IReader myErrorReader;

    private IText myResult;

    private ProcessOutputPrimitive myProcessOutputPrimitive;

    private IText myErrorText;

    @Before
    public void setUp() {
        mySystem = createMock(ISystem.class);
        myInterpreter = createMock(IInterpreter.class);
        myDataTypeFilter = createMock(IDataTypeFilter.class);
        myEnvironment = createMock(IEnvironment.class);
        expectFreeAccessToInterpretersAccessors();
        myRawDatum = createMock(Datum.class);
        myCommand = createMock(IText.class);
        myProcess = createMock(IProcess.class);
        myInputReader = createMock(IReader.class);
        myErrorReader = createMock(IReader.class);
        myErrorText = createMock(IText.class);
        myResult = createMock(IText.class);
        myProcessOutputPrimitive = new ProcessOutputPrimitive(mySystem);
    }

    public void replay() {
        EasyMock.replay(mySystem, myInterpreter, myDataTypeFilter, myEnvironment, myRawDatum, myCommand, myProcess, myInputReader, myErrorReader, myErrorText, myResult);
    }

    @After
    public void verify() {
        EasyMock.verify(mySystem, myInterpreter, myDataTypeFilter, myEnvironment, myRawDatum, myCommand, myProcess, myInputReader, myErrorReader, myErrorText, myResult);
    }

    @Test
    public void should_return_text_normally() {
        expect(myEnvironment.get(NoLatteVariables.COMMAND)).andReturn(myRawDatum);
        expect(myDataTypeFilter.convertToText(myRawDatum)).andReturn(myCommand);
        expect(myCommand.getString()).andReturn("the command");
        expect(mySystem.exec("the command")).andReturn(myProcess);
        myProcess.waitFor();
        expect(myProcess.getErrorReader()).andReturn(myErrorReader);
        expect(myDataTypeFilter.toText(myErrorReader)).andReturn(myErrorText);
        expect(myProcess.getExitValue()).andReturn(0);
        expect(myErrorText.getLength()).andReturn(0);
        expect(myProcess.getInputReader()).andReturn(myInputReader);
        expect(myDataTypeFilter.toText(myInputReader)).andReturn(myResult);
        replay();
        assertSame(myResult, myProcessOutputPrimitive.apply(myInterpreter));
        verify();
    }

    @Test(expected = ProcessOutputException.class)
    public void should_throw_exception_when_process_exits_badly() {
        expect(myEnvironment.get(NoLatteVariables.COMMAND)).andReturn(myRawDatum);
        expect(myDataTypeFilter.convertToText(myRawDatum)).andReturn(myCommand);
        expect(myCommand.getString()).andReturn("the command");
        expect(mySystem.exec("the command")).andReturn(myProcess);
        myProcess.waitFor();
        expect(myProcess.getErrorReader()).andReturn(myErrorReader);
        expect(myDataTypeFilter.toText(myErrorReader)).andReturn(myErrorText);
        expect(myProcess.getExitValue()).andReturn(1);
        replay();
        myProcessOutputPrimitive.apply(myInterpreter);
        verify();
    }

    @Test(expected = ProcessOutputException.class)
    public void should_throw_exception_when_process_generates_error_messages() {
        expect(myEnvironment.get(NoLatteVariables.COMMAND)).andReturn(myRawDatum);
        expect(myDataTypeFilter.convertToText(myRawDatum)).andReturn(myCommand);
        expect(myCommand.getString()).andReturn("the command");
        expect(mySystem.exec("the command")).andReturn(myProcess);
        myProcess.waitFor();
        expect(myProcess.getErrorReader()).andReturn(myErrorReader);
        expect(myDataTypeFilter.toText(myErrorReader)).andReturn(myErrorText);
        expect(myProcess.getExitValue()).andReturn(0);
        expect(myErrorText.getLength()).andReturn(123);
        replay();
        myProcessOutputPrimitive.apply(myInterpreter);
        verify();
    }

    private void expectFreeAccessToInterpretersAccessors() {
        expect(myInterpreter.getDataTypeFilter()).andReturn(myDataTypeFilter).anyTimes();
        expect(myInterpreter.getEnvironment()).andReturn(myEnvironment).anyTimes();
    }
}
