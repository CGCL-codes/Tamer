package com.magic.factory.abstractFactory;

public class Client {

    public static void main(String args[]) {
        Factory factory = new LinuxFactory();
        factory.createButton();
        factory.createTextbox();
        factory = new WindowsFactory();
        factory.createButton();
        factory.createTextbox();
    }
}
