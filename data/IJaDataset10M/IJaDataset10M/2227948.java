package org.xaware.ide.xadev.richui.editor.util;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.xaware.ide.xadev.gui.model.DocumentModel;
import org.xaware.ide.xadev.richui.editor.service.XAInputPage;
import org.xaware.ide.xadev.richui.editor.service.file.FileServiceHelper;
import org.xaware.ide.xadev.richui.editor.service.sql.SQLServiceHelper;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;

/**
 * This class provides Input XML for the {@link XAInputPage} depending on the given BizComponent model. It decides which
 * Util class to use depending on the BizComponent model
 * 
 * @author satishk
 * 
 */
public class XAServiceInputPageHelper {

    /** BizComponent types Supported. */
    public enum BizComponentTypes {

        SQL, FILE
    }

    /**
     * String constant to represent the text 'should xa:merge_template be created'; used to indicate type of data to be
     * extracted from model.
     */
    private static final String IS_MERGE_TEMPLATE_TO_BE_CREATED = "should xa:merge_template be created";

    /**
     * String constant to represent the text 'Input XML Response Map Description'; used to indicate type of data to be
     * extracted from model.
     */
    private static final String INPUT_XML_RESPONSE_MAP_DESC = "Input XML Response Map Description";

    /**
     * String constant to represent the text 'Input XML Section Name'; used to indicate type of data to be extracted
     * from model.
     */
    private static final String INPUT_XML_SECTION_NAME = "Input XML Section Name";

    /** String constant to represent the default name of the Input XML section. */
    public static final String INPUT_XML_SECTION_DEFAULT_NAME = "Input XML";

    private static final String INPUT_XML_SECTION_EXPANDED = "Input XML Exapanded";

    private static final String REFERENCE_SECTION_EXPANDED = "BizComponent Reference Exapanded";

    /**
     * This method finds the input path from the BizComponent reference element and returns the XML element present at
     * this input path in the BizComponent input XML root.
     * 
     * @param model
     *            BizComponent DocumentModel.
     * @param bizCompRefElement
     *            BizComponent reference element in the BizDocument
     * @return XML element present at the given input path in the BizComponent reference element.
     * @throws JDOMException
     * @throws XAwareException
     */
    public static Element getInputXML(DocumentModel model, Element bizCompRefElement) throws XAwareException {
        if (bizCompRefElement == null) {
            throw new XAwareException("BizComponent reference element cannot be null.");
        }
        String inputPath = bizCompRefElement.getAttributeValue(XAwareConstants.XAWARE_ATTR_INPUT, XAwareConstants.xaNamespace);
        if (inputPath == null) {
            return null;
        }
        if (inputPath.startsWith(XAwareConstants.XA_INPUT_URL)) {
            inputPath = inputPath.replaceFirst(XAwareConstants.XA_INPUT_URL, "");
        } else {
            return null;
        }
        String p = XAwareConstants.xaNamespace.getPrefix();
        String path = "//" + p + ":" + XAwareConstants.DESIGNER_ELEMENT_WIZARD_STEP + "[@" + p + ":" + XAwareConstants.DESIGNER_ATTR_NAME + "='" + XAInputPage.DESIGNER_WIZARD_STEP_NAME + "']/" + p + ":" + XAInputPage.DESIGNER_ELEMENT_INPUT_XML + inputPath;
        Object inputXML;
        try {
            inputXML = RichUIEditorXmlUtil.selectSingleNode(model, path);
        } catch (JDOMException e) {
            throw new XAwareException(e);
        }
        if (inputXML != null && inputXML instanceof Element) return (Element) ((Element) inputXML).clone();
        throw null;
    }

    /**
     * Returns the XML for the given model.
     * 
     * @param bizComponentModel
     *            BizComponent DocumentModel.
     * @return XML depending on the given model.
     * @throws XAwareException
     *             Exception while extracting Input xml from the model
     */
    private static Object getDataFromModel(DocumentModel bizComponentModel, DocumentModel bizDriverModel, String requiredData) throws XAwareException {
        String bizCompType = getBizComponentType(bizComponentModel);
        try {
            switch(BizComponentTypes.valueOf(bizCompType)) {
                case SQL:
                    if (requiredData.equals(IS_MERGE_TEMPLATE_TO_BE_CREATED)) return SQLServiceHelper.isMergeTemplateCreationConditionSatisfied(bizComponentModel);
                    if (requiredData.equals(INPUT_XML_RESPONSE_MAP_DESC)) return SQLServiceHelper.INPUT_XML_RESPONSE_MAP_DESC;
                    if (requiredData.equals(INPUT_XML_SECTION_NAME)) return INPUT_XML_SECTION_DEFAULT_NAME;
                    if (requiredData.equals(INPUT_XML_SECTION_EXPANDED)) return true;
                    if (requiredData.equals(REFERENCE_SECTION_EXPANDED)) return true;
                    break;
                case FILE:
                    if (requiredData.equals(IS_MERGE_TEMPLATE_TO_BE_CREATED)) return FileServiceHelper.isMergeTemplateCreationConditionSatisfied(bizDriverModel);
                    if (requiredData.equals(INPUT_XML_RESPONSE_MAP_DESC)) return FileServiceHelper.INPUT_XML_RESPONSE_MAP_DESC;
                    if (requiredData.equals(INPUT_XML_SECTION_NAME)) return FileServiceHelper.getInputXMLSectionName(bizDriverModel);
                    if (requiredData.equals(INPUT_XML_SECTION_EXPANDED)) return FileServiceHelper.isInputXMLSectionExpanded(bizDriverModel);
                    if (requiredData.equals(REFERENCE_SECTION_EXPANDED)) return FileServiceHelper.isBizCompReferenceSectionExpanded(bizDriverModel);
                    break;
                default:
                    throw new XAwareException("Unknown/Not supported Bizcomponent type: " + bizCompType);
            }
        } catch (Exception e) {
            throw new XAwareException("Exception raised while retrieving : " + requiredData + " from Bizcomponent model.");
        }
        throw new XAwareException("Unknown value '" + requiredData + "' requested from " + XAServiceInputPageHelper.class.getName());
    }

