package ch.nostromo.dampfkarte;

public class DampfKarte {

    public static final String VERSION = "0.1";

    public static void main(String[] args) {
        ch.nostromo.dampfkarte.clients.desktop.controller.DesktopController.getInstance().init(args);
    }
}
