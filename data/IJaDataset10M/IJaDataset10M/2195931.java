package com.foursoft.fourever.vmodell.regular;

public interface DisziplinUmbenennen extends ModelElement {

    String getConsistent_to_version();

    Disziplin getDisziplin();

    String getNeuerName();

    Disziplinaenderungen getParentDisziplinaenderungen();

    String getRefers_to_id();
}
