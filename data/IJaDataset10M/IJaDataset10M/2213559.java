package wsl.mdn.dataview;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.xml.*;
import javax.xml.namespace.QName;
import javax.wsdl.*;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ContentModelGroup;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.Structure;
import org.exolab.castor.xml.schema.reader.SchemaReader;
import org.jdom.Namespace;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

public class WebServiceDescription {

    private String wsdlUrl = null;

    private Definition definition = null;

    private ArrayList<WebServiceDetail> selectedOperationDetails = new ArrayList<WebServiceDetail>();

    private transient boolean _selectedOperationDetailLoaded = false;

    public WebServiceDescription(String wsdlUrl, Definition definition) {
        super();
        this.wsdlUrl = wsdlUrl;
        this.definition = definition;
    }

    public Definition getDefinition() {
        return definition;
    }

    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    public String getWsdlUrl() {
        return wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    public void setSelectedOperationDetails(ArrayList<WebServiceDetail> selectedOperationDetails) {
        this.selectedOperationDetails = selectedOperationDetails;
    }
}
