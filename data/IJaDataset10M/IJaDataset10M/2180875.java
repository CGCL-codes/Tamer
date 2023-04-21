package tests;

import fit.ColumnFixture;

public class SrudTest extends ColumnFixture {

    public String summand4;

    public String summand3;

    public String summand1;

    public String summand2;

    public String sum() {
        Float sum = Float.parseFloat(subtotal()) + Float.parseFloat(summand4);
        return sum.toString();
    }

    public String subtotal() {
        Float subtotal = (Float.parseFloat(summand1) + Float.parseFloat(summand2) + Float.parseFloat(summand3));
        return subtotal.toString();
    }
}
