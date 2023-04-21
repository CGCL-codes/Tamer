package at.rc.tacos.codec;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.DayInfoMessage;

public class DayInfoMessageDecoder implements MessageDecoder {

    @Override
    public AbstractMessage doDecode(XMLEventReader reader) throws XMLStreamException {
        DayInfoMessage dayInfo = new DayInfoMessage();
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                String startName = event.asStartElement().getName().getLocalPart();
                if (DayInfoMessage.ID.equalsIgnoreCase(startName)) dayInfo = new DayInfoMessage();
                if ("timestamp".equalsIgnoreCase(startName)) dayInfo.setTimestamp(Long.valueOf(reader.getElementText()));
                if ("message".equalsIgnoreCase(startName)) dayInfo.setMessage(reader.getElementText());
                if ("lastChangedBy".equalsIgnoreCase(startName)) dayInfo.setLastChangedBy(reader.getElementText());
                if ("dirty".equalsIgnoreCase(startName)) dayInfo.setDirty(Boolean.valueOf(reader.getElementText()));
            }
            if (event.isEndElement()) {
                String endElement = event.asEndElement().getName().getLocalPart();
                if (DayInfoMessage.ID.equalsIgnoreCase(endElement)) return dayInfo;
            }
        }
        return null;
    }
}
