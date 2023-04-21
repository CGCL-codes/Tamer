package net.sf.istcontract.wsimport.wsdl.writer.document;

import javax.xml.namespace.QName;
import net.sf.istcontract.wsimport.wsdl.writer.document.OpenAtts;
import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;

/**
 *
 * @author WS Development Team
 */
@XmlElement("part")
public interface Part extends TypedXmlWriter, OpenAtts {

    @XmlAttribute
    public net.sf.istcontract.wsimport.wsdl.writer.document.Part element(QName value);

    @XmlAttribute
    public net.sf.istcontract.wsimport.wsdl.writer.document.Part type(QName value);

    @XmlAttribute
    public net.sf.istcontract.wsimport.wsdl.writer.document.Part name(String value);
}
