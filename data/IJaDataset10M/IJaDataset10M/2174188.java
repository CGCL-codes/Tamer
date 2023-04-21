package org.koossery.adempiere.sisv.impl.calendar;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.MCalendar;
import org.compiere.model.X_C_Calendar;
import org.koossery.adempiere.core.backend.interfaces.dao.calendar.IC_CalendarDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.calendar.IC_CalendarSISV;
import org.koossery.adempiere.core.contract.criteria.calendar.C_CalendarCriteria;
import org.koossery.adempiere.core.contract.dto.calendar.C_CalendarDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class C_CalendarSISVImpl extends AbstractCommonSISV implements IC_CalendarSISV, InitializingBean {

    private IC_CalendarDAO ccalendarDAOImpl;

    private static Logger logger = Logger.getLogger(C_CalendarSISVImpl.class);

    public C_CalendarSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.ccalendarDAOImpl = (IC_CalendarDAO) this.getDaoController().get("DAO/C_Calendar");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createC_Calendar(Properties ctx, C_CalendarDTO c_CalendarDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_C_Calendar model = new X_C_Calendar(ctx, 0, trxname);
            if (ccalendarDAOImpl.isDuplicate(c_CalendarDTO.getName())) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CALENDAR_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setDescription(c_CalendarDTO.getDescription());
                model.setName(c_CalendarDTO.getName());
                model.setIsActive(c_CalendarDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                c_CalendarDTO.setC_Calendar_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CALENDAR_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public C_CalendarDTO getC_Calendar(Properties ctx, int c_Calendar_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (c_Calendar_ID == 0) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CALENDAR_SISV_002");
            MCalendar mcalendar = new MCalendar(ctx, c_Calendar_ID, trxname);
            return convertModelToDTO(mcalendar);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CALENDAR_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<C_CalendarDTO> findC_Calendar(Properties ctx, C_CalendarCriteria c_CalendarCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, c_CalendarCriteria);
            int id[] = MCalendar.getAllIDs("C_Calendar", whereclause, null);
            ArrayList<C_CalendarDTO> list = new ArrayList<C_CalendarDTO>();
            C_CalendarDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = this.getC_Calendar(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CALENDAR_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateC_Calendar(Properties ctx, C_CalendarDTO c_CalendarDTO) throws KTAdempiereAppException {
        try {
            X_C_Calendar model = new X_C_Calendar(ctx, c_CalendarDTO.getC_Calendar_ID(), null);
            if (model != null) {
                String oldname = model.getName() + "";
                String newname = c_CalendarDTO.getName() + "";
                if (oldname.compareToIgnoreCase(newname) != 0) {
                    if (ccalendarDAOImpl.isDuplicate(newname)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CALENDAR_SISV_000");
                }
                System.out.println("NO duplicates names for modification");
                model.setDescription(c_CalendarDTO.getDescription());
                model.setName(c_CalendarDTO.getName());
                model.setIsActive(c_CalendarDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                c_CalendarDTO.setC_Calendar_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CALENDAR_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean updateC_Calendar(C_CalendarCriteria c_CalendarCriteria) throws KTAdempiereAppException {
        try {
            return ccalendarDAOImpl.update(c_CalendarCriteria);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "C_CALENDAR_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, C_CalendarCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getC_Calendar_ID() > 0) temp.append(" AND (C_CALENDAR_ID=" + criteria.getC_Calendar_ID() + ")");
        if (criteria.getDescription() != null) temp.append(" AND ( DESCRIPTION LIKE '%" + criteria.getDescription() + "%')");
        if (criteria.getName() != null) temp.append(" AND ( NAME LIKE '%" + criteria.getName() + "%')");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected C_CalendarDTO convertModelToDTO(X_C_Calendar model) {
        C_CalendarDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new C_CalendarDTO();
            if ((obj = model.get_Value("C_CALENDAR_ID")) != null) dto.setC_Calendar_ID((Integer) obj);
            if ((obj = model.get_Value("DESCRIPTION")) != null) dto.setDescription((String) obj);
            if ((obj = model.get_Value("NAME")) != null) dto.setName((String) obj);
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_ORG_ID"));
        }
        return dto;
    }
}
