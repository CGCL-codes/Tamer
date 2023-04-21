package net.sf.brightside.gymcalendar.tapestry.utils;

public class Breadcrumb {

    String address;

    String name;

    public Breadcrumb(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}
