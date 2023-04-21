package com.foursoft.fourever.vmodell.regular;

public interface Aktivitaetkante extends ModelElement {

    String getConsistent_to_version();

    Vorgehensbaustein getParentVorgehensbaustein();

    ModelElement getQuelleAktivitaetsknoten();

    String getRefers_to_id();

    ModelElement getZielAktivitaetsknoten();
}
