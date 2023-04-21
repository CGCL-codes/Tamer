package org.tolven.app.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.tolven.core.entity.Account;

/**
 * <p>
 * This entity provides a cross reference between a placeholder in a sending account and a
 * placeholder in a receiving account. For example, if a patient records their weight in their
 * ePHR, it might have a placeholder id of ephr:patient-123:observation-456. If that observation
 * is sent to the patient's provider, then in the provider's account, the patient will have a 
 * different ID at the provider and the observation will also have a different id.
 * In other words, there is no direct correlation between MenuData in two different accounts.
 * This entity provides the next best thing which is to consider the source account's ID to be
 * useful for knowing when the sender is referring to the same patient (or other placeholder) without
 * having any particular meaning to the receiving account.</p>
 * <p>
 * The same ID can then be used when communicating back to the sender.
 * </p>
 * <p>
 * PlaceholderID's are not unique although an application can treat a placeholderID if it desired.
 * </p>
 *  
 * @author John Churin
 *
 */
@Entity
@Table
public class PlaceholderID implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "APP_SEQ_GEN")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuData placeholder;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountMenuStructure menuStrucuture;

    @Column
    private String root;

    @Column
    private String extension;

    @Column
    private String assigningAuthority;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MenuData getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(MenuData placeholder) {
        this.placeholder = placeholder;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    /**
     * Compare two PlaceholderId objects for equality.  
     * MenuStructure is not included in the test because placeholder (MenuData) is more selective and implies MenuStructure.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof PlaceholderID)) return false;
        if (!getPlaceholder().equals(((PlaceholderID) obj).getPlaceholder())) return false;
        if (getRoot() != null && !getRoot().equals(((PlaceholderID) obj).getRoot())) return false;
        if (getExtension() != null && !getExtension().equals(((PlaceholderID) obj).getExtension())) return false;
        return true;
    }

    /**
     * Return a hash code for this object. Note: The hashCode is based on the the extension only which is 
     * adequate for a hash.
     */
    public int hashCode() {
        return getHashCodeString().hashCode();
    }

    public AccountMenuStructure getMenuStrucuture() {
        return menuStrucuture;
    }

    public void setMenuStrucuture(AccountMenuStructure menuStrucuture) {
        this.menuStrucuture = menuStrucuture;
    }

    public String getAssigningAuthority() {
        return assigningAuthority;
    }

    public void setAssigningAuthority(String assigningAuthority) {
        this.assigningAuthority = assigningAuthority;
    }

    public String getHashCodeString() {
        return getExtension() + getRoot();
    }
}
