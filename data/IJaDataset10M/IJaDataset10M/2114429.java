package com.ideature.loanapp.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.ideature.loanapp.domain.Borrower;
import com.ideature.loanapp.domain.FundingRequest;
import com.ideature.loanapp.domain.Loan;

@XmlRootElement(name = "funding-request")
@XmlAccessorType(XmlAccessType.FIELD)
public class FundingRequestJaxbDTO {

    @XmlElements({ @XmlElement(name = "loan-info", type = Loan.class), @XmlElement(name = "borrower", type = Borrower.class), @XmlElement(name = "funding-info", type = FundingRequest.class) })
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
