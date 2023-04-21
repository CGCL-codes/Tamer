package ssnavi.test;

import ssnavi.xml.*;
import ssnavi.sl.*;
import java.io.*;

public class SlTest {

    public SlTest() {
    }

    public static void main(String[] args) {
        try {
            XmlServiceListParser parser = new XmlServiceListParser();
            SlServices services = parser.parse(new File("ServiceList.xml"));
            parser.write(services, System.out);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
