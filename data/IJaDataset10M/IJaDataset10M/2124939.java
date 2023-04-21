package com.xerox.amazonws.fps;

/**
 * @author J. Bernard
 * @author Elastic Grid, LLC.
 * @author jerome.bernard@elastic-grid.com
 */
public class Address {

    private final String name;

    private final String line1;

    private final String line2;

    private final String city;

    private final String state;

    private final String zipCode;

    public Address(String name, String line1, String line2, String city, String state, String zipCode) {
        this.name = name;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public String getName() {
        return name;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Address");
        sb.append("{name='").append(name).append('\'');
        sb.append(", line1='").append(line1).append('\'');
        sb.append(", line2='").append(line2).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append(", zipCode='").append(zipCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
