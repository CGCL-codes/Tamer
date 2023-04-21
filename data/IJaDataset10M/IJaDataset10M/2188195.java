package org.shopformat.domain.order.sales;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.shopformat.domain.shared.DomainObject;

@Entity
public class CreditCard implements DomainObject<Long> {

    private Long id;

    private CreditCardType cardType;

    private String name;

    private String number;

    private String expiry;

    private String start;

    private String cv2;

    private String issue;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public CreditCardType getCardType() {
        return cardType;
    }

    public void setCardType(CreditCardType cardType) {
        this.cardType = cardType;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(nullable = false)
    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getCv2() {
        return cv2;
    }

    public void setCv2(String cv2) {
        this.cv2 = cv2;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}
