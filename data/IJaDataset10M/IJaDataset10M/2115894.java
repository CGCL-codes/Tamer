package net.sf.sevenzipjbinding.junit.snippets;

import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipNativeInitializationException;

public class SevenZipJBindingInitCheck {

    public static void main(String[] args) {
        try {
            SevenZip.initSevenZipFromPlatformJAR();
            System.out.println("7-Zip-JBinding library was initialized");
        } catch (SevenZipNativeInitializationException e) {
            e.printStackTrace();
        }
    }
}
