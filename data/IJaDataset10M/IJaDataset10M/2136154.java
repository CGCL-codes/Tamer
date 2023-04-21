package info.gdeDengi;

import info.gdeDengi.common.IModuleTransaction;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "Expense")
public class Expense implements IModuleTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expense_id")
    @SequenceGenerator(name = "expense_id", sequenceName = "expense_id")
    @Column(name = "EXPENSETRANSACTIONID")
    private long id;

    @Column(name = "USERID")
    private String UserId;

    @Column(name = "DESCRIPTION")
    private String Descr;

    @Column(name = "CURRENCYID")
    private String CurrencyId;

    @Column(name = "TRANSDATE")
    private Date TransDate;

    @Column(name = "AMOUNTCUR")
    private Float AmountCur;

    @Column(name = "AMOUNTMST")
    private Float AmountMst;

    public Expense() {
    }

    ;

    public Expense(String _UserId, String _CurrencyId, Date _TransDate, String _Descr, Float _AmountCur, Float _AmountMst) {
        this.UserId = _UserId;
        this.CurrencyId = _CurrencyId;
        this.TransDate = _TransDate;
        this.AmountCur = _AmountCur;
        this.AmountMst = _AmountMst;
        this.Descr = _Descr;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCurrencyId() {
        return CurrencyId;
    }

    public void setCurrencyId(String currencyId) {
        CurrencyId = currencyId;
    }

    public Date getTransDate() {
        return TransDate;
    }

    public void setTransDate(Date transDate) {
        TransDate = transDate;
    }

    public String getDescr() {
        return Descr;
    }

    public void setDescr(String descr) {
        Descr = descr;
    }

    public Float getAmountCur() {
        return AmountCur;
    }

    public void setAmountCur(Float amountCur) {
        AmountCur = amountCur;
    }

    public Float getAmountMst() {
        return AmountMst;
    }

    public void setAmountMst(Float amountMst) {
        AmountMst = amountMst;
    }
}
