package com.idna.gav.exceptions;

/**
 * Thrown when CustomerType is not one amongst the supported types.
 * 
 * @author vinay.nayak
 * 
 */
public class CustomerListingTypeNotSupportedException extends VoltServiceException {

    private static final long serialVersionUID = 1L;

    public CustomerListingTypeNotSupportedException() {
        super("Customer Listing Type is not supported");
    }

    public CustomerListingTypeNotSupportedException(Throwable cause) {
        super(cause);
    }

    public CustomerListingTypeNotSupportedException(String internalErrorMessage) {
        super(internalErrorMessage);
    }

    public CustomerListingTypeNotSupportedException(String internalErrorMessage, Throwable t) {
        super(internalErrorMessage, t);
    }

    public CustomerListingTypeNotSupportedException(String gavErrorCode, String internalErrorMessage) {
        super(gavErrorCode, internalErrorMessage);
    }

    public CustomerListingTypeNotSupportedException(String gavErrorCode, String internalErrorMessage, Throwable t) {
        super(gavErrorCode, internalErrorMessage, t);
    }
}
