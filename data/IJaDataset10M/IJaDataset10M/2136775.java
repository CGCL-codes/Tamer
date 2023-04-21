package org.demoiselle.sample.auction5.bean;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import br.gov.component.demoiselle.common.pojo.extension.Description;
import br.gov.component.demoiselle.common.pojo.extension.EqualsField;
import br.gov.component.demoiselle.common.pojo.extension.IPojoExtension;
import br.gov.framework.demoiselle.core.bean.IPojo;

/**
 * @author CETEC/CTJEE
 * @see IPojo
 */
@Entity
@Table(name = "auctions")
@SequenceGenerator(name = "AuctionSequence", sequenceName = "auctions_seq")
@NamedQuery(name = "openAuctionsByItem", query = "select a from Auction a where a.status = :status and a.deadline > :deadline and a.item = :item")
@NamedNativeQuery(name = "newestAuctions", query = "select * from auctions where status = ?1 order by creation desc limit ?2", resultClass = Auction.class)
public class Auction implements IPojoExtension {

    private static final long serialVersionUID = 111L;

    @Id
    @GeneratedValue(generator = "AuctionSequence", strategy = GenerationType.SEQUENCE)
    @EqualsField
    @Description
    private Long id;

    @ManyToOne
    @Description
    @JoinColumn(nullable = false)
    private Item item;

    @Enumerated
    @Description
    @Column(nullable = false)
    private Mode mode;

    @Enumerated
    @Column(nullable = false)
    private Status status;

    private Double sellingPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date creation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;

    private Double startingPrice;

    private Double reservePrice;

    @ManyToOne(optional = true)
    private Bid bestBid;

    @OneToMany(mappedBy = "auction")
    private Set<Bid> bids;

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(Double reservePrice) {
        this.reservePrice = reservePrice;
    }

    public Bid getBestBid() {
        return bestBid;
    }

    public void setBestBid(Bid bestBid) {
        this.bestBid = bestBid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    /**
	 * Returns the starting price or the best bid value for the auction.
	 * 
	 * @return	a Double
	 */
    public Double getBestBidOrStartingPrice() {
        if (this.bestBid != null) return this.bestBid.getAmount(); else return this.startingPrice;
    }

    /**
	 * Retrieves the number of bids in a fancy style.
	 * 
	 * @return	a String
	 */
    public String getNumberBidsFormatted() {
        String result = null;
        if (this.bids.size() == 1) result = "1 bid"; else if (this.bids.size() > 1) result = this.bids.size() + " bids"; else result = "";
        return result;
    }

    /**
	 * Returns true whether regular selling (i.e. "buy now") is possible.
	 * 
	 * @return boolean
	 */
    public boolean isBuy() {
        return Mode.SELLING.equals(mode) || Mode.BOTH.equals(mode);
    }

    /**
	 * Returns true whether auction is possible.
	 * 
	 * @return boolean
	 */
    public boolean isAuction() {
        return Mode.AUCTION.equals(mode) || Mode.BOTH.equals(mode);
    }

    public String toLog() {
        return toString();
    }
}
