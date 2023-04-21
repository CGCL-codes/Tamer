package org.infoset.xml.jaxp;

import java.util.Iterator;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.TransformerHandler;
import org.infoset.xml.InfosetFactory;
import org.infoset.xml.Item;
import org.infoset.xml.Item.ItemType;
import org.infoset.xml.ItemDestination;
import org.infoset.xml.Name;
import org.infoset.xml.XMLException;
import org.infoset.xml.filter.*;
import org.infoset.xml.filter.ItemFilter;
import org.infoset.xml.sax.SAXEventGenerator;
import org.infoset.xml.sax.SAXItemDestination;

/**
 *
 * @author R. Alexander Milowski
 */
public class SAXTransformerFilter implements ItemFilter {

    ItemDestination sink;

    TransformerHandler xformer;

    SAXItemDestination saxSink;

    /** Creates a new instance of JAXPTransformer */
    public SAXTransformerFilter(TransformerHandler xformer) {
        this.xformer = xformer;
    }

    public void setParameter(Name name, Object value) {
        Transformer transformer = xformer.getTransformer();
        transformer.setParameter(name.getLocalName(), value);
    }

    public void attach(ItemDestination sink) {
        this.sink = sink;
    }

    public void send(Item item) throws XMLException {
        if (item.getType() == ItemType.DocumentItem) {
            saxSink = new SAXItemDestination(xformer);
            SAXEventGenerator generator = new SAXEventGenerator();
            generator.setItemConstructor(InfosetFactory.getDefaultInfoset().createItemConstructor());
            generator.setItemDestination(sink);
            xformer.setResult(new SAXResult(generator));
        } else if (saxSink == null) {
            throw new IllegalStateException("No document item was send before item type " + item.getType());
        }
        saxSink.send(item);
    }
}
