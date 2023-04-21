package org.dicom4j.dicom.dictionary;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import org.dicom4j.dicom.DicomTag;
import org.dicom4j.dicom.dictionary.item.SOPClassDictionaryItem;
import org.dicom4j.dicom.dictionary.item.TransferSyntaxDictionaryItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract {@link DicomDictionary} implements
 * 
 * @since 0.0.0
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public abstract class AbstractDictionary implements DicomDictionary {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDictionary.class);

    private DictionaryElements fSOPClasses;

    private LinkedList<?> modules;

    private TreeMap<String, SOPClassDictionaryItem> sopClasses = new TreeMap<String, SOPClassDictionaryItem>();

    private TreeMap<DicomTag, DicomTag> tags;

    private TreeMap<String, TransferSyntaxDictionaryItem> transferSyntaxItems = new TreeMap<String, TransferSyntaxDictionaryItem>();

    /**
	 * Creates new instance
	 * 
	 */
    public AbstractDictionary() {
        super();
        this.tags = new TreeMap<DicomTag, DicomTag>();
        this.fSOPClasses = new DictionaryElements();
        this.modules = new LinkedList<Object>();
    }

    public SOPClassDictionaryItem addSOPClassItem(SOPClassDictionaryItem sopClassItem) {
        this.sopClasses.put(sopClassItem.getKey(), sopClassItem);
        return sopClassItem;
    }

    public DicomTag addTag(DicomTag aTag) {
        this.tags.put(aTag, aTag);
        return aTag;
    }

    public TransferSyntaxDictionaryItem addTransferSyntaxtem(TransferSyntaxDictionaryItem item) {
        this.transferSyntaxItems.put(item.getKey(), item);
        return item;
    }

    protected LinkedList getModules() {
        return this.modules;
    }

    public Iterator getModulesIterator() {
        return this.getModules().iterator();
    }

    public String getNameFromTag(DicomTag aDicomTag) {
        DicomTag lElement = this.getTag(aDicomTag);
        if (lElement != null) {
            logger.info("null");
            return lElement.getName();
        } else {
            logger.info("unkown: " + aDicomTag.getElement());
            if (aDicomTag.isPrivate()) {
                return "Private Tag";
            } else if (aDicomTag.getElement() == 0x0000) {
                return "Group length";
            } else {
                logger.info("unkow");
                return "Unknown Tag";
            }
        }
    }

    public DictionarySopClass getSopClass(String aUID) {
        return (DictionarySopClass) this.fSOPClasses.get(aUID);
    }

    public Iterator<SOPClassDictionaryItem> getSOPClassesIterator() {
        return this.sopClasses.values().iterator();
    }

    public SOPClassDictionaryItem getSopClassItem(String sopClassUID) {
        logger.debug("getSopClassItem: " + sopClassUID);
        return this.sopClasses.get(sopClassUID);
    }

    public DicomTag getTag(DicomTag dicomTag) {
        assert this.tags != null;
        logger.debug("getTag: " + dicomTag);
        return this.tags.get(dicomTag);
    }

    public DicomTag getTag(int aGroup, int aElement) {
        return this.tags.get(new DicomTag(aGroup, aElement));
    }

    public Iterator<DictionaryTag> getTagIterator() {
        return this.getTags().values().iterator();
    }

    protected TreeMap getTags() {
        return this.tags;
    }

    public TransferSyntaxDictionaryItem getTransferSyntaxDictionaryItem(String key) {
        return this.transferSyntaxItems.get(key);
    }

    public Iterator<TransferSyntaxDictionaryItem> getTransferSyntaxesIterator() {
        return this.transferSyntaxItems.values().iterator();
    }

    public boolean hasTag(DicomTag aDicomTag) {
        return this.getTag(aDicomTag) != null;
    }
}
