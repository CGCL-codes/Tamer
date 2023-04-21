package org.dozer.vo.context;

import java.util.List;

/**
 * @author garsombke.franz
 * @author sullins.ben
 * @author tierney.matt
 * 
 */
public class ContextMappingPrime {

    private String loanNo;

    private List contextList;

    public List getContextList() {
        return contextList;
    }

    public void setContextList(List contextList) {
        this.contextList = contextList;
    }

    public String getLoanNo() {
        return loanNo;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }
}
