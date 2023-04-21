package com.foursoft.fourever.vmodell.regular;

public interface RolleSpezialisieren extends ModelElement {

    String getConsistent_to_version();

    Rollenaenderungen getParentRollenaenderungen();

    String getRefers_to_id();

    Rolle getSpezialisierendeRolle();

    Rolle getSpezialisierteRolle();
}
