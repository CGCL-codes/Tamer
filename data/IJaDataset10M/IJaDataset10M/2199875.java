package com.csc.at.services.health.simplebank.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author wschreiner
 *
 */
public class CustomerDetailsInputDTO extends AbstractInputDTO {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5495814070808262676L;

    private Long customerId = null;

    /**
	 * @return the customerId
	 */
    public Long getCustomerId() {
        return this.customerId;
    }

    /**
	 * @param customerId the customerId to set
	 */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(getCustomerId()).toString();
    }
}
