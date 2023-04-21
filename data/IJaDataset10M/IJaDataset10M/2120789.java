package com.csft.market.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.ForeignKey;
import com.csft.hibernate.BaseEntity;

@Entity
public class PercentageDistribution implements BaseEntity {

    private static final long serialVersionUID = -8765547806829640940L;

    @Id
    @GeneratedValue
    private Long id;

    private int thousandth;

    private int count;

    @ManyToOne
    @ForeignKey(name = "PD_RESEARCH_FK")
    @JoinColumn(name = "RESEARCH_ID")
    private Research research;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getThousandth() {
        return thousandth;
    }

    public void setThousandth(int thousandth) {
        this.thousandth = thousandth;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Research getResearch() {
        return research;
    }

    public void setResearch(Research research) {
        this.research = research;
    }
}
