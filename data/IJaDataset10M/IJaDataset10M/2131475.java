package net.sourceforge.symba.mapping.hibernatejaxb2.xml;

import net.sourceforge.fuge.collection.FuGE;
import net.sourceforge.symba.mapping.hibernatejaxb2.helper.FuGEMappingHelper;
import net.sourceforge.symba.mapping.hibernatejaxb2.DatabaseObjectHelper;
import net.sourceforge.fuge.service.EntityServiceException;
import net.sourceforge.fuge.util.generatedJAXB2.FuGECollectionFuGEType;
import net.sourceforge.fuge.common.audit.Person;
import org.xml.sax.SAXException;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public class XMLUnmarshaler {

    private final String schemaFilename, XMLFilename;

    public XMLUnmarshaler(String sf, String xf) {
        this.schemaFilename = sf;
        this.XMLFilename = xf;
    }

    /**
     * Use this constructor if, for some reason, you don't have access to the XSD. However, doing this
     * is dangerous as you won't see if there are validation errors.
     *
     * @param xf the input XML file
     */
    public XMLUnmarshaler(String xf) {
        this.schemaFilename = null;
        this.XMLFilename = xf;
    }

    public String Jaxb2ToFuGE(Person performer) throws JAXBException, SAXException, EntityServiceException, URISyntaxException, FileNotFoundException {
        JAXBContext jc = JAXBContext.newInstance("net.sourceforge.fuge.util.generatedJAXB2");
        Unmarshaller u = jc.createUnmarshaller();
        if (schemaFilename != null) {
            SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File(schemaFilename));
            u.setSchema(schema);
        }
        JAXBElement<?> genericTopLevelElement = (JAXBElement<?>) u.unmarshal(new FileInputStream(XMLFilename));
        FuGECollectionFuGEType frXML = (FuGECollectionFuGEType) genericTopLevelElement.getValue();
        FuGE fr;
        fr = (FuGE) DatabaseObjectHelper.getOrCreate(frXML.getIdentifier(), frXML.getEndurantRef(), frXML.getName(), "net.sourceforge.fuge.collection.FuGE");
        FuGEMappingHelper cf = new FuGEMappingHelper();
        fr = cf.unmarshal(frXML, fr, performer);
        DatabaseObjectHelper.save("net.sourceforge.fuge.collection.FuGE", fr, performer);
        return fr.getIdentifier();
    }
}
