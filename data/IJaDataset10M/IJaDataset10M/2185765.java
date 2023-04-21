package fr.soleil.actiongroup.collectiveaction.components.response;

import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.DeviceAttributeWrapper;

public interface IndividualAttributesResponse extends IndividualResponse {

    public DeviceAttributeWrapper[] get_data();

    public void setAttributes(DeviceAttributeWrapper[] attribute);
}
