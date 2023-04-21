package junk.scrap.apps;

import junk.scrap.worker.*;
import junk.scrap.elements.NewElement;

public class InsertorTest {

    public static void main(String[] args) {
        Integer temp;
        DoubleInteger etestobj = new DoubleInteger();
        InsertorSorter etestsort = new InsertorSorter();
        etestobj.add(new Integer(1));
        etestobj.add(new Integer(99));
        etestobj.add(new Integer(3));
        etestobj.add(new Integer(4));
        etestobj.add(new Integer(118));
        etestobj.add(new Integer(2));
        etestobj.add(new Integer(5));
        etestobj.add(new Integer(1000000));
        etestobj.add(new Integer(2002));
        etestsort.doIt(etestobj);
        temp = etestobj.retrieve();
        while (temp != null) {
            System.out.println(temp.toString());
            temp = etestobj.retrieve();
        }
    }
}
