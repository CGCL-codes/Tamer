package com.google.api.adwords.v200909.cm;

public interface CampaignAdExtensionServiceInterface extends java.rmi.Remote {

    /**
     * Returns a list of {@link CampaignAdExtension}s based on a
     *         {@link CampaignAdExtensionSelector}.
     *         
     * @param selector The selector specifying the query.
     *         
     * @return The page containing the {@link CampaignAdExtension}s which
     * meet the
     *         criteria specified by the selector.
     */
    public com.google.api.adwords.v200909.cm.CampaignAdExtensionPage get(com.google.api.adwords.v200909.cm.CampaignAdExtensionSelector selector) throws java.rmi.RemoteException, com.google.api.adwords.v200909.cm.ApiException;

    /**
     * Applies the list of mutate operations.
     *         
     * @param operations The operations to apply. The same {@link CampaignAdExtension}
     * cannot be specified in more than one operation.
     *         
     * @return The applied {@link CampaignAdExtension}s. The {@link Operator#SET}
     * is not supported.
     */
    public com.google.api.adwords.v200909.cm.CampaignAdExtensionReturnValue mutate(com.google.api.adwords.v200909.cm.CampaignAdExtensionOperation[] operations) throws java.rmi.RemoteException, com.google.api.adwords.v200909.cm.ApiException;
}
