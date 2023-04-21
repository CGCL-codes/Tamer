package com.lepidllama.packageeditor.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

public class Complate extends AbstractXmlResource<Complate.ComplateXml> {

    public Complate() {
        super(new Class[] { ComplateXml.class }, "Complate XML");
        unmarshalled = new ComplateXml();
    }

    @XmlType(name = "complate", propOrder = { "category", "type", "name", "typeConverter", "surfaceMaterial", "reskey" })
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "complate")
    public class ComplateXml {

        @XmlAttribute
        protected String category;

        @XmlAttribute
        protected String type;

        @XmlAttribute
        protected String name;

        @XmlAttribute
        protected String typeConverter;

        @XmlAttribute
        protected String surfaceMaterial;

        @XmlAttribute
        protected String reskey;
    }
}
