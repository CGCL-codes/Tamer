package org.vramework.vow.test.objectmodel;

import java.util.ArrayList;
import java.util.Collection;
import org.vramework.annotations.ContainedClass;

public class Contract {

    private boolean _active = true;

    private String _contractNumber;

    private String _contractDesription;

    private String _contractType;

    private int _customerID;

    private Collection<ContractDetail> details = new ArrayList<ContractDetail>(50);

    private CustomerAdvisor _responsibleAdvisor;

    public Contract() {
    }

    public Contract(String contractNumber, String contractDescription, String contractType, int customerID) {
        setContractNumber(contractNumber);
        setContractDesription(contractDescription);
        setContractType(contractType);
        setCustomerID(customerID);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(64);
        s.append("Contract ");
        s.append(getContractNumber());
        return s.toString();
    }

    public final boolean isActive() {
        return _active;
    }

    /**
   * @param active
   *          The active to set.
   */
    public final void setActive(boolean active) {
        _active = active;
    }

    public String getContractDesription() {
        return _contractDesription;
    }

    public void setContractDesription(String contractDesription) {
        _contractDesription = contractDesription;
    }

    public String getContractNumber() {
        return _contractNumber;
    }

    public final void setContractNumber(String contractNumber) {
        _contractNumber = contractNumber;
    }

    public String getContractType() {
        return _contractType;
    }

    public void setContractType(String contractType) {
        _contractType = contractType;
    }

    @ContainedClass(ContractDetail.class)
    public Collection<ContractDetail> getDetails() {
        return details;
    }

    public void setDetails(Collection<ContractDetail> details) {
        this.details = details;
    }

    public void addDetails(ContractDetail detail) {
        details.add(detail);
    }

    /**
   * @return Returns the responsibleAdvisor.
   */
    public final CustomerAdvisor getResponsibleAdvisor() {
        return _responsibleAdvisor;
    }

    /**
   * @param responsibleAdvisor
   *          The responsibleAdvisor to set.
   */
    public final void setResponsibleAdvisor(CustomerAdvisor responsibleAdvisor) {
        _responsibleAdvisor = responsibleAdvisor;
    }

    public int getCustomerID() {
        return _customerID;
    }

    public final void setCustomerID(int customerID) {
        _customerID = customerID;
    }
}
