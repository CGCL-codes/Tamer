package org.koossery.adempiere.sisv.impl.payroll;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.eevolution.model.MHRConcept;
import org.eevolution.model.X_HR_Concept;
import org.koossery.adempiere.core.backend.interfaces.dao.payroll.IHR_ConceptDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.payroll.IHR_ConceptSISV;
import org.koossery.adempiere.core.contract.criteria.payroll.HR_ConceptCriteria;
import org.koossery.adempiere.core.contract.dto.payroll.HR_ConceptDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class HR_ConceptSISVImpl extends AbstractCommonSISV implements IHR_ConceptSISV, InitializingBean {

    private IHR_ConceptDAO hrconceptDAOImpl;

    private static Logger logger = Logger.getLogger(HR_ConceptSISVImpl.class);

    public HR_ConceptSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.hrconceptDAOImpl = (IHR_ConceptDAO) this.getDaoController().get("DAO/HR_Concept");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createHR_Concept(Properties ctx, HR_ConceptDTO hR_ConceptDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_HR_Concept model = new X_HR_Concept(ctx, 0, trxname);
            if (hrconceptDAOImpl.isDuplicate(hR_ConceptDTO.getName())) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "HR_CONCEPT_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setColumnType(hR_ConceptDTO.getColumnType());
                model.setDescription(hR_ConceptDTO.getDescription());
                model.setHR_Concept_Category_ID(hR_ConceptDTO.getHr_Concept_Category_ID());
                model.setHR_Department_ID(hR_ConceptDTO.getHr_Department_ID());
                model.setHR_Job_ID(hR_ConceptDTO.getHr_Job_ID());
                model.setHR_Payroll_ID(hR_ConceptDTO.getHr_Payroll_ID());
                model.setName(hR_ConceptDTO.getName());
                model.setType(hR_ConceptDTO.getType());
                model.setValidFrom(hR_ConceptDTO.getValidFrom());
                model.setValidTo(hR_ConceptDTO.getValidTo());
                model.setValue(hR_ConceptDTO.getValue());
                model.setIsDefault(hR_ConceptDTO.getIsDefault() == "Y" ? true : false);
                model.setIsEmployee(hR_ConceptDTO.getIsEmployee() == "Y" ? true : false);
                model.setIsPrinted(hR_ConceptDTO.getIsPrinted() == "Y" ? true : false);
                model.setIsRegistered(hR_ConceptDTO.getIsRegistered() == "Y" ? true : false);
                model.setIsActive(hR_ConceptDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                hR_ConceptDTO.setHr_Concept_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "HR_CONCEPT_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public HR_ConceptDTO getHR_Concept(Properties ctx, int hr_Concept_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (hr_Concept_ID == 0) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "HR_CONCEPT_SISV_002");
            X_HR_Concept mconcept = new X_HR_Concept(ctx, hr_Concept_ID, trxname);
            return convertModelToDTO(mconcept);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "HR_CONCEPT_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<HR_ConceptDTO> findHR_Concept(Properties ctx, HR_ConceptCriteria hR_ConceptCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, hR_ConceptCriteria);
            int id[] = MHRConcept.getAllIDs("HR_Concept", whereclause, null);
            ArrayList<HR_ConceptDTO> list = new ArrayList<HR_ConceptDTO>();
            HR_ConceptDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = this.getHR_Concept(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "HR_CONCEPT_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateHR_Concept(Properties ctx, HR_ConceptDTO hR_ConceptDTO) throws KTAdempiereAppException {
        try {
            MHRConcept model = new MHRConcept(ctx, hR_ConceptDTO.getHr_Concept_ID(), null);
            if (model != null) {
                String oldname = model.getName() + "";
                String newname = hR_ConceptDTO.getName() + "";
                if (oldname.compareToIgnoreCase(newname) != 0) {
                    if (hrconceptDAOImpl.isDuplicate(newname)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "HR_CONCEPT_SISV_000");
                }
                System.out.println("NO duplicates names for modification");
                model.setColumnType(hR_ConceptDTO.getColumnType());
                model.setDescription(hR_ConceptDTO.getDescription());
                model.setHR_Concept_Category_ID(hR_ConceptDTO.getHr_Concept_Category_ID());
                model.setHR_Department_ID(hR_ConceptDTO.getHr_Department_ID());
                model.setHR_Job_ID(hR_ConceptDTO.getHr_Job_ID());
                model.setHR_Payroll_ID(hR_ConceptDTO.getHr_Payroll_ID());
                model.setName(hR_ConceptDTO.getName());
                model.setType(hR_ConceptDTO.getType());
                model.setValidFrom(hR_ConceptDTO.getValidFrom());
                model.setValidTo(hR_ConceptDTO.getValidTo());
                model.setValue(hR_ConceptDTO.getValue());
                model.setIsDefault(hR_ConceptDTO.getIsDefault() == "Y" ? true : false);
                model.setIsEmployee(hR_ConceptDTO.getIsEmployee() == "Y" ? true : false);
                model.setIsPrinted(hR_ConceptDTO.getIsPrinted() == "Y" ? true : false);
                model.setIsRegistered(hR_ConceptDTO.getIsRegistered() == "Y" ? true : false);
                model.setIsActive(hR_ConceptDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                hR_ConceptDTO.setHr_Concept_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "HR_CONCEPT_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean updateHR_Concept(HR_ConceptCriteria hR_ConceptCriteria) throws KTAdempiereAppException {
        try {
            return hrconceptDAOImpl.update(hR_ConceptCriteria);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "HR_CONCEPT_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, HR_ConceptCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getColumnType() != null) temp.append(" AND ( COLUMNTYPE LIKE '%" + criteria.getColumnType() + "%')");
        if (criteria.getDescription() != null) temp.append(" AND ( DESCRIPTION LIKE '%" + criteria.getDescription() + "%')");
        if (criteria.getHr_Concept_Category_ID() > 0) temp.append(" AND (HR_CONCEPT_CATEGORY_ID=" + criteria.getHr_Concept_Category_ID() + ")");
        if (criteria.getHr_Concept_ID() > 0) temp.append(" AND (HR_CONCEPT_ID=" + criteria.getHr_Concept_ID() + ")");
        if (criteria.getHr_Department_ID() > 0) temp.append(" AND (HR_DEPARTMENT_ID=" + criteria.getHr_Department_ID() + ")");
        if (criteria.getHr_Job_ID() > 0) temp.append(" AND (HR_JOB_ID=" + criteria.getHr_Job_ID() + ")");
        if (criteria.getHr_Payroll_ID() > 0) temp.append(" AND (HR_PAYROLL_ID=" + criteria.getHr_Payroll_ID() + ")");
        if (criteria.getName() != null) temp.append(" AND ( NAME LIKE '%" + criteria.getName() + "%')");
        if (criteria.getType() != null) temp.append(" AND ( TYPE LIKE '%" + criteria.getType() + "%')");
        if (criteria.getValidFrom() != null) temp.append(" AND (VALIDFROM=" + criteria.getValidFrom() + ")");
        if (criteria.getValidTo() != null) temp.append(" AND (VALIDTO=" + criteria.getValidTo() + ")");
        if (criteria.getValue() != null) temp.append(" AND ( VALUE LIKE '%" + criteria.getValue() + "%')");
        if (criteria.getIsDefault() != null) temp.append(" AND (ISDEFAULT='" + criteria.getIsDefault() + "')");
        if (criteria.getIsEmployee() != null) temp.append(" AND (ISEMPLOYEE='" + criteria.getIsEmployee() + "')");
        if (criteria.getIsPrinted() != null) temp.append(" AND (ISPRINTED='" + criteria.getIsPrinted() + "')");
        if (criteria.getIsRegistered() != null) temp.append(" AND (ISREGISTERED='" + criteria.getIsRegistered() + "')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected HR_ConceptDTO convertModelToDTO(X_HR_Concept model) {
        HR_ConceptDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new HR_ConceptDTO();
            if ((obj = model.get_Value("COLUMNTYPE")) != null) dto.setColumnType((String) obj);
            if ((obj = model.get_Value("DESCRIPTION")) != null) dto.setDescription((String) obj);
            if ((obj = model.get_Value("HR_CONCEPT_CATEGORY_ID")) != null) dto.setHr_Concept_Category_ID((Integer) obj);
            if ((obj = model.get_Value("HR_CONCEPT_ID")) != null) dto.setHr_Concept_ID((Integer) obj);
            if ((obj = model.get_Value("HR_DEPARTMENT_ID")) != null) dto.setHr_Department_ID((Integer) obj);
            if ((obj = model.get_Value("HR_JOB_ID")) != null) dto.setHr_Job_ID((Integer) obj);
            if ((obj = model.get_Value("HR_PAYROLL_ID")) != null) dto.setHr_Payroll_ID((Integer) obj);
            if ((obj = model.get_Value("NAME")) != null) dto.setName((String) obj);
            if ((obj = model.get_Value("TYPE")) != null) dto.setType((String) obj);
            if ((obj = model.get_Value("VALIDFROM")) != null) dto.setValidFrom((Timestamp) obj);
            if ((obj = model.get_Value("VALIDTO")) != null) dto.setValidTo((Timestamp) obj);
            if ((obj = model.get_Value("VALUE")) != null) dto.setValue((String) obj);
            if ((obj = model.get_Value("ISDEFAULT")) != null) dto.setIsDefault(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISEMPLOYEE")) != null) dto.setIsEmployee(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISPRINTED")) != null) dto.setIsPrinted(((Boolean) obj) == true ? "Y" : "N");
            if ((obj = model.get_Value("ISREGISTERED")) != null) dto.setIsRegistered(((Boolean) obj) == true ? "Y" : "N");
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
        }
        return dto;
    }
}
