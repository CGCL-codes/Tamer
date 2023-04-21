package org.koossery.adempiere.sisv.impl.server.request;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_R_RequestProcessor_Route;
import org.koossery.adempiere.core.backend.interfaces.dao.server.request.IR_RequestProcessor_RouteDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.server.request.IR_RequestProcessor_RouteSISV;
import org.koossery.adempiere.core.contract.criteria.server.request.R_RequestProcessor_RouteCriteria;
import org.koossery.adempiere.core.contract.dto.server.request.R_RequestProcessor_RouteDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;

public class R_RequestProcessor_RouteSISVImpl extends AbstractCommonSISV implements IR_RequestProcessor_RouteSISV, InitializingBean {

    private IR_RequestProcessor_RouteDAO rrequestprocessorrouteDAOImpl;

    private static Logger logger = Logger.getLogger(R_RequestProcessor_RouteSISVImpl.class);

    public R_RequestProcessor_RouteSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.rrequestprocessorrouteDAOImpl = (IR_RequestProcessor_RouteDAO) this.getDaoController().get("DAO/R_RequestProcessor_Route");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createR_RequestProcessor_Route(Properties ctx, R_RequestProcessor_RouteDTO r_RequestProcessor_RouteDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_R_RequestProcessor_Route model = new X_R_RequestProcessor_Route(ctx, 0, trxname);
            R_RequestProcessor_RouteCriteria criteria = new R_RequestProcessor_RouteCriteria();
            if (rrequestprocessorrouteDAOImpl.isDuplicate(criteria)) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "R_REQUESTPROCESSOR_ROUTE_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setAD_User_ID(r_RequestProcessor_RouteDTO.getAd_User_ID());
                model.setKeyword(r_RequestProcessor_RouteDTO.getKeyword());
                model.setR_RequestProcessor_ID(r_RequestProcessor_RouteDTO.getR_RequestProcessor_ID());
                model.setR_RequestType_ID(r_RequestProcessor_RouteDTO.getR_RequestType_ID());
                model.setSeqNo(r_RequestProcessor_RouteDTO.getSeqNo());
                model.setIsActive(r_RequestProcessor_RouteDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                r_RequestProcessor_RouteDTO.setR_RequestProcessor_Route_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "R_REQUESTPROCESSOR_ROUTE_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public R_RequestProcessor_RouteDTO getR_RequestProcessor_Route(Properties ctx, int r_RequestProcessor_Route_ID, String trxname) throws KTAdempiereAppException {
        try {
            if (r_RequestProcessor_Route_ID == 0) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "R_REQUESTPROCESSOR_ROUTE_SISV_002");
            X_R_RequestProcessor_Route mrequestprocessorroute = new X_R_RequestProcessor_Route(ctx, r_RequestProcessor_Route_ID, trxname);
            return convertModelToDTO(mrequestprocessorroute);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "R_REQUESTPROCESSOR_ROUTE_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<R_RequestProcessor_RouteDTO> findR_RequestProcessor_Route(Properties ctx, R_RequestProcessor_RouteCriteria r_RequestProcessor_RouteCriteria) throws KTAdempiereAppException {
        try {
            String whereclause = getWhereClause(ctx, r_RequestProcessor_RouteCriteria);
            int id[] = X_R_RequestProcessor_Route.getAllIDs("R_RequestProcessor_Route", whereclause, null);
            ArrayList<R_RequestProcessor_RouteDTO> list = new ArrayList<R_RequestProcessor_RouteDTO>();
            R_RequestProcessor_RouteDTO dto;
            for (int i = 0; i < id.length; i++) {
                dto = this.getR_RequestProcessor_Route(ctx, id[i], null);
                if (dto != null) list.add(dto);
            }
            return list;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "R_REQUESTPROCESSOR_ROUTE_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateR_RequestProcessor_Route(Properties ctx, R_RequestProcessor_RouteDTO r_RequestProcessor_RouteDTO) throws KTAdempiereAppException {
        try {
            X_R_RequestProcessor_Route model = new X_R_RequestProcessor_Route(ctx, r_RequestProcessor_RouteDTO.getR_RequestProcessor_Route_ID(), null);
            if (model != null) {
                R_RequestProcessor_RouteCriteria criteria = new R_RequestProcessor_RouteCriteria();
                if (rrequestprocessorrouteDAOImpl.isDuplicate(criteria)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "R_REQUESTPROCESSOR_ROUTE_SISV_000");
                System.out.println("NO duplicates names for modification");
                model.setAD_User_ID(r_RequestProcessor_RouteDTO.getAd_User_ID());
                model.setKeyword(r_RequestProcessor_RouteDTO.getKeyword());
                model.setR_RequestProcessor_ID(r_RequestProcessor_RouteDTO.getR_RequestProcessor_ID());
                model.setR_RequestType_ID(r_RequestProcessor_RouteDTO.getR_RequestType_ID());
                model.setSeqNo(r_RequestProcessor_RouteDTO.getSeqNo());
                model.setIsActive(r_RequestProcessor_RouteDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                r_RequestProcessor_RouteDTO.setR_RequestProcessor_Route_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "R_REQUESTPROCESSOR_ROUTE_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean deleteR_RequestProcessor_Route(Properties ctx, R_RequestProcessor_RouteCriteria r_RequestProcessor_RouteCriteria) throws KTAdempiereAppException {
        try {
            int id = r_RequestProcessor_RouteCriteria.getR_RequestProcessor_Route_ID();
            X_R_RequestProcessor_Route model = new X_R_RequestProcessor_Route(ctx, id, null);
            return model.delete(true);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "R_REQUESTPROCESSOR_ROUTE_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    protected String getWhereClause(Properties ctx, R_RequestProcessor_RouteCriteria criteria) {
        StringBuffer temp = new StringBuffer(super.getWhereClause(ctx, criteria));
        if (criteria.getAd_User_ID() > 0) temp.append(" AND (AD_USER_ID=" + criteria.getAd_User_ID() + ")");
        if (criteria.getKeyword() != null) temp.append(" AND ( KEYWORD LIKE '%" + criteria.getKeyword() + "%')");
        if (criteria.getR_RequestProcessor_ID() > 0) temp.append(" AND (R_REQUESTPROCESSOR_ID=" + criteria.getR_RequestProcessor_ID() + ")");
        if (criteria.getR_RequestProcessor_Route_ID() > 0) temp.append(" AND (R_REQUESTPROCESSOR_ROUTE_ID=" + criteria.getR_RequestProcessor_Route_ID() + ")");
        if (criteria.getR_RequestType_ID() > 0) temp.append(" AND (R_REQUESTTYPE_ID=" + criteria.getR_RequestType_ID() + ")");
        if (criteria.getSeqNo() > 0) temp.append(" AND (SEQNO=" + criteria.getSeqNo() + ")");
        if (criteria.getIsActive() != null) temp.append(" AND (ISACTIVE='" + criteria.getIsActive() + "')");
        return temp.toString();
    }

    protected R_RequestProcessor_RouteDTO convertModelToDTO(X_R_RequestProcessor_Route model) {
        R_RequestProcessor_RouteDTO dto = null;
        Object obj;
        if (model != null) {
            dto = new R_RequestProcessor_RouteDTO();
            if ((obj = model.get_Value("AD_USER_ID")) != null) dto.setAd_User_ID((Integer) obj);
            if ((obj = model.get_Value("KEYWORD")) != null) dto.setKeyword((String) obj);
            if ((obj = model.get_Value("R_REQUESTPROCESSOR_ID")) != null) dto.setR_RequestProcessor_ID((Integer) obj);
            if ((obj = model.get_Value("R_REQUESTPROCESSOR_ROUTE_ID")) != null) dto.setR_RequestProcessor_Route_ID((Integer) obj);
            if ((obj = model.get_Value("R_REQUESTTYPE_ID")) != null) dto.setR_RequestType_ID((Integer) obj);
            if ((obj = model.get_Value("SEQNO")) != null) dto.setSeqNo((Integer) obj);
            dto.setIsActive(((Boolean) model.get_Value("ISACTIVE")) == true ? "Y" : "N");
            dto.setAd_Client_ID((Integer) model.get_Value("AD_CLIENT_ID"));
            dto.setAd_Org_ID((Integer) model.get_Value("AD_Org_ID"));
        }
        return dto;
    }
}
