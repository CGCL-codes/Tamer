package org.dicom4j.dicom.dictionary.item;

import org.dicom4j.dicom.dictionary.DicomDictionary;

/**
 * {@link DicomDictionary}'s item 
 * 
 * @since 0.0.0
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public interface DictionaryItem {

    /**
	 * return the Element's Key
	 * 
	 * @return the key
	 */
    public String getKey();

    /**
	 * return the Element's Name
	 * 
	 * @return the name
	 */
    public String getName();

    /**
	 * return the Element's Type
	 * 
	 * @return the type
	 */
    public String getType();
}
