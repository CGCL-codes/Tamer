package org.norecess.nolatte.support;

import static org.easymock.EasyMock.expect;
import java.util.ArrayList;
import java.util.List;
import org.easymock.EasyMock;
import org.norecess.nolatte.ast.support.IDatumFactory;
import org.norecess.nolatte.environments.IEnvironment;
import org.norecess.nolatte.interpreters.IInterpreter;
import org.norecess.nolatte.types.IDataTypeFilter;

public class NoLatteMockControl {

    private final List<Object> myMocks;

    public NoLatteMockControl() {
        myMocks = new ArrayList<Object>();
    }

    public <T> T[] createMocks(T[] array, Class<T> name) {
        for (int i = 0; i < array.length; i++) {
            array[i] = createMock(name);
        }
        return array;
    }

    public void replay() {
        EasyMock.replay(myMocks.toArray());
    }

    public void verify() {
        EasyMock.verify(myMocks.toArray());
    }

    public <T> T createMock(Class<T> name) {
        T mock = org.easymock.EasyMock.createMock(name);
        myMocks.add(mock);
        return mock;
    }

    public IInterpreter createEasyInterpreter(IEnvironment environment, IDataTypeFilter filter, IDatumFactory factory) {
        IInterpreter interpreter = createMock(IInterpreter.class);
        expect(interpreter.getEnvironment()).andReturn(environment).atLeastOnce();
        expect(interpreter.getDataTypeFilter()).andReturn(filter).atLeastOnce();
        expect(interpreter.getDatumFactory()).andReturn(factory).atLeastOnce();
        return interpreter;
    }

    public IInterpreter createEasyInterpreter(IEnvironment environment, IDataTypeFilter filter) {
        IInterpreter interpreter = createMock(IInterpreter.class);
        expect(interpreter.getEnvironment()).andReturn(environment).atLeastOnce();
        expect(interpreter.getDataTypeFilter()).andReturn(filter).atLeastOnce();
        return interpreter;
    }
}
