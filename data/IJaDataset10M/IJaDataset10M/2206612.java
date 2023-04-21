package com.reactiveplot.library.events;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import com.reactiveplot.library.chunks.SpeechChunk;
import com.reactiveplot.library.chunks.UndoChunk;
import com.reactiveplot.library.extensions.ConversationExtensionsEngine;
import com.reactiveplot.library.interpreter.Interpreter;
import com.reactiveplot.library.io.ConversationIOEngine;
import com.reactiveplot.library.objectsstates.ObjectsStates;

public class SpeechEvent implements InterpreterEvent, SpeechChunk, HasCharacterIDsEvent {

    private static final long serialVersionUID = -1844904755668997072L;

    @XmlAttribute
    private String speakerID = "";

    @XmlAttribute
    private String dataID = "";

    @XmlValue
    private String speech = "";

    public SpeechEvent(String speaker, String dataID, String says) {
        this.speakerID = speaker;
        this.dataID = dataID;
        this.speech = says;
    }

    public SpeechEvent() {
    }

    public EventResult handle(ObjectsStates data, Interpreter interpreter, ConversationExtensionsEngine extensionEngine) {
        EventResult result = new EventResult(EventResult.Type.OK);
        result.setChunk(this);
        return result;
    }

    @Override
    public EventResult undoStateChanges(ObjectsStates data, Interpreter interpreter) {
        EventResult r = new EventResult(EventResult.Type.OK);
        r.setChunk(new UndoChunk(this));
        return r;
    }

    @Override
    public boolean isTheSameAs(Object obj) {
        SpeechEvent other = (SpeechEvent) obj;
        if (speech.equals(other.speech) && speakerID.equals(other.speakerID)) {
            if ((dataID == null) && (other.dataID == null) || dataID.equals(other.dataID)) return true;
        }
        return false;
    }

    @Override
    public String getSpeakerID() {
        return speakerID;
    }

    public void setSpeaker(String s) {
        speakerID = s;
    }

    @Override
    public String getTextOfSpeech() {
        return speech;
    }

    public void setSpeechText(String t) {
        speech = t;
    }

    @Override
    public String getDataID() {
        return dataID;
    }

    @Override
    public InterpreterEvent doChunkIOCallbacks(ConversationIOEngine ioEngine, ConversationExtensionsEngine controlEngine) {
        ioEngine.displayConversationSpeech(speakerID, speech, dataID, true);
        return null;
    }

    @Override
    public List<String> getCharacterIDs() {
        List<String> list = new ArrayList<String>();
        list.add(speakerID);
        return list;
    }
}
