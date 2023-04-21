package de.morknet.mrw.action.tour;

import de.morknet.mrw.gui.info.TourInfo;

public class KreuztourLinks extends TourInfo {

    private static final String names[] = { "Bahnhof Schattenbahnhof 201", "Bahnhof Schattenbahnhof 203", "Bahnhof Alt Ulm G", "Bahnhof Alt Ulm P2", "Bahnhof Schattenbahnhof G", "Bahnhof Schattenbahnhof", "Strecke West H", "Bahnhof Alt Ulm P4" };

    private static final TourInfo tour = new KreuztourLinks();

    private KreuztourLinks() {
        super(names);
    }

    public static TourInfo getTour() {
        return tour;
    }

    @Override
    public boolean getDirection() {
        return false;
    }

    @Override
    public String getName() {
        return "Kreuztour links";
    }

    @Override
    public boolean isLoop() {
        return true;
    }
}
