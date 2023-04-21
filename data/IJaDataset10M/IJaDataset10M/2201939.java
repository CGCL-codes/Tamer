package de.d3web.we.kdom.xml;

public class GenericXMLContent extends XMLContent {

    @Override
    protected void init() {
        this.childrenTypes.add(new GenericXMLObjectType());
    }
}
