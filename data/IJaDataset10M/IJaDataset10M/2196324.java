package com.pixelmed.dicom;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.Iterator;
import org.dicom4j.dicom.DicomTag;

/**
 * <p>The {@link com.pixelmed.dicom.DicomDictionaryBase DicomDictionaryBase} class
 * is an abstract class for creating and accessing a dictionary of DICOM
 * attributes and associated information.</p>
 *
 * <p>Defines methods for creating a dictionary of DICOM
 * attributes and associated information, and implements methods for accessing
 * that information.</p>
 * {@deprecated}
 * @author	dclunie
 */
public abstract class DicomDictionaryBase {

    private static final String identString = "@(#) $Header: /userland/cvs/pixelmed/imgbook/com/pixelmed/dicom/DicomDictionaryBase.java,v 1.4 2003/10/12 14:29:01 dclunie Exp $";

    protected TreeSet tagList;

    protected HashMap valueRepresentationsByTag;

    protected HashMap informationEntityByTag;

    protected HashMap nameByTag;

    protected HashMap tagByName;

    /**
	 * <p>Concrete sub-classes implement this method to create a list of all tags in the dictionary.</p>
	 */
    protected abstract void createTagList();

    /**
	 * <p>Concrete sub-classes implement this method to create a map of value representations for each tag in the dictionary.</p>
	 */
    protected abstract void createValueRepresentationsByTag();

    /**
	 * <p>Concrete sub-classes implement this method to create a map of information entities for each tag in the dictionary.</p>
	 */
    protected abstract void createInformationEntityByTag();

    /**
	 * <p>Concrete sub-classes implement this method to create a map of tags from attribute names for each tag in the dictionary.</p>
	 */
    protected abstract void createTagByName();

    /**
	 * <p>Concrete sub-classes implement this method to create a map of attribute names from tags for each tag in the dictionary.</p>
	 */
    protected abstract void createNameByTag();

    /**
	 * <p>Instantiate a dictionary by calling all create methods of the concrete sub-class.</p>
	 */
    public DicomDictionaryBase() {
        createTagList();
        createValueRepresentationsByTag();
        createInformationEntityByTag();
        createNameByTag();
        createTagByName();
    }

    /**
	 * <p>Get the value representation of an attribute.</p>
	 *
	 * @param	tag	the tag of the attribute
	 * @return		the value representation of the attribute as an array of two bytes
	 */
    public byte[] getValueRepresentationFromTag(DicomTag tag) {
        byte[] vr = (byte[]) valueRepresentationsByTag.get(tag);
        return vr;
    }

    /**
	 * <p>Get the information entity (patient, study, and so on) of an attribute.</p>
	 *
	 * @param	tag	the tag of the attribute
	 * @return		the information entity of the attribute
	 */
    public InformationEntity getInformationEntityFromTag(DicomTag tag) {
        return (InformationEntity) informationEntityByTag.get(tag);
    }

    /**
	 * <p>Get the tag of an attribute from its string name.</p>
	 *
	 * <p>Though the DICOM standard does not formally define names to be used as
	 * keys for attributes, the convention used here is to use the name from
	 * the PS 3.6 Name field and remove spaces, apostrophes, capitalize first
	 * letters of words and so on to come up with a unique name for each
	 * attribute.</p>
	 *
	 * @param	name	the string name of the attribute
	 * @return		the tag of the attribute
	 */
    public DicomTag getTagFromName(String name) {
        return (DicomTag) tagByName.get(name);
    }

    /**
	 * <p>Get the string name of an attribute from its tag.</p>
	 *
	 * @see #getTagFromName(String)
	 *
	 * @param	tag	the tag of the attribute
	 * @return		the string name of the attribute
	 */
    public String getNameFromTag(DicomTag tag) {
        return (String) nameByTag.get(tag);
    }

    /**
	 * <p>Get an {@link java.util.Iterator Iterator} to iterate through every tag in the dictionary.</p>
	 *
	 * <p>The order in which the dictionary attributes are returned is by ascending tag value.</p>
	 *
	 * @see org.dicom4j.dicom.DicomTag#compareTo(Object)
	 *
	 * @return		an iterator
	 */
    public Iterator getTagIterator() {
        return tagList.iterator();
    }

    /**
	 * <p>Unit test.</p>
	 *
	 * @param	arg	ignored
	 */
    public static void main(String arg[]) {
        try {
            DicomDictionaryBase dictionary = new DicomDictionary();
            System.err.println(new String(dictionary.getValueRepresentationFromTag(TagFromName.PixelRepresentation)));
            System.err.println(new String(dictionary.getValueRepresentationFromTag(new DicomTag(0x0028, 0x0103))));
            System.err.println(dictionary.getInformationEntityFromTag(TagFromName.PatientName));
            System.err.println(dictionary.getInformationEntityFromTag(TagFromName.StudyDate));
            System.err.println(dictionary.getInformationEntityFromTag(TagFromName.PixelRepresentation));
            System.err.println(dictionary.getNameFromTag(TagFromName.PatientName));
            System.err.println(dictionary.getNameFromTag(TagFromName.StudyDate));
            System.err.println(dictionary.getNameFromTag(TagFromName.PixelRepresentation));
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace(System.err);
            System.exit(0);
        }
    }
}
