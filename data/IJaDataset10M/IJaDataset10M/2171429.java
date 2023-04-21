package com.foursoft.fourever.vmodell.enhanced;

import java.util.List;
import java.util.Set;

public interface Produkt extends ModelElement {

    List<String> getBeispielprodukts();

    String getConsistent_to_version();

    Disziplin getDisziplin();

    String getExtern();

    String getInitial();

    String getNummer();

    Vorgehensbaustein getParentVorgehensbaustein();

    String getProduktvorlage();

    Set<Begriffsabbildung> getReferringBegriffsabbildungs();

    Set<ModelElement> getReferringModelElementsViaProdukt();

    Set<Produkt> getReferringProdukts();

    String getRefers_to_id();

    Produkt getSchnittstellenQuellprodukt();

    String getSinn_und_Zweck();

    Textbaustein getTextbaustein();
}
