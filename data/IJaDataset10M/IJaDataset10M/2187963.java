package Payroll;

import java.util.Calendar;

public class SalesReceipt {

    private Calendar itsSaleDate;

    double itsAmount;

    public SalesReceipt(Calendar saleDate, double amount) {
        itsSaleDate = saleDate;
        itsAmount = amount;
    }

    public double GetAmount() {
        return itsAmount;
    }

    public Calendar GetSaleDate() {
        return itsSaleDate;
    }
}
