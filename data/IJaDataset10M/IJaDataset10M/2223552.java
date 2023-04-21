package com.koossery.adempiere.fe.actions.Requisition_to_invoice.Requisition.Line.findReqLine;

import org.koossery.adempiere.core.contract.criteria.Requisition_to_invoice.Requisition.M_RequisitionLineCriteria;
import com.koossery.adempiere.fe.actions.Requisition_to_invoice.Requisition.Line.createModifyReqLine.ReqLineForm;

public class FindReqLineForm extends ReqLineForm {

    private M_RequisitionLineCriteria m_RequisitionLineCriteria;

    public M_RequisitionLineCriteria getM_RequisitionLineCriteria() {
        return m_RequisitionLineCriteria;
    }

    public void setM_RequisitionLineCriteria(M_RequisitionLineCriteria requisitionLineCriteria) {
        m_RequisitionLineCriteria = requisitionLineCriteria;
    }
}
