package com.yubuild.coreman.web.action.command;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.builder.*;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.yubuild.coreman.common.util.DateUtil;
import com.yubuild.coreman.data.BaseObject;

public class LuceneSearchDocumentData extends BaseObject implements Serializable, Validator {

    private static final long serialVersionUID = 1L;

    private String relatedRefsKeywordsSearch;

    private String additionalRefsKeywordsSearch;

    private String dateFromKeywordsSearch;

    private String dateToKeywordsSearch;

    private String bodyKeywordsSearch;

    private String keywordsKeywordsSearch;

    private String subjectKeywordsSearch;

    private String subsetId;

    private String identifierKeywordSearch;

    private String identifierFromKeywordSearch;

    private String identifierToKeywordSearch;

    private String lotKeywordSearch;

    private String memoKeywordSearch;

    private String senderKeywordSearch;

    private String addReferenceKeywordSearch;

    protected Map refFields = new TreeMap();

    private String documentType;

    public LuceneSearchDocumentData() {
    }

    public String getBodyKeywordsSearch() {
        return bodyKeywordsSearch;
    }

    public void setBodyKeywordsSearch(String bodyKeywordsSearch) {
        this.bodyKeywordsSearch = bodyKeywordsSearch;
    }

    public String getDateFromKeywordsSearch() {
        return dateFromKeywordsSearch;
    }

    public void setDateFromKeywordsSearch(String dateFromKeywordsSearch) {
        this.dateFromKeywordsSearch = dateFromKeywordsSearch;
    }

    public String getDateToKeywordsSearch() {
        return dateToKeywordsSearch;
    }

    public void setDateToKeywordsSearch(String dateToKeywordsSearch) {
        this.dateToKeywordsSearch = dateToKeywordsSearch;
    }

    public String getKeywordsKeywordsSearch() {
        return keywordsKeywordsSearch;
    }

    public void setKeywordsKeywordsSearch(String keywordsKeywordsSearch) {
        this.keywordsKeywordsSearch = keywordsKeywordsSearch;
    }

    public boolean supports(Class clazz) {
        return com.yubuild.coreman.web.action.command.LuceneSearchDocumentData.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        LuceneSearchDocumentData sd = (LuceneSearchDocumentData) obj;
        if (sd.getDateFromKeywordsSearch() != null || sd.getDateFromKeywordsSearch().length() > 0) try {
            DateUtil.getLuceneDateFormat(sd.getDateFromKeywordsSearch());
        } catch (ParseException ie) {
            errors.rejectValue("dateFromKeywordsSearch", "errors.dateFormat", new String[] { DateUtil.getDatePattern() }, null);
        }
        if (sd.getDateToKeywordsSearch() != null || sd.getDateToKeywordsSearch().length() > 0) try {
            DateUtil.getLuceneDateFormat(sd.getDateToKeywordsSearch());
        } catch (ParseException ie) {
            errors.rejectValue("dateToKeywordsSearch", "errors.dateFormat", new String[] { DateUtil.getDatePattern() }, null);
        }
        if ((sd.getBodyKeywordsSearch() != null || sd.getBodyKeywordsSearch().length() > 0) && (sd.getBodyKeywordsSearch().trim().startsWith("*") || sd.getBodyKeywordsSearch().trim().startsWith("?"))) errors.rejectValue("bodyKeywordsSearch", "errors.luceneAst");
        if ((sd.getKeywordsKeywordsSearch() != null || sd.getKeywordsKeywordsSearch().length() > 0) && (sd.getKeywordsKeywordsSearch().trim().startsWith("*") || sd.getKeywordsKeywordsSearch().trim().startsWith("?"))) errors.rejectValue("keywordsKeywordsSearch", "errors.luceneAst");
        if ((sd.getSubjectKeywordsSearch() != null || sd.getSubjectKeywordsSearch().length() > 0) && (sd.getSubjectKeywordsSearch().trim().startsWith("*") || sd.getSubjectKeywordsSearch().trim().startsWith("?"))) errors.rejectValue("subjectKeywordSearch", "errors.luceneAst");
        if ((sd.getIdentifierKeywordSearch() != null || sd.getIdentifierKeywordSearch().length() > 0) && (sd.getIdentifierKeywordSearch().trim().startsWith("*") || sd.getIdentifierKeywordSearch().trim().startsWith("?"))) errors.rejectValue("relatedRefsKeywordsSearch", "errors.luceneAst");
        if ((sd.getAdditionalRefsKeywordsSearch() != null || sd.getAdditionalRefsKeywordsSearch().length() > 0) && (sd.getAdditionalRefsKeywordsSearch().trim().startsWith("*") || sd.getAdditionalRefsKeywordsSearch().trim().startsWith("?"))) errors.rejectValue("additionalRefsKeywordsSearch", "errors.luceneAst");
        if ((sd.getIdentifierToKeywordSearch() != null || sd.getIdentifierToKeywordSearch().length() > 0) && (sd.getIdentifierToKeywordSearch().trim().startsWith("*") || sd.getIdentifierToKeywordSearch().trim().startsWith("?"))) errors.rejectValue("relatedRefsKeywordsSearch", "errors.luceneAst");
        if ((sd.getIdentifierFromKeywordSearch() != null || sd.getIdentifierFromKeywordSearch().length() > 0) && (sd.getIdentifierFromKeywordSearch().trim().startsWith("*") || sd.getIdentifierFromKeywordSearch().trim().startsWith("?"))) errors.rejectValue("relatedRefsKeywordsSearch", "errors.luceneAst");
    }

