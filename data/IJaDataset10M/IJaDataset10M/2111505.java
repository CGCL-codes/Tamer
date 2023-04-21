package org.koossery.adempiere.core.backend.interfaces.sisv;

import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.AcctSchemaDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

/**
 * @author Cedrick Essale
 *
 */
public interface IDocSISV {

    /**
	 * 
	 * @param ctx TODO
	 * @param ass1 : schema comptable
	 * @param recordID : ID enregistrement  � charger
	 * @param trxName  : nom de la transaction
	 * @param force: boolean permettant de declencher la validation
	 * @return
	 * @throws KTAdempiereException
	 */
    public String postImmediate(Properties ctx, AcctSchemaDTO[] ass1, int recordID, boolean force, String trxName) throws KTAdempiereException;

    /**
	 * 
	 * @param ctx TODO
	 * @param ass1 : schema comptable
	 * @param recordID : ID enregistrement  � charger
	 * @param trxName  : nom de la transaction
	 * @param force:boolean permettant de declencher la validation
	 * @param repost: booleen permettant de declencher une re  validation
	 * @return
	 * @throws KTAdempiereException
	 */
    public String post(Properties ctx, AcctSchemaDTO[] ass1, int recordID, boolean force, boolean repost, String trxName) throws KTAdempiereException;

    /**
	 * 	  	Process document
	 * @param processAction
	 *	@param processAction document action
	 *	@return true if performed
	 * @return
	 * @throws KTAdempiereException
	 */
    public boolean processit(Properties ctx, String processAction, int id) throws KTAdempiereException;
}
