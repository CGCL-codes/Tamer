package pedro.soa.ontology.provenance;

import pedro.system.PedroResources;
import pedro.soa.ontology.provenance.OntologyServiceMetaData;
import pedro.util.Parameter;
import pedro.util.PedroXMLParsingUtility;
import org.w3c.dom.Element;
import java.io.*;

public class OntologyMetaDataUtility {

    public void loadValuesFromParameters(Parameter[] parameters, OntologyServiceMetaData ontologyServiceMetaData) {
        for (int i = 0; i < parameters.length; i++) {
            String parameterName = parameters[i].getName();
            String parameterValue = parameters[i].getValue();
            if (parameterName.equals("name") == true) {
                ontologyServiceMetaData.setName(parameterValue);
            } else if (parameterName.equals("author") == true) {
                ontologyServiceMetaData.setAuthor(parameterValue);
            } else if (parameterName.equals("version") == true) {
                ontologyServiceMetaData.setVersion(parameterValue);
            } else if (parameterName.equals("description") == true) {
                ontologyServiceMetaData.setDescription(parameterValue);
            } else if (parameterName.equals("formalism") == true) {
                ontologyServiceMetaData.setFormalism(parameterValue);
            } else if (parameterName.equals("email") == true) {
                ontologyServiceMetaData.setEmail(parameterValue);
            }
        }
    }

    public OntologyMetaData readMetaDataValues(Element ontologyMetaDataElement) {
        OntologyMetaData ontologyMetaData = new OntologyMetaData();
        Element nameElement = PedroXMLParsingUtility.getElement(ontologyMetaDataElement, "name");
        if (nameElement != null) {
            String name = PedroXMLParsingUtility.getValue(nameElement);
            ontologyMetaData.setName(name);
        }
        Element authorElement = PedroXMLParsingUtility.getElement(ontologyMetaDataElement, "author");
        if (authorElement != null) {
            String author = PedroXMLParsingUtility.getValue(authorElement);
            ontologyMetaData.setAuthor(author);
        }
        Element versionElement = PedroXMLParsingUtility.getElement(ontologyMetaDataElement, "version");
        if (versionElement != null) {
            String version = PedroXMLParsingUtility.getValue(versionElement);
            ontologyMetaData.setVersion(version);
        }
        Element descriptionElement = PedroXMLParsingUtility.getElement(ontologyMetaDataElement, "description");
        if (descriptionElement != null) {
            String description = PedroXMLParsingUtility.getValue(descriptionElement);
            ontologyMetaData.setDescription(description);
        }
        Element formalismElement = PedroXMLParsingUtility.getElement(ontologyMetaDataElement, "formalism");
        if (formalismElement != null) {
            String formalism = PedroXMLParsingUtility.getValue(formalismElement);
            ontologyMetaData.setFormalism(formalism);
        }
        Element emailElement = PedroXMLParsingUtility.getElement(ontologyMetaDataElement, "email");
        if (emailElement != null) {
            String email = PedroXMLParsingUtility.getValue(emailElement);
            ontologyMetaData.setEmail(email);
        }
        return ontologyMetaData;
    }

    public void writeMetaDataValues(OntologyMetaData ontologyMetaData, OutputStreamWriter out) throws IOException {
        out.write("<ontologyMetaData>");
        String name = ontologyMetaData.getName();
        if (isFieldEmpty(name) == false) {
            out.write("<name>");
            out.write(name);
            out.write("</name>");
        }
        String author = ontologyMetaData.getAuthor();
        if (isFieldEmpty(author) == false) {
            out.write("<author>");
            out.write(author);
            out.write("</author>");
        }
        String version = ontologyMetaData.getVersion();
        if (isFieldEmpty(version) == false) {
            out.write("<version>");
            out.write(version);
            out.write("</version>");
        }
        String description = ontologyMetaData.getDescription();
        if (isFieldEmpty(description) == false) {
            out.write("<description>");
            out.write(description);
            out.write("</description>");
        }
        String formalism = ontologyMetaData.getFormalism();
        if (isFieldEmpty(formalism) == false) {
            out.write("<formalism>");
            out.write(formalism);
            out.write("</formalism>");
        }
        String email = ontologyMetaData.getEmail();
        if (isFieldEmpty(email) == false) {
            out.write("<email>");
            out.write(email);
            out.write("</email>");
        }
        out.write("</ontologyMetaData>");
    }

    private boolean isFieldEmpty(String fieldValue) {
        if (fieldValue == null) {
            return true;
        }
        if (fieldValue.equals(PedroResources.EMPTY_STRING) == true) {
            return true;
        }
        return false;
    }
}
