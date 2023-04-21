package pt.igeo.snig.mig.editor.record.identification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pt.igeo.snig.mig.editor.constants.Constants;
import pt.igeo.snig.mig.editor.list.FixedList;
import pt.igeo.snig.mig.editor.list.ListValueManager;
import pt.igeo.snig.mig.editor.record.Record;
import pt.igeo.snig.mig.editor.record.xPathHelper;
import pt.igeo.snig.mig.editor.record.contact.Contact;
import pt.igeo.snig.mig.editor.record.metadataOnMetadata.MetadataOnMetadata;
import pt.igeo.snig.mig.editor.utils.PrivateAccessor;
import pt.igeo.snig.mig.undoManager.UndoElement;
import pt.igeo.snig.mig.undoManager.UndoListener;
import pt.igeo.snig.mig.undoManager.UndoManager;

/**
 * Representation of the identification info.
 * 
 * @author Ant�nio Silva, Jos� Pedro Dias
 * @version $Revision: 11271 $
 * @since 1.0
 */
public abstract class IdentificationInfo implements Serializable, UndoListener {

    /** java requirement */
    private static final long serialVersionUID = 7503591371513240291L;

    /** xpath helper */
    private xPathHelper xHelp = xPathHelper.getInstance();

    /** citation */
    protected Citation citation = null;

    /** abstract */
    protected HashMap<String, String> iAbstract = new HashMap<String, String>();

    /** purpose */
    protected HashMap<String, String> purpose = new HashMap<String, String>();

    /** credits */
    protected Collection<Credit> credits = new ArrayList<Credit>();

    /** point of contact */
    protected Collection<Contact> pointsOfContact = new ArrayList<Contact>();

    /** descriptive keywords */
    protected Collection<KeywordGroup> descKeys = new ArrayList<KeywordGroup>();

    /** descriptive mandatory keyword */
    protected String keyWord = "";

    /** metadata data */
    private MetadataOnMetadata metadata = null;

    /** extents */
    Collection<Extent> extents = new ArrayList<Extent>();

    /** default language to use in internationalization fields*/
    private String internalLanguage = Constants.defaultListLanguage;

    /**
	 * constructor
	 * @param ident 
	 * @param metadata 
	 */
    public void identificationInfoRead(Node ident, MetadataOnMetadata metadata) {
        Node reader = xHelp.readPath(ident, "citation");
        if (reader != null) {
            setCitation(new Citation(reader));
        }
        setMetadata(metadata);
        setInternalLanguage(metadata.getLanguage());
        String foreignLanguage = ListValueManager.getInstance().getForeignLanguage(metadata.getLanguage().getCodeValue());
        reader = xHelp.readPath(ident, "abstract");
        setIAbstract(xHelp.readCharacterString(reader));
        if (metadata.getMultiLanguage()) {
            Node trans = xHelp.readPath(reader, "PT_FreeText/textGroup/LocalisedCharacterString");
            if (trans != null) {
                iAbstract.put(foreignLanguage, trans.getTextContent());
            } else {
                iAbstract.put(foreignLanguage, "");
            }
        }
        reader = xHelp.readPath(ident, "purpose");
        setPurpose(xHelp.readCharacterString(reader));
        if (metadata.getMultiLanguage()) {
            Node trans = xHelp.readPath(reader, "PT_FreeText/textGroup/LocalisedCharacterString");
            if (trans != null) {
                purpose.put(foreignLanguage, trans.getTextContent());
            } else {
                purpose.put(foreignLanguage, "");
            }
        }
        NodeList list = (NodeList) xHelp.read(ident, "credit", XPathConstants.NODESET);
        ArrayList<Credit> credits = (ArrayList<Credit>) getCredits();
        for (int i = 0; i < list.getLength(); i++) {
            Node credit = list.item(i);
            credits.add(new Credit(xHelp.readCharacterString(credit)));
        }
        ArrayList<Contact> contacts = (ArrayList<Contact>) getPointsOfContact();
        list = (NodeList) xHelp.read(ident, "pointOfContact", XPathConstants.NODESET);
        for (int i = 0; i < list.getLength(); i++) {
            Node contact = list.item(i);
            contacts.add(new Contact(contact));
        }
        ArrayList<KeywordGroup> keywords = (ArrayList<KeywordGroup>) getDescKeys();
        list = (NodeList) xHelp.read(ident, "descriptiveKeywords", XPathConstants.NODESET);
        for (int i = 0; i < list.getLength(); i++) {
            Node keyword = list.item(i);
            keywords.add(new KeywordGroup(keyword, metadata));
        }
        validateMandatoryFields(keywords);
        ArrayList<Extent> extents = (ArrayList<Extent>) getExtents();
        list = (NodeList) xHelp.read(ident, "extent", XPathConstants.NODESET);
        for (int i = 0; i < list.getLength(); i++) {
            Node extent = list.item(i);
            extents.add(new Extent(extent));
        }
    }

