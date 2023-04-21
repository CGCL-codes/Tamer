package com.talis.platform.client.jaxb;

import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRegistry
public class ObjectFactory {

    public SparqlBooleanResult createBooleanSparqlResult() {
        return new SparqlBooleanResult();
    }

    public static boolean getSparqlBooleanResult(InputStream in) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ObjectFactory.SparqlBooleanResult result = (ObjectFactory.SparqlBooleanResult) unmarshaller.unmarshal(in);
        return result.getBooleanResult();
    }

    @XmlRootElement(namespace = "http://www.w3.org/2005/sparql-results#", name = "sparql")
    public static class SparqlBooleanResult {

        @XmlElement(namespace = "http://www.w3.org/2005/sparql-results#", name = "boolean")
        protected String resultBoolean;

        public boolean getBooleanResult() {
            return Boolean.parseBoolean(resultBoolean);
        }
    }
}
