package com.assistsoft.swift.sequence;

import java.util.ArrayList;
import java.util.List;
import com.assistsoft.swift.annotations.ValidQualifiers;
import com.assistsoft.swift.field.SWIFTField_13B;
import com.assistsoft.swift.field.SWIFTField_16R;
import com.assistsoft.swift.field.SWIFTField_16S;
import com.assistsoft.swift.field.SWIFTField_19A;
import com.assistsoft.swift.field.SWIFTField_36B;
import com.assistsoft.swift.field.SWIFTField_70D;
import com.assistsoft.swift.field.SWIFTField_94BCF;
import com.assistsoft.swift.field.SWIFTField_97AB;

public class SWIFTSequence_C_Financial_Instrument_Account extends SWIFTSequence {

    @ValidQualifiers("FIAC")
    private SWIFTField_16R startOfBlock;

    @ValidQualifiers("SETT")
    private SWIFTField_36B quantityOfFinancialInstrumentToBeSettled;

    @ValidQualifiers("SETT")
    private SWIFTField_19A settlementAmount;

    @ValidQualifiers("DENC")
    private SWIFTField_70D denominationChoice;

    @ValidQualifiers("CERT")
    private List<SWIFTField_13B> certificatioNumberList = new ArrayList<SWIFTField_13B>();

    @ValidQualifiers("SAFE CASH")
    private List<SWIFTField_97AB> accountList = new ArrayList<SWIFTField_97AB>();

    @ValidQualifiers("SAFE")
    private SWIFTField_94BCF placeOfSafekeeping;

    @ValidQualifiers("FIAC")
    private SWIFTField_16S endOfBlock;

    public SWIFTSequence_C_Financial_Instrument_Account() {
        super("C", "Financial Instrument/Account");
    }

    public SWIFTField_16R getStartOfBlock() {
        return startOfBlock;
    }

    public void setStartOfBlock(SWIFTField_16R startOfBlock) {
        this.startOfBlock = startOfBlock;
    }

    public SWIFTField_36B getQuantityOfFinancialInstrumentToBeSettled() {
        return quantityOfFinancialInstrumentToBeSettled;
    }

    public void setQuantityOfFinancialInstrumentToBeSettled(SWIFTField_36B quantityOfFinancialInstrumentToBeSettled) {
        this.quantityOfFinancialInstrumentToBeSettled = quantityOfFinancialInstrumentToBeSettled;
    }

    public SWIFTField_19A getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(SWIFTField_19A settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public SWIFTField_70D getDenominationChoice() {
        return denominationChoice;
    }

    public void setDenominationChoice(SWIFTField_70D denominationChoice) {
        this.denominationChoice = denominationChoice;
    }

    public List<SWIFTField_13B> getCertificatioNumberList() {
        return certificatioNumberList;
    }

    public void setCertificatioNumberList(List<SWIFTField_13B> certificatioNumberList) {
        this.certificatioNumberList = certificatioNumberList;
    }

    public void addToCertificatioNumberList(SWIFTField_13B item) {
        certificatioNumberList.add(item);
    }

    public List<SWIFTField_97AB> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<SWIFTField_97AB> accountList) {
        this.accountList = accountList;
    }

    public void addToAccountList(SWIFTField_97AB item) {
        accountList.add(item);
    }

    public SWIFTField_94BCF getPlaceOfSafekeeping() {
        return placeOfSafekeeping;
    }

    public void setPlaceOfSafekeeping(SWIFTField_94BCF placeOfSafekeeping) {
        this.placeOfSafekeeping = placeOfSafekeeping;
    }

    public SWIFTField_16S getEndOfBlock() {
        return endOfBlock;
    }

    public void setEndOfBlock(SWIFTField_16S endOfBlock) {
        this.endOfBlock = endOfBlock;
    }
}
