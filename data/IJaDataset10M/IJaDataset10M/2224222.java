package abstractService.compensationManagement;

import java.io.File;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import xmlProcessing.IXMLValidationErrorHandler;
import xmlProcessing.XMLFileReader;
import xmlProcessing.XMLProcessing;
import xmlProcessing.XMLValidation;
import abstractService.commonData.AbstractServiceConstants;
import abstractService.commonData.IAbstractServiceData;

/**
 * The repository manages the existing compensation rules. It reads the
 * information from the configuration file that contains the compensation
 * rules, and provides the rules sorted by "internal" and "external" rules
 * via its methods.
 * 
 * @author Michael Sch�fer
 *
 */
public class CompensationRulesRepository implements ICompensationRulesRepository {

    private IAbstractServiceData abstractServiceData;

    private Vector<IInternalCompensationRule> internalCompensationRules;

    private Vector<IExternalCompensationRule> externalCompensationRules;

    /**
	 * Constructs a new compensation rules repository object. The compensation rules file
	 * specified in the given AbstractServiceData object is being read and its information
	 * retrieved.
	 * @param abstractServiceData The AbstractServiceData object that contains the file reference.
	 * @throws CompensationManagerException 
	 */
    public CompensationRulesRepository(IAbstractServiceData abstractServiceData) throws CompensationManagerException {
        this.abstractServiceData = abstractServiceData;
        this.internalCompensationRules = new Vector<IInternalCompensationRule>();
        this.externalCompensationRules = new Vector<IExternalCompensationRule>();
        this.readCompensationRulesFile();
    }

    /**
	 * Returns all existing internal compensation rules.
	 * @return An array with all internal compensation rules.
	 */
    public IInternalCompensationRule[] getInternalCompensationRules() {
        return this.internalCompensationRules.toArray(new IInternalCompensationRule[0]);
    }

    /**
	 * Returns all existing external compensation rules.
	 * @return An array with all external compensation rules.
	 */
    public IExternalCompensationRule[] getExternalCompensationRules() {
        return this.externalCompensationRules.toArray(new IExternalCompensationRule[0]);
    }

    /**
	 * Reads out the compensation rules file reference from the AbstractServiceData object, 
	 * and reads the file contents. Subsequently, it creates an XML document from it, and 
	 * retrieves all existing internal and external compensation rules. The rules
	 * are stored in objects of the respective holder classes, and are added to the
	 * two vectors.
	 * @throws CompensationManagerException 
	 * 
	 */
    private void readCompensationRulesFile() throws CompensationManagerException {
        File compensationRulesFile = abstractServiceData.getCompensationRulesFile();
        File compensationRulesSchemaFile = abstractServiceData.getCompensationRulesSchemaFile();
        File[] schemaFiles = new File[] { compensationRulesSchemaFile };
        Document compensationRulesDocument = null;
        try {
            compensationRulesDocument = XMLFileReader.readXMLFile(compensationRulesFile);
            IXMLValidationErrorHandler handler = XMLValidation.validateDocument(schemaFiles, compensationRulesDocument);
            if (!handler.hasErrors()) {
                NodeList internalCompensationRulesElements = compensationRulesDocument.getElementsByTagNameNS(AbstractServiceConstants.NAMESPACE_COMPENSATIONRULES, AbstractServiceConstants.ELEMENT_COMPENSATIONRULES_INTERNALCOMPENSATIONRULES);
                Element internalCompensationRulesElement = (Element) internalCompensationRulesElements.item(0);
                Element[] internalCompensationRuleElements = XMLProcessing.getChildElements(internalCompensationRulesElement, AbstractServiceConstants.NAMESPACE_COMPENSATIONRULES, AbstractServiceConstants.ELEMENT_COMPENSATIONRULES_INTERNALCOMPENSATIONRULES_INTERNALCOMPENSATIONRULE);
                Element internalCompensationRuleElement;
                IInternalCompensationRule internalCompensationRule;
                for (int i = 0; i < internalCompensationRuleElements.length; i++) {
                    internalCompensationRuleElement = internalCompensationRuleElements[i];
                    internalCompensationRule = new InternalCompensationRule(internalCompensationRuleElement);
                    this.internalCompensationRules.add(internalCompensationRule);
                }
                NodeList externalCompensationRulesElements = compensationRulesDocument.getElementsByTagNameNS(AbstractServiceConstants.NAMESPACE_COMPENSATIONRULES, AbstractServiceConstants.ELEMENT_COMPENSATIONRULES_EXTERNALCOMPENSATIONRULES);
                Element externalCompensationRulesElement = (Element) externalCompensationRulesElements.item(0);
                Element[] externalCompensationRuleElements = XMLProcessing.getChildElements(externalCompensationRulesElement, AbstractServiceConstants.NAMESPACE_COMPENSATIONRULES, AbstractServiceConstants.ELEMENT_COMPENSATIONRULES_EXTERNALCOMPENSATIONRULES_EXTERNALCOMPENSATIONRULE);
                Element externalCompensationRuleElement;
                IExternalCompensationRule externalCompensationRule;
                for (int i = 0; i < externalCompensationRuleElements.length; i++) {
                    externalCompensationRuleElement = externalCompensationRuleElements[i];
                    externalCompensationRule = new ExternalCompensationRule(externalCompensationRuleElement);
                    this.externalCompensationRules.add(externalCompensationRule);
                }
            } else {
                throw new CompensationManagerException("The compensation rules definition file is not structured correctly according to the XML schema: " + handler.getErrorDescription());
            }
        } catch (Exception e) {
            this.internalCompensationRules = new Vector<IInternalCompensationRule>();
            this.externalCompensationRules = new Vector<IExternalCompensationRule>();
            throw new CompensationManagerException("The compensation rules definition file could not be read successfully.");
        }
    }
}
