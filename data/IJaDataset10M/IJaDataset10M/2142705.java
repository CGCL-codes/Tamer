package org.demoiselle.sample.auction5.view.managedbean;

import java.util.List;
import org.demoiselle.sample.auction5.bean.Auction;
import org.demoiselle.sample.auction5.business.IAuctionBC;
import br.gov.framework.demoiselle.core.layer.integration.Injection;
import br.gov.framework.demoiselle.view.faces.controller.AbstractManagedBean;

/**
 * @author CETEC/CTJEE
 * @see AbstractManagedBean
 */
public class HomeMB extends AbstractManagedBean {

    @Injection
    private IAuctionBC auctionBC;

    private List<Auction> listNewest;

    private List<Auction> listMostOffered;

    private List<Auction> listEndingSoon;

    private List<Auction> listCheapestPrice;

    public List<Auction> getListNewest() {
        if (listNewest == null) {
            listNewest = auctionBC.listNewestAuctions();
            refreshAuctionsList(listNewest);
        }
        return listNewest;
    }

    public void setListNewest(List<Auction> listNewest) {
        this.listNewest = listNewest;
    }

    public List<Auction> getListMostOffered() {
        if (listMostOffered == null) {
            listMostOffered = auctionBC.listMostOfferedAuctions();
            refreshAuctionsList(listMostOffered);
        }
        return listMostOffered;
    }

    public void setListMostOffered(List<Auction> listMostOffered) {
        this.listMostOffered = listMostOffered;
    }

    public List<Auction> getListEndingSoon() {
        if (listEndingSoon == null) {
            listEndingSoon = auctionBC.listEndingSoonAuctions();
            refreshAuctionsList(listEndingSoon);
        }
        return listEndingSoon;
    }

    public void setListEndingSoon(List<Auction> listEndingSoon) {
        this.listEndingSoon = listEndingSoon;
    }

    public List<Auction> getListCheapestPrice() {
        if (listCheapestPrice == null) {
            listCheapestPrice = auctionBC.listCheapestPriceAuctions();
            refreshAuctionsList(listCheapestPrice);
        }
        return listCheapestPrice;
    }

    public void setListCheapestPrice(List<Auction> listCheapestPrice) {
        this.listCheapestPrice = listCheapestPrice;
    }

    private void refreshAuctionsList(List<Auction> listAuction) {
        if (listAuction != null && !listAuction.isEmpty()) {
            for (Auction auction : listAuction) {
                auction.getBids().size();
            }
        }
    }
}
