package org.translationcomponent.service.document.xml.queue;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.translationcomponent.api.ResponseCode;
import org.translationcomponent.api.ResponseState;
import org.translationcomponent.service.document.xml.StaxParserConfiguration;

public class MockQueueItem implements QueueItem {

    private String text;

    private StaxParserConfiguration config;

    private State state = State.WAITFORTRANSLATION;

    public MockQueueItem(String text, StaxParserConfiguration config) {
        super();
        this.text = text;
        this.config = config;
    }

    public CharactersTranslationItem asCharactersTranslationItem() {
        return null;
    }

    public StartElementTranslationItem asStartElementTranslationItem() {
        return null;
    }

    public String createText() {
        return text;
    }

    public boolean createXml(XMLEventWriter writer) throws XMLStreamException {
        writer.add(config.getEventFactory().createCharacters(text));
        return true;
    }

    public State getState() {
        return state;
    }

    public int getTextLength() {
        return text.length();
    }

    public XMLEvent getXmlEvent() {
        return config.getEventFactory().createCharacters(text);
    }

    public boolean hasEnded() {
        return state != State.WAITFORTRANSLATION;
    }

    public boolean isCharactersTranslationItem() {
        return false;
    }

    public boolean isStartElementTranslationItem() {
        return false;
    }

    public void readResponseXml(ResponseState state, XMLEventReader reader) throws XMLStreamException {
        if (state.getCode() == ResponseCode.OK) {
            StringBuilder b = new StringBuilder();
            while (true) {
                XMLEvent event = reader.nextEvent();
                int type = event.getEventType();
                if (type == XMLEvent.CHARACTERS || type == XMLEvent.ENTITY_DECLARATION || type == XMLEvent.SPACE || type == XMLEvent.CDATA) {
                    b.append(event.asCharacters().getData());
                } else {
                    break;
                }
            }
            this.text = b.toString();
            this.state = State.SUCCESS;
        } else {
            this.state = State.FAIL;
        }
    }

    public void setState(State state) {
        this.state = state;
    }
}