    public String getSubjectKeywordsSearch() {
        return subjectKeywordsSearch;
    }

    public void setSubjectKeywordsSearch(String subjectKeywordSearch) {
        subjectKeywordsSearch = subjectKeywordSearch;
    }

    public String getAdditionalRefsKeywordsSearch() {
        return additionalRefsKeywordsSearch;
    }

    public void setAdditionalRefsKeywordsSearch(String additionalRefsKeywordsSearch) {
        this.additionalRefsKeywordsSearch = additionalRefsKeywordsSearch;
    }

    public String getRelatedRefsKeywordsSearch() {
        return relatedRefsKeywordsSearch;
    }

    public void setRelatedRefsKeywordsSearch(String relatedRefsKeywordsSearch) {
        this.relatedRefsKeywordsSearch = relatedRefsKeywordsSearch;
    }

    public String getAddReferenceKeywordSearch() {
        return addReferenceKeywordSearch;
    }

    public void setAddReferenceKeywordSearch(String addReferenceKeywordSearch) {
        this.addReferenceKeywordSearch = addReferenceKeywordSearch;
    }

    public String getIdentifierKeywordSearch() {
        return identifierKeywordSearch;
    }

    public void setIdentifierKeywordSearch(String identifierKeywordSearch) {
        this.identifierKeywordSearch = identifierKeywordSearch;
    }

    public String getLotKeywordSearch() {
        return lotKeywordSearch;
    }

    public void setLotKeywordSearch(String lotKeywordSearch) {
        this.lotKeywordSearch = lotKeywordSearch;
    }

    public String getMemoKeywordSearch() {
        return memoKeywordSearch;
    }

    public void setMemoKeywordSearch(String memoKeywordSearch) {
        this.memoKeywordSearch = memoKeywordSearch;
    }

    public String getSenderKeywordSearch() {
        return senderKeywordSearch;
    }

    public void setSenderKeywordSearch(String senderKeywordSearch) {
        this.senderKeywordSearch = senderKeywordSearch;
    }

    public String getIdentifierFromKeywordSearch() {
        return identifierFromKeywordSearch;
    }

    public void setIdentifierFromKeywordSearch(String identifierFromKeywordSearch) {
        this.identifierFromKeywordSearch = identifierFromKeywordSearch;
    }

    public String getIdentifierToKeywordSearch() {
        return identifierToKeywordSearch;
    }

    public void setIdentifierToKeywordSearch(String identifierToKeywordSearch) {
        this.identifierToKeywordSearch = identifierToKeywordSearch;
    }

    public boolean equals(Object object) {
        if (!(object instanceof LuceneSearchDocumentData)) {
            return false;
        } else {
            LuceneSearchDocumentData rhs = (LuceneSearchDocumentData) object;
            return (new EqualsBuilder()).append(subjectKeywordsSearch, rhs.subjectKeywordsSearch).append(identifierKeywordSearch, rhs.identifierKeywordSearch).append(identifierFromKeywordSearch, rhs.identifierFromKeywordSearch).append(dateToKeywordsSearch, rhs.dateToKeywordsSearch).append(keywordsKeywordsSearch, rhs.keywordsKeywordsSearch).append(dateFromKeywordsSearch, rhs.dateFromKeywordsSearch).append(additionalRefsKeywordsSearch, rhs.additionalRefsKeywordsSearch).append(identifierToKeywordSearch, rhs.identifierToKeywordSearch).append(relatedRefsKeywordsSearch, rhs.relatedRefsKeywordsSearch).append(bodyKeywordsSearch, rhs.bodyKeywordsSearch).isEquals();
        }
    }

    public int hashCode() {
        return (new HashCodeBuilder(0x2bc27893, 0x2398947b)).append(subjectKeywordsSearch).append(identifierKeywordSearch).append(identifierFromKeywordSearch).append(dateToKeywordsSearch).append(keywordsKeywordsSearch).append(dateFromKeywordsSearch).append(additionalRefsKeywordsSearch).append(identifierToKeywordSearch).append(relatedRefsKeywordsSearch).append(bodyKeywordsSearch).toHashCode();
    }

    public String toString() {
        return (new ToStringBuilder(this)).append("relatedRefsKeywordsSearch", relatedRefsKeywordsSearch).append("dateToKeywordsSearch", dateToKeywordsSearch).append("bodyKeywordsSearch", bodyKeywordsSearch).append("identifierToKeywordSearch", identifierToKeywordSearch).append("identifierKeywordSearch", identifierKeywordSearch).append("dateFromKeywordsSearch", dateFromKeywordsSearch).append("keywordsKeywordsSearch", keywordsKeywordsSearch).append("subjectKeywordsSearch", subjectKeywordsSearch).append("identifierFromKeywordSearch", identifierFromKeywordSearch).toString();
    }

    public String getSubsetId() {
        return subsetId;
    }

    public void setSubsetId(String subsetId) {
        this.subsetId = subsetId;
    }

    public Map getRefFields() {
        return refFields;
    }

    public void setRefFields(Map refFields) {
        this.refFields = refFields;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
}
