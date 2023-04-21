package org.slasoi.gslam.syntaxconverter;

import org.apache.xmlbeans.XmlException;
import org.slasoi.slamodel.sla.SLATemplate;

/**
 * 
 * @author Peter A. Chronz
 * 
 *         SLASOITemplateParser is an implementation for the XML-representation of the SLA model for parsing templates. It uses the XmlElementParser
 *         for parsing individual elements.
 */
public abstract class SLATemplateParser {

    protected SLATemplate slaModel;

    protected ElementParser elementParser;

    /**
     * Parse a template from string to SLATemplate object.
     * @param slaTemplateRendering The serialized SLA template.
     * @return SLATemplate object.
     * @throws Exception
     */
    public SLATemplate parseTemplate(String slaTemplateRendering) throws Exception {
        initSLAModel(slaTemplateRendering);
        addParties();
        addInterfaces();
        addVariableDeclarations();
        addAgreementTerms();
        return this.slaModel;
    }

    protected abstract boolean validateSLAModel();

    protected abstract void addAgreementTerms() throws XmlException, Exception;

    protected abstract void addVariableDeclarations() throws XmlException, Exception;

    protected abstract void addInterfaces() throws XmlException, Exception;

    protected abstract void addParties() throws XmlException, Exception;

    protected abstract void initSLAModel(String slaTemplateRendering) throws Exception;
}
