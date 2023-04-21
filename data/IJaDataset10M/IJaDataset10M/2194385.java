package com.vangent.hieos.hl7v3util.model.subject;

import com.vangent.hieos.hl7v3util.model.builder.BuilderHelper;
import com.vangent.hieos.hl7v3util.model.message.PRPA_IN201301UV02_Message;
import com.vangent.hieos.hl7v3util.model.message.PRPA_IN201306UV02_Message;
import com.vangent.hieos.hl7v3util.model.message.PRPA_IN201310UV02_Message;
import com.vangent.hieos.hl7v3util.model.exception.ModelBuilderException;
import com.vangent.hieos.hl7v3util.model.message.HL7V3Message;
import com.vangent.hieos.hl7v3util.model.message.PRPA_IN201302UV02_Message;
import com.vangent.hieos.xutil.exception.XPathHelperException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

/**
 *
 * @author Bernie Thuman
 */
public class SubjectBuilder extends BuilderHelper {

    private static final Logger logger = Logger.getLogger(SubjectBuilder.class);

    private static final String XPATH_PATIENT = "./ns:registrationEvent/ns:subject1/ns:patient[1]";

    private static final String XPATH_PATIENT_ADD = "./ns:controlActProcess/ns:subject/ns:registrationEvent/ns:subject1/ns:patient[1]";

    private static final String XPATH_PATIENT_PERSON = "./ns:patientPerson[1]";

    private static final String XPATH_ADDRESSES = "./ns:addr";

    private static final String XPATH_TELECOM_ADDRESSES = "./ns:telecom";

    private static final String XPATH_LANGUAGES = "./ns:languageCommunication";

    private static final String XPATH_CITIZENSHIPS = "./ns:asCitizen";

    private static final String XPATH_NATION_CODE = "./ns:politicalNation/ns:code[1]";

    private static final String XPATH_NATION_NAME = "./ns:politicalNation/ns:name[1]";

    private static final String XPATH_LANGUAGE_PREFERENCE_INDICATOR = "./ns:preferenceInd[1]";

    private static final String XPATH_NAMES = "./ns:name";

    private static final String XPATH_GENDER = "./ns:administrativeGenderCode[1]";

    private static final String XPATH_MARITAL_STATUS = "./ns:maritalStatusCode[1]";

    private static final String XPATH_RELIGIOUS_AFFILIATION = "./ns:religiousAffiliationCode[1]";

    private static final String XPATH_RACE = "./ns:raceCode[1]";

    private static final String XPATH_ETHNIC_GROUP = "./ns:ethnicGroupCode[1]";

    private static final String XPATH_BIRTH_TIME = "./ns:birthTime[1]";

    private static final String XPATH_DECEASED_INDICATOR = "./ns:deceasedInd[1]";

    private static final String XPATH_DECEASED_TIME = "./ns:deceasedTime[1]";

    private static final String XPATH_MULTIPLE_BIRTH_INDICATOR = "./ns:multipleBirthInd[1]";

    private static final String XPATH_MULTIPLE_BIRTH_ORDER_NUMBER = "./ns:multipleBirthOrderNumber[1]";

    private static final String XPATH_AS_OTHER_IDS = "./ns:asOtherIDs";

    private static final String XPATH_PERSONAL_RELATIONSHIPS = "./ns:personalRelationship";

    private static final String XPATH_SUBJECTS = "./ns:controlActProcess/ns:subject";

    private static final String XPATH_SUBJECT_REGISTRATION_EVENT = "./ns:registrationEvent[1]";

    private static final String XPATH_QUERY_MATCH_OBSERVATION_VALUE = "./ns:subjectOf1/ns:queryMatchObservation/ns:value[1]";

    /**
     *
     * @param message
     * @return
     * @throws ModelBuilderException
     */
    public Subject buildSubject(PRPA_IN201301UV02_Message message) throws ModelBuilderException {
        return this.buildSubjectFromMessage(message);
    }

