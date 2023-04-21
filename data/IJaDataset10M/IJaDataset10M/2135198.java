package net.taylor.xml.jaxb;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public Entity createEntity() {
        return new Entity();
    }
}
