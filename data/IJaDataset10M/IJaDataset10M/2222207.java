package pkg.entity;

import com.googlecode.objectify.Key;
import java.util.Date;
import javax.persistence.Id;
import pkg.dao.EntityDAO;

public class Bid extends ObjectifyEntity {

    @Id
    Long id;

    Key<Auction> auctionKey;

    Key<User> userKey;

    Float amount;

    Date timestamp;

    boolean active;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuctionId() {
        return auctionKey.getId();
    }

    public void setAuctionId(Long auctionId) {
        this.auctionKey = new Key<Auction>(Auction.class, auctionId);
    }

    public Key<Auction> getAuctionKey() {
        return auctionKey;
    }

    public void setAuctionKey(Key<Auction> key) {
        auctionKey = key;
    }

    public Long getUserId() {
        return userKey.getId();
    }

    public void setUserId(Long userId) {
        this.userKey = new Key<User>(User.class, userId);
    }

    public Key<User> getUserKey() {
        return userKey;
    }

    public void setUserKey(Key<User> key) {
        userKey = key;
    }

    public User getUser() {
        User result = null;
        if (userKey != null) {
            EntityDAO dao = new EntityDAO();
            result = dao.find(userKey);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bid other = (Bid) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public Key<ObjectifyEntity> getPrimaryKey() {
        return new Key(this.getClass(), id);
    }
}
