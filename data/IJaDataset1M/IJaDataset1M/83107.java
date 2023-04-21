package org.isi.monet.core.model;

import java.io.IOException;
import java.io.StringReader;
import java.util.EventObject;
import org.isi.monet.core.constants.ErrorCode;
import org.isi.monet.core.constants.Strings;
import org.isi.monet.core.exceptions.DataException;
import org.isi.monet.core.library.LibraryXML;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class ServiceDefinition extends ModelDefinition implements ILoadListener {

    private ServiceDefinitionInput oInput;

    private ServiceDefinitionProcess oProcess;

    private ServiceDefinitionOutput oOutput;

    public ServiceDefinition() {
        super();
        this.oInput = new ServiceDefinitionInput();
        this.oProcess = new ServiceDefinitionProcess();
        this.oOutput = new ServiceDefinitionOutput();
        this.linkLoadListener(this);
    }

    public ServiceDefinitionInput getInput() {
        onLoad(this, ModelDefinition.DEFINITION);
        return this.oInput;
    }

    public ServiceDefinitionProcess getProcess() {
        onLoad(this, ModelDefinition.DEFINITION);
        return this.oProcess;
    }

    public ServiceDefinitionOutput getOutput() {
        onLoad(this, ModelDefinition.DEFINITION);
        return this.oOutput;
    }

    public Boolean unserializeFromXML(String sContent) {
        org.jdom.Document oDocument;
        SAXBuilder oBuilder = new SAXBuilder();
        StringReader oReader;
        Element oNode;
        if (sContent.equals(Strings.EMPTY)) return true;
        while (!sContent.substring(sContent.length() - 1).equals(">")) sContent = sContent.substring(0, sContent.length() - 1);
        sContent = LibraryXML.addEntities("definition", sContent);
        oReader = new StringReader(sContent);
        try {
            oDocument = oBuilder.build(oReader);
            oNode = oDocument.getRootElement();
            if (oNode.getAttribute("code") != null) this.code = oNode.getAttributeValue("code");
            if (oNode.getAttribute("type") != null) this.Type = oNode.getAttributeValue("type");
            if (oNode.getAttribute("scope") != null) this.Scope = oNode.getAttributeValue("scope");
            if (oNode.getAttribute("parent") != null) this.codeParent = oNode.getAttributeValue("parent");
            oNode = oNode.getChild("service");
            if (oNode.getChild("input") != null) this.oInput.unserializeFromXML(oNode.getChild("input"));
            if (oNode.getChild("process") != null) this.oProcess.unserializeFromXML(oNode.getChild("process"));
            if (oNode.getChild("output") != null) this.oOutput.unserializeFromXML(oNode.getChild("output"));
        } catch (JDOMException oException) {
            throw new DataException(ErrorCode.UNSERIALIZE_DEFINITION_FROM_XML, sContent, oException);
        } catch (IOException oException) {
            throw new DataException(ErrorCode.UNSERIALIZE_DEFINITION_FROM_XML, sContent, oException);
        }
        return true;
    }

    public Boolean loadAttribute(EventObject eventObject, String attribute) {
        String sContent = this.getDefinitionContent();
        this.unserializeFromXML(sContent);
        return true;
    }
}
