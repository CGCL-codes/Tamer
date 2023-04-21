package org.nakedobjects.example.expenses.claims;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.applib.annotation.Optional;
import org.nakedobjects.applib.value.Date;
import org.nakedobjects.example.expenses.currency.Currency;
import org.nakedobjects.example.expenses.employee.Employee;
import org.nakedobjects.example.expenses.services.UserFinder;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractClaim extends AbstractDomainObject {

    public String title() {
        return getDescription();
    }

    private ClaimRepository claimRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected ClaimRepository getClaimRepository() {
        return this.claimRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setClaimRepository(final ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    private ClaimFactory claimFactory;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected ClaimFactory getClaimFactory() {
        return this.claimFactory;
    }

    /**
     * Injected by the application container.
     */
    public void setClaimFactory(final ClaimFactory claimFactory) {
        this.claimFactory = claimFactory;
    }

    private UserFinder userFinder;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected UserFinder getUserFinder() {
        return this.userFinder;
    }

    /**
     * Injected by the application container.
     */
    public void setUserFinder(final UserFinder userFinder) {
        this.userFinder = userFinder;
    }

    @Hidden
    public abstract String modifiable();

    private String description;

    @MemberOrder(sequence = "1")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void modifyDescription(final String description) {
        setDescription(description);
    }

    public void clearDescription() {
        setDescription(null);
    }

    public String validateDescription(final String testDescription) {
        if (testDescription.equals(getDescription())) {
            return null;
        }
        if (testDescription.length() == 0) {
            return DESCRIPTION_MAY_NOT_BE_BLANK;
        }
        return claimRepository.descriptionIsUniqueForClaimant(employee, testDescription) ? null : DESCRIPTION_NOT_UNIQUE;
    }

    public static final String DESCRIPTION_NOT_UNIQUE = "Description is not unique";

    public static final String DESCRIPTION_MAY_NOT_BE_BLANK = "Description may not be blank";

    public String disableDescription() {
        return modifiable();
    }

    private Employee employee;

    @MemberOrder(sequence = "4")
    @Disabled
    public Employee getClaimant() {
        return this.employee;
    }

    public void setClaimant(final Employee employee) {
        this.employee = employee;
    }

    private Employee approver;

    @MemberOrder(sequence = "5")
    @Optional
    public Employee getApprover() {
        return this.approver;
    }

    public void setApprover(final Employee approver) {
        this.approver = approver;
    }

    public void modifyApprover(final Employee approver) {
        setApprover(approver);
    }

    public void clearApprover() {
        setApprover(null);
    }

    public String validateApprover(final Employee approver) {
        return approver == getClaimant() ? Employee.CANT_BE_APPROVER_FOR_OWN_CLAIMS : null;
    }

    /**
     * This method does nothing. It has been specified to allow it to be overridden in sub-classes.
     */
    public String disableApprover() {
        return null;
    }

    /**
     * Returns null if approver actions are permitted; else returns a reason why not.
     */
    public abstract String disableApproverActionsOnAllItems();

    private List<ExpenseItem> expenseItems = new ArrayList<ExpenseItem>();

    @Disabled
    public List<ExpenseItem> getExpenseItems() {
        return this.expenseItems;
    }

    @SuppressWarnings("unused")
    private void setExpenseItems(final List<ExpenseItem> expenseItems) {
        this.expenseItems = expenseItems;
    }

    public void addToExpenseItems(final ExpenseItem item) {
        getExpenseItems().add(item);
    }

    public void removeFromExpenseItems(final ExpenseItem item) {
        getExpenseItems().remove(item);
    }

    @MemberOrder(sequence = "1.1")
    public ExpenseItem createNewExpenseItem(final ExpenseType type) {
        return getClaimFactory().createNewExpenseItem(this, type);
    }

    public String disableCreateNewExpenseItem() {
        return modifiable();
    }

    @MemberOrder(sequence = "1.2")
    public ExpenseItem copyAnExistingExpenseItem(final ExpenseItem otherItem) {
        final ExpenseItem newItem = getClaimFactory().createNewExpenseItem(this, otherItem.getExpenseType());
        newItem.copyFrom(otherItem);
        return newItem;
    }

    public String disableCopyAnExistingExpenseItem() {
        return modifiable();
    }

    @MemberOrder(sequence = "1.3")
    public void copyAllExpenseItemsFromAnotherClaim(@Named("Claim or Template") final AbstractClaim otherClaim, @Optional() @Named("New date to apply to all items") final Date newDate) {
        assert otherClaim != this;
        for (final ExpenseItem otherItem : otherClaim.getExpenseItems()) {
            final ExpenseItem newItem = copyAnExistingExpenseItem(otherItem);
            newItem.modifyDateIncurred(newDate);
            claimFactory.makePersistent(newItem);
        }
    }

    public String disableCopyAllExpenseItemsFromAnotherClaim() {
        return modifiable();
    }

    public String validateCopyAllExpenseItemsFromAnotherClaim(final AbstractClaim otherClaim, final Date date) {
        if (otherClaim == this) {
            return "Cannot copy to same claim";
        }
        return null;
    }

    private ProjectCode projectCode;

    @MemberOrder(sequence = "6")
    @Optional()
    public ProjectCode getProjectCode() {
        return this.projectCode;
    }

    public void setProjectCode(final ProjectCode projectCode) {
        this.projectCode = projectCode;
    }

    public void modifyProjectCode(final ProjectCode newCode) {
        setProjectCode(newCode);
        for (final ExpenseItem item : expenseItems) {
            item.modifyProjectCode(projectCode);
        }
    }

    public void clearProjectCode() {
        setProjectCode(null);
    }

    public String disableProjectCode() {
        return modifiable();
    }

    @Hidden
    public String currencyCode() {
        if (getCurrency() == null && getClaimant() != null) {
            setCurrency(getClaimant().getCurrency());
        }
        if (getCurrency() != null) {
            return getCurrency().getCurrencyCode();
        }
        return DEFAULT_CURRENCY_CODE;
    }

    public static final String DEFAULT_CURRENCY_CODE = "EUR";

    private Currency currency;

    @Hidden
    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public void modifyCurrency(final Currency currency) {
        setCurrency(currency);
    }

    public void clearCurrency() {
        setCurrency(null);
    }

    @MemberOrder(sequence = "3.1")
    public Claim createNewClaimFromThis(@Named("Description") final String description, @Optional @Named("New date to apply to all items") final Date date) {
        final Claim newClaim = getClaimFactory().createNewClaim(getClaimant(), description);
        copyFieldsAndItemsTo(newClaim, date);
        return newClaim;
    }

    public String default0CreateNewClaimFromThis() {
        return getClaimFactory().defaultUniqueClaimDescription(getClaimant());
    }

    private void copyFieldsAndItemsTo(final AbstractClaim newClaim, final Date newDate) {
        newClaim.setProjectCode(getProjectCode());
        newClaim.setApprover(getApprover());
        newClaim.copyAllExpenseItemsFromAnotherClaim(this, newDate);
    }
}
