package org.koossery.adempiere.core.contract.interfaces.pa;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.dto.pa.*;
import org.koossery.adempiere.core.contract.criteria.pa.*;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;

public interface IPA_ColorSchemaSVCO {

    public int createPA_ColorSchema(Properties ctx, PA_ColorSchemaDTO pA_ColorSchemaDTO, String trxname) throws KTAdempiereException;

    public PA_ColorSchemaDTO findOnePA_ColorSchema(Properties ctx, int pA_ColorSchemaID) throws KTAdempiereException;

    public ArrayList<PA_ColorSchemaDTO> findPA_ColorSchema(Properties ctx, PA_ColorSchemaCriteria pA_ColorSchemaCriteria) throws KTAdempiereException;

    public void updatePA_ColorSchema(Properties ctx, PA_ColorSchemaDTO pA_ColorSchemaDTO) throws KTAdempiereException;

    public boolean updatePA_ColorSchema(PA_ColorSchemaCriteria criteria) throws KTAdempiereException;
}
