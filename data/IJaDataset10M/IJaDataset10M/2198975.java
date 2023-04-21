package org.nomadpim.core.internal.entity.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom.Element;
import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.entity.io.ILocalResolver;
import org.nomadpim.core.internal.entity.IDataStore;
import org.nomadpim.core.internal.entity.IEntityFactory;
import org.nomadpim.core.util.event.PropertyChangeEvent;
import org.nomadpim.core.util.key.Key;
import org.nomadpim.core.util.test.TestersOnly;

public class DOMDataStore implements IDataStore<IEntity> {

    private static final String ID_ATTRIBUTE = "id";

    private final String typeName;

    private final IXMLConverter converter;

    private final IDOMContainer domContainer;

    private final Map<Key, Element> elements = new HashMap<Key, Element>();

    private final ILocalResolver resolver;

    private final IEntityFactory factory;

    public DOMDataStore(IXMLConverter converter, IDOMContainer domContainer, IEntityFactory factory, ILocalResolver resolver) {
        this.factory = factory;
        this.resolver = resolver;
        this.converter = converter;
        this.domContainer = domContainer;
        this.typeName = factory.getType().getName();
    }

    @TestersOnly
    public IXMLConverter getConverter() {
        return converter;
    }

    @TestersOnly
    public IDOMContainer getDomContainer() {
        return domContainer;
    }

    @TestersOnly
    public IEntityFactory getFactory() {
        return factory;
    }

    @TestersOnly
    public ILocalResolver getResolver() {
        return resolver;
    }

    @TestersOnly
    public String getTypeName() {
        return typeName;
    }

    @TestersOnly
    public boolean isRegistered(Key key) {
        return elements.containsKey(key);
    }

    public void objectAdded(IEntity o) {
        Element element = new Element(o.getKey().getType());
        element.setAttribute("id", Long.toString(o.getKey().getID()));
        converter.fillXMLElement(o, element);
        domContainer.addElement(element);
        elements.put(o.getKey(), element);
    }

    public void objectChanged(IEntity o) {
    }

    public void objectChanged(IEntity o, PropertyChangeEvent event) {
        Element element = elements.get(o.getKey());
        element.removeContent();
        converter.fillXMLElement(o, element);
        domContainer.elementChanged(element);
    }

    public void objectRemoved(IEntity o) {
        Element element = elements.remove(o.getKey());
        domContainer.removeElement(element);
    }

    public List<IEntity> restore() {
        List<Element> elements = domContainer.getElements(typeName);
        List<IEntity> result = new ArrayList<IEntity>();
        for (Element element : elements) {
            long id = Long.parseLong(element.getAttributeValue(ID_ATTRIBUTE));
            IEntity t = factory.create(id);
            converter.fillSpaceObject(t, element, resolver);
            result.add(t);
            this.elements.put(t.getKey(), element);
        }
        return result;
    }
}
