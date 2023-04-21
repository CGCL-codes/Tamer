package virtuallab.gui;

import virtuallab.Specimen;
import virtuallab.Specimen.ControlState;

public interface SpecimenControl extends Control {

    void setValidControlStates(java.util.Map validStates);

    void setCurrentControlState(java.util.Collection controlStates);

    Specimen.ControlState getCurrentControlState();
}
