package com.birt.test;

public class ContactListFactory {

    public Contact[] createContactList() {
        Contact[] c = new Contact[4];
        c[0] = new Contact("stavros", "kounis", "2310886269");
        c[1] = new Contact("dimitris", "kounis", "2310888270");
        c[2] = new Contact("dimitris", "adamos", "2310998417");
        c[3] = new Contact("nikos", "koufotolis", "2321013770");
        return c;
    }
}
