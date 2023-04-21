package com.foursoft.fourever.vmodell.enhanced;

import java.util.List;

public interface TailoringAbhaengigkeitZuProdukt extends ModelElement {

    String getBeschreibung();

    String getConsistent_to_version();

    Produktabhaengigkeitsbeziehungen getParentProduktabhaengigkeitsbeziehungen();

    List<Produkt> getProdukts();

    String getRefers_to_id();

    Tailoringabhaengigkeit getTailoringabhaengigkeit();
}
