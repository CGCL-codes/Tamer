package net.mufly.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import net.sf.gilead.pojo.java5.LightEntity;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Transaction extends LightEntity implements Serializable {

    private static final long serialVersionUID = -4794001131832566810L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @ManyToOne(optional = true)
    protected Transaction parent;

    @Column(nullable = false)
    protected Boolean hasChilds;

    @Column(nullable = false)
    protected String transactionDesc;

    @Column(nullable = false)
    protected TransactionType transactionType;

    @Column(nullable = false)
    protected Double transactionAmount;

    @Column(nullable = false)
    protected Date transactionDate;

    @Column(nullable = true)
    protected String transactionComment;

    @ManyToOne(optional = false)
    protected Account account;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    protected Set<Tag> tags = new HashSet<Tag>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction getParent() {
        return parent;
    }

    public void setParent(Transaction parent) {
        this.parent = parent;
    }

    public Boolean getHasChilds() {
        return hasChilds;
    }

    public void setHasChilds(Boolean hasChilds) {
        this.hasChilds = hasChilds;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }

    public void setTransactionDesc(String transactionDesc) {
        this.transactionDesc = transactionDesc;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionComment() {
        return transactionComment;
    }

    public void setTransactionComment(String transactionComment) {
        this.transactionComment = transactionComment;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}
