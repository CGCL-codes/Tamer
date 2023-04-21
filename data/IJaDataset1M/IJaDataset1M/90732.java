package com.mattharrah.gedcom4j.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for source citation data.
 * 
 * @author frizbog1
 */
public class CitationData {

    /**
     * The date of the entry
     */
    public String entryDate;

    /**
     * The source text - one or more lines of it
     */
    public List<List<String>> sourceText = new ArrayList<List<String>>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CitationData other = (CitationData) obj;
        if (entryDate == null) {
            if (other.entryDate != null) {
                return false;
            }
        } else if (!entryDate.equals(other.entryDate)) {
            return false;
        }
        if (sourceText == null) {
            if (other.sourceText != null) {
                return false;
            }
        } else if (!sourceText.equals(other.sourceText)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entryDate == null) ? 0 : entryDate.hashCode());
        result = prime * result + ((sourceText == null) ? 0 : sourceText.hashCode());
        return result;
    }
}
