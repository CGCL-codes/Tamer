package steveshrader.budget.domain;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import steveshrader.budget.server.BudgetService;
import steveshrader.budget.shared.BudgetHelper;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Expense {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @NotNull
    private Date date;

    @NotNull
    private Long amount;

    @NotNull
    private String vendor;

    @NotNull
    private String expenseType;

    @NotNull
    private String paymentType;

    @NotNull
    @DecimalMin("0")
    private Integer version = 0;

    public Expense() {
    }

    public void persist() {
        BudgetService.addExpense(this);
    }

    public static Expense findExpense(Long id) {
        return BudgetService.findExpense(id);
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getAmount() {
        return amount;
    }

    public String getDisplayAmount() {
        return BudgetHelper.convertAmount(amount);
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setDisplayAmount(String amount) {
        this.amount = BudgetHelper.convertAmount(amount);
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
