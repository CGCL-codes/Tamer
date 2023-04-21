package com.google.api.adwords.v13;

public interface TrafficEstimatorInterface extends java.rmi.Remote {

    public com.google.api.adwords.v13.KeywordTraffic[] checkKeywordTraffic(com.google.api.adwords.v13.KeywordTrafficRequest[] requests) throws java.rmi.RemoteException, com.google.api.adwords.v13.ApiException;

    public com.google.api.adwords.v13.AdGroupEstimate[] estimateAdGroupList(com.google.api.adwords.v13.AdGroupRequest[] adGroupRequests) throws java.rmi.RemoteException, com.google.api.adwords.v13.ApiException;

    public com.google.api.adwords.v13.CampaignEstimate[] estimateCampaignList(com.google.api.adwords.v13.CampaignRequest[] campaignRequests) throws java.rmi.RemoteException, com.google.api.adwords.v13.ApiException;

    public com.google.api.adwords.v13.KeywordEstimate[] estimateKeywordList(com.google.api.adwords.v13.KeywordRequest[] keywordRequests) throws java.rmi.RemoteException, com.google.api.adwords.v13.ApiException;
}
