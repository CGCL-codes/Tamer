package org.koossery.adempiere.core.contract.interfaces;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.criteria.comptabilite.AcctSchemaGLCriteria;
import org.koossery.adempiere.core.contract.dto.AcctSchemaGLDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IAcctschemaGLSVCO {

    /**
  *  liste des grands journaux de schema comptable
  * @param schemaID
  * @return
  * @throws KTAdempiereException
  */
    public ArrayList<AcctSchemaGLDTO> listeSchemaGL(int schemaID) throws KTAdempiereException;

    /**
	 * rechercher les infos sur un journal de schema comptable 
	 * @param ctx
	 * @param recordID
	 * @return
	 */
    public ArrayList<AcctSchemaGLDTO> rechercherSchemaGL(Properties ctx, int recordID) throws KTAdempiereException;

    public ArrayList<AcctSchemaGLDTO> rechercherSchemaGL(AcctSchemaGLCriteria ctr) throws KTAdempiereException;

    /**
	 * 
	 * @param ctx
	 * @param recordID
	 * @throws KTAdempiereException
	 */
    public void desactiverSchemaGL(Properties ctx, int recordID) throws KTAdempiereException;

    /**
	 * 
	 * @param ctx
	 * @param recordID
	 * @throws KTAdempiereException
	 */
    public void modifierSchemaGL(Properties ctx, AcctSchemaGLDTO record) throws KTAdempiereException;
}
