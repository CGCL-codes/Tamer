package com.foursoft.fourever.vmodell.regular;

import java.util.List;
import java.util.Set;

public interface Projekttyp extends ModelElement {

    List<BedingteAblaufbausteinspezifikation> getBedingteAblaufbausteinspezifikationen();

    List<BedingtesProjektmerkmal> getBedingteProjektmerkmale();

    List<BedingterVorgehensbaustein> getBedingteVorgehensbausteine();

    String getBeschreibung();

    String getConsistent_to_version();

    String getNummer();

    SelektiverAusschluss getParentSelektiverAusschluss();

    V_Modellvariante getParentV_Modellvariante();

    Ablaufbausteinspezifikation getPDS_Spezifikation();

    Set<ModelElement> getReferringModelElementsViaProjekttyp();

    String getRefers_to_id();
}
