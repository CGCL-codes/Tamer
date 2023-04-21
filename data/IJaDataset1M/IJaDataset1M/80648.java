package org.koossery.adempiere.core.contract.interfaces.server.request;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.contract.criteria.server.request.R_RequestProcessor_RouteCriteria;
import org.koossery.adempiere.core.contract.dto.server.request.R_RequestProcessor_RouteDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.IKTADempiereServiceComposed;

public interface IR_RequestProcessor_RouteSVCO extends IKTADempiereServiceComposed {

    public int createR_RequestProcessor_Route(Properties ctx, R_RequestProcessor_RouteDTO r_RequestProcessor_RouteDTO, String trxname) throws KTAdempiereException;

    public R_RequestProcessor_RouteDTO findOneR_RequestProcessor_Route(Properties ctx, int r_RequestProcessor_RouteID) throws KTAdempiereException;

    public ArrayList<R_RequestProcessor_RouteDTO> findR_RequestProcessor_Route(Properties ctx, R_RequestProcessor_RouteCriteria r_RequestProcessor_RouteCriteria) throws KTAdempiereException;

    public void updateR_RequestProcessor_Route(Properties ctx, R_RequestProcessor_RouteDTO r_RequestProcessor_RouteDTO) throws KTAdempiereException;

    public boolean deleteR_RequestProcessor_Route(Properties ctx, R_RequestProcessor_RouteCriteria criteria) throws KTAdempiereException;
}
