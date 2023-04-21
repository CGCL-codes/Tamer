package com.makeabyte.jhosting.common.dns;

public interface DNSRecord {

    public String getRecordType();

    public void validate() throws ValidationException;
}
