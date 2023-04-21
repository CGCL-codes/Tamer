package testing;

import persistent.ManagerOnLSD;
import java.lang.System;
import java.util.Date;

public class PersistentTest {

    private static String engine = "Mysql";

    private static ManagerOnLSD manager = new ManagerOnLSD(engine);

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        printInvestiga();
        printArray();
    }

    private static void printArray() {
        System.out.println(manager.get("Investigacion", 1));
    }

    private static void printTime() {
        Date now = new Date();
        System.out.println(now.getTime());
    }

    private static void saveInvestiga() {
        System.out.println("URL: " + manager.getUrl());
        model.Investigacion i = new model.Investigacion();
        i.setNombre("Investiga de prueba");
        manager.save(i);
        System.out.println(i);
    }

    private static void printInvestiga() {
        System.out.println(manager.get("Investigacion", 1));
    }
}