    /***
	 * 
	 * Validate the keywords
	 * 
	 * If no mandatory keyword created creates
	 * If is a CGD and no thesaurus node created insert one   
	 * 
	 * @param keywords list of keyword groups
	 */
    private void validateMandatoryFields(ArrayList<KeywordGroup> keywords) {
        int mandatroyKeywords = 0;
        int thesaurus = 0;
        for (KeywordGroup keywordGroup : keywords) {
            if (keywordGroup.getKeywordType() == null) {
                mandatroyKeywords += keywordGroup.getKeywords().size();
                if (keywordGroup.getThesaurus() != null) {
                    thesaurus += 1;
                }
            }
        }
        if (mandatroyKeywords == 0) {
            KeywordGroup kg = new KeywordGroup(metadata);
            kg.setKeywordType(null);
            keywords.add(kg);
            Keyword kw = new Keyword();
            kw.setMandatoryKeyword(true);
            kw.setServiceKeyword(getMetadata().isService());
            kg.getKeywords().add(kw);
        }
        if (thesaurus == 0 && metadata.getIsCDG()) {
            keywords.get(0).setThesaurus(new Thesaurus());
        }
    }

    /**
	 * get this resource constraint's dom tree representation
	 * 
	 * @param document
	 * @param ident 
	 */
    public void updateDomTree(Document document, Node ident) {
        Node writer = null;
        if (getCitation() != null) {
            writer = xHelp.writePath(ident, "gmd:citation", true);
            writer.appendChild(getCitation().getCI_Citation(document));
        }
        String otherLanguage = detectMultiLanguage();
        if (otherLanguage != null) {
            String motherLanguage = ListValueManager.getInstance().getCurrentLanguage();
            writer = xHelp.writePath(ident, "gmd:abstract", true);
            Element element = (Element) writer;
            element.setAttribute("xsi:type", "gmd:PT_FreeText_PropertyType");
            xHelp.writeCharacterString(writer, iAbstract.get(motherLanguage));
            String locale = ListValueManager.getInstance().getLocaleLanguage(otherLanguage);
            Node temp = xHelp.writePath(writer, "gmd:PT_FreeText/gmd:textGroup/gmd:LocalisedCharacterString", true);
            element = (Element) temp;
            element.setAttribute("locale", locale);
            element.setTextContent(iAbstract.get(otherLanguage));
            writer = xHelp.writePath(ident, "gmd:purpose", true);
            element = (Element) writer;
            element.setAttribute("xsi:type", "gmd:PT_FreeText_PropertyType");
            xHelp.writeCharacterString(writer, purpose.get(motherLanguage));
            temp = xHelp.writePath(writer, "gmd:PT_FreeText/gmd:textGroup/gmd:LocalisedCharacterString", true);
            element = (Element) temp;
            element.setAttribute("locale", locale);
            element.setTextContent(purpose.get(otherLanguage));
        } else {
            writer = xHelp.writePath(ident, "gmd:abstract", true);
            xHelp.writeCharacterString(writer, getIAbstract());
            writer = xHelp.writePath(ident, "gmd:purpose", true);
            xHelp.writeCharacterString(writer, getPurpose());
        }
        for (Credit credit : getCredits()) {
            writer = xHelp.writePath(ident, "gmd:credit", true);
            xHelp.writeCharacterString(writer, credit.getCredit());
        }
        for (Contact contact : getPointsOfContact()) {
            writer = xHelp.writePath(ident, "gmd:pointOfContact", true);
            writer.appendChild(contact.getResponsibleParty(document));
        }
    }

