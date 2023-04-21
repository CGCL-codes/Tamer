package net.jautomock.test.mocking.basictypes;

class SimpleTypes {

    static int numberOfInstances = 0;

    private int myInteger;

    private double myDouble;

    private String myString;

    public SimpleTypes(int myInteger, double myDouble, String myString) {
        numberOfInstances++;
        this.myInteger = myInteger;
        this.myDouble = myDouble;
        this.myString = myString;
    }

    public int getMyInteger() {
        return this.myInteger;
    }

    public void setMyInteger(int myInteger) {
        this.myInteger = myInteger;
    }

    public double getMyDouble() {
        return this.myDouble;
    }

    public void setMyDouble(double myDouble) {
        this.myDouble = myDouble;
    }

    public String getMyString() {
        return this.myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }
}
