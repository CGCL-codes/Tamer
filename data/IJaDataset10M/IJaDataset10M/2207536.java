package com.reactiveplot.library.events;

import javax.xml.bind.annotation.XmlAttribute;
import com.reactiveplot.library.extensions.ConversationExtensionsEngine;
import com.reactiveplot.library.interpreter.Interpreter;
import com.reactiveplot.library.objectsstates.ObjectsStates;

public class ReturnEvent implements InterpreterEvent {

    private static final long serialVersionUID = -1L;

    @XmlAttribute(name = "value")
    private String returnValue = "";

    public ReturnEvent() {
    }

    public ReturnEvent(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getEndingReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String value) {
        returnValue = value;
    }

    public EventResult handle(ObjectsStates data, Interpreter interpreter, ConversationExtensionsEngine extensionEngine) {
        EventResult result = new EventResult(EventResult.Type.END_CONVERSATION);
        result.setReturnValue(returnValue);
        return result;
    }

    @Override
    public EventResult undoStateChanges(ObjectsStates data, Interpreter interpreter) {
        return new EventResult(EventResult.Type.OK);
    }

    @Override
    public boolean isTheSameAs(Object obj) {
        if (obj instanceof ReturnEvent) return true; else return false;
    }
}
