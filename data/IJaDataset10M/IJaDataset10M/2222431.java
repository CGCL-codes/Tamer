package tests;

import utilities.Date;

public class DateTest {

    public static void main(String args[]) {
        Date a = new Date(2018, 1, 1);
        a.setMonth(-1000);
        a.print();
    }
}
