package net.javaseminar;

public class Schaltungsdecorator implements Fahrzeug {

    private Fahrzeug fahrzeug;

    public Schaltungsdecorator(Fahrzeug fahrzeug) {
        this.fahrzeug = fahrzeug;
    }

    @Override
    public void fahren() {
        System.out.println("Schaltung kontrolliert");
        fahrzeug.fahren();
    }
}
