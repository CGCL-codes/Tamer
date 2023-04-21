package net.javaseminar;

public class Frau {

    public void briefboteKommtMitPaeckchen(Paeckchen p) {
        if (p != null && p.getInhalt() instanceof Schuh) System.out.println("AAAAAAHHH!"); else System.out.println("Schatz, da ist ein Paket f�r dich!");
    }

    public void briefboteKommtMitPaeckchen(GenerischesPaeckchen<Schuh> p) {
        if (p != null) System.out.println("AAAAAAHHH!"); else System.out.println("Schatz, da ist ein Paket f�r dich!");
    }
}
