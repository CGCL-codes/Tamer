package se.krka.kahlua.integration;

public class LuaSuccess extends LuaReturn {

    LuaSuccess(Object[] returnValues) {
        super(returnValues);
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public Object getErrorObject() {
        throw new UnsupportedOperationException("Not valid when isSuccess is true");
    }

    @Override
    public String getErrorString() {
        throw new UnsupportedOperationException("Not valid when isSuccess is true");
    }

    @Override
    public String getLuaStackTrace() {
        throw new UnsupportedOperationException("Not valid when isSuccess is true");
    }

    @Override
    public RuntimeException getJavaException() {
        throw new UnsupportedOperationException("Not valid when isSuccess is true");
    }
}