    /**
     *
     * @param message
     * @return
     * @throws ModelBuilderException
     */
    public Subject buildSubject(PRPA_IN201302UV02_Message message) throws ModelBuilderException {
        return this.buildSubjectFromMessage(message);
    }

    /**
     *
     * @param message
     * @return
     * @throws ModelBuilderException
     */
    public SubjectSearchResponse buildSubjectSearchResponse(PRPA_IN201306UV02_Message message) throws ModelBuilderException {
        return this.buildSubjectSearchResponse(message, true);
    }

    /**
     *
     * @param message
     * @return
     * @throws ModelBuilderException
     */
    public SubjectSearchResponse buildSubjectSearchResponse(PRPA_IN201310UV02_Message message) throws ModelBuilderException {
        return this.buildSubjectSearchResponse(message, false);
    }

    /**
     * 
     * @param message
     * @return
     * @throws ModelBuilderException
     */
    protected Subject buildSubjectFromMessage(HL7V3Message message) throws ModelBuilderException {
        OMElement patientNode = null;
        OMElement patientPersonNode = null;
        try {
            patientNode = this.selectSingleNode(message.getMessageNode(), XPATH_PATIENT_ADD);
            if (patientNode != null) {
                patientPersonNode = this.selectSingleNode(patientNode, XPATH_PATIENT_PERSON);
            }
        } catch (XPathHelperException e) {
            throw new ModelBuilderException("Patient not found on request: " + e.getMessage());
        }
        if (patientNode == null) {
            throw new ModelBuilderException("Patient not found on request");
        }
        Subject subject = this.getSubject(patientNode, patientPersonNode, null);
        return subject;
    }

    /**
     * 
     * @param message
     * @param getCompleteSubjects
     * @return
     * @throws ModelBuilderException
     */
    private SubjectSearchResponse buildSubjectSearchResponse(HL7V3Message message, boolean getCompleteSubjects) throws ModelBuilderException {
        List<OMElement> subjectNodes = null;
        try {
            subjectNodes = this.selectNodes(message.getMessageNode(), XPATH_SUBJECTS);
        } catch (XPathHelperException e) {
            throw new ModelBuilderException("No subjects found on request: " + e.getMessage());
        }
        if (subjectNodes == null) {
            throw new ModelBuilderException("No subjects found on request");
        }
        List<Subject> subjects = new ArrayList<Subject>();
        for (OMElement subjectNode : subjectNodes) {
            try {
                OMElement registrationEventNode = this.selectSingleNode(subjectNode, XPATH_SUBJECT_REGISTRATION_EVENT);
                OMElement patientNode = this.selectSingleNode(subjectNode, XPATH_PATIENT);
                OMElement patientPersonNode = this.selectSingleNode(patientNode, XPATH_PATIENT_PERSON);
                Subject subject;
                if (getCompleteSubjects == true) {
                    subject = this.getSubject(patientNode, patientPersonNode, registrationEventNode);
                } else {
                    subject = this.getSubjectWithIdentifiersOnly(patientNode, patientPersonNode, registrationEventNode);
                }
                subjects.add(subject);
            } catch (XPathHelperException e) {
                throw new ModelBuilderException("./controlActProcess/subject(s) is malformed: " + e.getMessage());
            }
        }
        SubjectSearchResponse subjectSearchResponse = new SubjectSearchResponse();
        subjectSearchResponse.setSubjects(subjects);
        return subjectSearchResponse;
    }

    /**
     *
     * @param patientNode
     * @param patientPersonNode
     * @param registrationEventNode
     * @return
     */
    private Subject getSubject(OMElement patientNode, OMElement patientPersonNode, OMElement registrationEventNode) {
        Subject subject = new Subject();
        this.setCustodian(subject, registrationEventNode);
        this.setSubjectComponents(subject, patientNode, patientPersonNode);
        return subject;
    }

