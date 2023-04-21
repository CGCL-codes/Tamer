package com.foursoft.fourever.vmodell.enhanced;

import java.util.List;

public interface Disziplinaenderungen extends ModelElement {

    String getConsistent_to_version();

    List<DisziplintextHintenanstellen> getDisziplintextHintenanstellens();

    List<DisziplintextVoranstellen> getDisziplintextVoranstellens();

    List<DisziplinUmbenennen> getDisziplinUmbenennens();

    Aenderungsoperationen getParentAenderungsoperationen();

    String getRefers_to_id();
}
