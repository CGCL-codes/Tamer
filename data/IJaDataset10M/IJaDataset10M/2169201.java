package ch8.p12;

import org.junit.Assert;
import org.junit.Test;

public class BankAccountTester {

    @Test
    public void BankAccountTest() {
        BankAccountMeasurer m = new BankAccountMeasurer();
        BankAccountFilter f = new BankAccountFilter();
        DataSet data = new DataSet(m, f);
        data.add(new BankAccount(999));
        data.add(new BankAccount(2000));
        data.add(new BankAccount(200));
        data.add(new BankAccount(4000));
        data.add(new BankAccount(5000));
        Assert.assertEquals(3, data.getCount(), 0);
    }
}
