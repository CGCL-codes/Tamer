package org.tripcom.dam.backend.auctionapi;

public interface AuctionAPI extends java.rmi.Remote {

    public java.lang.String publishAuction(org.tripcom.dam.frontend.Auction publishAuctionRequest) throws java.rmi.RemoteException;

    public java.lang.String openAuction(org.tripcom.dam.frontend.Auction openAuctionRequest) throws java.rmi.RemoteException;

    public java.lang.String closeAuction(org.tripcom.dam.frontend.Auction closeAuctionRequest) throws java.rmi.RemoteException;

    public java.lang.String subscribeToAuction(org.tripcom.dam.backend.auctionapi.AuctionSubscription subscribeToAuctionRequest) throws java.rmi.RemoteException;

    public org.tripcom.dam.frontend.Bid[] getCurrentBids(org.tripcom.dam.frontend.Auction getCurrentBidsRequest) throws java.rmi.RemoteException;

    public java.lang.String refuseBid(org.tripcom.dam.frontend.Bid refuseBidRequest) throws java.rmi.RemoteException;

    public org.tripcom.dam.frontend.Auction[] getActiveAuction(java.lang.String getActiveAuctionRequest) throws java.rmi.RemoteException;

    public java.lang.String publishBid(org.tripcom.dam.frontend.Bid publishBidRequest) throws java.rmi.RemoteException;

    public org.tripcom.dam.frontend.Auction[] getAuctionsSubscribed(org.tripcom.dam.frontend.Actor getAuctionsSubscribedRequest) throws java.rmi.RemoteException;

    public org.tripcom.dam.frontend.AuctionMessage[] getAuctionHistory(org.tripcom.dam.frontend.Auction getAuctionHistoryRequest) throws java.rmi.RemoteException;
}
