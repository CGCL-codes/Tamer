package org.koossery.adempiere.core.backend.interfaces.sisv;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.FactDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

/**
 * @author Cedrick Essale
 *
 */
public interface IFactSISV {

    /**
    * creer une ecriture comptable
    * @param ctx
    * @param factDTO
    * @param trxName
    * @return
    * @throws KTAdempiereException
    */
    public int createFact(Properties ctx, FactDTO factDTO, String trxName) throws KTAdempiereException;

    /**
	 *  desactiver une ecriture comptable
	 * @param ctx
	 * @param factID
	 * @throws KTAdempiereException
	 */
    public void desactivateFact(Properties ctx, int factID) throws KTAdempiereException;

    /**
	 * mise � jour
	 * @param ctx
	 * @param factID
	 * @throws KTAdempiereException
	 */
    public void updateFact(Properties ctx, FactDTO factDTO) throws KTAdempiereException;

    /**
	 * lister
	 * @param ctx
	 * @param recordID
	 * @return
	 * @throws KTAdempiereException
	 */
    public ArrayList<FactDTO> getFact(Properties ctx, int recordID) throws KTAdempiereException;

    /**
	 * suppression
	 * @param ctx
	 * @param recordID
	 * @throws KTAdempiereException
	 */
    public void deleteFact(FactDTO factDTO) throws KTAdempiereException;
}
