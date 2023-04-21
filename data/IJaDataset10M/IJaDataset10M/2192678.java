package iwallet.common.account;

import iwallet.common.account.util.AccountVisitor;
import iwallet.common.account.util.VisitorException;
import iwallet.common.currency.Currency;
import java.util.Date;

/**
 * @author 黄源河
 *
 */
public abstract class BaseAccountEntry extends BaseAccountObject implements AccountEntry {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Date date;

    private String desc;

    private Currency money;

    private transient AccountBill owner = null;

    protected BaseAccountEntry(String desc) {
        this("", -1, -1, new Date(), desc, null);
    }

    /**
     * Constructor
     * 
     * @param owner the owner of this account entry
     * @param date the date of this this account entry
     * @param desc the description of this account entry
     * @param money the money of this account entry
     */
    protected BaseAccountEntry(String username, int bookid, int billid, Date date, String desc, Currency money) {
        super(username, bookid, billid, 0);
        this.date = date;
        this.desc = desc;
        this.money = money;
    }

    protected BaseAccountEntry(AccountBill owner, Date date, String desc, Currency money) {
        this(owner.getUserName(), owner.getBookID(), owner.getBillID(), date, desc, money);
        setOwner(owner);
    }

    @Override
    public void setID(int id) {
        super.entryid = id;
    }

    @Override
    public void setOwner(AccountBill owner) {
        super.username = owner.getUserName();
        super.bookid = owner.getBookID();
        super.billid = owner.getBillID();
        this.owner = owner;
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    @Override
    public String getDescription() {
        return this.desc;
    }

    @Override
    public Currency getMoney() {
        return this.money;
    }

    @Override
    public void accept(AccountVisitor visitor) throws VisitorException {
        visitor.visit(this);
    }

    @Override
    public String getTypeName() {
        return "账目";
    }

    @Override
    public void loadInfoFrom(AccountObject obj) {
        super.updateModifyDate();
        AccountEntry entry = (AccountEntry) obj;
        this.date = entry.getDate();
        this.desc = entry.getDescription();
        this.money = entry.getMoney();
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public void setDescription(String desc) {
        this.desc = desc;
    }

    @Override
    public void setMoney(Currency money) {
        this.money = money;
    }

    @Override
    public AccountBill getOwner() {
        return this.owner;
    }

    @Override
    public void setLeaves(Object obj) {
    }
}
