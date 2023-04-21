package com.i3sp.address;

public interface Importer {

    public void setSink(AddressSink sink);

    public void setProvenance(String prov);

    public void run() throws Exception;
}
