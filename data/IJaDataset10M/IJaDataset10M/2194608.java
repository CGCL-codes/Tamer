package net.sf.chex4j.bankbalance;

import java.math.BigDecimal;
import net.sf.chex4j.Contract;
import net.sf.chex4j.Post;
import net.sf.chex4j.Pre;

@Contract
public class OverloadChexBankAccount extends SimplePublicBankAccount {

    public OverloadChexBankAccount(BigDecimal amount) {
        super(amount);
    }

    @Override
    @Pre("amount.doubleValue() >= 5.0d")
    @Post("$_.doubleValue() >= 1.0d")
    public BigDecimal deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        return this.balance;
    }
}
