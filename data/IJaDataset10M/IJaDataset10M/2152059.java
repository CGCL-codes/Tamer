package org.xeustechnologies.googleapi.spelling;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author Kamran Zafar
 * 
 */
@XmlRootElement(name = "spellrequest")
public class SpellRequest {

    protected Boolean textAlreadyClipped = false;

    protected Boolean ignoreDuplicates = false;

    protected Boolean ignoreWordsWithDigits = true;

    protected Boolean ignoreWordsWithAllCaps = true;

    protected String text;

    public SpellRequest() {
    }

    public SpellRequest(String text) {
        this.text = text;
    }

    @XmlElement(name = "text", required = true)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @XmlAttribute(name = "textalreadyclipped")
    @XmlJavaTypeAdapter(BooleanAdaptor.class)
    public Boolean isTextAlreadyClipped() {
        return textAlreadyClipped;
    }

    @XmlAttribute(name = "ignoredups")
    @XmlJavaTypeAdapter(BooleanAdaptor.class)
    public Boolean isIgnoreDuplicates() {
        return ignoreDuplicates;
    }

    @XmlAttribute(name = "ignoredigits")
    @XmlJavaTypeAdapter(BooleanAdaptor.class)
    public Boolean isIgnoreWordsWithDigits() {
        return ignoreWordsWithDigits;
    }

    @XmlAttribute(name = "ignoreallcaps")
    @XmlJavaTypeAdapter(BooleanAdaptor.class)
    public Boolean isIgnoreWordsWithAllCaps() {
        return ignoreWordsWithAllCaps;
    }

    public void setTextAlreadyClipped(Boolean textAlreadyClipped) {
        this.textAlreadyClipped = textAlreadyClipped;
    }

    public void setIgnoreDuplicates(Boolean ignoreDuplicates) {
        this.ignoreDuplicates = ignoreDuplicates;
    }

    public void setIgnoreWordsWithDigits(Boolean ignoreWordsWithDigits) {
        this.ignoreWordsWithDigits = ignoreWordsWithDigits;
    }

    public void setIgnoreWordsWithAllCaps(Boolean ignoreWordsWithAllCaps) {
        this.ignoreWordsWithAllCaps = ignoreWordsWithAllCaps;
    }
}
