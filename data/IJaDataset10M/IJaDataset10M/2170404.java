package com.google.api.ads.dfp.v201201;

public interface PublisherQueryLanguageServiceInterface extends java.rmi.Remote {

    /**
     * Retrieves rows of data that satisfy the given {@link Statement#query}
     * from
     *         the system.
     *         
     *         
     * @param selectStatement a Publisher Query Language statement used to
     * specify what data needs to returned
     *         
     *         
     * @return a result set of data that matches the given filter
     */
    public com.google.api.ads.dfp.v201201.ResultSet select(com.google.api.ads.dfp.v201201.Statement selectStatement) throws java.rmi.RemoteException, com.google.api.ads.dfp.v201201.ApiException;
}
