package gnu.xml.stream;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartEntity;

/**
 * A start-entity event.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class StartEntityImpl extends XMLEventImpl implements StartEntity {

    protected final String name;

    protected StartEntityImpl(Location location, String name) {
        super(location);
        this.name = name;
    }

    public int getEventType() {
        return START_ENTITY;
    }

    public String getName() {
        return name;
    }

    public void writeAsEncodedUnicode(Writer writer) throws XMLStreamException {
    }
}
