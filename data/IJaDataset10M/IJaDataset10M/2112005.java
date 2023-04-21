package com.reactiveplot.library.events;

import java.io.IOException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.stream.XMLStreamException;
import com.reactiveplot.library.extensions.ConversationExtensionsEngine;
import com.reactiveplot.library.interpreter.Interpreter;
import com.reactiveplot.library.interpreter.BadConversationScriptException;
import com.reactiveplot.library.objectsstates.ObjectsStates;

public class GotoEvent implements InterpreterEvent {

    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "segmentID")
    String segment = "";

    public String getGotoSegmentID() {
        return segment;
    }

    public void setTargetSegmentID(String newTarget) {
        segment = newTarget;
    }

    public GotoEvent(String segmentID) {
        segment = segmentID;
    }

    public GotoEvent() {
    }

    public EventResult handle(ObjectsStates data, Interpreter interpreter, ConversationExtensionsEngine extensionEngine) {
        try {
            interpreter.gotoSegment(segment);
            EventResult result = new EventResult(EventResult.Type.OK);
            result.requestClearQueue();
            return result;
        } catch (XMLStreamException e) {
            e.printStackTrace();
            return new EventResult(EventResult.Type.TERMINAL_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            return new EventResult(EventResult.Type.TERMINAL_ERROR);
        } catch (BadConversationScriptException e) {
            EventResult result = new EventResult(EventResult.Type.TERMINAL_ERROR);
            result.setReturnValue("Trying to goto segmentID \"" + segment + "\", got a BadConversationScriptException");
            return result;
        }
    }

    @Override
    public EventResult undoStateChanges(ObjectsStates data, Interpreter interpreter) {
        return new EventResult(EventResult.Type.OK);
    }

    @Override
    public boolean isTheSameAs(Object obj) {
        GotoEvent other = (GotoEvent) obj;
        if (segment.equals(other.segment)) return true; else return false;
    }
}
