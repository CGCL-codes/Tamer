package com.fdm.model;

public class AccountNumberGenerator implements _AccountNumberGenerator {

    private static int accountNumber;

    public AccountNumberGenerator(int number) {
        accountNumber = number;
    }

    public AccountNumberGenerator() {
        accountNumber = 0;
    }

    @Override
    public int next() {
        return ++accountNumber;
    }
}
