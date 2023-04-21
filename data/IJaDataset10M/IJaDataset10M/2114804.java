package org.gbif.portal.dto.resources;

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.model.resources.DataResourceAgent;

/**
 * Creates DataProviderDTO from DataProvider model objects.
 *
 * @author Donald Hobern
 */
public class DataResourceAgentDTOFactory extends BaseDTOFactory {

    /**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
    public Object createDTO(Object modelObject) {
        if (modelObject == null) return null;
        DataResourceAgent dataResourceAgent = (DataResourceAgent) modelObject;
        DataResourceAgentDTO dataResourceAgentDTO = new DataResourceAgentDTO();
        dataResourceAgentDTO.setKey(dataResourceAgent.getId().toString());
        dataResourceAgentDTO.setDataResourceKey(dataResourceAgent.getDataResource().getId().toString());
        dataResourceAgentDTO.setAgentKey(dataResourceAgent.getAgent().getId().toString());
        dataResourceAgentDTO.setAgentName(dataResourceAgent.getAgent().getName());
        dataResourceAgentDTO.setAgentAddress(dataResourceAgent.getAgent().getAddress());
        dataResourceAgentDTO.setAgentEmail(dataResourceAgent.getAgent().getEmail());
        dataResourceAgentDTO.setAgentTelephone(dataResourceAgent.getAgent().getTelephone());
        if (dataResourceAgent.getAgentType() != null) {
            dataResourceAgentDTO.setAgentType(dataResourceAgent.getAgentType().getValue());
            dataResourceAgentDTO.setAgentTypeName(dataResourceAgent.getAgentType().getName());
        }
        return dataResourceAgentDTO;
    }
}
