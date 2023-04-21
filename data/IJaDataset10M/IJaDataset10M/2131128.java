package com.amazon.ws;

public interface AWSECommerceServicePortType extends java.rmi.Remote {

    public com.amazon.ws.HelpResponse help(com.amazon.ws.Help body) throws java.rmi.RemoteException;

    public com.amazon.ws.ItemSearchResponse itemSearch(com.amazon.ws.ItemSearch body) throws java.rmi.RemoteException;

    public com.amazon.ws.ItemLookupResponse itemLookup(com.amazon.ws.ItemLookup body) throws java.rmi.RemoteException;

    public com.amazon.ws.BrowseNodeLookupResponse browseNodeLookup(com.amazon.ws.BrowseNodeLookup body) throws java.rmi.RemoteException;

    public com.amazon.ws.ListSearchResponse listSearch(com.amazon.ws.ListSearch body) throws java.rmi.RemoteException;

    public com.amazon.ws.ListLookupResponse listLookup(com.amazon.ws.ListLookup body) throws java.rmi.RemoteException;

    public com.amazon.ws.CustomerContentSearchResponse customerContentSearch(com.amazon.ws.CustomerContentSearch body) throws java.rmi.RemoteException;

    public com.amazon.ws.CustomerContentLookupResponse customerContentLookup(com.amazon.ws.CustomerContentLookup body) throws java.rmi.RemoteException;

    public com.amazon.ws.SimilarityLookupResponse similarityLookup(com.amazon.ws.SimilarityLookup body) throws java.rmi.RemoteException;

    public com.amazon.ws.SellerLookupResponse sellerLookup(com.amazon.ws.SellerLookup body) throws java.rmi.RemoteException;

    public com.amazon.ws.CartGetResponse cartGet(com.amazon.ws.CartGet body) throws java.rmi.RemoteException;

    public com.amazon.ws.CartAddResponse cartAdd(com.amazon.ws.CartAdd body) throws java.rmi.RemoteException;

    public com.amazon.ws.CartCreateResponse cartCreate(com.amazon.ws.CartCreate body) throws java.rmi.RemoteException;

    public com.amazon.ws.CartModifyResponse cartModify(com.amazon.ws.CartModify body) throws java.rmi.RemoteException;

    public com.amazon.ws.CartClearResponse cartClear(com.amazon.ws.CartClear body) throws java.rmi.RemoteException;

    public com.amazon.ws.TransactionLookupResponse transactionLookup(com.amazon.ws.TransactionLookup body) throws java.rmi.RemoteException;

    public com.amazon.ws.SellerListingSearchResponse sellerListingSearch(com.amazon.ws.SellerListingSearch body) throws java.rmi.RemoteException;

    public com.amazon.ws.SellerListingLookupResponse sellerListingLookup(com.amazon.ws.SellerListingLookup body) throws java.rmi.RemoteException;

    public com.amazon.ws.MultiOperationResponse multiOperation(com.amazon.ws.MultiOperation body) throws java.rmi.RemoteException;
}
