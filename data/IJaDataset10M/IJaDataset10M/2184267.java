package org.koossery.adempiere.core.backend.interfaces.sisv.order;

import java.util.ArrayList;
import java.util.Properties;
import org.koossery.adempiere.core.backend.interfaces.sisv.IKTADempiereSimpleService;
import org.koossery.adempiere.core.contract.criteria.order.C_OrderCriteria;
import org.koossery.adempiere.core.contract.dto.order.C_OrderDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public interface IC_OrderSISV extends IKTADempiereSimpleService {

    public int createC_Order(Properties ctx, C_OrderDTO c_OrderDTO, String trxname) throws KTAdempiereAppException;

    public C_OrderDTO getC_Order(Properties ctx, int c_OrderID, String trxname) throws KTAdempiereAppException;

    public ArrayList<C_OrderDTO> findC_Order(Properties ctx, C_OrderCriteria c_OrderCriteria) throws KTAdempiereAppException;

    public void updateC_Order(Properties ctx, C_OrderDTO c_OrderDTO) throws KTAdempiereAppException;

    public boolean deleteC_Order(Properties ctx, C_OrderCriteria c_OrderCriteria) throws KTAdempiereAppException;
}
