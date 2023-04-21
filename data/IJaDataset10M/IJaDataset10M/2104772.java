package org.serendepity.client.data;

public class AccountData {

    private static AccountRecord[] records;

    public static AccountRecord[] getRecords() {
        if (records == null) {
            records = getNewRecords();
        }
        return records;
    }

    public static AccountRecord[] getNewRecords() {
        return new AccountRecord[] { new AccountRecord("contact", "ABC Pty Ltd", "(02) 251 6641", "Sydney", "Sean Doyle", "sales@uptick.com.au"), new AccountRecord("contact", "DEF Pty Ltd", "(02) 251 6642", "Melbourne", "Mario Bernatovic", "sales@uptick.com.au"), new AccountRecord("account", "GHI Pty Ltd", "(02) 251 6643", "Newcastle", "Rob Ferguson", "sales@uptick.com.au"), new AccountRecord("account", "JKL Pty Ltd", "(02) 251 6644", "Perth", "Alister Bennett", "sales@uptick.com.au"), new AccountRecord("account", "MNO Pty Ltd", "(02) 251 6645", "Newcastle", "Mark Kirkpatrick", "sales@uptick.com.au"), new AccountRecord("account", "PQR Pty Ltd", "(02) 251 6647", "Bankstown", "Grahame King", "sales@uptick.com.au"), new AccountRecord("account", "STU Pty Ltd", "(02) 251 6648", "Sydney", "Peter Wood", "sales@uptick.com.au"), new AccountRecord("account", "VWX Pty Ltd", "(02) 251 6649", "Newcastle", "Ross Hodge", "sales@uptick.com.au"), new AccountRecord("account", "YZA Pty Ltd", "(02) 251 6610", "Sydney", "Darren Poyner", "sales@uptick.com.au"), new AccountRecord("account", "123 Pty Ltd", "(02) 251 6611", "Glebe", "Carl Blick", "sales@uptick.com.au"), new AccountRecord("account", "456 Pty Ltd", "(02) 251 6612", "Sydney", "Mark Powrie", "sales@uptick.com.au"), new AccountRecord("account", "789 Pty Ltd", "(02) 251 6613", "Perth", "Jason Bance", "sales@uptick.com.au"), new AccountRecord("account", "101 Pty Ltd", "(02) 251 6614", "Newcastle", "Patrick Keegan", "sales@uptick.com.au") };
    }
}
