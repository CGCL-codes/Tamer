package com.gampire.pc.model;

import java.io.Serializable;

public class BattleFieldBean implements Serializable {

    public BattleFieldBean(CampBean camp1, CampBean camp2) {
        this.camp1 = camp1;
        this.camp2 = camp2;
    }

    private CampBean camp1;

    private CampBean camp2;

    public CampBean getCamp1() {
        return camp1;
    }

    public void setCamp1(CampBean camp1) {
        this.camp1 = camp1;
    }

    public CampBean getCamp2() {
        return camp2;
    }

    public void setCamp2(CampBean camp2) {
        this.camp2 = camp2;
    }
}
