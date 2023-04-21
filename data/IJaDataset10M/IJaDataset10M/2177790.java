package de.psisystems.dmachinery.xml.pipe.items;

import java.net.URL;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import com.ctc.wstx.stax.WstxInputFactory;

public class MergeXml extends FilterPipeItem {

    private URL urlDataToInject;

    public MergeXml(URL urlDataToInject, EventFilter eventFilter, PipeItem next) {
        super(eventFilter, next);
        this.urlDataToInject = urlDataToInject;
    }

    @Override
    public void add(XMLEvent event) throws XMLStreamException {
        if (getEventFilter().accept(event)) {
            if (urlDataToInject != null) {
                WstxInputFactory inputFactory = new WstxInputFactory();
                XMLEventReader eventReader = inputFactory.createXMLEventReader(urlDataToInject);
                while (eventReader.hasNext()) {
                    XMLEvent xmlEvent = eventReader.nextEvent();
                    if (xmlEvent.getEventType() != XMLEvent.START_DOCUMENT && xmlEvent.getEventType() != XMLEvent.END_DOCUMENT) {
                        getNext().add(xmlEvent);
                    }
                }
            }
        }
        getNext().add(event);
    }
}
