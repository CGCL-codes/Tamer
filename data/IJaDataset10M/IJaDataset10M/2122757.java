package khl.ooo.domain.cursus;

import khl.ooo.domain.Lesmoment;

public class StateAfgesloten implements CursusState {

    Cursus cursus;

    public StateAfgesloten(Cursus cu) {
        this.cursus = cu;
    }

    public void VoegLesmomentToe(Lesmoment les) {
        cursus.addLesmoment(les);
    }
}
