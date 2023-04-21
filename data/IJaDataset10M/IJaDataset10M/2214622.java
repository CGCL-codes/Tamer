package org.jostraca.section;

import org.jostraca.util.ErrorUtil;
import java.util.Hashtable;
import java.util.Enumeration;

/** Manage a set of Sections.
 */
public class SectionSet {

    public static final String CN = SectionSet.class.getName();

    private Hashtable iSectionsByName = new Hashtable();

    /** Add a Section. Overwrites existing Section of same name if it exists.
   *  If null, ignore.
   *  @param pSection Section to add
   */
    public void addSection(Section pSection) {
        if (ErrorUtil.not_null(pSection, "pSection")) {
            String name = pSection.getName();
            if (ErrorUtil.not_null(name, "name")) {
                iSectionsByName.put(name, pSection);
            }
        }
    }

    /** Append content to an existing Section. If Section does not exist, create new BasicSection. 
   *  @param pSectionName name of Section to append to or create
   *  @param pContent     content to append to Section
   */
    public void appendToSection(String pSectionName, String pContent) {
        if (hasSection(pSectionName)) {
            Section section = getSection(pSectionName);
            section.append(pContent);
        } else {
            Section newSection = new BasicSection(pSectionName);
            newSection.append(pContent);
            addSection(newSection);
        }
    }

    /** Clear an existing Section. If Section does not exist, create new Section. 
   *  @param pSectionName name of Section to clear
   */
    public void clearSection(String pSectionName) {
        if (hasSection(pSectionName)) {
            Section section = getSection(pSectionName);
            section.clear();
        } else {
            Section newSection = new BasicSection(pSectionName);
            newSection.clear();
            addSection(newSection);
        }
    }

    /** Clear all existing sections. */
    public void clearAllSections() {
        Section section;
        Enumeration sectionEnum = iSectionsByName.elements();
        while (sectionEnum.hasMoreElements()) {
            section = (Section) sectionEnum.nextElement();
            section.clear();
        }
    }

    /** Get section. Return empty BasicSection if unknown.
   *  @param pSectionName name of Section to get
   */
    public Section getSection(String pSectionName) {
        if (iSectionsByName.containsKey(pSectionName)) {
            return (Section) iSectionsByName.get(pSectionName);
        } else {
            return (Section) (new BasicSection(pSectionName));
        }
    }

    /** True if Section exists in set.
   *  @param pSectionName name of Section
   */
    public boolean hasSection(String pSectionName) {
        boolean result = false;
        if (ErrorUtil.not_null(pSectionName, "pSectionName")) {
            result = iSectionsByName.containsKey(pSectionName);
        }
        return result;
    }

    /** True if section content is empty. 
   *  @param pSectionName name of Section
   */
    public boolean isEmptySection(String pSectionName) {
        boolean result = true;
        if (ErrorUtil.not_null(pSectionName, "pSectionName")) {
            if (iSectionsByName.containsKey(pSectionName)) {
                result = ((Section) iSectionsByName.get(pSectionName)).isEmpty();
            }
        }
        return result;
    }

    /** Get an Enumeration of Sections */
    public Enumeration enumerateSections() {
        return iSectionsByName.elements();
    }

    /** Get an Enumeration of Section names */
    public Enumeration enumerateSectionNames() {
        return iSectionsByName.keys();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        Enumeration sections = enumerateSections();
        while (sections.hasMoreElements()) {
            Section section = (Section) sections.nextElement();
            result.append(section.toString());
        }
        return result.toString();
    }

    public boolean equals(Object pObject) {
        if (pObject instanceof SectionSet) {
            SectionSet other = (SectionSet) pObject;
            if (other.iSectionsByName.size() == iSectionsByName.size()) {
                Enumeration sections = enumerateSections();
                while (sections.hasMoreElements()) {
                    Section section = (Section) sections.nextElement();
                    if (!section.equals(other.iSectionsByName.get(section.getName()))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
