package org.bissa;

import org.bissa.tuple.Tuple;
import org.testng.annotations.Test;
import rice.tutorial.lesson4.MyApp;
import java.util.Iterator;

public class OperationsTest extends AbstractBissaTest {

    public void readWriteTest() {
        Iterator appIterator = bissaImpls.iterator();
        BissaImpl bissaApp = (BissaImpl) appIterator.next();
        bissaApp.write(new Tuple("hello, world"));
    }
}
