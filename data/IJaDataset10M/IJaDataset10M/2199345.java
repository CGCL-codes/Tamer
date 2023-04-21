package de.morknet.mrw.action.tour;

import de.morknet.mrw.gui.info.TourInfo;

public class ReinigungSchattenbahnhofLinks extends TourInfo {

    private static final String names[] = { "Bahnhof Schattenbahnhof G", "Bahnhof Schattenbahnhof P2,Bahnhof Schattenbahnhof P3,Bahnhof Schattenbahnhof P4,Bahnhof Schattenbahnhof P5", "Strecke West H", "Bahnhof Alt Ulm P6,Bahnhof Alt Ulm P2,Bahnhof Alt Ulm P3,Bahnhof Alt Ulm P4", "Bahnhof Schattenbahnhof G", "Bahnhof Schattenbahnhof P3,Bahnhof Schattenbahnhof P4,Bahnhof Schattenbahnhof P5,Bahnhof Schattenbahnhof P2", "Strecke West H", "Bahnhof Alt Ulm P2,Bahnhof Alt Ulm P3,Bahnhof Alt Ulm P4,Bahnhof Alt Ulm P6", "Bahnhof Schattenbahnhof G", "Bahnhof Schattenbahnhof P4,Bahnhof Schattenbahnhof P5,Bahnhof Schattenbahnhof P2,Bahnhof Schattenbahnhof P3", "Strecke West H", "Bahnhof Alt Ulm P3,Bahnhof Alt Ulm P4,Bahnhof Alt Ulm P6,Bahnhof Alt Ulm P2", "Bahnhof Schattenbahnhof G", "Bahnhof Schattenbahnhof P5,Bahnhof Schattenbahnhof P2,Bahnhof Schattenbahnhof P3,Bahnhof Schattenbahnhof P4", "Strecke West H", "Bahnhof Alt Ulm P4,Bahnhof Alt Ulm P6,Bahnhof Alt Ulm P2,Bahnhof Alt Ulm P3" };

    private static final TourInfo tour = new ReinigungSchattenbahnhofLinks();

    public static TourInfo getTour() {
        return tour;
    }

    private ReinigungSchattenbahnhofLinks() {
        super(names);
    }

    @Override
    public boolean getDirection() {
        return false;
    }

    @Override
    public String getName() {
        return "Reinigung Schattenbahnhof links";
    }

    @Override
    public boolean isLoop() {
        return true;
    }
}