    /**
	 * Detects if the abstract and purpose have multi language contents
	 * @return the non-native language, or null when there is no i18n for these fields
	 */
    private String detectMultiLanguage() {
        String language = ListValueManager.getInstance().getCurrentLanguage();
        String testLanguage;
        if (language.equals(Constants.defaultListLanguage)) {
            testLanguage = "eng";
        } else {
            testLanguage = Constants.defaultListLanguage;
        }
        if (iAbstract.get(testLanguage) != null) {
            if ((iAbstract.get(testLanguage).length() > 0) || (purpose.get(testLanguage).length() > 0)) {
                return testLanguage;
            }
        }
        return null;
    }

    /**
	 * @return the citation
	 */
    public Citation getCitation() {
        return citation;
    }

    /**
	 * @param citation the citation to set
	 */
    public void setCitation(Citation citation) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.citation, "citation"));
        this.citation = citation;
    }

    /**
	 * @return the credits
	 */
    public Collection<Credit> getCredits() {
        return credits;
    }

    /**
	 * @param credits the credits to set
	 */
    public void setCredits(Collection<Credit> credits) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.credits, "credits"));
        this.credits = credits;
    }

    /**
	 * @return the descKeys
	 */
    public Collection<KeywordGroup> getDescKeys() {
        return descKeys;
    }

    /**
	 * @param descKeys the descKeys to set
	 */
    public void setDescKeys(Collection<KeywordGroup> descKeys) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.descKeys, "descKeys"));
        this.descKeys = descKeys;
    }

    /**
	 * @return the iAbstract
	 */
    public String getIAbstract() {
        return iAbstract.get(internalLanguage);
    }

    /**
	 * @param abstract1 the iAbstract to set
	 */
    public void setIAbstract(String abstract1) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, abstract1, "iAbstract"));
        iAbstract.put(internalLanguage, abstract1);
    }

    /**
	 * @return the pointOfContact
	 */
    public Collection<Contact> getPointsOfContact() {
        return pointsOfContact;
    }

    /**
	 * @param pointsOfContact the pointOfContact to set
	 */
    public void setPointsOfContact(Collection<Contact> pointsOfContact) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.pointsOfContact, "pointsOfContact"));
        this.pointsOfContact = pointsOfContact;
    }

    /**
	 * @return the purpose
	 */
    public String getPurpose() {
        return purpose.get(internalLanguage);
    }

    /**
	 * @param purpose the purpose to set
	 */
    public void setPurpose(String purpose) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, purpose, "purpose"));
        this.purpose.put(internalLanguage, purpose);
    }

    /**
	 * @return the extents
	 */
    public Collection<Extent> getExtents() {
        return extents;
    }

    /**
	 * @param extents the extents to set
	 */
    public void setExtents(Collection<Extent> extents) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.extents, "extents"));
        this.extents = extents;
    }

    /**
	 * Sets the internal language to use
	 * @param language
	 */
    public void setInternalLanguage(FixedList fllanguage) {
        String language = fllanguage.getCodeValue();
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.internalLanguage, "internalLanguage"));
        this.internalLanguage = language;
        if (iAbstract.get(language) == null) {
            iAbstract.put(language, "");
        }
        if (purpose.get(language) == null) {
            purpose.put(language, "");
        }
    }

    public FixedList getInternalLanguage() {
        return ListValueManager.getInstance().getFixedListItem(Constants.languageCodeList, internalLanguage);
    }

    /**
	 * @return the metadata
	 */
    public MetadataOnMetadata getMetadata() {
        return metadata;
    }

    /**
	 * @param metadata the metadata to set
	 */
    public void setMetadata(MetadataOnMetadata metadata) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.metadata, "metadata"));
        this.metadata = metadata;
    }

    @Override
    public Object undo(Object olderObject, String fieldName, Record record) {
        Object ret;
        if (fieldName.equals("purpose")) {
            ret = this.purpose.get(internalLanguage);
            this.purpose.put(internalLanguage, (String) olderObject);
        } else if (fieldName.equals("iAbstract")) {
            ret = this.iAbstract.get(internalLanguage);
            iAbstract.put(internalLanguage, (String) olderObject);
        } else {
            ret = PrivateAccessor.getPrivateField(this, fieldName);
            PrivateAccessor.setPrivateField(this, olderObject, fieldName);
        }
        return ret;
    }
}
