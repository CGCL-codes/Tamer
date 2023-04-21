package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import example.message.filereader.Address;

public class Array {

    public static void main(String[] args) {
        ArrayList<Address> array = new ArrayList<Address>();
        array.add(new Address());
        array.add(new Address());
        array.add(new Address());
        Address[] ars = array.toArray(new Address[0]);
        for (Address ar : ars) System.out.println(ar);
        List<Address> list = new ArrayList<Address>(Arrays.asList(ars));
        List<Address> list2 = new ArrayList<Address>();
        Collections.addAll(list2, ars);
        System.out.println(list2.size());
    }
}
