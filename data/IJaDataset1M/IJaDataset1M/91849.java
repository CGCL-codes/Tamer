package com.pck.logic;

import java.util.List;
import com.pck.util.ClientSession;
import com.pck.util.ReturnValue;

public abstract class InputHandlerBase {

    private InputHandlerBase next;

    public InputHandlerBase(InputHandlerBase next_) {
        next = next_;
    }

    public ReturnValue handleChain(ClientSession session, InputHandlerBase nextHandler) {
        ReturnValue retVal = handleInput(session);
        if (retVal.getStatus() != ReturnValue.Retcode.SUCCESS) return retVal;
        if (nextHandler != null) {
            return nextHandler.handleChain(session, next);
        }
        return retVal;
    }

    public abstract ReturnValue handleInput(ClientSession session);
}