    /**
     *
     * @param subject
     * @param patientNode
     * @param patientPersonNode
     */
    private void setSubjectComponents(Subject subject, OMElement patientNode, OMElement patientPersonNode) {
        this.setSubjectIdentifiers(subject, patientNode);
        this.setSubjectPersonalRelationships(subject, patientPersonNode);
        this.setSubjectLanguages(subject, patientPersonNode);
        this.setSubjectCitizenships(subject, patientPersonNode);
        this.setGender(subject, patientPersonNode);
        this.setBirthTime(subject, patientPersonNode);
        this.setNames(subject, patientPersonNode);
        this.setTelecomAddresses(subject, patientPersonNode);
        this.setAddresses(subject, patientPersonNode);
        this.setMultipleBirthIndicator(subject, patientPersonNode);
        this.setDeceasedIndicator(subject, patientPersonNode);
        this.setMaritalStatus(subject, patientPersonNode);
        this.setReligiousAffiliation(subject, patientPersonNode);
        this.setRace(subject, patientPersonNode);
        this.setEthnicGroup(subject, patientPersonNode);
        this.setSubjectOtherIdentifiers(subject, patientPersonNode);
        this.setMatchConfidencePercentage(subject, patientNode);
    }

    /**
     *
     * @param patientNode
     * @param patientPersonNode
     * @param registrationEventNode
     * @return
     */
    private Subject getSubjectWithIdentifiersOnly(OMElement patientNode, OMElement patientPersonNode, OMElement registrationEventNode) {
        Subject subject = new Subject();
        this.setSubjectIdentifiers(subject, patientNode);
        this.setSubjectOtherIdentifiers(subject, patientPersonNode);
        this.setCustodian(subject, registrationEventNode);
        return subject;
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setGender(Subject subject, OMElement rootNode) {
        try {
            OMElement genderNode = this.selectSingleNode(rootNode, XPATH_GENDER);
            CodedValue subjectGender = this.buildCodedValue(genderNode);
            subject.setGender(subjectGender);
        } catch (XPathHelperException ex) {
        }
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setMaritalStatus(Subject subject, OMElement rootNode) {
        try {
            OMElement maritalStatusNode = this.selectSingleNode(rootNode, XPATH_MARITAL_STATUS);
            CodedValue maritalStatus = this.buildCodedValue(maritalStatusNode);
            subject.setMaritalStatus(maritalStatus);
        } catch (XPathHelperException ex) {
        }
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setReligiousAffiliation(Subject subject, OMElement rootNode) {
        try {
            OMElement religiousAffiliationNode = this.selectSingleNode(rootNode, XPATH_RELIGIOUS_AFFILIATION);
            CodedValue religiousAffiliation = this.buildCodedValue(religiousAffiliationNode);
            subject.setReligiousAffiliation(religiousAffiliation);
        } catch (XPathHelperException ex) {
        }
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setRace(Subject subject, OMElement rootNode) {
        try {
            OMElement raceNode = this.selectSingleNode(rootNode, XPATH_RACE);
            CodedValue race = this.buildCodedValue(raceNode);
            subject.setRace(race);
        } catch (XPathHelperException ex) {
        }
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setEthnicGroup(Subject subject, OMElement rootNode) {
        try {
            OMElement ethnicGroupNode = this.selectSingleNode(rootNode, XPATH_ETHNIC_GROUP);
            CodedValue ethnicGroup = this.buildCodedValue(ethnicGroupNode);
            subject.setEthnicGroup(ethnicGroup);
        } catch (XPathHelperException ex) {
        }
    }

    /**
     *
     * @param node
     * @return
     */
    public CodedValue buildCodedValue(OMElement node) {
        CodedValue codedValue = null;
        if (node != null) {
            String code = node.getAttributeValue(new QName("code"));
            codedValue = new CodedValue();
            codedValue.setCode(code);
        }
        return codedValue;
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setBirthTime(Subject subject, OMElement rootNode) {
        subject.setBirthTime(this.getHL7DateValue(rootNode, XPATH_BIRTH_TIME));
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setNames(Subject subject, OMElement rootNode) {
        try {
            List<SubjectName> subjectNames = subject.getSubjectNames();
            List<OMElement> subjectNameNodes = this.selectNodes(rootNode, XPATH_NAMES);
            for (OMElement subjectNameNode : subjectNameNodes) {
                SubjectName subjectName = this.buildSubjectName(subjectNameNode);
                subjectNames.add(subjectName);
            }
        } catch (XPathHelperException ex) {
        }
    }

    /**
     *
     * @param rootNode
     * @return
     */
    public SubjectName buildSubjectName(OMElement rootNode) {
        SubjectName subjectName = new SubjectName();
        subjectName.setFamilyName(this.getFirstChildNodeValue(rootNode, "family"));
        subjectName.setPrefix(this.getFirstChildNodeValue(rootNode, "prefix"));
        subjectName.setSuffix(this.getFirstChildNodeValue(rootNode, "suffix"));
        try {
            List<OMElement> givenNames = this.selectNodes(rootNode, "./ns:given");
            if (givenNames != null) {
                int numGivenNames = givenNames.size();
                if (numGivenNames > 0) {
                    subjectName.setGivenName(givenNames.get(0).getText());
                    if (numGivenNames > 1) {
                        subjectName.setMiddleName(givenNames.get(1).getText());
                    }
                }
            }
        } catch (XPathHelperException e) {
        }
        subjectName.nullEmptyFields();
        return subjectName;
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setAddresses(Subject subject, OMElement rootNode) {
        try {
            List<Address> addresses = subject.getAddresses();
            List<OMElement> addressNodes = this.selectNodes(rootNode, XPATH_ADDRESSES);
            for (OMElement addressNode : addressNodes) {
                Address address = this.buildAddress(addressNode);
                addresses.add(address);
            }
        } catch (XPathHelperException ex) {
        }
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setDeceasedIndicator(Subject subject, OMElement rootNode) {
        subject.setDeceasedIndicator(this.getBooleanValue(rootNode, XPATH_DECEASED_INDICATOR));
        subject.setDeceasedTime(this.getHL7DateValue(rootNode, XPATH_DECEASED_TIME));
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setMultipleBirthIndicator(Subject subject, OMElement rootNode) {
        subject.setMultipleBirthIndicator(this.getBooleanValue(rootNode, XPATH_MULTIPLE_BIRTH_INDICATOR));
        subject.setMultipleBirthOrderNumber(this.getIntegerValue(rootNode, XPATH_MULTIPLE_BIRTH_ORDER_NUMBER));
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setTelecomAddresses(Subject subject, OMElement rootNode) {
        try {
            List<TelecomAddress> telecomAddresses = subject.getTelecomAddresses();
            List<OMElement> telecomAddressNodes = this.selectNodes(rootNode, XPATH_TELECOM_ADDRESSES);
            for (OMElement telecomAddressNode : telecomAddressNodes) {
                TelecomAddress telecomAddress = this.buildTelecomAddress(telecomAddressNode);
                telecomAddresses.add(telecomAddress);
            }
        } catch (XPathHelperException ex) {
        }
    }

    /**
     *
     * @param rootNode
     * @return
     */
    public TelecomAddress buildTelecomAddress(OMElement rootNode) {
        TelecomAddress telecomAddress = new TelecomAddress();
        telecomAddress.setUse(rootNode.getAttributeValue(new QName("use")));
        telecomAddress.setValue(rootNode.getAttributeValue(new QName("value")));
        return telecomAddress;
    }

    /**
     *
     * @param rootNode
     * @return
     */
    public Address buildAddress(OMElement rootNode) {
        Address address = new Address();
        address.setUse(rootNode.getAttributeValue(new QName("use")));
        address.setStreetAddressLine1(this.getFirstChildNodeValue(rootNode, "streetAddressLine"));
        address.setCity(this.getFirstChildNodeValue(rootNode, "city"));
        address.setState(this.getFirstChildNodeValue(rootNode, "state"));
        address.setPostalCode(this.getFirstChildNodeValue(rootNode, "postalCode"));
        address.setCountry(this.getFirstChildNodeValue(rootNode, "country"));
        return address;
    }

    /**
     * 
     * @param subject
     * @param rootNode
     */
    protected void setSubjectIdentifiers(Subject subject, OMElement rootNode) {
        List<SubjectIdentifier> subjectIdentifiers = subject.getSubjectIdentifiers();
        Iterator<OMElement> iter = rootNode.getChildrenWithName(new QName("id"));
        while (iter.hasNext()) {
            OMElement idNode = iter.next();
            SubjectIdentifier subjectIdentifier = this.buildSubjectIdentifier(idNode);
            subjectIdentifiers.add(subjectIdentifier);
        }
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setSubjectOtherIdentifiers(Subject subject, OMElement rootNode) {
        List<SubjectIdentifier> subjectOtherIdentifiers = subject.getSubjectOtherIdentifiers();
        try {
            List<OMElement> asOtherIDs = this.selectNodes(rootNode, XPATH_AS_OTHER_IDS);
            for (OMElement asOtherID : asOtherIDs) {
                OMElement idNode = this.getFirstChildNodeWithName(asOtherID, "id");
                SubjectIdentifier subjectIdentifier = this.buildSubjectIdentifier(idNode);
                subjectOtherIdentifiers.add(subjectIdentifier);
            }
        } catch (XPathHelperException ex) {
        }
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setSubjectPersonalRelationships(Subject subject, OMElement rootNode) {
        List<SubjectPersonalRelationship> subjectPersonalRelationships = subject.getSubjectPersonalRelationships();
        try {
            List<OMElement> personalRelationshipNodes = this.selectNodes(rootNode, XPATH_PERSONAL_RELATIONSHIPS);
            for (OMElement personalRelationshipNode : personalRelationshipNodes) {
                SubjectPersonalRelationship subjectPersonalRelationship = this.buildSubjectPersonalRelationship(personalRelationshipNode);
                subjectPersonalRelationships.add(subjectPersonalRelationship);
            }
        } catch (XPathHelperException ex) {
        }
    }

    /**
     *
     * @param rootNode
     * @return
     */
    public SubjectPersonalRelationship buildSubjectPersonalRelationship(OMElement rootNode) {
        SubjectPersonalRelationship subjectPersonalRelationship = new SubjectPersonalRelationship();
        OMElement codeNode = this.getFirstChildNodeWithName(rootNode, "code");
        CodedValue relationshipType = this.buildCodedValue(codeNode);
        subjectPersonalRelationship.setRelationshipType(relationshipType);
        OMElement relationshipHolder1Node = this.getFirstChildNodeWithName(rootNode, "relationshipHolder1");
        Subject relatedSubject = new Subject();
        this.setSubjectComponents(relatedSubject, rootNode, relationshipHolder1Node);
        subjectPersonalRelationship.setSubject(relatedSubject);
        return subjectPersonalRelationship;
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setSubjectLanguages(Subject subject, OMElement rootNode) {
        List<SubjectLanguage> subjectLanguages = subject.getSubjectLanguages();
        try {
            List<OMElement> languageNodes = this.selectNodes(rootNode, XPATH_LANGUAGES);
            for (OMElement languageNode : languageNodes) {
                SubjectLanguage subjectLanguage = this.buildSubjectLanguage(languageNode);
                subjectLanguages.add(subjectLanguage);
            }
        } catch (XPathHelperException ex) {
        }
    }

    /**
     *
     * @param rootNode
     * @return
     */
    public SubjectLanguage buildSubjectLanguage(OMElement rootNode) {
        SubjectLanguage subjectLanguage = new SubjectLanguage();
        OMElement languageCodeNode = this.getFirstChildNodeWithName(rootNode, "languageCode");
        CodedValue languageCode = this.buildCodedValue(languageCodeNode);
        subjectLanguage.setLanguageCode(languageCode);
        subjectLanguage.setPreferenceIndicator(this.getBooleanValue(rootNode, XPATH_LANGUAGE_PREFERENCE_INDICATOR));
        return subjectLanguage;
    }

    /**
     *
     * @param subject
     * @param rootNode
     */
    private void setSubjectCitizenships(Subject subject, OMElement rootNode) {
        List<SubjectCitizenship> subjectCitizenships = subject.getSubjectCitizenships();
        try {
            List<OMElement> citizenshipNodes = this.selectNodes(rootNode, XPATH_CITIZENSHIPS);
            for (OMElement citizenshipNode : citizenshipNodes) {
                SubjectCitizenship subjectCitizenship = this.buildSubjectCitizenship(citizenshipNode);
                subjectCitizenships.add(subjectCitizenship);
            }
        } catch (XPathHelperException ex) {
        }
    }

    /**
     *
     * @param rootNode
     * @return
     */
    public SubjectCitizenship buildSubjectCitizenship(OMElement rootNode) {
        SubjectCitizenship subjectCitizenship = new SubjectCitizenship();
        try {
            OMElement nationCodeNode = this.selectSingleNode(rootNode, XPATH_NATION_CODE);
            CodedValue nationCode = this.buildCodedValue(nationCodeNode);
            subjectCitizenship.setNationCode(nationCode);
            OMElement nationNameNode = this.selectSingleNode(rootNode, XPATH_NATION_NAME);
            if (nationNameNode != null) {
                String nationName = nationNameNode.getText();
                subjectCitizenship.setNationName(nationName);
            }
        } catch (XPathHelperException ex) {
        }
        return subjectCitizenship;
    }

    /**
     * 
     * @param subject
     * @param rootNode
     */
    private void setCustodian(Subject subject, OMElement rootNode) {
        if (rootNode != null) {
            try {
                Custodian custodian = null;
                OMElement idNode = this.selectSingleNode(rootNode, "./ns:custodian/ns:assignedEntity/ns:id[1]");
                if (idNode != null) {
                    String custodianId = idNode.getAttributeValue(new QName("root"));
                    if (custodianId != null) {
                        custodian = new Custodian();
                        custodian.setCustodianId(custodianId);
                        subject.setCustodian(custodian);
                    }
                }
                if (custodian != null) {
                    OMElement codeNode = this.selectSingleNode(rootNode, "./ns:custodian/ns:assignedEntity/ns:code[1]");
                    if (codeNode != null) {
                        String healthLocatorCodeValue = codeNode.getAttributeValue(new QName("code"));
                        if (healthLocatorCodeValue != null) {
                            if (healthLocatorCodeValue.equalsIgnoreCase("SupportsHealthDataLocator")) {
                                custodian.setSupportsHealthDataLocator(true);
                            } else {
                                custodian.setSupportsHealthDataLocator(false);
                            }
                        }
                    }
                }
            } catch (XPathHelperException ex) {
            }
        }
    }

    /**
     * 
     * @param subject
     * @param rootNode
     */
    private void setMatchConfidencePercentage(Subject subject, OMElement rootNode) {
        try {
            OMElement valueNode = this.selectSingleNode(rootNode, XPATH_QUERY_MATCH_OBSERVATION_VALUE);
            if (valueNode != null) {
                String value = valueNode.getAttributeValue(new QName("value"));
                if (value != null) {
                    subject.setMatchConfidencePercentage(new Integer(value));
                }
            }
        } catch (XPathHelperException ex) {
        }
    }

    /**
     *
     * @param rootNode
     * @return
     */
    public SubjectIdentifier buildSubjectIdentifier(OMElement rootNode) {
        SubjectIdentifier subjectIdentifier = new SubjectIdentifier();
        String root = rootNode.getAttributeValue(new QName("root"));
        String extension = rootNode.getAttributeValue(new QName("extension"));
        String assigningAuthorityName = rootNode.getAttributeValue(new QName("assigningAuthorityName"));
        subjectIdentifier.setIdentifier(extension);
        SubjectIdentifierDomain identifierDomain = new SubjectIdentifierDomain();
        identifierDomain.setUniversalId(root);
        identifierDomain.setNamespaceId(assigningAuthorityName);
        identifierDomain.setUniversalIdType("ISO");
        subjectIdentifier.setIdentifierDomain(identifierDomain);
        return subjectIdentifier;
    }
}
