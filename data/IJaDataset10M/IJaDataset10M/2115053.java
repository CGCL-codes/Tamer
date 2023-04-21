package cn.jsprun.domain;

public class Attachpaymentlog implements java.io.Serializable {

    private static final long serialVersionUID = -169055094667766171L;

    private AttachpaymentlogId id;

    private Integer authorid;

    private Integer dateline;

    private Integer amount;

    private Integer netamount;

    public Attachpaymentlog() {
    }

    public Attachpaymentlog(AttachpaymentlogId id, Integer authorid, Integer dateline, Integer amount, Integer netamount) {
        this.id = id;
        this.authorid = authorid;
        this.dateline = dateline;
        this.amount = amount;
        this.netamount = netamount;
    }

    public AttachpaymentlogId getId() {
        return this.id;
    }

    public void setId(AttachpaymentlogId id) {
        this.id = id;
    }

    public Integer getAuthorid() {
        return this.authorid;
    }

    public void setAuthorid(Integer authorid) {
        this.authorid = authorid;
    }

    public Integer getDateline() {
        return this.dateline;
    }

    public void setDateline(Integer dateline) {
        this.dateline = dateline;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getNetamount() {
        return this.netamount;
    }

    public void setNetamount(Integer netamount) {
        this.netamount = netamount;
    }
}
