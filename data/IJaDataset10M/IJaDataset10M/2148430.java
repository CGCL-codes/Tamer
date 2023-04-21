package com.assistsoft.swift.sequence;

import java.util.ArrayList;
import java.util.List;
import com.assistsoft.swift.annotations.ValidQualifiers;
import com.assistsoft.swift.field.SWIFTField_16R;
import com.assistsoft.swift.field.SWIFTField_16S;
import com.assistsoft.swift.field.SWIFTField_20C;
import com.assistsoft.swift.field.SWIFTField_70CDE;
import com.assistsoft.swift.field.SWIFTField_95CPQRS;
import com.assistsoft.swift.field.SWIFTField_97A;

public class SWIFTSequence_F_Other_Parties extends SWIFTSequence {

    @ValidQualifiers("OTHRPRTY")
    private SWIFTField_16R startOfBlock;

    @ValidQualifiers("EXCH MEOR MERE TRRE INVE VEND TRAG ALTE")
    private List<SWIFTField_95CPQRS> partyList = new ArrayList<SWIFTField_95CPQRS>();

    @ValidQualifiers("CASH SAFE")
    private List<SWIFTField_97A> accountList = new ArrayList<SWIFTField_97A>();

    @ValidQualifiers("DECL REGI PACO PART")
    private List<SWIFTField_70CDE> narrativeList = new ArrayList<SWIFTField_70CDE>();

    @ValidQualifiers("PROC")
    private SWIFTField_20C processingReference;

    @ValidQualifiers("OTHRPRTY")
    private SWIFTField_16S endOfBlock;

    public SWIFTSequence_F_Other_Parties() {
        super("F", "Other Parties");
    }

    public SWIFTField_16R getStartOfBlock() {
        return startOfBlock;
    }

    public void setStartOfBlock(SWIFTField_16R startOfBlock) {
        this.startOfBlock = startOfBlock;
    }

    public List<SWIFTField_95CPQRS> getPartyList() {
        return partyList;
    }

    public void setPartyList(List<SWIFTField_95CPQRS> partyList) {
        this.partyList = partyList;
    }

    public void addToPartyList(SWIFTField_95CPQRS item) {
        partyList.add(item);
    }

    public List<SWIFTField_97A> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<SWIFTField_97A> accountList) {
        this.accountList = accountList;
    }

    public void addToAccountList(SWIFTField_97A item) {
        accountList.add(item);
    }

    public List<SWIFTField_70CDE> getNarrativeList() {
        return narrativeList;
    }

    public void setNarrativeList(List<SWIFTField_70CDE> narrativeList) {
        this.narrativeList = narrativeList;
    }

    public void addToNarrativeList(SWIFTField_70CDE item) {
        narrativeList.add(item);
    }

    public SWIFTField_20C getProcessingReference() {
        return processingReference;
    }

    public void setProcessingReference(SWIFTField_20C processingReference) {
        this.processingReference = processingReference;
    }

    public SWIFTField_16S getEndOfBlock() {
        return endOfBlock;
    }

    public void setEndOfBlock(SWIFTField_16S endOfBlock) {
        this.endOfBlock = endOfBlock;
    }
}