    /**
     * Extracts the BizComponent type from the given model
     * 
     * @param model
     *            BizComponent DocumentModel.
     * @return BizComponent type.
     * @throws XAwareException
     *             Exception during obtaining the BizComponent type.
     */
    public static String getBizComponentType(DocumentModel model) throws XAwareException {
        Attribute bizCompTypeAttribute = null;
        try {
            bizCompTypeAttribute = (Attribute) RichUIEditorXmlUtil.selectSingleNode(model, "@" + XAwareConstants.xaNamespace.getPrefix() + ":" + XAwareConstants.BIZCOMPONENT_ATTR_TYPE);
        } catch (JDOMException e) {
            throw new XAwareException("Exception raised while getting " + XAwareConstants.xaNamespace.getPrefix() + ":" + XAwareConstants.BIZCOMPONENT_ATTR_TYPE + " value");
        }
        if (bizCompTypeAttribute == null || bizCompTypeAttribute.getValue().equals("")) {
            throw new XAwareException("Unknown Bizcomponent type: xa:bizcomp attribute should be present and have a valid bizcomponent type.");
        }
        return bizCompTypeAttribute.getValue().toUpperCase();
    }

    /**
     * evaluates whether the xa:merge_template element creation conditions are satisfied or not. for the given model.
     * 
     * @param bizComponentModel
     *            BizComponent DocumentModel.
     * @return true if xa:merge_template element can be created, otherwise false.
     * @throws XAwareException
     *             Exception while extracting Input xml from the model
     */
    public static boolean isMergeTemplateCreationConditionSatisfied(DocumentModel bizComponentModel, DocumentModel bizDriverModel) throws XAwareException {
        return ((Boolean) getDataFromModel(bizComponentModel, bizDriverModel, IS_MERGE_TEMPLATE_TO_BE_CREATED)).booleanValue();
    }

    /**
     * Returns the Input XML /Response Map section description for the Input Data service page.
     * 
     * @param bizComponentModel
     *            BizComponent DocumentModel.
     * @return the Input XML /Response Map section description for the Input Data service page.
     * @throws XAwareException
     *             Exception while retrieving the description.
     */
    public static String getInputXMLResponseMapDescription(DocumentModel bizComponentModel, DocumentModel bizDriverModel) throws XAwareException {
        return getDataFromModel(bizComponentModel, bizDriverModel, INPUT_XML_RESPONSE_MAP_DESC).toString();
    }

    /**
     * Returns the Input XML section name for the Input Data service page.
     * 
     * @param bizComponentModel
     *            BizComponent DocumentModel.
     * @return name of the Input XML section
     * @throws XAwareException
     *             Exception while retrieving the name.
     */
    public static String getInputXMLSectionName(DocumentModel bizComponentModel, DocumentModel bizDriverModel) throws XAwareException {
        return getDataFromModel(bizComponentModel, bizDriverModel, INPUT_XML_SECTION_NAME).toString();
    }

    /**
     * Determines the enabling status for the Input XML section.
     * 
     * @param bizComponentModel
     *            BizComponent DocumentModel.
     * @return Enabling status of the Input XML section
     * @throws XAwareException
     *             Exception while retrieving the enabling status.
     */
    public static boolean isInputXMLSectionExpanded(DocumentModel bizComponentModel, DocumentModel bizDriverModel) throws XAwareException {
        return ((Boolean) getDataFromModel(bizComponentModel, bizDriverModel, INPUT_XML_SECTION_EXPANDED)).booleanValue();
    }

    /**
     * Determines the enabling status for the BizComponent Reference section.
     * 
     * @param bizComponentModel
     *            BizComponent DocumentModel.
     * @return Enabling status of the BizComponent Reference section
     * @throws XAwareException
     *             Exception while retrieving the enabling status.
     */
    public static boolean isBizCompReferenceSectionExpanded(DocumentModel bizComponentModel, DocumentModel bizDriverModel) throws XAwareException {
        return ((Boolean) getDataFromModel(bizComponentModel, bizDriverModel, REFERENCE_SECTION_EXPANDED)).booleanValue();
    }
}
