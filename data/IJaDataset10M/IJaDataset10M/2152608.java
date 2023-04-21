package org.gbif.portal.dto.taxonomy;

import org.gbif.portal.dto.BaseDTOFactory;
import org.gbif.portal.model.taxonomy.CommonName;

/**
 * A DTOFactory for Common Name objects. A Common Name is associated with a particular taxon concept.
 * 
 * @author Dave Martin
 */
public class CommonNameDTOFactory extends BaseDTOFactory {

    /**
	 * @see org.gbif.portal.dto.DTOFactory#createDTO(java.lang.Object)
	 */
    public Object createDTO(Object modelObject) {
        CommonName commonName = (CommonName) modelObject;
        CommonNameDTO commonNameDTO = new CommonNameDTO();
        commonNameDTO.setKey(commonName.getId().toString());
        commonNameDTO.setName(commonName.getName());
        commonNameDTO.setTaxonName(commonName.getTaxonConcept().getTaxonName().getCanonical());
        commonNameDTO.setTaxonConceptKey(String.valueOf(commonName.getTaxonConceptId()));
        commonNameDTO.setLanguage(commonName.getLanguage());
        return commonNameDTO;
    }
}
