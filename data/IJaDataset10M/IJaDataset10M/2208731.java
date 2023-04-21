package com.foursoft.fourever.vmodell.regular;

import java.util.List;

public interface ArbeitsschrittknotenZuMethodenreferenz extends ModelElement {

    Arbeitsschrittknoten getArbeitsschrittknoten();

    String getBeschreibung();

    String getConsistent_to_version();

    List<Methodenreferenz> getMethodenreferenzs();

    Aktivitaetsbeziehungen getParentAktivitaetsbeziehungen();

    String getRefers_to_id();
}
