package org.plazmaforge.bsolution.finance.common.beans;

import org.plazmaforge.bsolution.document.common.beans.DocumentType;
import org.plazmaforge.framework.core.data.Dictionary;

/**
 * @author Oleh Hapon
 * 
 * $Id: FinanceDocumentType.java,v 1.2 2010/04/28 06:24:25 ohapon Exp $
 */
public class FinanceDocumentType extends Dictionary {

    private DocumentType documentType;

    private String moveType;

    private boolean bothTaxAmount;

    private boolean includeTaxInAmount;

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Integer getDocumentTypeId() {
        return documentType == null ? null : documentType.getId();
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getCode() {
        return documentType == null ? null : documentType.getCode();
    }

    public String getName() {
        return documentType == null ? null : documentType.getName();
    }

    public boolean isBothTaxAmount() {
        return bothTaxAmount;
    }

    public void setBothTaxAmount(boolean bothTaxAmount) {
        this.bothTaxAmount = bothTaxAmount;
    }

    public boolean isIncludeTaxInAmount() {
        return includeTaxInAmount;
    }

    public void setIncludeTaxInAmount(boolean includeTaxInAmount) {
        this.includeTaxInAmount = includeTaxInAmount;
    }
}
