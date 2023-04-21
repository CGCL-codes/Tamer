package artem.finance.server.persist;

import java.io.Serializable;

public class PaymentOrder implements Serializable, Persistancible {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer sum;

    private String debetOrganisation;

    private String debetBank;

    private Long debetAccount;

    private String creditOrganisation;

    private String creditBank;

    private Long creditAccount;

    private String description;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Integer getSum() {
        return this.sum;
    }

    public void setDebetOrganisation(String deborg) {
        this.debetOrganisation = deborg;
    }

    public String getDebetOrganisation() {
        return this.debetOrganisation;
    }

    public void setDebetBank(String bank) {
        this.debetBank = bank;
    }

    public String getDebetBank() {
        return this.debetBank;
    }

    public void setDebetAccount(Long account) {
        this.debetAccount = account;
    }

    public Long getDebetAccount() {
        return this.debetAccount;
    }

    public void setCreditOrganisation(String deborg) {
        this.creditOrganisation = deborg;
    }

    public String getCreditOrganisation() {
        return this.creditOrganisation;
    }

    public void setCreditBank(String bank) {
        this.creditBank = bank;
    }

    public String getCreditBank() {
        return this.creditBank;
    }

    public void setCreditAccount(Long account) {
        this.creditAccount = account;
    }

    public Long getCreditAccount() {
        return this.creditAccount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
