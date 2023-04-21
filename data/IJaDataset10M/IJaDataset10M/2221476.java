package org.authorsite.bib.ejb.services.dto;

import org.authorsite.bib.ejb.entity.*;
import org.authorsite.bib.dto.*;

/**
 *
 * @author  jejking
 * @version $Revision: 1.1 $
 */
public class PersonDTOAssembler {

    private PersonLocal myPersonLocal;

    private PersonDTO myPersonDTO;

    /** Creates a new instance of PersonDTOAssembler */
    public PersonDTOAssembler(PersonLocal newPersonLocal) {
        myPersonLocal = newPersonLocal;
        myPersonDTO = new PersonDTO(myPersonLocal.getPersonID());
    }

    public PersonDTO assembleDTO() {
        myPersonDTO.setMainName(myPersonLocal.getMainName());
        myPersonDTO.setGivenName(myPersonLocal.getGivenName());
        myPersonDTO.setOtherNames(myPersonLocal.getOtherNames());
        myPersonDTO.setPrefix(myPersonLocal.getPrefix());
        myPersonDTO.setSuffix(myPersonLocal.getSuffix());
        myPersonDTO.setGenderCode(myPersonLocal.getGenderCode());
        return myPersonDTO;
    }
}
