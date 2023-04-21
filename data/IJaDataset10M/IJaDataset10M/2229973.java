package co.edu.unal.ungrid.services.client.applet;

public class PrintableFactory {

    public static void register(final PrintableApplet app) {
        gApp = app;
    }

    public static PrintableApplet getInstance() {
        return gApp;
    }

    private static PrintableApplet gApp;
}
