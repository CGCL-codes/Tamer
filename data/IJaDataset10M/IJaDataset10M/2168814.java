package org.koossery.adempiere.core.backend.interfaces.dao;

import java.util.ArrayList;
import org.koossery.adempiere.core.contract.dto.RegistrationAttributeDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempierePersistenceException;

/**
 * @author Cedrick Essale
 *
 */
public interface IRegistrationAttributeDAO {

    ArrayList<RegistrationAttributeDTO> GetAll(int cliendID) throws KTAdempierePersistenceException;
}
