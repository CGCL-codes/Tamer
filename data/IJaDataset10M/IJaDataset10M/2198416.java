package com.javaeye.lonlysky.lforum.entity.forum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 辩论帖子
 * 
 * @author 寂寞地铁
 *
 */
@Entity
@Table(name = "debates")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Debates implements java.io.Serializable {

    private static final long serialVersionUID = -3858662427799222315L;

    private Integer tid;

    private Topics topics;

    private String positiveopinion;

    private String negativeopinion;

    private String terminaltime;

    private Integer positivediggs;

    private Integer negativediggs;

    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "increment")
    @Column(name = "tid", unique = true, nullable = false, insertable = true, updatable = true)
    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    @ManyToOne(cascade = {  }, fetch = FetchType.LAZY)
    @JoinColumn(name = "tid", unique = true, nullable = false, insertable = false, updatable = false)
    public Topics getTopics() {
        return topics;
    }

    public void setTopics(Topics topics) {
        this.topics = topics;
    }

    @Column(name = "positiveopinion", unique = false, nullable = false, insertable = true, updatable = true, length = 200)
    public String getPositiveopinion() {
        return positiveopinion;
    }

    public void setPositiveopinion(String positiveopinion) {
        this.positiveopinion = positiveopinion;
    }

    @Column(name = "negativeopinion", unique = false, nullable = false, insertable = true, updatable = true, length = 200)
    public String getNegativeopinion() {
        return negativeopinion;
    }

    public void setNegativeopinion(String negativeopinion) {
        this.negativeopinion = negativeopinion;
    }

    @Column(name = "terminaltime", unique = false, nullable = false, insertable = true, updatable = true, length = 23)
    public String getTerminaltime() {
        return terminaltime;
    }

    public void setTerminaltime(String terminaltime) {
        this.terminaltime = terminaltime;
    }

    @Column(name = "positivediggs", unique = false, nullable = false, insertable = true, updatable = true)
    public Integer getPositivediggs() {
        return positivediggs;
    }

    public void setPositivediggs(Integer positivediggs) {
        this.positivediggs = positivediggs;
    }

    @Column(name = "negativediggs", unique = false, nullable = false, insertable = true, updatable = true)
    public Integer getNegativediggs() {
        return negativediggs;
    }

    public void setNegativediggs(Integer negativediggs) {
        this.negativediggs = negativediggs;
    }
}
