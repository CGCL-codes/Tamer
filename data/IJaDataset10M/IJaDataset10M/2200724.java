package com.foursoft.fourever.vmodell.enhanced;

import java.util.List;

public interface TailoringInformation extends ModelElement {

    VMProjekt getParentVMProjekt();

    List<PM> getPMs();

    Projekttyp getPT();

    String getPTVBegruendung();

    List<Projekttypvariante> getPTVs();

    String getVBBegruendung();

    List<Vorgehensbaustein> getVBs();
}
