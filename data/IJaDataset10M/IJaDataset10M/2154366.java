package eu.annocultor.tagger.terms;

import java.util.Properties;
import eu.annocultor.common.Language.Lang;

/**
 * Terms stored in vocabularies.
 * 
 * @author Borys Omelayenko
 * 
 */
public class Term implements Comparable<Term> {

    private static final String SLASH = "/";

    private String label;

    private String ns;

    private String code;

    private Term parent;

    private String vocabularyName;

    private Lang lang;

    private Properties properties;

    private String disambiguatingComment;

    private String confidenceComment;

    public Term(String label, Lang lang, CodeURI termCode, String vocabularyName) {
        if (label == null || termCode == null || vocabularyName == null) {
            throw new NullPointerException("One of parameters is null");
        }
        this.label = label;
        this.lang = lang;
        this.code = termCode.toString();
        int p = code.lastIndexOf(SLASH);
        if (p > 0 && p < code.length()) {
            this.ns = code.substring(0, p + 1);
            this.code = code.substring(p + 1);
        } else {
            this.ns = null;
        }
        this.vocabularyName = vocabularyName;
        this.properties = new Properties();
    }

    public String getCode() {
        if (ns == null) return code;
        return ns + code;
    }

    public String getVocabularyName() {
        return vocabularyName;
    }

    public String getLabel() {
        return label;
    }

    public Lang getLang() {
        return lang;
    }

    public Term getParent() {
        return parent;
    }

    public void setParent(Term parent) {
        this.parent = parent;
    }

    public boolean hasParent(CodeURI potentialParentCode) {
        String potentialParentStr = potentialParentCode.toString();
        Term term = this.parent;
        while (term != null) {
            if (potentialParentStr.equals(term.getCode())) return true;
            term = term.parent;
        }
        return false;
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public void setProperty(String propertyName, String value) {
        properties.setProperty(propertyName, value);
    }

    @Override
    public String toString() {
        return "\"" + getCode() + "=" + label + (lang == null ? "" : ("@" + lang)) + "\"";
    }

    @Override
    public int compareTo(Term t) {
        int r;
        r = this.getVocabularyName().compareTo(t.getVocabularyName());
        if (r != 0) return r;
        r = this.getCode().compareTo(t.getCode());
        if (r != 0) return r;
        r = this.getLabel().compareTo(t.getLabel());
        if (r != 0) return r;
        if (this.getLang() == null && t.getLang() != null) return -1;
        if (this.getLang() != null && t.getLang() == null) return 1;
        if (this.getLang() == null && t.getLang() == null) return 0;
        r = this.getLang().compareTo(t.getLang());
        return r;
    }

    public String getDisambiguatingComment() {
        return disambiguatingComment;
    }

    public void setDisambiguatingComment(String disambiguatingComment) {
        this.disambiguatingComment = disambiguatingComment;
    }

    public String getConfidenceComment() {
        return confidenceComment;
    }

    public void setConfidenceComment(String confidenceComment) {
        this.confidenceComment = confidenceComment;
    }
}
