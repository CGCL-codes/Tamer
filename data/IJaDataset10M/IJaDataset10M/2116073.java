package net.jlnx.Uebung1_3.util;

import net.jlnx.Uebung1_3.Auto;

public class AutoHelper extends net.jlnx.Uebung1_2.util.AutoHelper {

    private static int anzahlwaegen = 0;

    private static double erloesOhneNachlaesse(Auto[] autos) {
        double amount = 0;
        for (int i = 0; i < autos.length; i++) {
            amount += autos[i].getPreis();
        }
        return amount;
    }

    private static double unfallwaegenProz(Auto[] autos) {
        double amount = 0;
        for (int i = 0; i < autos.length; i++) {
            if (autos[i].hatteUnfall()) {
                amount++;
            }
        }
        return (100 / (double) anzahlwaegen) * amount;
    }

    private static double dieselwaegenProz(Auto[] autos) {
        double amount = 0;
        for (int i = 0; i < autos.length; i++) {
            if (autos[i].isDiesel()) {
                amount++;
            }
        }
        return (100 / (double) anzahlwaegen) * amount;
    }

    private static double elektrowaegenProz(Auto[] autos) {
        double amount = 0;
        for (int i = 0; i < autos.length; i++) {
            if (autos[i].isElektro()) {
                amount++;
            }
        }
        return (100 / (double) anzahlwaegen) * amount;
    }

    private static double erloesMitNachlaessen(Auto[] autos) {
        double amount = 0;
        for (int i = 0; i < autos.length; i++) {
            if (autos[i].hatteUnfall()) amount += autos[i].getPreis() - (autos[i].getPreis() / 100 * 25); else amount += autos[i].getPreis() - (autos[i].getPreis() / 100 * 10);
        }
        return amount * 100 / 100;
    }

    public static String getWagenbestand(Auto[] autos) {
        anzahlwaegen = autos.length;
        StringBuilder ausgabe = new StringBuilder();
        ausgabe.append("Erlös ohne Nachlässe: " + erloesOhneNachlaesse(autos) + " EUR\n");
        ausgabe.append("Anteil an Unfallwagen: " + unfallwaegenProz(autos) + "%\n");
        ausgabe.append("Anteil an Dieselwagen: " + dieselwaegenProz(autos) + "%\n");
        ausgabe.append("Anteil an Elektrowagen: " + elektrowaegenProz(autos) + "%\n");
        ausgabe.append("Erlös mit Nachlässen: " + erloesMitNachlaessen(autos) + " EUR\n");
        return ausgabe.toString();
    }
}
